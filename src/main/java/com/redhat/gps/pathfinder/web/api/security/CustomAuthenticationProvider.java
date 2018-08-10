package com.redhat.gps.pathfinder.web.api.security;

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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.MembersRepository;
import com.redhat.gps.pathfinder.service.util.RequestUtils;

@Component
public class CustomAuthenticationProvider implements AuthenticationManager {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
  
    public CustomAuthenticationProvider(MembersRepository userRepository){
      this.userRepository=userRepository;
    }
    MembersRepository userRepository;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        log.debug("CustomAuthenticationProvider::authenticate() name={}, password={}", name, password);
        
        Member user=userRepository.findOne(name);
        
        if (null==user) return null;
        
        log.debug("CustomAuthenticationProvider::authenticate():: user={}, password match?={}", name, (password.equals(user.getPassword())));
        
        if (password.equals(user.getPassword())){
          return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        }else{
           throw new RuntimeException("login error - user not known or password incorrect");
        }
    }
 
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(
//          UsernamePasswordAuthenticationToken.class);
//    }
}
