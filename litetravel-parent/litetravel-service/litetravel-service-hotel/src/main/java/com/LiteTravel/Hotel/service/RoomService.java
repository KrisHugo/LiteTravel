package com.LiteTravel.Hotel.service;

import com.LiteTravel.Hotel.DTO.RoomDTO;
import com.LiteTravel.Hotel.pojo.Room;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoomService {

    /*
     * 查询所有
     * */
    List<RoomDTO> findAll();

    /*
     * 条件查询
     * */
    List<RoomDTO> findList(Room room);

    /*
     * 分页查询
     * */
    PageInfo<RoomDTO> findPage(Integer page, Integer size);

    /*
     * 分页条件查询
     * */
    PageInfo<RoomDTO> findPage(Integer page, Integer size, Room room);

    /*
     * 根据主键查询
     * */
    RoomDTO findById(Integer id);

    /*
     * 增加
     * */
    void add(RoomDTO room);

    /*
     * 修改
     * */
    void update(RoomDTO room);

    /*
     * 主键Id筛选删除
     * */
    void delete(Integer id);
    /*
     * 酒店Id筛选删除
     * */
    void deleteByHotelId(Integer hotelId);
}
