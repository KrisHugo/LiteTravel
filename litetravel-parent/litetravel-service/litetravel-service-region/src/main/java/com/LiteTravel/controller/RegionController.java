package com.LiteTravel.controller;


import com.LiteTravel.region.pojo.Region;
import com.LiteTravel.service.RegionService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
@CrossOrigin
public class RegionController {
    /*
    * 查询所有
    * */
    @Autowired
    private RegionService regionService;

    @GetMapping
    public Result<List<Region>> findAll(){
        List<Region> regions = regionService.findAll();
        /* 此处可以进行错误检测 */

        /* 返回Rest风格数据至前端 */
        return new Result<>(true, 20000, "查询地址信息集合成功", regions);
    }

    /*
    * 根据主键ID查询
    * */
    @GetMapping("/{id}")
    public Result<Region> findById(@PathVariable("id") Integer id){
        Region region = regionService.findById(id);
        boolean flag = region != null;
        return new Result<>(flag, flag?20000:20006, "通过主键Id查询地址集合成功", region);
    }

}
