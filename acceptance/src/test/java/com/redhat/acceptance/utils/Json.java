/*
* JBoss, Home of Professional Open Source
* Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
* contributors by the @authors tag. See the copyright.txt in the
* distribution for a full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.redhat.acceptance.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Json {
  public static String toJson(Object o) throws JsonGenerationException, JsonMappingException, IOException{
    if (o==null) return "<null>";
    ObjectMapper mapper = new ObjectMapper();
//    mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    mapper.writeValue(baos, o);
    return new String(baos.toByteArray());
  }
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> T toObject(String payload, Class clazz) throws JsonParseException, JsonMappingException, IOException {
    return (T)new ObjectMapper().readValue(new ByteArrayInputStream(payload.getBytes()), clazz);
  }
}
