package com.LiteTravel.service.impl;

import com.LiteTravel.hotel.DTO.BedDTO;
import com.LiteTravel.hotel.pojo.Bed;
import com.LiteTravel.hotel.pojo.RoomBedMap;
import com.LiteTravel.mapper.BedMapper;
import com.LiteTravel.mapper.RoomBedMapMapper;
import com.LiteTravel.service.BedService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private RoomBedMapMapper roomBedMapMapper;

    @Autowired
    private BedMapper bedMapper;
    /*
    * 通过RoomId 使用RoomBedMap来找到Bed并生成BedDTO
    * */
    @Override
    public List<BedDTO> findListByRoomId(Integer roomId){
        Example bedMapExample = new Example(RoomBedMap.class);
        bedMapExample.createCriteria()
                .andEqualTo("roomId", roomId);
        List<RoomBedMap> roomBedMaps = roomBedMapMapper.selectByExample(bedMapExample);
        // 验空
        if(roomBedMaps == null || roomBedMaps.size() == 0){
            return new ArrayList<>();
        }
        Map<Integer, Integer> bedCounts = roomBedMaps.stream().collect(Collectors.toMap(RoomBedMap::getBedId, RoomBedMap::getBedCount));
        List<Integer> bedIds = roomBedMaps.stream().map(RoomBedMap::getBedId).distinct().collect(Collectors.toList());
        Example bedExample = new Example(Bed.class);
        bedExample.createCriteria()
                .andIn("bedId", bedIds);
        List<Bed> beds = bedMapper.selectByExample(bedExample);

        return beds.stream().map(bed -> {
            BedDTO bedDTO = new BedDTO();
            BeanUtils.copyProperties(bed, bedDTO);
            bedDTO.setBedCount(bedCounts.get(bed.getBedId()));
            return bedDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Bed> findAll() {
        return bedMapper.selectAll();
    }

    @Override
    public List<Bed> findList(Bed bed) {
        return bedMapper.selectByExample(createExample(bed));
    }

    @Override
    public PageInfo<Bed> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Bed> beds = bedMapper.selectAll();
        return new PageInfo<>(beds);
    }

    @Override
    public PageInfo<Bed> findPage(Integer page, Integer size, Bed bed) {
        PageHelper.startPage(page, size);
        List<Bed> beds = bedMapper.selectByExample(createExample(bed));
        return new PageInfo<>(beds);
    }

    @Override
    public Bed findById(Integer id) {
        return bedMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Bed bed) {
        bedMapper.insertSelective(bed);
    }

    @Override
    public void update(Bed bed) {
        bedMapper.updateByPrimaryKeySelective(bed);
    }

    @Override
    public void delete(Integer id) {
        bedMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Bed bed) {
        Example example = new Example(Bed.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(bed.getBedName())){
            criteria.andLike("bed_name", "%" + bed.getBedName() + "%");
        }
        if (bed.getBedSize() != null){
            criteria.andGreaterThanOrEqualTo("bed_size", bed.getBedSize());
        }
        return example;
    }
}
