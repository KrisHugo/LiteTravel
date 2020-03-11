package com.LiteTravel.hotel.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BedDTO {
    private Integer bedId;
    private String bedName;
    private BigDecimal bedSize;
    private Integer bedCount;// 对应酒店房间提供的该床的数量
}
