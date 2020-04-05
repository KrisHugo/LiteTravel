package com.LiteTravel.Search.Hotel.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
@Document(indexName = "room", type = "docs")
public class RoomInfo implements Serializable {
    @Id
    private Integer roomId;
    @Field(type = FieldType.Integer)
    private Integer hotelId;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String roomName;
    @Field(type = FieldType.Double)
    private Double roomPrice;

    private Integer roomRemaining;

    private Integer roomMax;

    private Integer roomBookMax;

    private Integer roomCancel;

    private Integer roomSize;

    @Field(type = FieldType.Keyword)
    private Byte roomWifi;
    @Field(type = FieldType.Double)
    private Double roomBedAdd;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String roomDescription;
    @Field(type = FieldType.Nested)
    private List<BedInfo> beds;
}
