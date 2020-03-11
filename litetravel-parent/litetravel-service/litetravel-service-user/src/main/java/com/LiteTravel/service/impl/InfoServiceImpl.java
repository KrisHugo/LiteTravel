package com.LiteTravel.service.impl;

import com.LiteTravel.User.pojo.UserInfo;
import com.LiteTravel.mapper.UserInfoMapper;
import com.LiteTravel.service.InfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserInfo> findList(UserInfo userInfo) {
        return userInfoMapper.selectByExample(createExample(userInfo));
    }

    @Override
    public PageInfo<UserInfo> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(userInfoMapper.selectAll());
    }

    @Override
    public PageInfo<UserInfo> findPage(Integer page, Integer size, UserInfo userInfo) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(userInfoMapper.selectByExample(createExample(userInfo)));
    }

    @Override
    public UserInfo findById(Integer userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    /*
    * 此处新建是通过Account微服务同步创建，不允许单独创建UserInfo而没有UserAccount
    * */
    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insertSelective(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public void delete(Integer userId) {
        userInfoMapper.deleteByPrimaryKey(userId);
    }


    private Example createExample(UserInfo userInfo) {
        Example infoExample = new Example(UserInfo.class);
        Example.Criteria criteria = infoExample.createCriteria();
        if (!StringUtils.isEmpty(userInfo.getUserName())){
            criteria.andLike("userName", "%"+ userInfo.getUserName() +"%");
        }
        if (userInfo.getUserAddress() != null){
            criteria.andEqualTo("userAddress", userInfo.getUserAddress());
        }
        if (userInfo.getUserGender() != null){
            criteria.andEqualTo("userGender", userInfo.getUserGender());
        }
        if (userInfo.getUserBirth() != null){
            criteria.andEqualTo("userBirth", userInfo.getUserBirth());
        }
        return infoExample;
    }
}
