package com.pivotal.cf.broker.model;


import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.jdbc.core.JdbcTemplate;

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
@SuppressWarnings("unused")
@Entity
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class PlanMetadataRes {
	
	@JsonSerialize
	@JsonProperty("id")
	@Id
	private String id = UUID.randomUUID().toString();

	@ManyToOne
	private PlanMetadata planMetadata;

	@JsonProperty("kcxpaddr")
	private String kcxpaddr;

	@JsonProperty("kcxpport")
	private String kcxpport;

	@JsonProperty("rvipaddr")
	private String rvipaddr;

	@JsonProperty("rvport")
	private String rvport;

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	public PlanMetadataRes() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKCXPAddr() {
		return kcxpaddr;
	}

	public void setKCXPAddr(String kcxpaddr) {
		this.kcxpaddr = kcxpaddr;
	}

	public String getKCXPPort() {
		return kcxpport;
	}

	public void setKCXPPort(String kcxpport) {
		this.kcxpport = kcxpport;
	}

	public String getRVIPAddr() {
		return rvipaddr;
	}

	public void setRVIPAddr(String rvipaddr) {
		this.rvipaddr = rvipaddr;
	}

	public String getRVPort() {
		return rvport;
	}

	public void setRVPort(String rvport) {
		this.rvport = rvport;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PlanMetadata getPlanMetadata() {
		return planMetadata;
	}

	public void setPlanMetadata(PlanMetadata sid) {
		this.planMetadata = sid;
	}

	
}
