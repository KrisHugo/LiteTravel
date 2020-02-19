package com.LiteTravel.Blog.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@ApiModel
@Table(name = "travel_blog_comment")
public class Comment {
    @Id
    private Integer commentId;


    private Integer parentType;

    private Integer parentId;

    private Date commentPostTime;

    private Date commentModifyTime;

    private Integer commentPosterId;

    private Integer commentLikeCount;

    private String commentContent;
}