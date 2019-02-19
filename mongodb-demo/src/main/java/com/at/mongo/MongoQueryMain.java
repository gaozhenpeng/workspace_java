package com.at.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoQueryMain {
    private static final Logger logger = LoggerFactory.getLogger(MongoQueryMain.class);
    
    private static final String serverHost = "localhost";
    private static final Integer serverPort = 27017;
    private static final String username = "test";
    private static final String authenticationDatabase = "test";
    private static final String password = "test";
    
    private static final String databaseName = "test";
    
    public static void main(String[] args) {
        MongoClient mongoClient = null;
        try {
            
            // 1. server address
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            
            ServerAddress serverAddress = new ServerAddress(serverHost, serverPort);
            addrs.add(serverAddress);

            // 2. mongo credential
            List<MongoCredential> credentialList = new ArrayList<MongoCredential>();
            
            MongoCredential credential = MongoCredential.createScramSha1Credential(username, authenticationDatabase, password.toCharArray());
            credentialList.add(credential);

            // connection
            //    WARNING: if the credential is not available, no exception will be thrown here.
            logger.debug("before new client");
            mongoClient = new MongoClient(addrs, credentialList);
            logger.debug("after new client");

            // switch to database
            logger.debug("before getDatabase");
            MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
            logger.debug("before listCollectionNames");
            // throw exception when calling listCollectionNames() if authentication failed.
            for(String collectionName : mongoDatabase.listCollectionNames()) {
                logger.info("Collection Name: '{}'", collectionName);
                MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
                Integer i = 1;
                for(Document doc : collection.find()) {
                    logger.info("\t/* {} */", i);
                    if(i == 1) {
                        logger.info("\tjson: '{}'", doc.toJson());
                    }else {
                        logger.info("\t_id : {}", doc.getObjectId("_id").toHexString());
                    }
                    logger.info("\n");
                    i++;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("mongo query exception.", e);
        }finally {
            if(mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}
