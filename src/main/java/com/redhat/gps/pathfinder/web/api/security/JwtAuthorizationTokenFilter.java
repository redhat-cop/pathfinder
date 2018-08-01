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


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;

    public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, /*@Value("${jwt.header}")*/ @Value("Authorization") String tokenHeader) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("processing authentication for '{}'", request.getRequestURL());
        
//        String requestHeader = request.getHeader(tokenHeader);
        
        String username = null;
        String authToken=null;
        if (authToken==null) authToken = request.getHeader("Authorization");
//        if (authToken==null) authToken = request.getHeader("Access-Control-Request-Headers");
        if (authToken==null) authToken = request.getParameter("_t");
        
        
        if (authToken!=null && authToken.startsWith("Bearer "))
          authToken=authToken.substring(7);
        
        logger.debug("Discovered token: {}", authToken);
        
//        if (authToken==null || "".equals(authToken))
//          throw new AccessDeniedException("Access is denied");
        
        if (authToken!=null && !"".equals(authToken)){
          try {
            username = jwtTokenUtil.getUsernameFromToken(authToken);
          } catch (IllegalArgumentException e) {
              logger.error("an error occured during getting username from token", e);
          } catch (ExpiredJwtException e) {
              logger.warn("the token is expired and not valid anymore", e);
          }
        }else{
          logger.warn("couldn't find bearer string, will ignore the header");
        }
        
//        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
//            authToken = requestHeader.substring(7);
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(authToken);
//            } catch (IllegalArgumentException e) {
//                logger.error("an error occured during getting username from token", e);
//            } catch (ExpiredJwtException e) {
//                logger.warn("the token is expired and not valid anymore", e);
//            }
//        } else {
//            logger.warn("couldn't find bearer string, will ignore the header");
//        }

        logger.debug("checking authentication for user '{}'", username);
        
//        logger.debug("XXXXXXXXXXXXXXXX "+SecurityContextHolder.getContext().getAuthentication().getName());
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        boolean wasNull=SecurityContextHolder.getContext().getAuthentication() == null;
        boolean wasAnon=!wasNull?SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"):false;
        
        if (username != null && (wasNull || wasAnon)) {
            logger.debug("security context was "+(wasNull?"null":"anonymous")+", so authorizing user");
            
            
//            logger.debug("XXXXXXXX userdetailsservice="+this.userDetailsService);
            
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            logger.debug("XXXXXXXX userdetails found for "+username+" = "+userDetails);
            
            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authorized user '{}', setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
              logger.debug("XXXXXXXX jwt token was NOT valid");
            }
        }

        chain.doFilter(request, response);
    }
}
