package com.redhat.gps.pathfinder.config;

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


import com.redhat.gps.pathfinder.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterProperties;
//import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

//@KeycloakConfiguration
//@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityKeycloakConfig
//  extends KeycloakWebSecurityConfigurerAdapter
  {
//    private final CorsFilter corsFilter;
//
////    private final SecurityProblemSupport problemSupport;
//
//    private final JHipsterProperties jHipsterProperties;
//
//    public SecurityKeycloakConfig(AuthenticationManagerBuilder authenticationManagerBuilder,
//                                  JHipsterProperties jHipsterProperties, CorsFilter corsFilter) {//}, SecurityProblemSupport problemSupport) {
////        this.authenticationManagerBuilder = authenticationManagerBuilder;
////        this.userDetailsService = userDetailsService;
//        this.jHipsterProperties = jHipsterProperties;
////        this.rememberMeServices = rememberMeServices;
//        this.corsFilter = corsFilter;
////        this.problemSupport = problemSupport;
//    }
//
//
//    /**
//     * Registers the KeycloakAuthenticationProvider with the authentication manager.
//     */
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(keycloakAuthenticationProvider());
//    }
//
//    /**
//     * Defines the session authentication strategy.
//     */
//    @Bean
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new NullAuthenticatedSessionStrategy();
////        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//            .antMatchers(HttpMethod.OPTIONS, "/**")
//            .antMatchers(HttpMethod.GET, "/**")
//            .antMatchers("/app/**/*.{js,html}")
//            .antMatchers("/i18n/**")
//            .antMatchers("/content/**")
//            .antMatchers("/swagger-ui/index.html")
//            .antMatchers("/test/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http
//            .csrf().disable()
//            .addFilterBefore(corsFilter, CsrfFilter.class)
////            .exceptionHandling()
////            .authenticationEntryPoint(problemSupport)
////            .accessDeniedHandler(problemSupport)
////            .and()
//            .authorizeRequests()
//            .antMatchers("/management/health").permitAll()
////            .antMatchers(HttpMethod.POST, "/api/pathfinder/customers/").hasRole(AuthoritiesConstants.ADMIN)
////            .antMatchers(HttpMethod.DELETE, "/api/pathfinder/customers/*").hasRole(AuthoritiesConstants.ADMIN)
//            .antMatchers("/api/pathfinder/customers/**/*").anonymous()// hasRole(AuthoritiesConstants.USER)
//            .anyRequest().authenticated();
//    }
}
