package com.aaron.ren.spring.sqlsessiontemplate;

import com.aaron.ren.normal.common.UserEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {

  UserEntity getUser(int id);
}
