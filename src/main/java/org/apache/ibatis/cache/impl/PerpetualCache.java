package org.apache.ibatis.cache.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;

/**
 * 一级缓存叫localCache,它的封装类叫PerpetualCache 里面维护了一个String 类型的id, 和一个hashMap
 * 取名字也很讲究,perpetual意味永不间断,事实上确实如此,一级缓存默认存在,
 * 也关不了(至少我真的不知道),但是在与Spring整合时,Spring把这个缓存给关了,这并不奇怪,因为spring 直接干掉了这个sqlSession
 * @author Clinton Begin
 */
public class PerpetualCache implements Cache {

  /**
   * 标识
   */
  private final String id;

  /**
   * 缓存容器
   */
  private Map<Object, Object> cache = new HashMap<>();

  public PerpetualCache(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getSize() {
    return cache.size();
  }

  @Override
  public void putObject(Object key, Object value) {
    cache.put(key, value);
  }

  @Override
  public Object getObject(Object key) {
    return cache.get(key);
  }

  @Override
  public Object removeObject(Object key) {
    return cache.remove(key);
  }

  @Override
  public void clear() {
    cache.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (getId() == null) {
      throw new CacheException("Cache instances require an ID.");
    }
    if (this == o) {
      return true;
    }
    if (!(o instanceof Cache)) {
      return false;
    }

    Cache otherCache = (Cache) o;
    return getId().equals(otherCache.getId());
  }

  @Override
  public int hashCode() {
    if (getId() == null) {
      throw new CacheException("Cache instances require an ID.");
    }
    return getId().hashCode();
  }

}
