package io.manju.vertx.crudapi.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import io.manju.vertx.crudapi.entity.Taskdetail;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


/**
 * This class handles the task detail data transactions
 * @author e1066
 */
public class TaskdetailDao {
    private static TaskdetailDao instance;
    protected EntityManager entityManager;

    public static TaskdetailDao getInstance(){
        if (instance == null){
            instance = new TaskdetailDao();
        }

        return instance;
    }

    private TaskdetailDao() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }
    
    /**
     * This method should return task info for given task_id
     * @param taskid
     * @return
     */
	    public Taskdetail getByTaskid(String taskid) {
	  	  Object result = entityManager.find(Taskdetail.class, taskid);
	        if (result != null) {
	            return (Taskdetail) result;
	        } else {
	            return null;
	        }
	    }
    
     /**
      * This method should return all task info
      * @return
      */
	    @SuppressWarnings("unchecked")
	    public List<Taskdetail> findAll() {
	        return entityManager.createQuery("FROM " + Taskdetail.class.getName()).getResultList();
	    }
	    
     /**This method used to save the task
      * @param newTaskdetail
      */
	    public void persistTaskdetail(Taskdetail newTaskdetail)
	   	{
		        try {
		        	 
		            entityManager.getTransaction().begin();
		            entityManager.persist(newTaskdetail);
		            entityManager.getTransaction().commit();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            entityManager.getTransaction().rollback();
		            
		        }
		    }

	 /**
	  * This method should return list the task info for given user name
	  * @param assignto
	  * @return
	  */
		public List<Taskdetail> getByName(String assignto) {
			
			return entityManager.createQuery(
		    		"FROM Taskdetail WHERE assignto = :assignto", Taskdetail.class)
	          .setParameter("assignto", assignto)
	          .getResultList();
		}
	
	 /**
	  * This method should return task info for given user name
	  * @param assignto
	  * @return
	  */
		 public Taskdetail getByTaskName(String assignto) {
			
			return entityManager.createQuery(
		    		"FROM Taskdetail WHERE assignto = :assignto", Taskdetail.class)
	          .setParameter("assignto", assignto)
	          .getSingleResult();
	    	}
		
	 /**
	  * This method should return edit task info 
	  * @param taskdetail
	  */
		 public void merge(Taskdetail taskdetail) {
	        try {
	            entityManager.getTransaction().begin();
	            entityManager.merge(taskdetail);
	            entityManager.getTransaction().commit();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            entityManager.getTransaction().rollback();
	        }
	      }
	    
     /**
      * This method used to remove user info
      * @param taskdetail
      */
		 public void remove(Taskdetail taskdetail) {
		        try {
		            entityManager.getTransaction().begin();
		            taskdetail = entityManager.find(Taskdetail.class, taskdetail.getTaskid());
		            entityManager.remove(taskdetail);
		            entityManager.getTransaction().commit();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            entityManager.getTransaction().rollback();
		        }
		}
		 
	     /**
	      * This method used to remove user info by passing task_id
	      * @param Taskid
	      */
		    public void removeById(String Taskid) {
		        try {
		        	Taskdetail taskdetail = getByTaskid(Taskid);
		            remove(taskdetail);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }

	      
		 /**
          * This method used to update the status of the task
		  * @param context
		  * @param taskid
		  * @param status
		  */
			public void UserStatusChange(RoutingContext context,Integer taskid, String status) {
				try {
		    		  
		            entityManager.getTransaction().begin();
		            Query taskdetail = entityManager.createQuery("UPDATE Taskdetail set status='"+status+"'  WHERE taskid='"+taskid+"'");
		            taskdetail.executeUpdate();
	        		entityManager.getTransaction().commit();
	        		sendSuccess(" Status Updated", context.response(),200);   		 
	    	  }  catch (Exception ex) {
			            ex.printStackTrace();
			            entityManager.getTransaction().rollback();       
			        }
				
			}
			    
			
		 /**
	       * This method send Success message
	       * @param errorMessage
	       * @param response
	       * @param code
	       */
		      private void sendSuccess(String successMessage,HttpServerResponse response,int code) {
		    	  JsonObject jo = new JsonObject();
		          jo.put("successMessage", successMessage);
		    	  response
			              .setStatusCode(200)
			              .setStatusCode(code)
			              .putHeader("content-type", "application/json; charset=utf-8")
			              .end(Json.encodePrettily(jo));
			  }
			
}
