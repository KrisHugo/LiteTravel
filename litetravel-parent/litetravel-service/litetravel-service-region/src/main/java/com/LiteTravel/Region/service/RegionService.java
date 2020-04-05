package com.LiteTravel.Region.service;

import com.LiteTravel.Region.pojo.Region;
import interfaces.BeanService;

import java.util.List;

public interface RegionService extends BeanService<Region> {

    List<Region> findByIds(List<Integer> ids);
}
