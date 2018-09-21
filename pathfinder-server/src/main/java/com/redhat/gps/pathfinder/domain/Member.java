package com.redhat.gps.pathfinder.domain;

import java.io.Serializable;

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

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "member")
public class Member implements Serializable{
  private static final long serialVersionUID=-7923097463946026030L;

  @Id
  @NotNull
  @Field("username")
  private String username;

  @Field("displayname")
  private String displayName;

  @Field("email")
  private String email;

  @Field("password")
  private String password;
  
  @Field("privileges")
  private List<String> privileges;
  
  @Field("roles")
  private List<String> roles;
  
//  @DBRef
//  private Customer customer;
  @Field("customerId")
  private String customerId;


  public String getUsername(){
    return username;
  }

  public String getDisplayName(){
    return displayName;
  }

  public String getEmail(){
    return email;
  }

  public String getPassword(){
    return password;
  }

  public String getCustomerId(){
    return customerId;
  }

  public void setUsername(String username){
    this.username=username;
  }

  public void setDisplayName(String displayName){
    this.displayName=displayName;
  }

  public void setEmail(String email){
    this.email=email;
  }

  public void setPassword(String password){
    this.password=password;
  }

  public void setCustomerId(String customerId){
    this.customerId=customerId;
  }
  
  public List<String> getPrivileges(){
    return privileges;
  }

  public void setPrivileges(List<String> privileges){
    this.privileges=privileges;
  }

  public List<String> getRoles(){
    return roles;
  }

  public void setRoles(List<String> roles){
    this.roles=roles;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Member xThis = (Member) o;
      return Objects.equals(username, xThis.username) &&
          Objects.equals(email,       xThis.email);
  }

  @Override
  public int hashCode() {
      return Objects.hash(username, displayName, password, email);
  }

  @Override
  public String toString() {
      return "Member{" +
          "username='" + username + '\'' +
          ", displayname='" + displayName + '\'' +
          ", password='" + password.replaceAll(".", "*") + '\'' +
          ", email='" + email+ '\'' +
          ", roles='" + roles+ '\'' +
          ", privileges='" + privileges+ '\'' +
          '}';
  }
  
}
