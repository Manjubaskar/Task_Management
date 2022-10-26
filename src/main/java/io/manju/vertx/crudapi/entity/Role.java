package io.manju.vertx.crudapi.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.vertx.core.json.JsonObject;

@Entity
@Table(name = "role")
/**
 *Create the role entity inside this class
 */
public class Role implements Serializable {

	@Id
	@Column(name = "role_id")
	private int roleid;	  
	
	@Column(name = "role_name",unique = true)
    private String rolename;
    
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
    
    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleId(Integer roleid) {
        this.roleid = roleid;
    }
    public String toJsonString(){
         return String.valueOf(JsonObject.mapFrom(this));
    }
}
