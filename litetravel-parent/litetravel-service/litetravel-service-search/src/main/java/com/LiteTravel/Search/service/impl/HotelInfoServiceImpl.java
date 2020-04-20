package com.LiteTravel.Search.service.impl;

import com.LiteTravel.Hotel.DTO.HotelDTO;
import com.LiteTravel.Hotel.feign.HotelFeign;
import com.LiteTravel.Search.Hotel.pojo.HotelInfo;
import com.LiteTravel.Search.dao.HotelInfoEsMapper;
import com.LiteTravel.Search.service.HotelInfoService;
import com.alibaba.fastjson.JSON;
import entity.Result;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        Map<String, Object> resultMap = new HashMap<>();

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        /* 条件查询主要酒店信息数据 */
        if(searchMap != null && searchMap.size() > 0)
        {
            // done 根据关键词搜索
            String keywords = searchMap.get("keywords");
            if(!StringUtils.isEmpty(keywords)){
                builder.withQuery(
                        QueryBuilders.queryStringQuery(keywords).field("hotelName")
                );
            }

            // 根据地址编码搜索
            // 这里直接用准确的编码来指代了，但
            // todo 实际上若是一级或是二级编码查询，还不能实现,
            // 这个是因为如果不获取region表, 便并不能完全准确的分析不同级别的编码
            String region = searchMap.get("region");
            if(!StringUtils.isEmpty(region)){
                builder.withQuery(
                        QueryBuilders.termsQuery("hotelAddress", region)
                );
            }

            /*
            * 根据价格范围查询
            * */
            String rangeMoney = searchMap.get("price");
            if(!StringUtils.isEmpty(rangeMoney)){
                String[] split = StringUtils.split(rangeMoney, ":");
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("rooms.roomPrice");
                if((split != null ? split.length : 0) == 2){
                    if(!StringUtils.isEmpty(split[0]))
                        rangeQueryBuilder.gte(Integer.parseInt(split[0]));
                    if(!StringUtils.isEmpty(split[1]))
                        rangeQueryBuilder.lte(Integer.parseInt(split[1]));
                    builder.withQuery(QueryBuilders.nestedQuery("rooms",
                            rangeQueryBuilder,
                            ScoreMode.None));
                }
            }

            // 分页显示
            int curPage = 1, pageSize = 3;
            String curPageStr = searchMap.get("page");
            if (!StringUtils.isEmpty(curPageStr)) {
                curPage = Integer.parseInt(curPageStr);
            }
            builder.withPageable(PageRequest.of(curPage - 1, pageSize));

            // 排序实现
            String sortField = searchMap.get("sortField");
            String sortRule = searchMap.get("sortRule");
            if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule))
            {
                builder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule.toUpperCase())));
            }

            /* 结果集高亮 */
            HighlightBuilder highlightBuilder = new HighlightBuilder().field("hotelName");
            // 高亮前缀
            highlightBuilder.preTags("<em style=\"color:red\">");
            // 高亮后缀
            highlightBuilder.postTags("</em>");
            // 碎片长度
            highlightBuilder.fragmentSize(100);
            // 引入高亮
            builder.withHighlightBuilder(highlightBuilder);
        }

        // 执行查询，获得高亮和非高亮数据
        AggregatedPage<HotelInfo> page =
                elasticsearchTemplate.queryForPage(
                        builder.build(),
                        HotelInfo.class,
                        new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        List<T> list = new ArrayList<>();
                        for (SearchHit hit: searchResponse.getHits()) {
                            // 分析结果集 获取非高亮数据
                            HotelInfo hotelInfo = JSON.parseObject(hit.getSourceAsString(), HotelInfo.class);

                            // 分析结果集 获取高亮数据
                            HighlightField highlightField = hit.getHighlightFields().get("hotelName");
                            // 找到被高亮的域, 进行替换
                            if(highlightField != null && highlightField.getFragments() != null){
                                // 获取高亮数据
                                Text[] fragments = highlightField.getFragments();
                                StringBuffer buffer = new StringBuffer();
                                for (Text fragment: fragments) {
                                    buffer.append(fragment.toString());
                                }
                                // 将非高亮数据中的指定的域替换成高亮数据
                                hotelInfo.setHotelName(buffer.toString());
                            }
                            list.add((T) hotelInfo);
                        }
                        // 返回数据: 数据List, 分页信息, 总数
                        return new AggregatedPageImpl<T>(list, pageable, searchResponse.getHits().getTotalHits());
                    }
                });

        List<HotelInfo> hotels = page.getContent();

        /*
        * 查询附属信息
        * 例如：
        *   酒店类型 ：星级酒店，快捷酒店，连锁酒店，普通旅馆，民宿
        *   酒店标签 ：景点附近，安静，快速入住等等
        *   酒店地址多层级别地址 ： 如东莞虎门的酒店，应该可以进行筛选东莞其他地区，或广东其他地区。或中国其他地区，就需要通过上一级数据获取
        *   等等。
        * */

        /* 主要列表 */
        resultMap.put("rows", hotels);
        /* 附属信息: 页数 */
        resultMap.put("totalPages", page.getTotalPages());
        /* 附属信息: 元素个数 */
        resultMap.put("totalElements", page.getTotalElements());
        return resultMap;
    }
}
