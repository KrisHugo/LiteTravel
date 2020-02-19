package com.LiteTravel.User.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ApiModel(description = "User Account", value = "User")
@Table(name = "travel_user_account")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_state")
    private Integer userState;
}