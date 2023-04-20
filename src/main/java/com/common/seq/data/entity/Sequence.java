package com.common.seq.data.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_sequence")
@Schema(description = "Sequence Response")
public class Sequence extends BaseEntity{
	
	@Id //PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) //해당 데이터베이스 번호증가 전략을 따라가겠다. 
	private Long id;

	@Column
	private Long seq;

	@Column(unique= true, nullable = false)
	private String date;

	public void updateSeq(Long seq) {
		this.seq = seq;
	}
	
}