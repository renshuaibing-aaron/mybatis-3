package com.aaron.ren.normal;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
  private Integer id;
  private Date birdate;
  private String name;
}
