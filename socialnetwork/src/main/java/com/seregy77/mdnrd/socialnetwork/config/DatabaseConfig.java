package com.seregy77.mdnrd.socialnetwork.config;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DatabaseConfig {
    @Bean
    public OrientDB orientDB() {
        OrientDB orientDB = new OrientDB("memory:social_network", OrientDBConfig.defaultConfig());
        orientDB.createIfNotExists("social_network", ODatabaseType.MEMORY);

        return orientDB;
    }

    @Bean
    public ODatabaseSession oDatabaseSession(OrientDB orientDB) {
        ODatabaseSession session = orientDB.open("social_network", "admin", "admin");
        session.activateOnCurrentThread();
        return session;
    }
}
