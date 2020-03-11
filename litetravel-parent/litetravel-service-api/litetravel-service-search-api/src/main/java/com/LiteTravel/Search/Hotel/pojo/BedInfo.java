package com.LiteTravel.Search.Hotel.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@Document(indexName = "bedInfo", type = "docs")
public class BedInfo {
    @Id
    private Integer bedId;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String bedName;
    @Field(type = FieldType.Double)
    private Double bedSize;
    @Field(type = FieldType.Integer)
    private Integer bedCount;// 对应酒店房间提供的该床的数量
}
