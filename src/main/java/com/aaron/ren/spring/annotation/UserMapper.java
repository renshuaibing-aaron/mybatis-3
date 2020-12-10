package com.aaron.ren.spring.annotation;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {
  @Select("select * from user")
  List<Map<String,String>> getAllUser();
}
