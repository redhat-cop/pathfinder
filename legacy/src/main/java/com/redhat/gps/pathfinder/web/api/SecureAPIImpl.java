package com.redhat.gps.pathfinder.web.api;

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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Joiner;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.MembersRepository;
import com.redhat.gps.pathfinder.web.api.security.JwtTokenUtil;

public class SecureAPIImpl{
  private final Logger log = LoggerFactory.getLogger(SecureAPIImpl.class);
  
  public SecureAPIImpl(MembersRepository membersRepo){
    this.membersRepo=membersRepo;
  }
  
//  @Autowired
//  private JwtTokenUtil jwtTokenUtil;
  
  @Autowired
  HttpServletRequest request;

  private MembersRepository membersRepo;

  /**
   * Lazy init current user because I expect it to be called multiple times per request and don't want to repeatedly hit the datastore 
   */
  private Member _currentUser=null;
  protected Member getCurrentUser(){
    if (null==_currentUser || !SecurityContextHolder.getContext().getAuthentication().getName().equals(_currentUser.getUsername())){
//    String tusername=jwtTokenUtil.getUsernameFromToken(token);
      String username=SecurityContextHolder.getContext().getAuthentication().getName();
      // Shouldnt be possible to have a null username, otherwise Spring Security's filter chain has messed up again
      _currentUser=membersRepo.findOne(username);
      log.debug("_currentUser is {}", _currentUser);
    }
    return _currentUser;
  }
  
  public boolean isAuthorizedFor(Customer c){
    Member currentUser=getCurrentUser();
    boolean result= currentUser.getRoles().contains("SUPER")
              || 
           (currentUser.getRoles().contains("ADMIN") 
            && currentUser.getCustomer()!=null 
            && currentUser.getCustomer().getId().equals(c.getId()));
//    log.debug("AUTH: user->{}, roles->{}, customer->{}, auth->{}", currentUser.getUsername(), Joiner.on(",").join(currentUser.getRoles()), c.getName(), result);
    return result;
  }
  
}
