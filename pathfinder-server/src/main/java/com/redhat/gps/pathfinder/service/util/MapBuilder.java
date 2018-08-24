package com.redhat.gps.pathfinder.service.util;

/*-
 * #%L
 * Pathfinder
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
