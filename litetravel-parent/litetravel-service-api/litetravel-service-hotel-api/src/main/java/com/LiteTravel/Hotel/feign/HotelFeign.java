package com.LiteTravel.Hotel.feign;

import com.LiteTravel.Hotel.DTO.HotelDTO;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient("hotel")
@RequestMapping("/hotel")
public interface HotelFeign {

    @GetMapping
    Result<List<HotelDTO>> findAll();

    @GetMapping("/search/{page}/{size}")
    Result<List<HotelDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size);

}
