package com.redhat.gps.pathfinder.web.api.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.MembersRepository;

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
  @Autowired
  private JwtAuthenticationEntryPoint authEntryPoint;
  
  @Autowired
  UserDetailsService userDetailsService;
  
  @Autowired
  JwtTokenUtil jwtTokenUtil;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception{
    boolean authentication=true;
    if (authentication) {
//
////      DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
////      provider.setUserDetailsService(new JwtUserDetailsService());
////      http.authenticationProvider(provider);
//      
//      
//      // new
////      http.sessionManagement()
////      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////      .and()
////      .httpBasic()
//////      .realmName(securityRealm)
////      .and().csrf().disable();
//      
//      http.addFilterAfter(new MyFilter(), BasicAuthenticationFilter.class);
      http.addFilterAfter(new JwtAuthorizationTokenFilter(userDetailsService,jwtTokenUtil,"Authorization"), BasicAuthenticationFilter.class);
      
      http.authorizeRequests()
          .antMatchers("/api/pathfinder/survey").permitAll()
          .antMatchers("/auth").permitAll()
          .antMatchers("/api/pathfinder/login").permitAll()
          .antMatchers("/api/pathfinder/**").access("hasAnyAuthority('SUPER','ADMIN','USER')")
//          .antMatchers("/api/pathfinder/").permitAll()
////          .antMatchers("/api/pathfinder/auth").permitAll()
////          .antMatchers("/api/pathfinder/oauth").permitAll()
////          .antMatchers("/api/pathfinder/oauth/**").permitAll()
////          .antMatchers("/api/pathfinder/login").permitAll()
////        //antMatchers("/api/pathfinder/customers/").permitAll().
////          
////          .antMatchers("/api/pathfinder/**").access("hasAnyAuthority('SUPER','ADMIN','USER')")//.accessDecisionManager(accessDecisionManager()).
////          
////          .and().formLogin().loginPage("/login.html")
////                .loginProcessingUrl("/api/pathfinder/login")
////                .usernameParameter("j_username")
////                .passwordParameter("j_password")
////                .defaultSuccessUrl("/api/pathfinder/secure/index.xhtml")
////          
////          .and().logout()
////                .logoutUrl("/api/pathfinder/logout")
////                .logoutSuccessUrl("/api/pathfinder/logout.jsp")
          ;
    }
    http.csrf().disable();
    http.headers().frameOptions().disable();
  }

}
