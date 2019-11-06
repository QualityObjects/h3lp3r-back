package com.qualityobjects.oss.h3lp3r.domain.entiity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualityobjects.oss.h3lp3r.domain.enums.Gender;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;

import lombok.Data;

@Data
@Entity
@Table(name = "first_names")
public class FirstName {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="first_name")
	@JsonProperty("first_name")
	private String firstName;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private Lang lang;
}
