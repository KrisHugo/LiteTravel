package com.LiteTravel.region.pojo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
/*
* ApiModel 使用Swagger自动生成Api文档
* Table 使之与数据库中的表对应
* */
@Data
@ApiModel(description = "Region Address For all", value = "Region")
@Table(name = "global_region")
public class Region {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "sname")
    private String sname;
    @Column(name = "level")
    private Integer level;
    @Column(name = "citycode")
    private String citycode;
    @Column(name = "yzcode")
    private String yzcode;
    @Column(name = "mername")
    private String mername;
    @Column(name = "lng")
    private Float lng;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "pinyin")
    private String pinyin;

}