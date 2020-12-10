package com.aaron.ren.spring.xmltype;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserMapper {
  @Select("select * from user")
  List<Map<String,String>> getAllUser();


  @Insert("insert  into user (name) value ('nijao')")
  void saveUser();
}
