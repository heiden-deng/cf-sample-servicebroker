package com.pivotal.cf.broker.repositories;

import org.springframework.data.repository.CrudRepository;
import com.pivotal.cf.broker.model.PlanMetadata;
import com.pivotal.cf.broker.model.PlanMetadataCost;

public interface PlanMetadataCostRepository extends CrudRepository<PlanMetadataCost, String> {
	public Long countByPlanMetadata(PlanMetadata planMetadata);
}
