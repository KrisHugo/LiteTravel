package com.LiteTravel.service.impl;

import com.LiteTravel.hotel.pojo.Hotel;
import com.LiteTravel.mapper.HotelMapper;
import com.LiteTravel.service.HotelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    HotelMapper hotelMapper;

    @Override
    public List<Hotel> findAll() {
        return hotelMapper.selectAll();
    }

    @Override
    public List<Hotel> findList(Hotel hotel) {
        return hotelMapper.selectByExample(createExample(hotel));
    }

    @Override
    public PageInfo<Hotel> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Hotel> hotels = hotelMapper.selectAll();
        return new PageInfo<>(hotels);
    }

    @Override
    public PageInfo<Hotel> findPage(Integer page, Integer size, Hotel hotel) {
        PageHelper.startPage(page, size);
        List<Hotel> hotels = hotelMapper.selectByExample(createExample(hotel));
        return new PageInfo<>(hotels);
    }

    private Example createExample(Hotel hotel){
        Example example = new Example(Hotel.class);
        Example.Criteria criteria = example.createCriteria();

        /* 名字相似度筛选(用户提供名字时未必是完整的) */
        if (!StringUtils.isEmpty(hotel.getHotelName())) {
            criteria.andLike("hotel_name", "%" + hotel.getHotelName() + "%");
        }
        /* 所属集团筛选 */
        if (hotel.getHotelManagerId() != null){
            criteria.andEqualTo("hotel_manager_id", hotel.getHotelManagerId());
        }
        /* 地址筛选 */
        if (hotel.getHotelAddress() != null){
            criteria.andEqualTo("hotel_address", hotel.getHotelAddress());
        }
        /* 大于等于评分筛选 */
        if (hotel.getHotelReplyLevel() != null){
            criteria.andGreaterThanOrEqualTo("hotel_reply_level", hotel.getHotelReplyLevel());
        }
        if (hotel.getHotelMinPrice() != null){
            criteria.andLessThanOrEqualTo("hotel_min_price", hotel.getHotelMinPrice());
        }
        return example;
    }

    @Override
    public Hotel findById(Integer hotelId) {
        return hotelMapper.selectByPrimaryKey(hotelId);
    }

    @Override
    public void add(Hotel hotel) {
        hotelMapper.insertSelective(hotel);
    }

    @Override
    public void update(Hotel hotel) {
        System.out.println(hotel);
        hotelMapper.updateByPrimaryKeySelective(hotel);
    }

    @Override
    public void delete(Integer hotelId) {
        hotelMapper.deleteByPrimaryKey(hotelId);
    }
}
