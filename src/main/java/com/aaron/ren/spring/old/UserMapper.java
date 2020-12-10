package com.aaron.ren.spring.old;

import com.aaron.ren.normal.common.UserEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {
  UserEntity getUser(int id);
  List<UserEntity> getAllUser();
  List<UserEntity> addUser();
}
