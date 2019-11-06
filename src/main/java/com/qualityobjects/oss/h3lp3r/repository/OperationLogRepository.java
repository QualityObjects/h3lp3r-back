package com.qualityobjects.oss.h3lp3r.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualityobjects.oss.h3lp3r.domain.entiity.OperationLog;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Integer> {
	
}
