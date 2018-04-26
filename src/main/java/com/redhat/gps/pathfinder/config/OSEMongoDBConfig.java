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

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@ConfigurationProperties
public class OSEMongoDBConfig {
    private final Logger log = LoggerFactory.getLogger(OSEMongoDBConfig.class);

    private MongoClient mongo;

    private final MongoClientOptions options;

    private final Environment environment;

    @Value("${database-user}")
    private String username;

    @Value("${database-password}")
    private String password;

    @Value("${database-name}")
    private String dbname;

    @Value("${database-admin-password}")
    private String adminpwd;

    @Value("${dbhostname:mongodb}")
    private String dbhost;

    @Value("${dbhostport:27017}")
    private int dbport;

    public OSEMongoDBConfig(ObjectProvider<MongoClientOptions> options, Environment environment) {
        this.options = options.getIfAvailable();
        this.environment = environment;
    }

    protected String getDatabaseName() {
        return this.dbname;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getHostname() {
        return this.dbhost;
    }

    public int getPort() {
        return this.dbport;
    }

    public String getAdminPassword() {
        return this.adminpwd;
    }

    @Bean
    public MongoClient mongo() throws UnknownHostException {
        MongoClientOptions tmpoptions;
        if (options == null) {
            tmpoptions = MongoClientOptions.builder().build();
        } else {
            tmpoptions = options;
        }
        this.mongo = createMongoClient(tmpoptions);
        return this.mongo;
    }

    private MongoClient createMongoClient(MongoClientOptions options) {

        List<MongoCredential> credentials = new ArrayList<MongoCredential>();

        credentials.add(MongoCredential.createCredential(this.username, this.dbname,
            this.password.toCharArray()));

        MongoClientURI dburi = new MongoClientURI(this.createMongoURL());

        return new MongoClient(dburi);

//        return new MongoClient(
//            Collections.singletonList(new ServerAddress(this.dbhost, this.dbport)), credentials,
//            options);

    }

    //mongodb://dbuser:dbuser12345@mongodb/pathfinder?authSource=admin

    private String createMongoURL(){
        StringBuilder res = new StringBuilder();
        res.append("mongodb://")
            .append(this.username)
            .append(":")
            .append(this.password)
            .append("@")
            .append(this.dbhost)
            .append(":")
            .append(this.dbport)
            .append("/")
            .append(this.dbname)
            .append("?authSource=")
            .append(this.dbname);
        log.debug("Mongo URL=============>"+res.toString());
        return res.toString();
    }

    @Override
    public String toString() {
        return "OSEMongoDBConfig{" +
            "options=" + options +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", dbname='" + dbname + '\'' +
            ", adminpwd='" + adminpwd + '\'' +
            ", dbhost='" + dbhost + '\'' +
            ", dbport=" + dbport +
            '}';
    }
}
