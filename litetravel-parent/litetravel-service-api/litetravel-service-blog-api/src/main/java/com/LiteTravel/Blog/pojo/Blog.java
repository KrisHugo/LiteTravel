package com.LiteTravel.Blog.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@ApiModel(description = "Blog Main Info", value = "Blog")
@Table(name = "travel_blog")
public class Blog {
    @Id
    @Column(name = "blog_id")
    private Integer blogId;

    @Column(name = "blog_title")
    private String blogTitle;

    @Column(name = "blog_post_time")
    private Date blogPostTime;

    @Column(name = "blog_modify_time")
    private Date blogModifyTime;

    @Column(name = "blog_poster_id")
    private Integer blogPosterId;

    @Column(name = "blog_img_uri")
    private String blogImgUri;

    @Column(name = "blog_like_count")
    private Integer blogLikeCount;

    @Column(name = "blog_comment_count")
    private Integer blogCommentCount;

    @Column(name = "blog_content")
    private String blogContent;
}