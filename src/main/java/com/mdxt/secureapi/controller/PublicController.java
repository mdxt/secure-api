package com.mdxt.secureapi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdxt.secureapi.dto.AbstractFormControl;
import com.mdxt.secureapi.dto.NumberInputControl;
import com.mdxt.secureapi.dto.SelectControl;
import com.mdxt.secureapi.dto.request.RequestDentalPolicyList;
import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyList;
import com.mdxt.secureapi.dto.response.DentalPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyListResponse;
import com.mdxt.secureapi.entity.BasePolicy;
import com.mdxt.secureapi.entity.DentalPolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;
import com.mdxt.secureapi.entity.TestClass;
import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.InsuranceTypesEnum;
import com.mdxt.secureapi.repository.DentalPolicyRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;

@RestController
@RequestMapping("api/public")
public class PublicController {
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
	
	@Autowired
	private LifeInsurancePolicyRepository lifeInsurancePolicyRepository;
	
	@Autowired
	private DentalPolicyRepository dentalPolicyRepository;
	
	private static final List<TestClass> students = new ArrayList<>();
	static {
		students.add(new TestClass(1, "James Bond"));
		students.add(new TestClass(2, "Bruce Wayne"));
	}
	
	@GetMapping(path = "types")
	public ResponseEntity<InsuranceTypesEnum[]> getPolicyTypes() {
		return ResponseEntity.ok(InsuranceTypesEnum.values());
	}
	
	@PostMapping(path = "getForm")
	public ResponseEntity<List<AbstractFormControl>> getFormData(@RequestBody String type) {
		System.out.println("getForm request - "+type);
		List<AbstractFormControl> result = new ArrayList<>();
		
		if(type.equals(InsuranceTypesEnum.LIFE.toString())) {
			//create and return life insurance form
			
			//gender
			SelectControl gender = new SelectControl(Arrays.stream(GenderEnum.values())
															.map(value -> value.toString())
															.collect(Collectors.toList()));
			gender.setName("gender");
			result.add(gender);
			
			NumberInputControl age = new NumberInputControl(18l, 99l);
			age.setName("age");
			result.add(age);
			
			return ResponseEntity.ok(result);
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	
	@GetMapping(path = "test/{id}")
	public TestClass getData(@PathVariable("id") Integer id) {
		return students.stream()
				.filter(testClass -> testClass.getId()==id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No TestClass of id "+id));
	}
	
	@PostMapping(path = "policies/LIFE")
	public List<LifeInsurancePolicyListResponse> getLifeInsurancePolicies(@RequestBody RequestLifeInsurancePolicyList request) {
		System.out.println("received policies list request - "+request);
		
		List<LifeInsurancePolicy> result = lifeInsurancePolicyRepository
											.findAvailablePolicies(request.getCoverValue(),
																	request.getCoverTillAge()
																	);
		
		return result.stream()
					.map(policy -> { 
						LifeInsurancePolicyListResponse policyWithCost = mapper.convertValue(policy, LifeInsurancePolicyListResponse.class);
						policyWithCost.setCost(calculateCost(policy, request));
						return policyWithCost;
						})
					.collect(Collectors.toList());
	}
	
	@PostMapping(path = "policies/DENTAL")
	public List<DentalPolicyPurchaseResponse> getDentalPolicies(@RequestBody RequestDentalPolicyList request) {
		System.out.println("received policies list request - "+request);
		
		List<DentalPolicy> result = dentalPolicyRepository
											.findAvailablePolicies(request.getCoverValue(),
																	request.getCoverTillAge(),
																	request.getNumberCovered()
																	);
		
		return result.stream()
					.map(policy -> { 
							DentalPolicyPurchaseResponse policyWithCost = mapper.convertValue(policy, DentalPolicyPurchaseResponse.class);
							policyWithCost.setCost(calculateCost(policy, request));
							return policyWithCost;
						})
					.collect(Collectors.toList());
	}
	
//	@PostMapping(path = "policy/LIFE/{id}")
//	public PolicyWithCost getPolicyDetailsWithCost(@RequestBody @Nullable RequestLifeInsurancePolicyList request, @PathVariable("id") Long id) {
//		LifeInsurancePolicy temp = lifeInsurancePolicyRepository.findById(id).get();
//			
//		System.out.println("got request "+request);
//		
//		PolicyWithCost result;
//		
//		if(request == null || !isPolicyApplicable(temp, request)) return new PolicyWithCost(temp, -1.0);
//		
//		result = new PolicyWithCost(temp, calculateCost(temp, request));
//		
//		System.out.println("got policy cost-"+result.getTotalCost()+" for details- "+result);
//		
//		return result;
//	}
//	
//	@PostMapping(path = "policy/DENTAL/{id}")
//	public PolicyWithCost getPolicyDetailsWithCost(@RequestBody @Nullable RequestDentalPolicyList request, @PathVariable("id") Long id) {
//		DentalPolicy temp = dentalPolicyRepository.findById(id).get();
//			
//		System.out.println("got request "+request);
//		
//		PolicyWithCost result;
//		
//		if(request == null || !isPolicyApplicable(temp, request)) return new PolicyWithCost(temp, -1.0);
//		
//		result = new PolicyWithCost(temp, calculateCost(temp, request));
//		
//		System.out.println("got policy cost-"+result.getTotalCost()+" for details- "+result);
//		
//		return result;
//	}
	
	private boolean isPolicyApplicable(LifeInsurancePolicy policy, RequestLifeInsurancePolicyList request) {
		if(request.getCoverValue() < policy.getMinCoverValue() ||
			request.getCoverValue() > policy.getMaxCoverValue()) {
			return false;
		}
		if(request.getCoverTillAge() < policy.getMinCoverTillAge() ||
			request.getCoverTillAge() > policy.getMaxCoverTillAge()) {
			return false;
		}
		return true;
	}
	
	private boolean isPolicyApplicable(DentalPolicy policy, RequestDentalPolicyList request) {
		if(request.getCoverValue() < policy.getMinCoverValue() ||
			request.getCoverValue() > policy.getMaxCoverValue()) {
			return false;
		}
		if(request.getCoverTillAge() < policy.getMinCoverTillAge() ||
			request.getCoverTillAge() > policy.getMaxCoverTillAge()) {
			return false;
		}
		if(request.getNumberCovered() < policy.getMinNumberCovered() ||
			request.getNumberCovered() > policy.getMaxNumberCovered()) {
			return false;
		}
		return true;
	}
	
	private Double calculateCost(LifeInsurancePolicy policy, RequestLifeInsurancePolicyList request) {
		Double result = 3.14159;
		return result;
	}
	
	private Double calculateCost(DentalPolicy policy, RequestDentalPolicyList request) {
		Double result = 2.718;
		return result;
	}
	
	//@Bean
	private void createSampleLifeInsurancePolicies() {
		System.out.println("creating sample Life Insurance Policies");
		
		lifeInsurancePolicyRepository.deleteAll();
		
		LifeInsurancePolicy policy = new LifeInsurancePolicy();
		policy.setInsurer("mdxt");
		policy.setName("fantabulous policy");
		policy.setDocumentPath("/public/static/sample.pdf");
		policy.setMinCoverValue(1000000l);
		policy.setMaxCoverValue(10000000l);
		policy.setMultiplierCoverValue(0.25);
		policy.setMinCoverTillAge(30);
		policy.setMaxCoverTillAge(99);
		policy.setMultiplierCoverTillAge(2.0);
		policy.setAdditionalFeatures(new String[] {"some other feature"});
		
		lifeInsurancePolicyRepository.saveAndFlush(policy);
	}
	
	//@Bean
	private void createSampleDentalPolicies() {
		System.out.println("creating sample Dental Insurance Policies");
		
		dentalPolicyRepository.deleteAll();
		
		DentalPolicy policy = new DentalPolicy();
		policy.setInsurer("mdxt");
		policy.setName("fantabulous dental policy");
		policy.setDocumentPath("/public/static/sample.pdf");
		policy.setMinCoverValue(1000000l);
		policy.setMaxCoverValue(10000000l);
		policy.setMultiplierCoverValue(0.25);
		policy.setMinCoverTillAge(30);
		policy.setMaxCoverTillAge(99);
		policy.setMultiplierCoverTillAge(2.0);
		policy.setAdditionalFeatures(new String[] {"some other feature"});
		
		policy.setMinNumberCovered(1);
		policy.setMaxNumberCovered(4);
		policy.setMultiplierNumberCovered(4.0);
		
		dentalPolicyRepository.saveAndFlush(policy);
	}
}
