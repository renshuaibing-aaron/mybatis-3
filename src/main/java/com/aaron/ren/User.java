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

  public int getId()
  {
    return id;
  }
  public void setId(int id)
  {
    this.id = id;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName(String username)
  {
    this.userName = username;
  }

  public int getUserAge()
  {
    return userAge;
  }

  public void setUserAge(int userAge)
  {
    this.userAge = userAge;
  }


  public String getuserAddress()
  {
    return userAddress;
  }

  public void setUserAddress(String userAddress)
  {
    this.userAddress = userAddress;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + userName + ", userAge=" + userAge + ", address=" + userAddress + "]";
  }
}
