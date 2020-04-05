package com.LiteTravel.Search.service;

import java.util.Map;

public interface HotelInfoService {
    void importData();

    Map<String, Object> search(Map<String, String> searchMap);

}
