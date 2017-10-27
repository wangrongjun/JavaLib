package com.wangrj.java_lib.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * by wangrongjun on 2017/9/2.
 */
public class MongoTest {

    public static void main(String[] aaa) {
        MongoClient mongoClient = MongoUtil.getMongoClient();
        MongoDatabase wang = mongoClient.getDatabase("wang");
        MongoCollection<Document> col = wang.getCollection("col");

        FindIterable<Document> documents = col.find();
        for (Document document : documents) {
            System.out.println(document);
        }

        mongoClient.close();
    }

}
