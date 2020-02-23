package com.LiteTravel.service.impl;

import com.LiteTravel.mapper.RegionMapper;
import com.LiteTravel.region.pojo.Region;
import com.LiteTravel.service.RegionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<Region> findAll() {
        return regionMapper.selectAll();
    }

    @Override
    public List<Region> findList(Region region) {
        /* 返回list, 防止外部调用再进行多余的防错误判断, 外部只需要无脑foreach就可以了 */
        if(region == null)
            return new ArrayList<>();
        return regionMapper.selectByExample(CreateExample(region));
    }

    @Override
    public PageInfo<Region> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Region> regions = regionMapper.selectAll();
        return new PageInfo<>(regions);
    }

    @Override
    public PageInfo<Region> findPage(Integer page, Integer size, Region region) {
        PageHelper.startPage(page, size);
        List<Region> regions = regionMapper.selectByExample(CreateExample(region));
        return new PageInfo<>(regions);
    }

    /* 复用条件筛选 */
    private Example CreateExample(Region region){
        Example example = new Example(Region.class);
        Example.Criteria criteria = example.createCriteria();
        if(region.getId() != null){
            criteria.andEqualTo("id", region.getId());
        }
        if(!StringUtils.isEmpty(region.getName())){
            criteria.andLike("name", "%" + region.getName() + "%");
        }
        if(region.getLevel() != null){
            criteria.andEqualTo("level", region.getLevel());
        }
        if(region.getPid() != null){
            criteria.andEqualTo("pid", region.getPid());
        }
        return example;
    }


    @Override
    public Region findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Region region) {
        /*Selective注：可忽略空值，如ID*/
        regionMapper.insertSelective(region);
    }

    @Override
    public void update(Region region){
        regionMapper.updateByPrimaryKeySelective(region);
    }

    @Override
    public void delete(Integer id) {
        regionMapper.deleteByPrimaryKey(id);
    }
}
