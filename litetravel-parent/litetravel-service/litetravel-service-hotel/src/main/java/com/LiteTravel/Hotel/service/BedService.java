package com.LiteTravel.Hotel.service;

import com.LiteTravel.Hotel.DTO.BedDTO;
import com.LiteTravel.Hotel.pojo.Bed;
import interfaces.BeanService;

import java.util.List;

public interface BedService extends BeanService<Bed> {
    public List<BedDTO> findListByRoomId(Integer roomId);
}
