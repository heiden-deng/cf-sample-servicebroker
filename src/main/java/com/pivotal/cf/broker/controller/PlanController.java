package com.pivotal.cf.broker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pivotal.cf.broker.model.Plan;
import com.pivotal.cf.broker.model.PlanMetadata;
import com.pivotal.cf.broker.model.PlanMetadataCost;
import com.pivotal.cf.broker.model.PlanMetadataRes;
import com.pivotal.cf.broker.model.ServiceDefinition;
import com.pivotal.cf.broker.services.PlanService;

@Controller
@RequestMapping(value="/v2/catalog/services/{sid}/plans")
public class PlanController {
	
	@Autowired
	private PlanService service;
	
	@ResponseBody
	@RequestMapping(consumes="application/json", produces="application/json", method=RequestMethod.POST)
	public ResponseEntity<Plan> createPlan(@RequestBody Plan plan, @PathVariable("sid") String sid){
		ServiceDefinition serviceDefinition = new ServiceDefinition(sid);
		plan.setServiceDefinition(serviceDefinition);
		Plan persistedPlan = service.create(plan);
		ResponseEntity<Plan> response= new ResponseEntity<>(persistedPlan, HttpStatus.CREATED);
		return response;
	}

	@ResponseBody
	@RequestMapping(consumes="application/json", produces="application/json", value="/{planId}/costs", method=RequestMethod.PUT)
	public ResponseEntity<Plan> addCost(@RequestBody PlanMetadataCost cost, @PathVariable("planId") String planId) {
		PlanMetadata metadata = new PlanMetadata();
		metadata.setId(planId);
		metadata.getCosts().add(cost);
		Plan plan = service.addCost(metadata);
		HttpStatus status = (plan != null) ? HttpStatus.CREATED : HttpStatus.GONE;
		ResponseEntity<Plan> response = new ResponseEntity<>(plan,status);  
		return response;
	}

	@ResponseBody
	@RequestMapping(consumes="application/json", produces="application/json", value="/{planId}/pool", method=RequestMethod.PUT)
	public ResponseEntity<Plan> addRes(@RequestBody PlanMetadataRes res, @PathVariable("planId") String planId) {
		PlanMetadata metadata = new PlanMetadata();
		metadata.setId(planId);
		metadata.getPool().add(res);
		Plan plan = service.addRes(metadata);
		HttpStatus status = (plan != null) ? HttpStatus.CREATED : HttpStatus.GONE;
		ResponseEntity<Plan> response = new ResponseEntity<>(plan,status);  
		return response;
	}

	
	@RequestMapping(value="/{planId}",produces="application/json", method=RequestMethod.DELETE)
	public ResponseEntity<String> deletePlan(@PathVariable("sid") String sid, @PathVariable("planId") String planId){
		boolean deleted = service.delete(planId);
		HttpStatus status = deleted ? HttpStatus.OK : HttpStatus.GONE;
		ResponseEntity<String> response = new ResponseEntity<>("{}",status);  
		return response;
	}

	
	@RequestMapping(value="/{planId}/costs/{costId}",produces="application/json", method=RequestMethod.DELETE)
	public ResponseEntity<Plan> clearCosts(@PathVariable("sid") String sid, @PathVariable("planId") String planId, @PathVariable("costId") String costId){
		PlanMetadata metadata = new PlanMetadata();
		PlanMetadataCost cost = new PlanMetadataCost();
		cost.setUnit(costId);
		metadata.setId(planId);
		metadata.getCosts().add(cost);
		Plan plan = service.deleteCost(metadata);
		HttpStatus status = (plan != null) ? HttpStatus.OK : HttpStatus.GONE;
		ResponseEntity<Plan> response = new ResponseEntity<>(plan,status);  
		return response;
	}
	
	@RequestMapping(value="/{planId}/pool/{resId}",produces="application/json", method=RequestMethod.DELETE)
	public ResponseEntity<Plan> clearPool(@PathVariable("sid") String sid, @PathVariable("planId") String planId, @PathVariable("resId") String resId){
		PlanMetadata metadata = new PlanMetadata();
		PlanMetadataRes res = new PlanMetadataRes();
		res.setId(resId);
		metadata.setId(planId);
		metadata.getPool().add(res);
		Plan plan = service.deleteRes(metadata);
		HttpStatus status = (plan != null) ? HttpStatus.OK : HttpStatus.GONE;
		ResponseEntity<Plan> response = new ResponseEntity<>(plan,status);  
		return response;
	}
}
