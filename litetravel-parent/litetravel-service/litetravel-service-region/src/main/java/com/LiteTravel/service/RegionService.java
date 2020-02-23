package com.LiteTravel.service;

import com.LiteTravel.region.pojo.Region;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RegionService {
    /*
    * 查询所有
    * */
    List<Region> findAll();
    /*
    * 多条件查询
    * */
    List<Region> findList(Region region);

    /*
    * 分页查询
    * */
    PageInfo<Region> findPage(Integer page, Integer size);

    /*
    * 分页条件查询
    * */
    PageInfo<Region> findPage(Integer page, Integer size, Region region);

    /*
    * 根据主键查询
    * */
    Region findById(Integer id);

    /*
    * 增加地址
    * */
    void add(Region region);

    /*
    * 修改地址
    * */
    void update(Region region);

    /*
    * 删除地址
    * */
    void delete(Integer id);

}
