package com.pivotal.cf.broker.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pivotal.cf.broker.exceptions.EntityNotFoundException;
import com.pivotal.cf.broker.model.Plan;
import com.pivotal.cf.broker.model.PlanMetadata;
import com.pivotal.cf.broker.model.PlanMetadataCost;
import com.pivotal.cf.broker.model.PlanMetadataRes;
import com.pivotal.cf.broker.model.ServiceDefinition;
import com.pivotal.cf.broker.model.ServiceInstance;
import com.pivotal.cf.broker.repositories.PlanRepository;
import com.pivotal.cf.broker.repositories.PlanMetadataCostRepository;
import com.pivotal.cf.broker.repositories.PlanMetadataResRepository;
import com.pivotal.cf.broker.repositories.ServiceDefinitionRepository;
import com.pivotal.cf.broker.repositories.ServiceInstanceRepository;
import com.pivotal.cf.broker.services.DatabaseService;
import com.pivotal.cf.broker.services.PlanService;
import com.pivotal.cf.broker.services.TemplateService;

import freemarker.template.Configuration;

@SuppressWarnings("unused")
@Service
public class PlanServiceImpl implements PlanService {
	
	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private ServiceDefinitionRepository serviceRepository;
	
	@Autowired
	private ServiceInstanceRepository instanceRepository;
	
	@Autowired
	private PlanMetadataCostRepository planMetadataCostRepository;

	@Autowired
	private PlanMetadataResRepository planMetadataResRepository;

	@Autowired
	private TemplateService templateService;
	
	
	
	@Override
	public Plan create(Plan plan) {
		ServiceDefinition serviceDefinition = serviceRepository.findOne(plan.getServiceDefinition().getId());
		if(serviceDefinition == null){
			throw new IllegalArgumentException("No such service definition : " + plan.getServiceDefinition().getId());
		}
		//Map<String,Object> model = new HashMap<String, Object>();
		plan.setServiceDefinition(serviceDefinition);
		plan.getMetadata().setId(plan.getId());
		//model.put("plan",plan);
		//templateService.execute("plan/create.ftl", model);
		return planRepository.save(plan);
	}

	@Override
	public boolean delete(String planId) {
		Plan plan = planRepository.findOne(planId);
		if(plan == null){
			return false;
		}
		if(instanceRepository.countByPlanId(planId) > 0){
			throw new IllegalStateException("Can not remove plan, it's being used by service instances");
		}
		if(planMetadataCostRepository.countByPlanMetadata(plan.getMetadata()) > 0){
			throw new IllegalStateException("Can not remove plan, the plan has costs associated to it");
		}
		if(planMetadataResRepository.countByPlanMetadata(plan.getMetadata()) > 0){
			throw new IllegalStateException("Can not remove plan, the plan has pool associated to it");
		}
		//Map<String,Object> model = new HashMap<String, Object>();
		//model.put("plan",plan);
		//templateService.execute("plan/delete.ftl", model);
		planRepository.delete(plan);
		return true;
	}

	@Override
	public Plan addCost(PlanMetadata metadata) {
		Plan plan = planRepository.findOne(metadata.getId());
		if(plan == null){
			return null;
		}
		PlanMetadataCost cost = metadata.getCosts().get(0);
		cost.setPlanMetadata(plan.getMetadata());
		plan.getMetadata().getCosts().add(cost);
		planMetadataCostRepository.save(cost);
		return plan;
	}

	@Override
	public Plan deleteCost(PlanMetadata metadata) {
		Plan plan = planRepository.findOne(metadata.getId());
		if(plan == null){
			return null;
		}
		PlanMetadataCost cost = planMetadataCostRepository.findOne(metadata.getCosts().get(0).getUnit());
		plan.getMetadata().getCosts().remove(cost);
		planMetadataCostRepository.delete(cost.getId());
		return plan;
	}

	@Override
	public Plan addRes(PlanMetadata metadata) {
		Plan plan = planRepository.findOne(metadata.getId());
		if(plan == null){
			return null;
		}
		PlanMetadataRes res = metadata.getPool().get(0);
		res.setPlanMetadata(plan.getMetadata());
		plan.getMetadata().getPool().add(res);
		planMetadataResRepository.save(res);
		return plan;
	}

	@Override
	public Plan deleteRes(PlanMetadata metadata) {
		Plan plan = planRepository.findOne(metadata.getId());
		if(plan == null){
			return null;
		}
		PlanMetadataRes res = planMetadataResRepository.findOne(metadata.getPool().get(0).getId());
		plan.getMetadata().getPool().remove(res);
		planMetadataResRepository.delete(res);
		return plan;
	}

}
