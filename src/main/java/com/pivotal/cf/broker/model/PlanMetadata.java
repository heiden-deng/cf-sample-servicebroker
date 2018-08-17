package com.pivotal.cf.broker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * Plan metadata entity
 * 
 * @author vcarvalho@gopivotal.com
 *
 */
@SuppressWarnings("unused")
@Entity
@GenericGenerator(name="metadata-pk", strategy="foreign", parameters={@Parameter(name="property",value="plan")})
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class PlanMetadata {
	
	@Id
	@JsonIgnore
	private String id;
	
	@JsonSerialize
	@JsonProperty("bullets")
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(
	        name="plan_metadata_bullets",
	        joinColumns=@JoinColumn(name="plan_id")
	)
	@Column(name="bullets")
	private List<String> bullets;

	
	@JsonSerialize
	@JsonProperty("costs")
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="plan_metadata_id")
	private List<PlanMetadataCost> costs = new ArrayList<PlanMetadataCost>();
	
	@JsonSerialize
	@JsonProperty("pool")
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="plan_metadata_id")
	private List<PlanMetadataRes> pool = new ArrayList<PlanMetadataRes>();

	@JsonProperty("displayName")
	private String displayName;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Plan plan;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(name="mapkey")
	@Column(name="mapvalue")
	@CollectionTable(
			name="plan_metadata_extra", 
			joinColumns = @JoinColumn(name="plan_id")
	)
	protected Map<String,String> other = new HashMap<>();

	
	@JsonAnyGetter
    public Map<String,String> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, String value) {
        other.put(name, value);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getBullets() {
		return bullets;
	}

	public void setBullets(List<String> bullets) {
		this.bullets = bullets;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Map<String, String> getOther() {
		return other;
	}

	public void setOther(Map<String, String> other) {
		this.other = other;
	}

	public List<PlanMetadataCost> getCosts() {
		return costs;
	}

	public void setCosts(List<PlanMetadataCost> costs) {
		if ( costs == null ) {
			// ensure serialization as an empty array and not null
			this.costs = new ArrayList<PlanMetadataCost>();
		} else {
			this.costs = costs;
		}
	}
	
	public List<PlanMetadataRes> getPool() {
		return pool;
	}

	public void setPool(List<PlanMetadataRes> pool) {
		if ( pool == null ) {
			// ensure serialization as an empty array and not null
			this.pool = new ArrayList<PlanMetadataRes>();
		} else {
			this.pool = pool;
		}
	}
	
}
