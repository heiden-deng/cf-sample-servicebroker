package com.pivotal.cf.broker.repositories;

import org.springframework.data.repository.CrudRepository;
import com.pivotal.cf.broker.model.PlanMetadata;
import com.pivotal.cf.broker.model.PlanMetadataRes;

public interface PlanMetadataResRepository extends CrudRepository<PlanMetadataRes, String> {
	public Long countByPlanMetadata(PlanMetadata planMetadata);
}
