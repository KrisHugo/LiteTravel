package com.LiteTravel.controller;

import com.LiteTravel.DTO.BedDTO;
import com.LiteTravel.DTO.HotelDTO;
import com.LiteTravel.DTO.RoomDTO;
import com.LiteTravel.hotel.pojo.Hotel;
import com.LiteTravel.hotel.pojo.Room;
import com.LiteTravel.service.BedService;
import com.LiteTravel.service.HotelService;
import com.LiteTravel.service.RoomService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {
    @Autowired
    HotelService hotelService;

    @Autowired
    RoomService roomService;

    @Autowired
    BedService bedService;

    @GetMapping
    @ResponseBody
    public Result<List<HotelDTO>> findAll(){
        List<HotelDTO> hotels = hotelService.findAll();
        for (HotelDTO h: hotels) {
            h.setRooms(getRooms(h.getHotelId(), new HotelDTO()));
        }
        return new Result<>(true, StatusCode.OK, "查询成功", hotels);
    }
    /*
    * 条件查询
    * */
    @PostMapping("/search")
    @ResponseBody
    public Result<List<HotelDTO>> findList(@RequestBody HotelDTO hotelDTOExample){
        Hotel hotelExample = new Hotel();
        BeanUtils.copyProperties(hotelDTOExample, hotelExample);
        List<HotelDTO> hotels = hotelService.findList(hotelExample);
        for (HotelDTO h: hotels) {
            h.setRooms(getRooms(h.getHotelId(), hotelDTOExample));
        }
        return new Result<>(true, StatusCode.OK, "条件查询成功", hotels);
    }
    @GetMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<HotelDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        PageInfo<HotelDTO> hotels = hotelService.findPage(page, size);
        for (HotelDTO h: hotels.getList()) {
            h.setRooms(getRooms(h.getHotelId(), new HotelDTO()));
        }
        return new Result<>(true, StatusCode.OK, "分页查询成功", hotels);
    }
    /*
    * 条件查询
    * */
    @PostMapping("/search/{page}/{size}")
    @ResponseBody
    public Result<PageInfo<HotelDTO>> findPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody HotelDTO hotelDTOExample){
        Hotel hotelExample = new Hotel();
        BeanUtils.copyProperties(hotelDTOExample, hotelExample);
        PageInfo<HotelDTO> hotels = hotelService.findPage(page, size, hotelExample);
        for (HotelDTO h: hotels.getList()) {
            h.setRooms(getRooms(h.getHotelId(), hotelDTOExample));
        }
        return new Result<>(true, StatusCode.OK, "分页条件查询成功", hotels);
    }
    @GetMapping("/{hotelId}")
    @ResponseBody
    public Result<HotelDTO> findAll(@PathVariable("hotelId") Integer hotelId){
        HotelDTO hotel = hotelService.findById(hotelId);
        hotel.setRooms(getRooms(hotelId, new HotelDTO()));
        return new Result<>(true, StatusCode.OK, "主键查询成功", hotel);
    }
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody HotelDTO hotel) {
        Integer hotelId = hotelService.add(hotel);
        // 添加房间信息
        if(hotel.getRooms() != null){
            for (RoomDTO roomDTO: hotel.getRooms()){
                /* 设置HotelId */
                roomDTO.setHotelId(hotelId);
                roomService.add(roomDTO);
            }
        }
        return new Result(true, StatusCode.OK, "增加成功");
    }
    @PutMapping("/{hotelId}")
    @ResponseBody
    public Result update(@PathVariable("hotelId") Integer hotelId, @RequestBody HotelDTO hotel){
        hotel.setHotelId(hotelId);
        hotelService.update(hotel);
        /* 更新房间信息 */
        if(hotel.getRooms() != null){
            for (RoomDTO roomDTO: hotel.getRooms()){
                roomDTO.setHotelId(hotelId);
                roomService.update(roomDTO);
            }
        }
        return new Result(true, StatusCode.OK, "修改成功");
    }
    @DeleteMapping("/{hotelId}")
    @ResponseBody
    public Result delete(@PathVariable("hotelId") Integer hotelId){
        roomService.deleteByHotelId(hotelId);
        hotelService.delete(hotelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }
    /*
    * 条件搜索房间
    * */
    private List<RoomDTO> getRooms(Integer hotelId, HotelDTO hotel){
        Room room = new Room();
        List<RoomDTO> rooms = hotel.getRooms();
        if(rooms != null && rooms.size() > 0)
        {
            // 复制属性, 以满足查询需要
            BeanUtils.copyProperties(rooms.get(0), room);
        }
        room.setHotelId(hotelId);
        List<RoomDTO> roomDTOs = roomService.findList(room);
        for (RoomDTO roomDTO : roomDTOs) {
            roomDTO.setBeds(bedService.findListByRoomId(roomDTO.getRoomId()));
        }
        return roomDTOs;
    }
}
