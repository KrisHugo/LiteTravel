package com.LiteTravel.Region.feign;

import com.LiteTravel.Region.pojo.Region;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("region")
@RequestMapping("/region")
public interface RegionFeign {

    @PostMapping("/search/ids")
    Result<List<Region>> findByIds(@RequestBody Integer[] ids);

    @GetMapping("/{id}")
    Result<Region> findById(@PathVariable("id") Integer id);
}
