package com.LiteTravel.User.service.impl;

import com.LiteTravel.User.pojo.User;
import com.LiteTravel.User.service.AccountService;
import com.LiteTravel.User.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> findList(User user) {
        return userMapper.selectByExample(createExample(user));
    }


    @Override
    public PageInfo<User> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(userMapper.selectAll());
    }

    @Override
    public PageInfo<User> findPage(Integer page, Integer size, User user) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(userMapper.selectByExample(createExample(user)));
    }

    @Override
    public User findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void add(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delete(Integer userId) {
        userMapper.deleteByPrimaryKey(userId);
    }

    private Example createExample(User user) {
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        if (!StringUtils.isEmpty(user.getUserCode()))
        {
            criteria.andLike("userCode", user.getUserCode());
        }
        if (!StringUtils.isEmpty(user.getUserPassword()))
        {
            criteria.andEqualTo("userPassword", user.getUserPassword());
        }
        if (user.getUserState() != null)
        {
            criteria.andEqualTo("userState", user.getUserState());
        }
        return userExample;
    }
}
