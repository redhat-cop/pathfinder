package com.redhat.gps.pathfinder.web.api.security;

import java.io.Serializable;
import java.util.Collection;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.MembersRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService,Serializable {
  private static final long serialVersionUID=-9185903572193525471L;
  
    @Autowired
    MembersRepository userRepository;
  
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member user = userRepository.findOne(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
          return new UserDetails(){
            private static final long serialVersionUID=1L;
            @Override public boolean isEnabled(){                 return true;}
            @Override public boolean isCredentialsNonExpired(){   return true;}
            @Override public boolean isAccountNonLocked(){        return true;}
            @Override public boolean isAccountNonExpired(){       return true;}
            @Override public String getUsername(){                return user.getUsername();}
            @Override public String getPassword(){                return user.getPassword();}
            @Override public Collection<? extends GrantedAuthority> getAuthorities(){
              return Sets.newHashSet(Iterables.transform(user.getRoles(), new Function<String,GrantedAuthority>(){
                @Override
                public GrantedAuthority apply(String role){
                  return new SimpleGrantedAuthority(role);
                }}));
            }
            public String toString(){
              return "UserDetails{username:"+getUsername()+",enabled="+isEnabled()+",authorities="+Joiner.on(",").join(getAuthorities())+"}";
            }
          };
        }
    }
}
