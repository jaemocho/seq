package com.common.seq.data.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Schema(description = "Sequence Response")
public class Sequence {
	@Id //PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) //해당 데이터베이스 번호증가 전략을 따라가겠다. 
	private Long id;
	private Long seq;
	@Column(unique= true, nullable = false)
	private String date;
	
}