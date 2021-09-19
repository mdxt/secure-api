package com.mdxt.secureapi.dto.response;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.web.bind.annotation.RequestMethod;

import com.mdxt.secureapi.entity.LifeInsurancePolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.IncomeRangeEnum;
import com.mdxt.secureapi.enums.OccupationTypeEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;
import com.mdxt.secureapi.enums.QualificationLevelEnum;

import lombok.Data;


@Data
public class LifeInsurancePolicyPurchaseResponse {
	
	private Long id;
	
	private GenderEnum gender;
	
	private Integer age;
	
	private Boolean tobaccoUser;
	
	private IncomeRangeEnum incomeRange;
	
	private OccupationTypeEnum occupationType;
	
	private QualificationLevelEnum qualificationLevel;
	
	private Long coverValue;
	
	private Integer coverTillAge;
	
	private PaymentPeriodEnum paymentPeriod;
	
	private Long policy_id;
	
	private ApplicationStateEnum applicationState;
	
	private String buyerName;
	
	private String address;
	
	private Double cost;
	
	private Long user_id;
	
	public LifeInsurancePolicyPurchaseResponse(LifeInsurancePolicyPurchase request) {
		id = request.getId();
		gender = request.getGender();
		age = request.getAge();
		tobaccoUser = request.getTobaccoUser();
		incomeRange = request.getIncomeRange();
		occupationType = request.getOccupationType();
		qualificationLevel = request.getQualificationLevel();
		coverValue = request.getCoverValue();
		coverTillAge = request.getCoverTillAge();
		paymentPeriod = request.getPaymentPeriod();
		policy_id = request.getPolicy().getId();
		applicationState = request.getApplicationState();
		buyerName = request.getBuyerName();
		address = request.getAddress();
		cost = request.getCost();
		user_id = request.getUser().getId();
	}
}
