package com.pivotal.cf.broker.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pivotal.cf.broker.exceptions.EntityNotFoundException;
import com.pivotal.cf.broker.model.CreateServiceInstanceRequest;
import com.pivotal.cf.broker.model.Plan;
import com.pivotal.cf.broker.model.PlanMetadataRes;
import com.pivotal.cf.broker.model.ServiceDefinition;
import com.pivotal.cf.broker.model.ServiceInstance;
import com.pivotal.cf.broker.model.ServiceInstanceBinding;
import com.pivotal.cf.broker.model.ServiceInstanceBindingRequest;
import com.pivotal.cf.broker.repositories.PlanMetadataCostRepository;
import com.pivotal.cf.broker.repositories.PlanMetadataResRepository;
import com.pivotal.cf.broker.repositories.PlanRepository;
import com.pivotal.cf.broker.repositories.ServiceDefinitionRepository;
import com.pivotal.cf.broker.repositories.ServiceInstanceBindingRepository;
import com.pivotal.cf.broker.repositories.ServiceInstanceRepository;
import com.pivotal.cf.broker.services.BaseService;
import com.pivotal.cf.broker.services.DatabaseService;
import com.pivotal.cf.broker.services.ServiceManagement;
import com.pivotal.cf.broker.services.TemplateService;
import com.pivotal.cf.broker.utils.StringUtils;

@SuppressWarnings("unused")
@Service
public class ServiceManagementImpl extends BaseService implements ServiceManagement {
	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private ServiceDefinitionRepository serviceRepository;
	
	@Autowired 
	private ServiceInstanceRepository serviceInstanceRepository;
	
	@Autowired 
	private ServiceInstanceBindingRepository bindingRepository;
	
	@Autowired
	private PlanMetadataCostRepository planMetadataCostRepository;

	@Autowired
	private PlanMetadataResRepository planMetadataResRepository;

	@Autowired
	TemplateService templateService;
	
	@Autowired
	private Environment env;
	
	@Override
	public ServiceInstance createInstance(CreateServiceInstanceRequest serviceRequest) {
		ServiceDefinition serviceDefinition = serviceRepository.findOne(serviceRequest.getServiceDefinitionId());
		if(serviceDefinition == null){
			throw new IllegalArgumentException("Service definition not found: " + serviceRequest.getServiceDefinitionId());
		}
		Plan plan = planRepository.findOne(serviceRequest.getPlanId());
		if(plan == null){
			throw new IllegalArgumentException("Invalid plan identifier");
		}
		if(serviceInstanceRepository.exists(serviceRequest.getServiceInstanceId())){
			throw new IllegalStateException("There's already an instance of this service");
		}
		if(plan.getMetadata().getPool().isEmpty())
		{
			throw new IllegalArgumentException("No resource left for this plan, contact PaaS admin");
		}
		PlanMetadataRes res = planMetadataResRepository.findOne(plan.getMetadata().getPool().remove(0).getId());
		ServiceInstance instance = new ServiceInstance(serviceRequest.getServiceInstanceId(), serviceDefinition.getId(), plan.getId(), serviceRequest.getOrganizationGuid(), serviceRequest.getSpaceGuid(), null, res.getId(), res.getKCXPAddr(), res.getKCXPPort(), res.getRVIPAddr(), res.getRVPort(), res.getUserName(), res.getPassword());
		
		planMetadataResRepository.delete(res.getId());
		instance = serviceInstanceRepository.save(instance);
		return instance;
	}

	@Override
	public boolean removeServiceInstance(String serviceInstanceId) {
		if(!serviceInstanceRepository.exists(serviceInstanceId)){
			return false;
		}
		if(bindingRepository.countByServiceInstanceId(serviceInstanceId) > 0){
			throw new IllegalStateException("Can not delete service instance, there are still apps bound to it");
		}
		ServiceInstance instance = serviceInstanceRepository.findOne(serviceInstanceId);
		Plan plan = planRepository.findOne(instance.getPlanId());
		PlanMetadataRes res = new PlanMetadataRes();
		res.setId(instance.getResId());
		res.setKCXPAddr(instance.getKCXPAddr());
		res.setKCXPPort(instance.getKCXPPort());
		res.setRVIPAddr(instance.getRVIPAddr());
		res.setRVPort(instance.getRVPort());
		res.setUserName(instance.getUserName());
		res.setPassword(instance.getPassword());
		res.setPlanMetadata(plan.getMetadata());
		plan.getMetadata().getPool().add(res);
		planMetadataResRepository.save(res);
		serviceInstanceRepository.delete(serviceInstanceId);
		return true;
	}

	@Override
	public List<ServiceInstance> listInstances() {
		return makeCollection(serviceInstanceRepository.findAll());
	}

	@Override
	public ServiceInstanceBinding createInstanceBinding(ServiceInstanceBindingRequest bindingRequest) {
		if(bindingRepository.exists(bindingRequest.getBindingId())){
			throw new IllegalStateException("Binding Already exists");
		}
		ServiceInstance instance = serviceInstanceRepository.findOne(bindingRequest.getInstanceId());
		
		Map<String, String> credentials = new HashMap<>();
		credentials.put("KCXPAddr0", instance.getKCXPAddr());
		credentials.put("KCXPPort0", instance.getKCXPPort());
		credentials.put("RVIPAddr", instance.getRVIPAddr());
		credentials.put("RVPort", instance.getRVPort());
		credentials.put("UserName",instance.getUserName());
		credentials.put("Password",instance.getPassword());
		ServiceInstanceBinding binding = new ServiceInstanceBinding();
		binding.setId(bindingRequest.getBindingId());
		binding.setServiceInstanceId(bindingRequest.getInstanceId());
		binding.setAppGuid(bindingRequest.getAppGuid());
		binding.setCredentials(credentials);
		binding = bindingRepository.save(binding);
		return binding;
	}

	@Override
	public boolean removeBinding(String serviceBindingId) {
		ServiceInstanceBinding binding = bindingRepository.findOne(serviceBindingId);
		if(binding == null){
			return false;
		}
		ServiceInstance instance = serviceInstanceRepository.findOne(binding.getServiceInstanceId());

		bindingRepository.delete(binding);
		return true;
	}

	@Override
	public List<ServiceInstanceBinding> listBindings() {
		// TODO Auto-generated method stub
		return null;
	}

}
