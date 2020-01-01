package com.redhat.acceptance.utils;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K,V> {
  private Map<K, V> s=new HashMap<K, V>();
  
  public MapBuilder<K,V> put(K key, V value){
    s.put(key, value);
    return this;
  }
  
  public Map<K, V> build(){
    return s;
  }

  public MapBuilder<K,V> putAll(Map<K, V> values) {
    s.putAll(values);
    return this;
  }
}