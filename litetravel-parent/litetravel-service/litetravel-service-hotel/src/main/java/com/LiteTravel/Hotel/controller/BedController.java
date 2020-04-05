package com.LiteTravel.Hotel.controller;

import com.LiteTravel.Hotel.pojo.Bed;
import com.LiteTravel.Hotel.service.BedService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bed")
@CrossOrigin
public class BedController {
    @Autowired
    BedService bedService;

    @GetMapping
    @ResponseBody
    public Result<List<Bed>> findAll(){
        List<Bed> beds = bedService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", beds);
    }
    @PostMapping("/search")
    @ResponseBody
    public Result<List<Bed>> findList(@RequestBody Bed bed){
        List<Bed> beds = bedService.findList(bed);
        return new Result<>(true, StatusCode.OK, "条件查询成功", beds);
    }
    @GetMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<Bed>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        PageInfo<Bed> beds = bedService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", beds);
    }
    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<Bed>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody Bed bed){
        PageInfo<Bed> beds = bedService.findPage(page, size, bed);
        return new Result<>(true, StatusCode.OK, "分页查询成功", beds);
    }
    @GetMapping("/{bedId}")
    @ResponseBody
    public Result<Bed> findAll(@PathVariable("bedId") Integer bedId){
        Bed bed = bedService.findById(bedId);
        return new Result<>(true, StatusCode.OK, "主键查询成功", bed);
    }
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody Bed bed) {
        bedService.add(bed);
        return new Result(true, StatusCode.OK, "增加成功");
    }
    @PutMapping("/{bedId}")
    @ResponseBody
    public Result update(@PathVariable("bedId") Integer bedId, @RequestBody Bed bed){
        bed.setBedId(bedId);
        bedService.update(bed);
        return new Result(true, StatusCode.OK, "修改成功");
    }
    @DeleteMapping("/{bedId}")
    @ResponseBody
    public Result delete(@PathVariable("bedId") Integer bedId){
        bedService.delete(bedId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
