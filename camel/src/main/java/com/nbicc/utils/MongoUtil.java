package com.nbicc.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 大泥藕 on 2017/5/17.
 */


public class MongoUtil {

	
	private MongoClient mongoClient;
	
	private static final String DB_NAME = "bugtrapper";
	
	public MongoUtil(){init();}
	

	public void init(){

		//------------------------------------------------------------------------
		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
		build.connectionsPerHost(50);   //与目标数据库能够建立的最大connection数量为50
//        build.autoConnectRetry(true);   //自动重连数据库启动
		build.threadsAllowedToBlockForConnectionMultiplier(50); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
		build.maxWaitTime(1000*60*2);
		build.connectTimeout(1000*60*1);    //与数据库建立连接的timeout设置为1分钟

		MongoClientOptions myOptions = build.build();
		try {
			//数据库连接实例
			mongoClient = new MongoClient(MongoConfig.MONGODB_IP, myOptions);
		} catch (MongoException e){
			e.printStackTrace();
		}
		//------------------------------------------------------------------------
	}
	@SuppressWarnings("finally")
	public boolean insert(String collectionName,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		boolean result = true;
		try{
			collection.insertOne(object);
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			return result;
		}
	}
	
	public List<BasicDBObject> find(String collectionName,Map<String, Object> queryMap){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		Document queryDocument = new Document(queryMap);
		collection.find(queryDocument).into(result);
		return result;
	}
	
	public List<BasicDBObject> find(String collectionName,BasicDBObject condition){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		collection.find(condition).into(result);
		return result;
	}
	
	public List<BasicDBObject> findAndSort(String collectionName,BasicDBObject condition,BasicDBObject sort){
		List<BasicDBObject> result = new ArrayList<BasicDBObject>();
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		collection.find(condition).sort(sort).into(result);
		return result;
	}
	
	public BasicDBObject get(String collectionName,BasicDBObject condition){
		List<BasicDBObject> result = find(collectionName, condition);
		if(result != null && result.size() == 1){
			return result.get(0);
		}
		return null;
	}
	
	public BasicDBObject get(String collectionName,Map<String, Object> queryMap){
		List<BasicDBObject> result = find(collectionName, queryMap);
		if(result != null && result.size() == 1){
			return result.get(0);
		}
		return null;
	}
	
	public boolean update(String collectionName,Map<String, Object> queryMap,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		Document queryDocument = new Document(queryMap);
		UpdateResult result = collection.replaceOne(queryDocument, object);
		return result.wasAcknowledged();
	}
	
	public boolean update(String collectionName,BasicDBObject query,BasicDBObject object){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		BasicDBObject innerQuery = (BasicDBObject) query.clone();
		innerQuery.put("_id", object.get("_id"));
		UpdateResult result = collection.replaceOne(innerQuery, object);
		return result.wasAcknowledged();
	}
	public boolean update(String collectionName,Document query,BasicDBObject newDoc){
		MongoDatabase fdb = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> table = fdb.getCollection(collectionName, BasicDBObject.class);
		UpdateOptions options = new UpdateOptions();
		//如果这里是true，当查不到结果的时候会添加一条newDoc,默认为false
		options.upsert(true);
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDoc);
		UpdateResult result = table.updateMany(query, updateObj, options);
		return ((result.getModifiedCount()>0)?true:false);

	}
	public boolean update(String collectionName,Document query,BasicDBObject newDoc,Boolean isUpsert){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> table = db.getCollection(collectionName, BasicDBObject.class);
		UpdateOptions options = new UpdateOptions();
		//如果这里是true，当查不到结果的时候会添加一条newDoc,默认为false
		options.upsert(isUpsert);
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDoc);
		UpdateResult result = table.updateMany(query, updateObj, options);
		return ((result.getModifiedCount()>0)?true:false);

	}
	public boolean delete(String collectionName,BasicDBObject query){
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
		DeleteResult result = collection.deleteMany(query);
		return result.wasAcknowledged();
	}
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}
	public enum MongoSigleton{
		INSTANCE;
		public MongoUtil build(){
			MongoUtil mu = new MongoUtil();
			return mu;
		}
	}
}
