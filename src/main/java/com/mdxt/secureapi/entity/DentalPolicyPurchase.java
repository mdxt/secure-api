package com.mdxt.secureapi.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.mdxt.secureapi.dto.request.RequestDentalPolicyList;
import com.mdxt.secureapi.enums.ApplicationStateEnum;

import lombok.Data;

@Data
@Entity
public class DentalPolicyPurchase extends RequestDentalPolicyList{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="policy_id")
	private DentalPolicy policy;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private String buyerName;
	
	private String address;
	
	private Double cost;
	
	@Enumerated(EnumType.STRING)
	private ApplicationStateEnum applicationState;
	
	@ManyToOne
	@JoinColumn(name="assigned_underwriter")
	private User assignedUnderwriter;
}
