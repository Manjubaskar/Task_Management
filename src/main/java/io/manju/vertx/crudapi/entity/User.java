package io.manju.vertx.crudapi.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.vertx.core.json.JsonObject;

@Entity
@Table(name = "user_table")
/**
 *Create the user signup entity inside this class
 */
public class User implements Serializable{

	@Id
	@Column(name = "id")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")	  
	private String id;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "employee_id")
	private String eid;
	
	@Column(name = "first_name", nullable=false)
    private String fname;
    
    @Column(name = "last_name", nullable=false)
    private String lname;
    
    @Column(name = "designation", nullable=false)
    private String designation;
	
    @Column(name = "user_name",unique = true, nullable=false)
    private String name;
    
    @Column(name = "email",unique = true, nullable=false)
    private String email;
    
    @Column(name = "password", nullable=false)
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private Role role;
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
     
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getLname() {
        return lname;
    }
    
    public void setLname(String lname) {
        this.lname = lname;
    }
    
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
        public void setPassword(String password) {
            this.password = password;
    }
       
    public String toJsonString(){
         return String.valueOf(JsonObject.mapFrom(this));
    }
  
}
   