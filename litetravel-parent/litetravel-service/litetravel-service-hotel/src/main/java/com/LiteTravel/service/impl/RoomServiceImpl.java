package com.LiteTravel.service.impl;

import com.LiteTravel.DTO.BedDTO;
import com.LiteTravel.DTO.RoomDTO;
import com.LiteTravel.hotel.pojo.Bed;
import com.LiteTravel.hotel.pojo.Room;
import com.LiteTravel.hotel.pojo.RoomBedMap;
import com.LiteTravel.mapper.BedMapper;
import com.LiteTravel.mapper.RoomBedMapMapper;
import com.LiteTravel.mapper.RoomMapper;
import com.LiteTravel.service.RoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomMapper roomMapper;

    @Autowired
    RoomBedMapMapper roomBedMapper;

    @Autowired
    BedMapper bedMapper;


    @Override
    public List<RoomDTO> findAll() {
        return getRoomDTOs(roomMapper.selectAll());
    }
    @Override
    public List<RoomDTO> findList(Room room) {
        return getRoomDTOs(roomMapper.selectByExample(createExample(room)));
    }

    // todo 实际应该修改使用RoomExampleDTO来筛选, 有太多条件单纯使用Room的数据无法解决
    private Example createExample(Room room) {
        Example example = new Example(Room.class);
        Example.Criteria criteria = example.createCriteria();
        if(room.getHotelId() != null)
        {
            criteria.andEqualTo("hotelId", room.getHotelId());
        }
        if(!StringUtils.isEmpty(room.getRoomName())){
            criteria.andLike("roomName", "%" + room.getRoomName() + "%");
        }
        // todo 应该是个范围
        if(room.getRoomPrice() != null){
            criteria.andLessThanOrEqualTo("roomPrice", room.getRoomPrice());
        }
        // todo 查找空余房间? 应该需要更加复杂的算法
        if(room.getRoomRemaining() != null){
            criteria.andGreaterThan("roomRemaining", room.getRoomRemaining());
        }
        if(room.getRoomCancel() != null){
            criteria.andEqualTo("roomCancel", room.getRoomCancel());
        }
        // todo 应该是个范围
        if(room.getRoomSize() != null){
            criteria.andGreaterThan("roomSize", room.getRoomSize());
        }
        if(room.getRoomBookMax() != null){
            criteria.andGreaterThanOrEqualTo("roomBookMax", room.getRoomBookMax());
        }
        if(room.getRoomWifi() != null){
            criteria.andEqualTo("roomWifi", room.getRoomWifi());
        }
        // 加床条件筛选
        if(room.getRoomBedAdd() != null){
            BigDecimal i = room.getRoomBedAdd();
            // 判断——不想加床
            if(i.equals(BigDecimal.valueOf(-1)))
            {
                criteria.andEqualTo("roomBedAdd", i);
            }
            // ——想要加床
            else{
                criteria.andGreaterThanOrEqualTo("roomBedAdd", 0)
                        .andLessThanOrEqualTo("roomBedAdd", i);
            }
        }
        return example;
    }

    @Override
    public PageInfo<RoomDTO> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Room> rooms = roomMapper.selectAll();
        return new PageInfo<>(getRoomDTOs(rooms));
    }

    @Override
    public PageInfo<RoomDTO> findPage(Integer page, Integer size, Room room) {
        PageHelper.startPage(page, size);
        List<Room> rooms = roomMapper.selectByExample(createExample(room));
        return new PageInfo<>(getRoomDTOs(rooms));
    }

    @Override
    public RoomDTO findById(Integer roomId) {
        return getRoomDTO(roomMapper.selectByPrimaryKey(roomId));
    }

    /*
    * 增删改仍然有冗余
    * */
    @Override
    public void add(RoomDTO roomDTO) {
        Room room = new Room();
        BeanUtils.copyProperties(roomDTO, room);
        // 当使用插入时, 必须使用hotel为参数,而不是hotelDTO, 因为插入时, 主键是由mybatis赋予的
        // 且切勿忘记了
        room.setHotelId(roomDTO.getHotelId());
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

    @Override
    public void update(RoomDTO roomDTO) {
        // 新增房间
        Room room = new Room();
        // 同步属性值
        BeanUtils.copyProperties(roomDTO, room);
        // 设置所属hotel
        room.setHotelId(roomDTO.getHotelId());
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

    @Override
    public void delete(Integer roomId) {
        Example bedExample = new Example(RoomBedMap.class);
        bedExample.createCriteria()
                .andEqualTo("roomId", roomId);
        // 删除房间和床的联系
        roomBedMapper.deleteByExample(bedExample);
        // 删除房间
        roomMapper.deleteByPrimaryKey(roomId);
    }
    private List<RoomDTO> getRoomDTOs(List<Room> rooms){
        return rooms.stream().map(this::getRoomDTO).collect(Collectors.toList());
    }

    private RoomDTO getRoomDTO(Room room){
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
    }
}
