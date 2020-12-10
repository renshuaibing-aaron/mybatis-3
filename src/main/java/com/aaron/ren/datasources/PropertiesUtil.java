package com.aaron.ren.datasources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

  private static Properties configObj = new Properties();

  static{
    // 使用类加载器读取的文件要放在src下
    InputStream connectionPool = Thread.currentThread().getContextClassLoader().getResourceAsStream("pool.properties");
    InputStreamReader inputStreamReader = new InputStreamReader(connectionPool);
    try {
      configObj.load(inputStreamReader);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static String getValue(String key){
    return configObj.getProperty(key);
  }
}
