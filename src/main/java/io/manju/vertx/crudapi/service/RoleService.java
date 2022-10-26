package io.manju.vertx.crudapi.service;

import java.util.List;

import io.manju.vertx.crudapi.entity.Role;
import io.manju.vertx.crudapi.repository.RoleDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RoleService {
    private RoleDao roleDao = RoleDao.getInstance();

    public void list(RoutingContext context,String Authorization, Handler<AsyncResult<List<Role>>> handler){
        Future<List<Role>> future = Future.future();
        future.setHandler(handler);
        if(Authorization.isEmpty()) {		
    		sendError("Unauthorized", context.response(),401);
    	}else {
        
        try {
        
				          JedisPool jedisPool = new JedisPool("localhost", 6379);
						  // Get the pool and use the database
						  try (Jedis jedis = jedisPool.getResource()) {
						  //Get token value from redis
						  String result =  jedis.get(Authorization);
						  //Convert String to Json object
						  JsonObject jsonObject = new JsonObject(result);
						  System.out.print(jsonObject);
						  
						 // JsonObject jsonObject1 = new JsonObject();
						 String  jsonObject1=jsonObject.getString("rolename");
						  if(jsonObject1.equals("Admin")) {
						 System.out.println(jsonObject1+"\n"); 
       
            List<Role> result1 = roleDao.findAll();
            future.complete(result1);
            
						  }
						  else {
							  System.out.print("fail");
							  sendError("Admin only view a Role", context.response(),400);
						  }}
					  jedisPool.close();	
			            future.complete();	   	 
        	} 
        catch (Throwable ex) {
            future.fail(ex);
        }}
    }


	public void save(RoutingContext context,String Authorization, Role newrole,Handler<AsyncResult<Role>> handler) {
		Future<Role> future = Future.future();
        future.setHandler(handler);
        if(Authorization.isEmpty()) {
    		
    		
    		sendError("Unauthorized", context.response(),401);
    	}else {
        
        try {
        
				          JedisPool jedisPool = new JedisPool("localhost", 6379);
						  // Get the pool and use the database
						  try (Jedis jedis = jedisPool.getResource()) {
						  //Get token value from redis
						  String result =  jedis.get(Authorization);
						  //Convert String to Json object
						  JsonObject jsonObject = new JsonObject(result);
						  System.out.print(jsonObject);
						  
						 // JsonObject jsonObject1 = new JsonObject();
						 String  jsonObject1=jsonObject.getString("rolename");
						  if(jsonObject1.equals("Admin")) {
						 System.out.println(jsonObject1+"\n"); 
				        	
				        	System.out.print("dfghj");
				        	roleDao.persistRole(newrole);
				        	future.complete();
				            
						  }
						  else {
							  System.out.print("fail");
							  sendError("Admin only create a Role", context.response(),400);
						  }}
					  jedisPool.close();	
			            future.complete();	   	 
        	} 
        catch (Throwable ex) {
            future.fail(ex);
        }}
    }

	
	 /**
     *  This method used to send a error message
     * @param errorMessage
     * @param response
     * @param code
     */
     private static void sendError(String errorMessage, HttpServerResponse response,int code) {
         JsonObject jo = new JsonObject();
         jo.put("errorMessage", errorMessage);
         response
                 .setStatusCode(400)
                 .setStatusCode(code)
                 .putHeader("content-type", "application/json; charset=utf-8")
                 .end(Json.encodePrettily(jo));
     }
     
     /**
      * This method used to send a success message
      * @param successMessage
      * @param response
      * @param code
      */
     private void sendSuccess(String successMessage, HttpServerResponse response,int code) {
         JsonObject jo = new JsonObject();
         jo.put("successMessage", successMessage);
         response
                 .setStatusCode(200)
                 .setStatusCode(code)
                 .putHeader("content-type", "application/json; charset=utf-8")
                 .end(Json.encodePrettily(jo));
     	}
}
