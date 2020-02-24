package com.LiteTravel.controller;

import com.LiteTravel.service.HotelService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {
    @Autowired
    HotelService hotelService;

    @GetMapping
    public Result<List<Hotel>> findAll(){
        List<Hotel> hotels = hotelService.findAll();
        return new Result<>(true, 20000, "查询酒店信息集合成功", hotels);
    }
    @GetMapping("/{hotelId}")
    public Result<Hotel> findAll(@PathVariable("hotelId") Integer hotelId){
        Hotel hotel = hotelService.findById(hotelId);
        boolean flag = hotel != null;
        return new Result<>(flag, flag?20000:20006, "查询对应酒店信息成功", hotel);
    }


}
