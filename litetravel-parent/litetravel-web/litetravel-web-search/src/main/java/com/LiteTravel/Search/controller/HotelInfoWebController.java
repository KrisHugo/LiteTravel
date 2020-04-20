package com.LiteTravel.Search.controller;

import com.LiteTravel.Search.Feign.HotelInfoFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search/hotel")
public class HotelInfoWebController {
    @Autowired
    private HotelInfoFeign hotelInfoFeign;

    /*
    * 搜索
    * */
    @GetMapping("/list")
    public String search(@RequestParam(required=false) Map searchMap, Model model){
        Map resultMap = hotelInfoFeign.search(searchMap);
        model.addAttribute("result", resultMap);
        return "search";
    }

}
