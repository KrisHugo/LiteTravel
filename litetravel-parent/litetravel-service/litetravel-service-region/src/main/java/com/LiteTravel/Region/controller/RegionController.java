package com.LiteTravel.Region.controller;


import com.LiteTravel.Region.pojo.Region;
import com.LiteTravel.Region.service.RegionService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/region")
@CrossOrigin
public class RegionController {
    @Autowired
    private RegionService regionService;

    /*
     * 查询所有
     * */
    @GetMapping
    @ResponseBody
    public Result<List<Region>> findAll(){
        List<Region> regions = regionService.findAll();
        /* 返回Rest风格数据至前端 */
        return new Result<>(true, StatusCode.OK, "查询成功", regions);
    }

    /*
     * 通过id数组来条件查询
     * */
    @PostMapping("/search/ids")
    public Result<List<Region>> findByIds(@RequestBody Integer[] ids){
        System.out.println(Arrays.toString(ids));
        List<Region> regions = regionService.findByIds(Arrays.asList(ids));
        return new Result<>(true, StatusCode.OK, "Ids查询成功", regions);
    }

    /*
     * 条件查询
     * */
    @PostMapping("/search")
    @ResponseBody
    public Result<List<Region>> findList(@RequestBody Region region){
        List<Region> regions = regionService.findList(region);
        return new Result<>(true, StatusCode.OK, "条件查询成功", regions);
    }

    /*
    * 分页查询
    * */
    @GetMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<Region>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        PageInfo<Region> regions = regionService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", regions);
    }
    /*
     * 条件分页查询
     * */
    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<Region>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody Region region){
        PageInfo<Region> regions = regionService.findPage(page, size, region);
        return new Result<>(true, StatusCode.OK, "条件分页查询成功", regions);
    }
    /*
    * 根据主键ID查询
    * */
    @GetMapping("/{id}")
    @ResponseBody
    public Result<Region> findById(@PathVariable("id") Integer id){
        Region region = regionService.findById(id);
        return new Result<>(true, StatusCode.OK, "主键查询成功", region);
    }

    /*
    * 增加地址
    * */
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody Region region) {
        regionService.add(region);
        return new Result(true, StatusCode.OK, "增加成功");
    }
    /*
    * 修改操作
    * */
    @PutMapping("/{id}")
    @ResponseBody
    public Result update(@PathVariable("id") Integer id, @RequestBody Region region){
        region.setId(id);
        regionService.update(region);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /*
    * 删除操作
    * */
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id){
        regionService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


}
