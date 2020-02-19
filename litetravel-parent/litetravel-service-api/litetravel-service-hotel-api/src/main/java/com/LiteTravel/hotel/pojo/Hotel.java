package com.LiteTravel.hotel.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ApiModel(description = "Hotel Main Info", value = "Hotel")
@Table(name = "travel_hotel")
public class Hotel {
    @Id
    @Column(name = "hotel+_id")
    private Integer hotelId;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "hotel_min_price")
    private Integer hotelMinPrice;

    @Column(name = "hotel_manager_id")
    private Integer hotelManagerId;

    @Column(name = "hotel_phone")
    private String hotelPhone;

    @Column(name = "hotel_address")
    private Integer hotelAddress;

    @Column(name = "hotel_address_specific")
    private String hotelAddressSpecific;

    @Column(name = "hotel_img_uri")
    private String hotelImgUri;


    @Column(name = "hotelReplyLevel")
    private Integer hotelReplyLevel;

    @Column(name = "hotelReplyCount")
    private Integer hotelReplyCount;

    @Column(name = "hotelDescription")
    private String hotelDescription;

}