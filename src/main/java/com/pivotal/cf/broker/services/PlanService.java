package com.pivotal.cf.broker.services;

import com.pivotal.cf.broker.model.Plan;
import com.pivotal.cf.broker.model.PlanMetadata;

public interface PlanService {
	public Plan create(Plan plan);
	public boolean delete(String planId);
	public Plan addCost(PlanMetadata metadata);
	public Plan deleteCost(PlanMetadata metadata);
	public Plan addRes(PlanMetadata metadata);
	public Plan deleteRes(PlanMetadata metadata);

}
