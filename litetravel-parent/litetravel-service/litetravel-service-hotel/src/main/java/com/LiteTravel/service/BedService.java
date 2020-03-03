package com.LiteTravel.service;

import com.LiteTravel.DTO.BedDTO;
import com.LiteTravel.hotel.pojo.Bed;
import interfaces.BeanService;

import java.util.List;

public interface BedService extends BeanService<Bed> {
    public List<BedDTO> findListByRoomId(Integer roomId);
}
