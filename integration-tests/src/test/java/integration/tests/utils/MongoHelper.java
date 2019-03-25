package integration.tests.utils;

import com.mongodb.MongoClient;

public class MongoHelper {
    private String db;
    private int port;
    private MongoClient mongoClient;

    public MongoHelper(String db, int port) {
        this.db = db;
        this.port = port;
    }

    public MongoHelper(String db) {
        this.db = db;
        this.port = 27017;
    }

    public void start() {
        mongoClient = new MongoClient("localhost", port);
    }

    public void stop() {
    }

    public void dropCollection(String collectionName) {
        mongoClient.getDatabase(collectionName).drop();
    }
}
