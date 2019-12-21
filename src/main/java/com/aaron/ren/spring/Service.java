package com.aaron.ren.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Service {

  @Autowired
  private UserMapper userMapper;

  public Object findAll(){
    List<Map<String, String>> all = userMapper.findAll();

    return all;
  }

}
