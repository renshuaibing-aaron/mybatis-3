package com.aaron.ren.spring;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {
  @Select("select * from user")
  List<Map<String,String>> findAll();
}
