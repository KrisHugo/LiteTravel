package com.LiteTravel.service.impl;

import com.LiteTravel.DTO.HotelDTO;
import com.LiteTravel.hotel.pojo.Hotel;
import com.LiteTravel.mapper.*;
import com.LiteTravel.region.pojo.Region;
import com.LiteTravel.service.HotelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    RoomBedMapMapper roomBedMapper;

    @Autowired
    BedMapper bedMapper;

    @Autowired
    RegionMapper regionMapper;

    @Override
    public List<HotelDTO> findAll() {
        return getHotelDTOs(hotelMapper.selectAll());
    }

    @Override
    public List<HotelDTO> findList(Hotel hotel) {
        return getHotelDTOs(hotelMapper.selectByExample(createExample(hotel)));
    }

    @Override
    public PageInfo<HotelDTO> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Hotel> hotels = hotelMapper.selectAll();
        return new PageInfo<>(getHotelDTOs(hotels));
    }

    @Override
    public PageInfo<HotelDTO> findPage(Integer page, Integer size, Hotel hotel) {
        PageHelper.startPage(page, size);
        List<Hotel> hotels = hotelMapper.selectByExample(createExample(hotel));
        return new PageInfo<>(getHotelDTOs(hotels));
    }

    @Override
    public HotelDTO findById(Integer hotelId) {
        Hotel hotel = hotelMapper.selectByPrimaryKey(hotelId);
        return getHotelDTO(hotel, regionMapper.selectByPrimaryKey(hotel.getHotelAddress()).getMername());
    }
    /*
    * 增删改中仍有Room和Bed的冗余代码, 在考虑要怎么优化
    * @Return
    * */
    @Override
    public Integer add(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        // 不知道有没有效果
        BeanUtils.copyProperties(hotelDTO, hotel);
        hotelMapper.insertSelective(hotel);
/*
        // 添加房间信息
        if(hotelDTO.getRooms() != null){
            for (RoomDTO roomDTO: hotelDTO.getRooms()) {
                Room room = new Room();
                BeanUtils.copyProperties(roomDTO, room);
                // 当使用插入时, 必须使用hotel为参数,而不是hotelDTO, 因为插入时, 主键是由mybatis赋予的
                // 且切勿忘记了
                room.setHotelId(hotel.getHotelId());
                roomMapper.insertSelective(room);
                //判断床是否为空
                if(roomDTO.getBeds() != null){
                    // 添加床信息以及房间与床的联系信息
                    for (BedDTO bedDTO: roomDTO.getBeds()) {
                        Bed bed;
                        // 检测是否需要新建床型, 或查询bedId为空
                        if(bedDTO.getBedId() == null ||
                                (bed = bedMapper.selectByPrimaryKey(bedDTO.getBedId())) == null){
                            bed = new Bed();
                            bed.setBedName(bedDTO.getBedName());
                            bed.setBedSize(bedDTO.getBedSize());
                            bedMapper.insertSelective(bed);
                        }
                        // 添加连接
                        RoomBedMap roomBedMap = new RoomBedMap();
                        roomBedMap.setRoomId(room.getRoomId());
                        roomBedMap.setBedId(bed.getBedId());
                        roomBedMap.setBedCount(bedDTO.getBedCount());
                        roomBedMapper.insert(roomBedMap);
                    }
                }
            }
        }
*/
        return hotel.getHotelId();
    }

    @Override
    public Integer update(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDTO, hotel);
        hotelMapper.updateByPrimaryKeySelective(hotel);
/*
        // 此处其实应该修改到RoomService中, 暂时先写在此处
        // 若没有房间, 则直接跳过
        if(hotelDTO.getRooms() != null){
            for (RoomDTO roomDTO: hotelDTO.getRooms()) {
                // 新增房间
                Room room = new Room();
                // 同步属性值
                BeanUtils.copyProperties(roomDTO, room);
                // 设置所属hotel
                room.setHotelId(hotel.getHotelId());
                // 判断房间号
                if(room.getRoomId() != null){
                    roomMapper.updateByPrimaryKeySelective(room);
                }
                else{
                    // 插入房间
                    roomMapper.insertSelective(room);
                }
                // 设置床型
                for (BedDTO bedDTO: roomDTO.getBeds()) {
                    // 需要修改时, 只允许修改房间拥有的床型号以及数量, 因此只允许修改房间与床的联系
                    RoomBedMap roomBedMap = new RoomBedMap();
                    //删除已经在新床型列中不存在的元组
                    List<Integer> bedIds = roomDTO.getBeds().stream().map(BedDTO::getBedId).distinct().collect(Collectors.toList());
                    Example roomBedMapNotExist = new Example(RoomBedMap.class);
                    roomBedMapNotExist.createCriteria().andEqualTo("roomId", room.getRoomId())
                            .andNotIn("bedId", bedIds);
                    roomBedMapper.deleteByExample(roomBedMapNotExist);
                    // 如果为旧床型, 更新旧床型
                    Example roomBedMapExist = new Example(RoomBedMap.class);
                    roomBedMapExist.createCriteria().andEqualTo("roomId", room.getRoomId())
                            .andEqualTo("bedId", bedDTO.getBedId());
                    if(roomBedMapper.selectByExample(roomBedMapExist).size() > 0){
                        roomBedMap.setBedId(bedDTO.getBedId());
                        roomBedMap.setBedCount(bedDTO.getBedCount());
                        Example roomBedMapExample = new Example(RoomBedMap.class);
                        roomBedMapExample.createCriteria().andEqualTo("roomId", room.getRoomId());
                        // 更新旧床型
                        roomBedMapper.updateByExampleSelective(roomBedMap, roomBedMapExample);
                    }
                    // 床型在原来的表中不存在
                    else{
                        // 插入新床型

                        roomBedMap.setRoomId(room.getRoomId());
                        roomBedMap.setBedId(bedDTO.getBedId());
                        roomBedMap.setBedCount(bedDTO.getBedCount());
                        //
                        roomBedMapper.insert(roomBedMap);
                    }
                }

            }
        }
*/
        return hotel.getHotelId();
    }
    // 将房间和房间与床的联系抽取至RoomService
    // 需要删除酒店, 房间, 以及与床的联系
    @Override
    public void delete(Integer hotelId) {
/*        // 查询所有房间
        Example roomExample = new Example(Room.class);
        roomExample.createCriteria()
                .andEqualTo("hotelId", hotelId);
        List<Integer> roomIds =
                roomMapper.selectByExample(roomExample).stream()
                .map(Room::getRoomId).distinct().collect(Collectors.toList());
        //如果有房间, 才进行删除'房间'，'房间床联系'操作
        if(roomIds.size() > 0){
            Example bedExample = new Example(RoomBedMap.class);
            bedExample.createCriteria()
                    .andIn("roomId", roomIds);
            // 删除房间和床的联系
            roomBedMapper.deleteByExample(bedExample);
            // 删除房间
            roomMapper.deleteByExample(roomExample);
        }*/
        // 删除酒店本身
        hotelMapper.deleteByPrimaryKey(hotelId);
    }

    private List<HotelDTO> getHotelDTOs(List<Hotel> hotels){
        // 获取所有地址
        List<Integer> addressIds = hotels.stream().map(Hotel::getHotelAddress).distinct().collect(Collectors.toList());
        Example regionExample = new Example(Region.class);
        Example.Criteria criteria = regionExample.createCriteria();
        criteria.andIn("id", addressIds);
        List<Region> regions = regionMapper.selectByExample(regionExample);
        // 使用Map存储地址数据以方便后续赋值
        Map<Integer, String> addressMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getMername));
        // 获取所有酒店DTO
        return hotels.stream().map(hotel -> {
            String addressString = addressMap.get(hotel.getHotelAddress());
            // 调用方法转换成HotelDTO
            return getHotelDTO(hotel, addressString);
        }).collect(Collectors.toList());
    }

    private HotelDTO getHotelDTO(Hotel hotel, String addressString){
        HotelDTO hotelDTO = new HotelDTO();
        BeanUtils.copyProperties(hotel, hotelDTO);
        hotelDTO.setHotelAddressString(addressString.substring(addressString.indexOf("省,") + 2));
        // 保证重用而不是冗余, 使用RoomService获取Room数据
        /*        Example roomExample = new Example(Room.class);
        Example.Criteria criteria = roomExample.createCriteria();
        criteria.andEqualTo("hotelId", hotel.getHotelId());
        List<Room> rooms = roomMapper.selectByExample(roomExample);
        List<RoomDTO> roomDTOs = rooms.stream().map(this::getRoomDTO).collect(Collectors.toList());
        hotelDTO.setRooms(roomDTOs);*/
        return hotelDTO;
    }
    // 将getRoomDTO放在了RoomService
    /*    private RoomDTO getRoomDTO(Room room){
            RoomDTO roomDTO = new RoomDTO();
            BeanUtils.copyProperties(room, roomDTO);

            // 获取roomBedMap
            Example roomBedMapExample = new Example(RoomBedMap.class);
            roomBedMapExample.createCriteria()
                    .andEqualTo("roomId", room.getRoomId());
            List<RoomBedMap> roomBedMaps = roomBedMapper.selectByExample(roomBedMapExample);
            if(roomBedMaps.size() > 0){
                // 通过roomId找到该room的所有床型
                List<Integer> bedIds = roomBedMaps.stream().map(RoomBedMap::getBedId).distinct().collect(Collectors.toList());
                // 获取bed
                Example bedExample = new Example(Bed.class);
                bedExample.createCriteria()
                        .andIn("bedId", bedIds);
                List<Bed> bedList = bedMapper.selectByExample(bedExample);
                // 获取bedCount
                Map<Integer, Integer> bedCountMap = roomBedMaps.stream().collect(Collectors.toMap(RoomBedMap::getBedId, RoomBedMap::getBedCount));
                // bed和bedCount写入bedDTO
                List<BedDTO> bedDTOs = bedList.stream().map(bed -> {
                    BedDTO bedDTO = new BedDTO();
                    BeanUtils.copyProperties(bed, bedDTO);
                    bedDTO.setBedCount(bedCountMap.get(bed.getBedId()));
                    return bedDTO;
                }).collect(Collectors.toList());
                roomDTO.setBeds(bedDTOs);
            }
            return roomDTO;
        }*/
    private Example createExample(Hotel hotel){
        Example example = new Example(Hotel.class);
        Example.Criteria criteria = example.createCriteria();

        /* 名字相似度筛选(用户提供名字时未必是完整的) */
        if (!StringUtils.isEmpty(hotel.getHotelName())) {
            criteria.andLike("hotelName", "%" + hotel.getHotelName() + "%");
        }
        /* 所属集团筛选 */
        if (hotel.getHotelManagerId() != null){
            criteria.andEqualTo("hotelManagerId", hotel.getHotelManagerId());
        }
        /* 地址筛选 */
        if (hotel.getHotelAddress() != null){
            criteria.andEqualTo("hotelAddress", hotel.getHotelAddress());
        }
        /* 大于等于评分筛选 */
        if (hotel.getHotelReplyLevel() != null){
            criteria.andGreaterThanOrEqualTo("hotelReplyLevel", hotel.getHotelReplyLevel());
        }
        /* 小于等于最高价格 */
        if (hotel.getHotelMinPrice() != null){
            criteria.andLessThanOrEqualTo("hotelMinPrice", hotel.getHotelMinPrice());
        }
        /* 大于等于最低价格 */
        /* 尚未实现 */
        return example;
    }
}
