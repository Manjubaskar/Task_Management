package io.manju.vertx.crudapi.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import io.manju.vertx.crudapi.entity.Role;

/**
 * This class handles the role data transactions
 * @author e1066
 */
public class RoleDao {
    private static RoleDao instance;
    protected EntityManager entityManager;

    public static RoleDao getInstance(){
        if (instance == null){
            instance = new RoleDao();
        }

        return instance;
    }

    private RoleDao() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }

    /**This method should return roles list 
     * @return
     */
     
    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        return entityManager.createQuery("FROM " + Role.class.getName()).getResultList();
    }
    
     /**This method used to save roles
      */
    public void persistRole(Role newRole)
   	{
	        try {
	        	 
	            entityManager.getTransaction().begin();
	            entityManager.persist(newRole);
	            entityManager.getTransaction().commit();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            entityManager.getTransaction().rollback();
	            
	        }
	    }
}
