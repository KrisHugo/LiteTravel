package com.LiteTravel.service.impl;

import com.LiteTravel.mapper.RegionMapper;
import com.LiteTravel.region.pojo.Region;
import com.LiteTravel.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Region findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }
}
