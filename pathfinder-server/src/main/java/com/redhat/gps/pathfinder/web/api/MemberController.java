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

import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.web.api.model.MemberType;

public class MemberController{
  
  
  // TOOD: Later on I will add all member REST api operations here
  
  public static MemberType populate(Member member, MemberType result){
    result.setUsername(member.getUsername());
    result.setDisplayName(member.getDisplayName());
    result.setEmail(member.getEmail());
//    result.setPassword(member.getPassword());
//    result.setCustomerId(member.getCustomer().getId());
    return result;
  }
}
