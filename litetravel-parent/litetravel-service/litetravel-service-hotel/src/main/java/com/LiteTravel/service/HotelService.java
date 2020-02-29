package com.LiteTravel.service;

import com.LiteTravel.DTO.HotelDTO;
import com.LiteTravel.hotel.pojo.Hotel;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HotelService {
    /*
     * 查询所有
     * */
    List<HotelDTO> findAll();

    /*
     * 条件查询
     * */
    List<HotelDTO> findList(Hotel hotel);

    /*
     * 分页查询
     * */
    PageInfo<HotelDTO> findPage(Integer page, Integer size);

    /*
     * 分页条件查询
     * */
    PageInfo<HotelDTO> findPage(Integer page, Integer size, Hotel hotel);

    /*
     * 根据主键查询
     * */
    HotelDTO findById(Integer id);

    /*
     * 增加
     * */
    void add(HotelDTO hotel);

    /*
     * 修改
     * */
    void update(HotelDTO hotel);

    /*
     * 删除
     * */
    void delete(Integer id);
}
