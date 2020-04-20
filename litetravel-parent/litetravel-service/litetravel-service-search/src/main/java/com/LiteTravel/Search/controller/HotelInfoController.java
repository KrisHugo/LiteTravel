package com.LiteTravel.Search.controller;

import com.LiteTravel.Search.service.HotelInfoService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/search/hotel")
public class HotelInfoController {
    @Autowired
    private HotelInfoService hotelInfoService;

    @GetMapping(value = "/import")
    public Result importData(){
        hotelInfoService.importData();

        return new Result(true, StatusCode.OK, "导入数据成功!");
    }

    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, String> searchMap) throws Exception{
        return hotelInfoService.search(searchMap);
    }


}
