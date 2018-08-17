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
import javax.persistence.Transient;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * An instance of a ServiceDefinition.
 * 
 * @author sgreenberg@gopivotal.com
 * @author vcarvalho@gopivotal.com
 *
 */
@SuppressWarnings("unused")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
public class ServiceInstance {

	@JsonSerialize
	@JsonProperty("service_instance_id")
	@Id
	private String id;
	
	@JsonSerialize
	@JsonProperty("service_id")
	@Column(name="service_id")
	private String serviceDefinitionId;
	
	@JsonSerialize
	@JsonProperty("plan_id")
	@Column(name="plan_id")
	private String planId;
	
	@JsonSerialize
	@JsonProperty("organization_guid")
	@Column(name="organization_guid")
	private String organizationGuid;
	
	@JsonSerialize
	@JsonProperty("space_guid")
	@Column(name="space_guid")
	private String spaceGuid;
	
	@JsonSerialize
	@JsonProperty("dashboard_url")
	@Column(name="dashboard_url")
	private String dashboardUrl;

	@JsonSerialize
	@JsonProperty("res_id")
	@Column(name="res_id")
	private String resid;

	@JsonSerialize
	@JsonProperty("kcxpaddr")
	@Column(name="kcxpaddr")
	private String kcxpaddr;

	@JsonSerialize
	@JsonProperty("kcxpport")
	@Column(name="kcxpport")
	private String kcxpport;

	@JsonSerialize
	@JsonProperty("rvipaddr")
	@Column(name="rvipaddr")
	private String rvipaddr;

	@JsonSerialize
	@JsonProperty("rvport")
	@Column(name="rvport")
	private String rvport;

	@JsonSerialize
	@JsonProperty("username")
	@Column(name="username")
	private String username;

	@JsonSerialize
	@JsonProperty("password")
	@Column(name="password")
	private String password;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(name="mapkey")
	@Column(name="mapvalue")
	@CollectionTable(name="service_instance_config", joinColumns = {@JoinColumn(name="sid")})
	@JsonIgnore
	private Map<String,String> config = new HashMap<String,String>();

	private ServiceInstance() {}
	
	public ServiceInstance( String id, String serviceDefinitionId, String planId, String organizationGuid, String spaceGuid, String dashboardUrl ) {
		setId(id);
		setServiceDefinitionId(serviceDefinitionId);
		setPlanId(planId);
		setOrganizationGuid(organizationGuid);
		setSpaceGuid(spaceGuid);
		setDashboardUrl(dashboardUrl);
	}

	public ServiceInstance( String id, String serviceDefinitionId, String planId, String organizationGuid, String spaceGuid, String dashboardUrl, String resid, String kcxpaddr, String kcxpport, String rvipaddr, String rvport, String username, String password ) {
		setId(id);
		setServiceDefinitionId(serviceDefinitionId);
		setPlanId(planId);
		setOrganizationGuid(organizationGuid);
		setSpaceGuid(spaceGuid);
		setDashboardUrl(dashboardUrl);
		setResId(resid);
		setKCXPAddr(kcxpaddr);
		setKCXPPort(kcxpport);
		setRVIPAddr(rvipaddr);
		setRVPort(rvport);
		setUserName(username);
		setPassword(password);
	}
	

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	private void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

	public String getPlanId() {
		return planId;
	}

	private void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getOrganizationGuid() {
		return organizationGuid;
	}

	private void setOrganizationGuid(String organizationGuid) {
		this.organizationGuid = organizationGuid;
	}

	public String getSpaceGuid() {
		return spaceGuid;
	}

	private void setSpaceGuid(String spaceGuid) {
		this.spaceGuid = spaceGuid;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	private void setDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	public String getResId() {
		return resid;
	}

	public void setResId(String resid) {
		this.resid = resid;
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
}
