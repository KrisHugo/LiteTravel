package com.LiteTravel.service.impl;

import com.LiteTravel.hotel.pojo.Bed;
import com.LiteTravel.mapper.BedMapper;
import com.LiteTravel.service.BedService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public class BedServiceImpl implements BedService {
    @Autowired
    private BedMapper bedMapper;

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
