package com.redhat.gps.pathfinder.web.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redhat.gps.pathfinder.domain.Customer;

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
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.MembersRepository;
import com.redhat.gps.pathfinder.web.api.model.IdentifierList;
import com.redhat.gps.pathfinder.web.api.model.MemberType;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/pathfinder/")
public class MemberController extends SecureAPIImpl{
  
	private final Logger log = LoggerFactory.getLogger(MemberController.class);
  private final CustomerRepository custRepo;
  private final MembersRepository membersRepo;

  public MemberController(CustomerRepository custRepo, MembersRepository membersRepository) {
    super(membersRepository);
    this.custRepo = custRepo;
    this.membersRepo=membersRepository;
  }

  private static MemberType populate(Member member, MemberType result){
    result.setUsername(member.getUsername());
    result.setDisplayName(member.getDisplayName());
    result.setEmail(member.getEmail());
    return result;
  }
  
  // Get Members
  // GET: /api/pathfinder/customers/{customerId}/member/
//  public ResponseEntity<List<MemberType>> customersCustIdMembersGet(@ApiParam(value="", required=true) @PathVariable("custId") String custId){
  public ResponseEntity<List<MemberType>> getMembers(String custId){
  	log.info("getMembers...");
    List<MemberType> result=new ArrayList<MemberType>();

    Customer customer=custRepo.findOne(custId);

    if (customer == null) {
      log.error("customersCustIdMembersGet....customer not found " + custId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    if (null==customer.getMembers())
      customer.setMembers(new ArrayList<>());
    
    for(Member m:customer.getMembers()){
      MemberType member=new MemberType();
      member.setUsername(m.getUsername());
      member.setDisplayName(m.getDisplayName());
//      member.setId(m.getId());
      member.setEmail(m.getEmail());
      member.setPassword(m.getPassword());
      member.setCustomerId(customer.getId());
//      member.setCustomer(m.get);
      result.add(member);
    }
    
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  
  // Create Member
  // POST: /api/pathfinder/customers/{customerId}/members/
//  public ResponseEntity<String> customersCustIdMembersPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Member Details"  )  @Valid @RequestBody MemberType body) {
  public ResponseEntity<String> createMember(String custId, MemberType body) {
    log.debug("createMember....");
    return createOrUpdateMember(custId, null, body);
  }
  
  // Get Member
  // GET: /api/pathfinder/customers/{customerId}/members/{memberId}
//  public ResponseEntity<MemberType> customersCustIdMembersMemberIdGet(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Member Identifier",required=true ) @PathVariable("memberId") String memberId) {
  public ResponseEntity<MemberType> getMember(String custId, String memberId) {
  	log.info("getMember...");
  	Customer customer=custRepo.findOne(custId);
    if (customer == null) {
      log.error("customersCustIdMembersMemberIdGet....customer not found {}", custId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    Member member=membersRepo.findOne(memberId);
    if (null==member){
      log.error("customersCustIdMembersMemberIdGet....member not found {}", memberId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    if (!customer.getMembers().contains(member)){
      log.error("customersCustIdMembersMemberIdGet....member {} is not child of customer {} ", memberId, custId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<>(populate(member, new MemberType()), HttpStatus.OK);
  }
  
  // Update Member
  // POST: /api/pathfinder/customers/{customerId}/members/{memberId}
//  public ResponseEntity<String> customersCustIdMembersMemberIdPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId, @ApiParam(value = "Member Identifier",required=true ) @PathVariable("memberId") String memberId,@ApiParam(value = "Member Details"  )  @Valid @RequestBody MemberType body) {
  public ResponseEntity<String> updateMember(String custId, String memberId, MemberType body){
    log.debug("updateMember....");
    return createOrUpdateMember(custId, memberId, body);
  }
  
  private ResponseEntity<String> createOrUpdateMember(String custId, String existingUsername, MemberType body){
    Customer customer=custRepo.findOne(custId);
    
    if (customer == null) {
      log.error("createOrUpdateMember....customer not found " + custId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    Member member;
    if (existingUsername==null){
      member=new Member();
      member.setUsername(body.getUsername());
//      member.setId(UUID.randomUUID().toString());
    }else{
      member=membersRepo.findOne(existingUsername);
    }
    
//    Member newMember=new Member();
//    newMember.setId(UUID.randomUUID().toString());
    member.setDisplayName(body.getDisplayName());
    if (!StringUtils.isEmpty(body.getPassword())){
      member.setPassword(body.getPassword());
    }
    member.setEmail(body.getEmail());
    member.setRoles(Arrays.asList("ADMIN")); // SUPER, ADMIN OR USER
    member.setPrivileges(Arrays.asList("ALL")); // can add apps etc... not currently used
    
    member.setCustomerId(customer.getId());
    membersRepo.save(member);
    
    if (existingUsername==null){
      if (null==customer.getMembers())
        customer.setMembers(new ArrayList<>());
      
      customer.getMembers().add(member);
      custRepo.save(customer);
    }
    
    return new ResponseEntity<String>(HttpStatus.OK);
  }


  // Delete Member(s)
//  public ResponseEntity<String> customersCustIdMembersDelete(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Target member IDs"  )  @Valid @RequestBody IdentifierList body) {
  public ResponseEntity<String> deleteMembers(String custId, IdentifierList body) {
  	log.info("customersCustIdMembersDelete...");
    Customer customer=custRepo.findOne(custId);
    
    if (customer == null) {
      log.error("customersCustIdMembersPost....customer not found " + custId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    if (null==customer.getMembers())
      customer.setMembers(new ArrayList<>());
    
    body.forEach((id)-> {
      log.debug("Deleting Member "+id);
      List<Member> newMembers=new ArrayList<>();
      for(Member m:customer.getMembers()){
        if (!m.getUsername().equals(id))
          newMembers.add(m);
      }
      customer.setMembers(newMembers);
    });
    custRepo.save(customer);
    
    return new ResponseEntity<String>(HttpStatus.OK);
  }
}
