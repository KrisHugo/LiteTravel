package com.LiteTravel.controller;

import com.LiteTravel.DTO.RoomDTO;
import com.LiteTravel.hotel.pojo.Room;
import com.LiteTravel.service.RoomService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping
    @ResponseBody
    public Result<List<RoomDTO>> findAll(){
        List<RoomDTO> rooms = roomService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", rooms);
    }
    @PostMapping("/search")
    @ResponseBody
    public Result<List<RoomDTO>> findList(@RequestBody Room room){
        List<RoomDTO> rooms = roomService.findList(room);
        return new Result<>(true, StatusCode.OK, "条件查询成功", rooms);
    }
    @GetMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<RoomDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        PageInfo<RoomDTO> rooms = roomService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", rooms);
    }
    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<RoomDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody Room room){
        PageInfo<RoomDTO> rooms = roomService.findPage(page, size, room);
        return new Result<>(true, StatusCode.OK, "分页条件查询成功", rooms);
    }
    @GetMapping("/{roomId}")
    @ResponseBody
    public Result<RoomDTO> findAll(@PathVariable("roomId") Integer roomId){
        RoomDTO room = roomService.findById(roomId);
        return new Result<>(true, StatusCode.OK, "主键查询成功", room);
    }
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody RoomDTO room) {
        roomService.add(room);
        return new Result(true, StatusCode.OK, "增加成功");
    }
    @PutMapping("/{roomId}")
    @ResponseBody
    public Result update(@PathVariable("roomId") Integer roomId, @RequestBody RoomDTO room){
        room.setRoomId(roomId);
        roomService.update(room);
        return new Result(true, StatusCode.OK, "修改成功");
    }
    @DeleteMapping("/{roomId}")
    @ResponseBody
    public Result delete(@PathVariable("roomId") Integer roomId){
        roomService.delete(roomId);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
