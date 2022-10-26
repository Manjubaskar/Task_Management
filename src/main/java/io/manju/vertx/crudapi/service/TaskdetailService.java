package io.manju.vertx.crudapi.service;

import java.util.List;

import io.manju.vertx.crudapi.entity.Taskdetail;
import io.manju.vertx.crudapi.repository.TaskdetailDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TaskdetailService {
	 private TaskdetailDao taskdetailDao = TaskdetailDao.getInstance();
	 	
	 	/**
	 	 * This method used to list all task for admin check
	 	 * @param context
	 	 * @param Authorization
	 	 * @param handler
	 	 */
		 
	    public void list(RoutingContext context,String Authorization,Handler<AsyncResult<List<Taskdetail>>> handler){
	        Future<List<Taskdetail>> future = Future.future();
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
				        	 //This condition used to check the user role admin
							  if(jsonObject1.equals("Admin")) {
							 System.out.println(jsonObject1+"\n"); 
					            List<Taskdetail> result1 = taskdetailDao.findAll();
					            future.complete(result1);
	            
							  }
							  else {
								  System.out.print("fail");
								  sendError("Admin only read all Task", context.response(),400);
							  }}
						  jedisPool.close();	
				            future.complete();	   	 
	        	} 
	        catch (Throwable ex) {
	            future.fail(ex);
	        }}
	    }

	    /**
	     * This method used to user get their own tasks
	     * @param context
	     * @param Authorization
	     * @param assignto
	     * @param handler
	     */
		 
	    public void getUserTasks(RoutingContext context,String Authorization, String assignto,Handler<AsyncResult<List<Taskdetail>>> handler) {
	    	Future<List<Taskdetail>> future = Future.future();
	        future.setHandler(handler);
	        if(Authorization.isEmpty()) {		
        		sendError("Unauthorized", context.response(),401);
        	}else {
        		 try {
					          JedisPool jedisPool = new JedisPool("localhost", 6379);
							  // Get the pool and use the database
							  try (Jedis jedis = jedisPool.getResource()) {
								  System.out.print("cvbn");
							  //Get token value from redis
								  System.out.print(Authorization);
							  String result =  jedis.get(Authorization);
							  System.out.print(result);
							  //Convert String to Json object
							  JsonObject jsonObject = new JsonObject(result);
							  System.out.print(jsonObject);
							  
							 // JsonObject jsonObject1 = new JsonObject();
							 String  jsonObject1=jsonObject.getString("name");
				        	 //This condition used to check the user name
							  if(jsonObject1.equals(assignto)) {
							 System.out.println(jsonObject1+"\n"); 
					        	List<Taskdetail> result1 = taskdetailDao.getByName(assignto);
					            future.complete(result1);
							  }
							  else {
								  System.out.print("fail");
								  sendError("Admin only read all Tasks and user read their own tasks", context.response(),400);
							  }}
						  jedisPool.close();	
				            future.complete();	   	 
		    	} 
		    catch (Throwable ex) {
		        future.fail(ex);
		    }}
	    	}
	    
	    /**
	     * This method used to save task details by admin
	     * @param context
	     * @param Authorization
	     * @param newtask
	     * @param handler
	     */
		 
		public void saveTasks(RoutingContext context,String Authorization,Taskdetail newtask,Handler<AsyncResult<Taskdetail>> handler) {
			Future<Taskdetail> future = Future.future();
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
				        	 //This condition used to check the user role admin
							  if(jsonObject1.equals("Admin")) {
							 System.out.println(jsonObject1+"\n"); 
							 
					        	taskdetailDao.persistTaskdetail(newtask);
					        	 sendSuccess("Task assigned", context.response(),200);
							  }
							  else {
								  System.out.print("fail");
								  sendError("Admin only create a Task", context.response(),400);
							  }}
						  jedisPool.close();	
				            future.complete();	   	 
	        	} 
	        catch (Throwable ex) {
	            future.fail(ex); 
	            sendError("Admin only create a Task", context.response(),400);
	        }	  
		}}
		
		/**
		 * This method used to update the task details
		 * @param context
		 * @param Authorization
		 * @param taskdetail
		 * @param handler
		 */
		
	     public void updateTasks(RoutingContext context,String Authorization,Taskdetail taskdetail, Handler<AsyncResult<Taskdetail>> handler) {
	         Future<Taskdetail> future = Future.future();
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
					        	 //This condition used to check the user role admin
								  if(jsonObject1.equals("Admin")) {
								 System.out.println(jsonObject1+"\n"); 
	        
						        	 taskdetailDao.merge(taskdetail);
						        	 sendSuccess("Task updated", context.response(),200);
								  }
								  else {
									  System.out.print("fail");
									  sendError("Admin only edit a Task", context.response(),400);
								  }}
							  jedisPool.close();	
					            future.complete();	   	 
		        	} 
		        catch (Throwable ex) {
		            future.fail(ex); 
		            sendError("Admin only edit a Task", context.response(),400);
		        }	  
			}}
						       
	     
	     /**
	      * This method used to update the status of the task by user
	      * @param context
	      * @param Authorization
	      * @param assignto
	      * @param taskdetail
	      * @param handler
	      */
		    
	     public void updateTaskStatus(RoutingContext context,String Authorization,String assignto,Taskdetail taskdetail, Handler<AsyncResult<Taskdetail>> handler) {
	         Future<Taskdetail> future = Future.future();
	         future.setHandler(handler);
	         if(Authorization.isEmpty()) {
	        		sendError("Unauthorized", context.response(),401);
	        	}else {
		        
	        		 try {
				          JedisPool jedisPool = new JedisPool("localhost", 6379);
						  // Get the pool and use the database
						  try (Jedis jedis = jedisPool.getResource()) {
							  System.out.print("cvbn");
						  //Get token value from redis
							  System.out.print(Authorization);
						  String result =  jedis.get(Authorization);
						  System.out.print(result);
						  //Convert String to Json object
						  JsonObject jsonObject = new JsonObject(result);
						  System.out.print(jsonObject);
						  
						 // JsonObject jsonObject1 = new JsonObject();
						 String  jsonObject1=jsonObject.getString("name");
			        	 //This condition used to check the user name
						  if(jsonObject1.equals(assignto)) {
						 System.out.println(jsonObject1+"\n"); 
						 Taskdetail task = taskdetailDao.getByTaskName(assignto);
			             if(task!=null) {
			            	 
			            	 taskdetailDao.UserStatusChange(context,taskdetail.getTaskid(),taskdetail.getStatus());
					           // future.complete();
					            sendSuccess("Status updated", context.response(),200);
			             }
							  }
							  else {
								  System.out.print("fail");
								  sendError("Invalid user", context.response(),400);
							  }}
						  jedisPool.close();	
				            future.complete();	   	 
		    	} 
		    catch (Throwable ex) {
		        future.fail(ex);
		    }}
		 }




	     /**
	      * This method used to delete the task by admin using taskid				        	 
	      * @param context
	      * @param Authorization
	      * @param taskid
	      * @param handler
	      */
	     public void deleteTasks(RoutingContext context,String Authorization,String taskid, Handler<AsyncResult<Taskdetail>> handler) {
	         Future<Taskdetail> future = Future.future();
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
					        	 //This condition used to check the user role admin
								  if(jsonObject1.equals("Admin")) {
								 System.out.println(jsonObject1+"\n"); 
	         
						        	 taskdetailDao.removeById(taskid);
						        	 sendSuccess("Task deleted", context.response(),200);
								  }
								  else {
									  System.out.print("fail");
									  sendError("Admin only delete a Task", context.response(),400);
								  }}
							  jedisPool.close();	
					            future.complete();	   	 
		        	} 
		        catch (Throwable ex) {
		            future.fail(ex); 
		            sendError("Admin only delete a Task", context.response(),400);
		        }	  
			}}
		
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
