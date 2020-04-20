package com.LiteTravel.Search.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("search")
@RequestMapping("/search/hotel")
public interface HotelInfoFeign {

    /*
    * feign搜索调用
    * */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, String> searchMap);

}
