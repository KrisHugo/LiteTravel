package com.LiteTravel.hotel.feign;

import com.LiteTravel.hotel.DTO.HotelDTO;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient(value = "hotel")
@RequestMapping("/ES/hotel")
public interface HotelFeign {

    @GetMapping
    Result<List<HotelDTO>> findPage(Integer page, Integer size);

}
