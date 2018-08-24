package com.redhat.pathfinder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class Json {

  public static ObjectMapper newObjectMapper(boolean pretty){
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT,pretty);
    return mapper;
  }
}
