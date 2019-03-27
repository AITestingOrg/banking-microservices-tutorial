package integration.tests.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoHelper {
    private String db;
    private int port;
    private MongoClient mongoClient;
    private static final String ACCOUNT_QUERY_COLLECTION = "Account";

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

    public void dropDb() {
        mongoClient.getDatabase(db).drop();
    }

    public String createAccount(String personId, double balance) {
        ObjectId id = ObjectId.get();
        Document dbObject = new Document();
        dbObject.put("id", id);
        dbObject.put("customerId", personId);
        dbObject.put("balance", balance);
        mongoClient.getDatabase(db).getCollection(ACCOUNT_QUERY_COLLECTION).insertOne(dbObject);
        return id.toHexString();
    }

    public String createAccount(String personId) {
        return createAccount(personId, 0.0);
    }
}
