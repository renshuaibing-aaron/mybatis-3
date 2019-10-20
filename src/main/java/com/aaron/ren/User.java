package com.aaron.ren;

import lombok.Data;

@Data
public class User {
  private int id;
  private String userName;
  private int userAge;
  private String userAddress;

  public User(){}
  public User(int id, String userName, int userAge, String userAddress)
  {
    this.id = id;
    this.userName = userName;
    this.userAge = userAge;
    this.userAddress = userAddress;
  }

}
