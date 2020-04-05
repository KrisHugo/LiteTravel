package com.LiteTravel.Search.dao;

import com.LiteTravel.Search.Hotel.pojo.HotelInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface HotelInfoEsMapper extends ElasticsearchRepository<HotelInfo, Integer> {

}
