package io.manju.vertx.crudapi;

import java.util.HashSet;
import java.util.Set;
import io.manju.vertx.crudapi.entity.Role;
import io.manju.vertx.crudapi.entity.Taskdetail;
import io.manju.vertx.crudapi.entity.User;
import io.manju.vertx.crudapi.service.RoleService;
import io.manju.vertx.crudapi.service.TaskdetailService;
import io.manju.vertx.crudapi.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Verticle extends AbstractVerticle {
	@Override
	  public void start(Future<Void> fut) {
	      Router router = Router.router(vertx); // <1>
	      // CORS support
	      Set<String> allowHeaders = new HashSet<>();
	      allowHeaders.add("x-requested-with");
	      allowHeaders.add("Access-Control-Allow-Origin");
	      allowHeaders.add("origin");
	      allowHeaders.add("Content-Type");
	      allowHeaders.add("accept");
	      Set<HttpMethod> allowMethods = new HashSet<>();
	      allowMethods.add(HttpMethod.GET);
	      allowMethods.add(HttpMethod.POST);
	      allowMethods.add(HttpMethod.DELETE);
	      allowMethods.add(HttpMethod.PUT);

	      router.route().handler(CorsHandler.create("*") // <2>
	              .allowedHeaders(allowHeaders)
	              .allowedMethods(allowMethods));
	      router.route().handler(BodyHandler.create()); // <3>

	      // routes
	      router.get("/role/").handler(this::getRoles);
	      router.post("/role/").handler(this::saveRoles);
	      
	      router.post("/user").handler(this::signup);
	      router.put("/user").handler(this::passwordUpdate);
	      router.put("/adminRoleChange").handler(this::adminRoleChange);
	      router.post("/login").handler(this::login);
	      router.get("/user/:userToken").handler(this::getUserName);
	      
	      router.get("/taskdetail").handler(this::getTasks);
	      router.post("/taskdetail").handler(this::saveTasks);
	      router.put("/taskdetail").handler(this::updateTasks);
	      router.delete("/taskdetail/:taskid").handler(this::deleteTasks);
	      
	      router.get("/taskdetail/:assignto").handler(this::getUserTasks);
	      router.put("/taskdetail/:assignto").handler(this::updateTaskStatus);
	      vertx.createHttpServer() // <4>
	              .requestHandler(router::accept)
	              .listen(8080, "0.0.0.0", result -> {
	                  if (result.succeeded())
	                      fut.complete();
	                  else
	                      fut.fail(result.cause());
	              });
	  }
	  UserService userService = new UserService();
	  RoleService roleService = new RoleService();
	  TaskdetailService taskdetailService = new TaskdetailService();
	  
			  /**
			   * This method used to get roles
			   * @param context
			   */
			   private void getRoles(RoutingContext context) {
			        roleService.list(context,context.request().getHeader("Authorization"),ar -> {
			            if (ar.succeeded()) {
			                sendSuccess(Json.encodePrettily(ar.result()), context.response());
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
	  
			   /**
				   * This method used to save role
				   * @param context
				   */
			  private void saveRoles(RoutingContext context) {
					  	roleService.save(context,context.request().getHeader("Authorization"),Json.decodeValue(context.getBodyAsString(), Role.class), ar -> {
					          if (ar.succeeded()) {
					              sendSuccess(context.response());
					          } else {
					              sendError(ar.cause().getMessage(), context.response());
					          }
					      });
					  }
			  
			  /**
			   * This method used to get all task by admin
			   * @param context
			   */
			  private void getTasks(RoutingContext context) {
				   taskdetailService.list(context,context.request().getHeader("Authorization"),ar -> {
			            if (ar.succeeded()) {
			                sendSuccess(Json.encodePrettily(ar.result()), context.response());
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			  
			  /**
			   * This method used to save tasks by admin
			   * @param context
			   */
			  private void saveTasks(RoutingContext context) {
				  taskdetailService.saveTasks(context,context.request().getHeader("Authorization"),Json.decodeValue(context.getBodyAsString(), Taskdetail.class), ar -> {
				          if (ar.succeeded()) {
				              sendSuccess(context.response());
				          } else {
				              sendError(ar.cause().getMessage(), context.response());
				          }
				      });
				  }
			  
			  /**
			   * This method used to edit saved task by admin
			   * @param context
			   */
			  private void updateTasks(RoutingContext context) {
				  taskdetailService.updateTasks(context,context.request().getHeader("Authorization"),Json.decodeValue(context.getBodyAsString(), Taskdetail.class), ar -> {
			            if (ar.succeeded()) {
			                sendSuccess(context.response());
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			  
			  /**
			   * This method used to delete task by admin
			   * @param context
			   */
			  private void deleteTasks(RoutingContext context) {
				  taskdetailService.deleteTasks(context,context.request().getHeader("Authorization"),context.request().getParam("taskid"), ar -> {
			            if (ar.succeeded()) {
			                sendSuccess(context.response());
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			  
			  /**
			   * This method used to user get their own task
			   * @param context
			   */
			  private void getUserTasks(RoutingContext context) {
				  taskdetailService.getUserTasks(context,context.request().getHeader("Authorization"),context.request().getParam("assignto"), ar -> {
			            if (ar.succeeded()) {
			                if (ar.result() != null){
			                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
			                } else {
			                    sendSuccess(context.response());
			                }
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			  
			  /**
			   * This method used to user edit the status of their task
			   * @param context
			   */
			  private void updateTaskStatus(RoutingContext context) {
				  taskdetailService.updateTaskStatus(context,context.request().getHeader("Authorization"),context.request().getParam("assignto"),Json.decodeValue(context.getBodyAsString(), Taskdetail.class), ar -> {
			            if (ar.succeeded()) {
			                if (ar.result() != null){
			                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
			                } else {
			                    sendSuccess(context.response());
			                }
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			  
			  /**
			   * This method used to admin change the role of the user
			   * @param context
			   */
			  private void adminRoleChange(RoutingContext context) {       
					userService.adminRoleChange(  context,context.request().getHeader("Authorization"),Json.decodeValue(context.getBodyAsString(), User.class),ar -> {
					       if (ar.succeeded()) {
					             if (ar.result() != null){
					                  sendSuccess(Json.encodePrettily(ar.result()), context.response());
					              } else {
					                  sendSuccess(context.response());
					              }
					          } else {
					              sendError(ar.cause().getMessage(), context.response());
					           }
					        });
					    }
		  
			  /**
			   * This method used to post user signup
			   * @param context
			   */
			  private void signup(RoutingContext context) {
				  	userService.signup(context,Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
				          if (ar.succeeded()) {
				              sendSuccess(context.response());
				          } else {
				              sendError(ar.cause().getMessage(), context.response());
				          }
				      });
				  }
		
			  
		
			  /**
			   * This method used to get user details by passing Authorization header and  userToken
			   * @param context
			   */
			    private void getUserName(RoutingContext context) {       
					userService.getUserName(  context,context.request().getHeader("Authorization"),context.request().getParam("userToken"),ar -> {
			            if (ar.succeeded()) {
			                if (ar.result() != null){
			                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
			                } else {
			                    sendSuccess(context.response());
			                }
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
		
			    /**
			     * This method used to post a login details 
			     * @param context
			     */
			  private void login(RoutingContext context) {
				  	userService.login(context,Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
				          if (ar.succeeded()) {
				              sendSuccess(context.response());
				          } else {
				              sendError(ar.cause().getMessage(), context.response());
				          }
				      });
				  }
			  
			  /**
			   * This method used to update a password 
			   * @param context
			   */
			  private void passwordUpdate(RoutingContext context) {
				  userService.passwordUpdate(context,Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
			            if (ar.succeeded()) {
			                sendSuccess(context.response());
			                
			            } else {
			                sendError(ar.cause().getMessage(), context.response());
			            }
			        });
			    }
			    
				/**
				 * This method used to send Error message
				 * @param errorMessage
				 * @param response
				 */
			    private void sendError(String errorMessage, HttpServerResponse response) {
			        JsonObject jo = new JsonObject();
			        jo.put("errorMessage", errorMessage);
		
			        response
			                .setStatusCode(500)
			                .putHeader("content-type", "application/json; charset=utf-8")
			                .end(Json.encodePrettily(jo));
			    }
			    
				/**
				 * This method used to Success message
				 * @param response
				 */
			    private void sendSuccess(HttpServerResponse response) {
			        response
			                .setStatusCode(200)
			                .putHeader("content-type", "application/json; charset=utf-8")
			                .end();
			    }
			    
				/**
				 * This method used to Success with resposeBody
				 * @param responseBody
				 * @param response
				 */
			    private void sendSuccess(String responseBody, HttpServerResponse response) {
			        response
			                .setStatusCode(200)
			                .putHeader("content-type", "application/json; charset=utf-8")
			                .end(responseBody);
			    } 
	}
