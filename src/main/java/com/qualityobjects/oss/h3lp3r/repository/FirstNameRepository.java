package com.qualityobjects.oss.h3lp3r.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualityobjects.oss.h3lp3r.domain.entiity.FirstName;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;

@Repository
public interface FirstNameRepository extends JpaRepository<FirstName, Integer> {

	public List<FirstName> findByLang(Lang valueOf);
	
}
