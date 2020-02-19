package com.LiteTravel.service.impl;

import com.LiteTravel.hotel.pojo.Hotel;
import com.LiteTravel.mapper.HotelMapper;
import com.LiteTravel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    HotelMapper hotelMapper;


    @Override
    public List<Hotel> findAll() {
        return hotelMapper.selectAll();
    }

    @Override
    public Hotel findById(Integer hotelId) {
        return hotelMapper.selectByPrimaryKey(hotelId);
    }
}
