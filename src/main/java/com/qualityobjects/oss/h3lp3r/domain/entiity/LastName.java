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
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;

import lombok.Data;

@Data
@Entity
@Table(name = "last_names")
public class LastName {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="last_name")
	@JsonProperty("last_name")
	private String lastName;

	@Enumerated(EnumType.STRING)
	private Lang lang;
}
