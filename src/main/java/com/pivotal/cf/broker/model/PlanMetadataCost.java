package com.pivotal.cf.broker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * Plan metadata costs entity
 * 
 * @author sxiang@citics.com
 *
 */
@Entity
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class PlanMetadataCost {

	@Id
	@JsonIgnore
	private String id = UUID.randomUUID().toString();

	@JsonProperty("unit")
	private String unit;

	@ManyToOne
	private PlanMetadata planMetadata;
	
	@JsonSerialize
	@JsonProperty("amount")
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(name="mapkey")
	@Column(name="mapvalue")
	@CollectionTable(name="plan_metadata_cost_amount", joinColumns = {@JoinColumn(name="plan_metadata_cost_id")})
	private Map<String, Float> amount = new HashMap<String,Float>();

	public PlanMetadataCost() {}

	public String getId() {
		return id;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Map<String, Float> getAmount() {
		return amount;
	}

	public void setAmount(Map<String, Float> amount) {
		if (amount == null) {
			this.amount = new HashMap<String,Float>();
		} else {
			this.amount = amount;
		}
	}

	public PlanMetadata getPlanMetadata() {
		return planMetadata;
	}

	public void setPlanMetadata(PlanMetadata sid) {
		this.planMetadata = sid;
	}

	
}
