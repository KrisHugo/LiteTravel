package com.LiteTravel.service;

import com.LiteTravel.region.pojo.Region;

import java.util.List;

public interface RegionService {
    /*
    * 查询所有
    * */
    List<Region> findAll();

    /*
    * 根据主键查询
    * */
    Region findById(Integer id);
}
