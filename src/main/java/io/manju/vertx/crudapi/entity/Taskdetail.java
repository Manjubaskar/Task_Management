package io.manju.vertx.crudapi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import io.vertx.core.json.JsonObject;

@Entity
@Table(name = "taskdetail")
/**
 *Create the Taskdetail entity inside this class
 */
public class Taskdetail implements Serializable {

	@Id
	@Column(name = "task_id",unique = true)
	private Integer taskid;
	
	@Column(name = "status")
    private String status;
    
    @Column(name = "timeline")
    private String timeline;
    
    @Column(name = "tittle")
    private String tittle;
    
    @Column(name = "description",unique = true, nullable=false)
    private String description;
    
    @Column(name = "assignto", nullable=false)
    private String assignto;

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
    
    public Integer getTaskid() {
        return taskid;
    }
    
    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAssignto() {
        return assignto;
    }

    public void setAssignto(String assignto) {
        this.assignto = assignto;
    }

    public String toJsonString(){
         return String.valueOf(JsonObject.mapFrom(this));
    }
  
}

