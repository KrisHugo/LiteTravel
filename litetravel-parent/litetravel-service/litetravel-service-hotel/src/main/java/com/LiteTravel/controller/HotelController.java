package com.LiteTravel.controller;

import com.LiteTravel.DTO.HotelDTO;
import com.LiteTravel.hotel.pojo.Hotel;
import com.LiteTravel.service.HotelService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
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
    @ResponseBody
    public Result<List<HotelDTO>> findAll(){
        List<HotelDTO> hotels = hotelService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", hotels);
    }
    @PostMapping("/search")
    @ResponseBody
    public Result<List<HotelDTO>> findList(@RequestBody Hotel hotel){
        List<HotelDTO> hotels = hotelService.findList(hotel);
        return new Result<>(true, StatusCode.OK, "条件查询成功", hotels);
    }
    @GetMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<HotelDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        PageInfo<HotelDTO> hotels = hotelService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", hotels);
    }
    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<HotelDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody Hotel hotel){
        PageInfo<HotelDTO> hotels = hotelService.findPage(page, size, hotel);
        return new Result<>(true, StatusCode.OK, "分页条件查询成功", hotels);
    }
    @GetMapping("/{hotelId}")
    @ResponseBody
    public Result<HotelDTO> findAll(@PathVariable("hotelId") Integer hotelId){
        HotelDTO hotel = hotelService.findById(hotelId);
        return new Result<>(true, StatusCode.OK, "主键查询成功", hotel);
    }
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody HotelDTO hotel) {
        hotelService.add(hotel);
        return new Result(true, StatusCode.OK, "增加成功");
    }
    @PutMapping("/{hotelId}")
    @ResponseBody
    public Result update(@PathVariable("hotelId") Integer hotelId, @RequestBody HotelDTO hotel){
        hotel.setHotelId(hotelId);
        hotelService.update(hotel);
        return new Result(true, StatusCode.OK, "修改成功");
    }
    @DeleteMapping("/{hotelId}")
    @ResponseBody
    public Result delete(@PathVariable("hotelId") Integer hotelId){
        hotelService.delete(hotelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
