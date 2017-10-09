package com.wangrg.java_lib.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.wangrg.java_lib.java_util.TextUtil;

import java.util.Arrays;

/**
 * by wangrongjun on 2017/9/2.
 */
public class MongoUtil {

    public static class Config {
        private String host;
        private int port;
        private String username;
        private String password;
        private String database;

        String getHost() {
            return host;
        }

        public Config setHost(String host) {
            this.host = host;
            return this;
        }

        int getPort() {
            return port;
        }

        public Config setPort(int port) {
            this.port = port;
            return this;
        }

        String getUsername() {
            return username;
        }

        public Config setUsername(String username) {
            this.username = username;
            return this;
        }

        String getPassword() {
            return password;
        }

        public Config setPassword(String password) {
            this.password = password;
            return this;
        }

        String getDatabase() {
            return database;
        }

        public Config setDatabase(String database) {
            this.database = database;
            return this;
        }
    }

    public static MongoClient getMongoClient() {
        return getMongoClient(new Config().setHost("localhost").setPort(27017));
    }

    public static MongoClient getMongoClient(Config config) {
        MongoClient mongoClient;
        if (TextUtil.isEmpty(config.getUsername())) {
            mongoClient = new MongoClient(config.getHost(), config.getPort());
        } else {
            ServerAddress serverAddress = new ServerAddress(config.getHost(), config.getPort());
            MongoCredential credential = MongoCredential.createCredential(
                    config.getUsername(),
                    config.getDatabase(),
                    config.getPassword().toCharArray()
            );
            mongoClient = new MongoClient(Arrays.asList(serverAddress), Arrays.asList(credential));
        }
        return mongoClient;
    }

}
