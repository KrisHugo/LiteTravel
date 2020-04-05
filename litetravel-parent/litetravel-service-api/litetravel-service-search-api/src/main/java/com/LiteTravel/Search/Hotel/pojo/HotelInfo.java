package com.LiteTravel.Search.Hotel.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Document(indexName = "hotel", type = "docs")
public class HotelInfo {
    /*
    * type 类型, Text支持分词
    * index 是否分词
    * analyzer 创建索引时使用的分词器
    * store 是否存储
    * searchAnalyzer 搜索时候使用的分词器
    * */
    @Id
    private Integer hotelId;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String hotelName;
    @Field(type = FieldType.Float)
    private Integer hotelMinPrice;
    @Field(type = FieldType.Integer)
    private Integer hotelManagerId;
    @Field(type = FieldType.Text, index = false)
    private String hotelPhone;
    @Field(type = FieldType.Integer)
    private Integer hotelReplyLevel;
    @Field(type = FieldType.Text)
    private Integer hotelAddress;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String hotelAddressString;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String hotelAddressSpecific;
    @Field(type = FieldType.Text, index = false)
    private String hotelImgUri;
    @Field(type = FieldType.Integer)
    private Integer hotelReplyCount;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String hotelDescription;
    @Field(type = FieldType.Nested)
    private List<RoomInfo> rooms;
}
