package com.qualityobjects.oss.h3lp3r.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.qualityobjects.oss.h3lp3r.domain.document.OperationLog;

@Repository
public interface OperationLogRepository extends ElasticsearchRepository<OperationLog, String> {
	
}
