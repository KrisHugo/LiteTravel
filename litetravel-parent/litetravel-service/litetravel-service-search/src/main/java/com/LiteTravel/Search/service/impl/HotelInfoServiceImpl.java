package com.LiteTravel.Search.service.impl;

import com.LiteTravel.Hotel.DTO.HotelDTO;
import com.LiteTravel.Hotel.feign.HotelFeign;
import com.LiteTravel.Search.Hotel.pojo.HotelInfo;
import com.LiteTravel.Search.dao.HotelInfoEsMapper;
import com.LiteTravel.Search.service.HotelInfoService;
import com.alibaba.fastjson.JSON;
import entity.Result;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelInfoServiceImpl implements HotelInfoService {
    @Autowired
    private HotelFeign hotelFeign;

    @Autowired
    private HotelInfoEsMapper hotelInfoEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void importData() {

        Result<List<HotelDTO>> hotelResult = hotelFeign.findAll();
        /* 酒店数据还需要进一步修改，不能直接保存房间数据和床型数据，而是通过数组或引用的形式将其结构化储存 */
        List<HotelInfo> data = JSON.parseArray(JSON.toJSONString(hotelResult.getData()), HotelInfo.class);
        hotelInfoEsMapper.saveAll(data);

    }

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        if(searchMap != null && searchMap.size() > 0)
        {
            //根据关键词搜索
            String keywords = searchMap.get("keywords");
            if(!StringUtils.isEmpty(keywords)){
                builder.withQuery(
                        QueryBuilders.queryStringQuery(keywords).field("hotelName")
                );
            }
            //根据地址编码搜索
            String region = searchMap.get("region");
            if(!StringUtils.isEmpty(region)){
                builder.withQuery(
                        QueryBuilders.matchQuery("hotelAddress", region)
                );
            }
        }
        AggregatedPage<HotelInfo> page = elasticsearchTemplate.queryForPage(builder.build(), HotelInfo.class);
        List<HotelInfo> hotels = page.getContent();


        Map<String, Object> resultMap = new HashMap<>();
        /* 主要列表 */
        resultMap.put("rows", hotels);
        /* 附属信息: 页数 */
        resultMap.put("total", page.getTotalPages());
        /* 附属信息: 元素个数 */
        resultMap.put("totalElements", page.getTotalElements());
        return resultMap;
    }

}
