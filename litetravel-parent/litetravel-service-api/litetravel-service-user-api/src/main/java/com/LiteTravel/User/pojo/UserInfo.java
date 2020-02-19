package com.LiteTravel.User.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@ApiModel(description = "User Info", value = "UserInfo")
@Table(name = "travel_user_info")
public class UserInfo {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_gender")
    private Integer userGender;
    @Column(name = "user_birth")
    private Date userBirth;
    @Column(name = "user_phone")
    private String userPhone;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "user_address")
    private Integer userAddress;
    @Column(name = "user_address_specific")
    private String userAddressSpecific;
    @Column(name = "user_avatar_uri")
    private String userAvatarUri;
}