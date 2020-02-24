package com.LiteTravel.service;

import java.util.List;

public interface HotelService {
    /*
     * 查询所有
     * */
    List<Hotel> findAll();

    /*
     * 根据主键查询
     * */
    Hotel findById(Integer hotelId);
}
