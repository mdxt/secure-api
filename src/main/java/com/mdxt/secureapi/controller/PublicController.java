package com.mdxt.secureapi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.Valid;
import javax.validation.Validator;

import org.hibernate.criterion.Restrictions;
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
import com.mdxt.secureapi.dto.request.RequestSignUp;
import com.mdxt.secureapi.dto.response.DentalPolicyListResponse;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyListResponse;
import com.mdxt.secureapi.dto.response.PolicyWithCost;
import com.mdxt.secureapi.entity.BasePolicy;
import com.mdxt.secureapi.entity.DentalPolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;
import com.mdxt.secureapi.entity.Role;
import com.mdxt.secureapi.entity.TestClass;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ExaminationTypeEnum;
import com.mdxt.secureapi.enums.GenderEnum;
import com.mdxt.secureapi.enums.InsuranceTypesEnum;
import com.mdxt.secureapi.enums.PaymentPeriodEnum;
import com.mdxt.secureapi.repository.DentalPolicyRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;
import com.mdxt.secureapi.repository.RoleRepository;
import com.mdxt.secureapi.repository.UserRepository;
import com.mdxt.secureapi.security.AppSecurityConfig;
import com.mdxt.secureapi.security.RolesEnum;
import com.mdxt.secureapi.security.UserDetailsImpl;

@RestController
@RequestMapping("api/public")
public class PublicController {
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	
	@Autowired
	EntityManager entityManager;
	
	@PostMapping(path = "policies/DENTAL")
	public List<DentalPolicyListResponse> getDentalPolicies(@RequestBody RequestDentalPolicyList request) {
		System.out.println("received policies list request - "+request);
		
		List<DentalPolicy> result = dentalPolicyRepository
											.findAvailablePolicies(
																	request.getCoverPeriod(),
																	request.getNumberCovered()
																	)
											.stream()
											.filter(dentalPolicy -> Arrays.asList(dentalPolicy.getCoverValues()).contains(request.getCoverValue()))
											.collect(Collectors.toList());
		
		return result.stream()
					.map(policy -> { 
							DentalPolicyListResponse policyWithCost = mapper.convertValue(policy, DentalPolicyListResponse.class);
							policyWithCost.setCost(calculateCost(policy, request));
							return policyWithCost;
						})
					.collect(Collectors.toList());
	}
	
	@PostMapping(path = "policy/LIFE/{id}")
	public PolicyWithCost getPolicyDetailsWithCost(@RequestBody @Nullable RequestLifeInsurancePolicyList request, @PathVariable("id") Long id) {
		LifeInsurancePolicy temp = lifeInsurancePolicyRepository.findById(id).get();
			
		System.out.println("got request "+request);
		
		PolicyWithCost result;
		
		if(request == null || !validator.validate(request).isEmpty() || !isPolicyApplicable(temp, request)) return new PolicyWithCost(temp, -1.0);
		
		result = new PolicyWithCost(temp, calculateCost(temp, request));
		
		System.out.println("got policy cost-"+result.getCost()+" for details- "+result);
		
		return result;
	}
	
	
	@PostMapping(path = "policy/DENTAL/{id}")
	public PolicyWithCost getPolicyDetailsWithCost(@RequestBody @Nullable RequestDentalPolicyList request, @PathVariable("id") Long id) {
		DentalPolicy temp = dentalPolicyRepository.findById(id).get();
			
		System.out.println("got request "+request);
		
		PolicyWithCost result;
		
		if(request == null  || !validator.validate(request).isEmpty() || !isPolicyApplicable(temp, request)) return new PolicyWithCost(temp, -1.0);
		
		result = new PolicyWithCost(temp, calculateCost(temp, request));
		
		System.out.println("got policy cost-"+result.getCost()+" for details- "+result);
		
		return result;
	}
	
	private boolean isPolicyApplicable(LifeInsurancePolicy policy, RequestLifeInsurancePolicyList request) {
		if(request.getAge() >= request.getCoverTillAge()) {
			return false;
		}
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
		if(Arrays.binarySearch(policy.getCoverValues(), request.getCoverValue())<0) {
			return false;
		}
		if(request.getCoverPeriod() < policy.getMinCoverPeriod() ||
			request.getCoverPeriod() > policy.getMaxCoverPeriod()) {
			return false;
		}
		if(request.getNumberCovered() < policy.getMinNumberCovered() ||
			request.getNumberCovered() > policy.getMaxNumberCovered()) {
			return false;
		}
		return true;
	}
	
	private Double calculateCost(LifeInsurancePolicy policy, RequestLifeInsurancePolicyList request) {
		System.out.println("calc is "+policy.getMultiplierCoverValue() * request.getCoverValue() +
						policy.getMultiplierCoverTillAge() * (request.getCoverTillAge() - request.getAge()) +
						(request.getTobaccoUser() ? 10000 : 0));
		Double result = policy.getMultiplierCoverValue() * request.getCoverValue() +
						policy.getMultiplierCoverTillAge() * (request.getCoverTillAge() - request.getAge()) +
						(request.getTobaccoUser() ? 10000 : 0);
		
		if(request.getPaymentPeriod() == PaymentPeriodEnum.YEARLY) result /= (request.getCoverTillAge() - request.getAge());
		if(request.getPaymentPeriod() == PaymentPeriodEnum.MONTHLY) result/=(12*(request.getCoverTillAge() - request.getAge()));
		return result;
	}
	
	private Double calculateCost(DentalPolicy policy, RequestDentalPolicyList request) {
		Double result = policy.getMultiplierCoverValue() * request.getCoverValue() *
						(policy.getMultiplierCoverPeriod() * request.getCoverPeriod()) *
						(policy.getMultiplierNumberCovered() * request.getNumberCovered());
		if(request.getPaymentPeriod() == PaymentPeriodEnum.YEARLY) result /= request.getCoverPeriod();
		if(request.getPaymentPeriod() == PaymentPeriodEnum.MONTHLY) result/=(12*request.getCoverPeriod());
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
		policy.setExaminationType(ExaminationTypeEnum.PHYSICAL);
		
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
		policy.setCoverValues(new Long[] {500000l, 1000000l, 2000000l, 5000000l});
		policy.setMultiplierCoverValue(0.25);
		policy.setMinCoverPeriod(1);
		policy.setMaxCoverPeriod(3);
		policy.setMultiplierCoverPeriod(2.0);
		policy.setAdditionalFeatures(new String[] {"some other feature"});
		
		policy.setMinNumberCovered(1);
		policy.setMaxNumberCovered(4);
		policy.setMultiplierNumberCovered(4.0);
		policy.setDeductible(10000l);
		dentalPolicyRepository.saveAndFlush(policy);
	}
	
	@PostMapping(path = "signup")
	public ResponseEntity<Boolean> signUp(@RequestBody @Valid RequestSignUp request){
		if(userRepository.existsByEmail(request.getEmail())) return ResponseEntity.ok(false);
		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(AppSecurityConfig.passwordEncoder().encode(request.getPassword()));
		Set<Role> userRoles = new HashSet<Role>();
		
		userRoles.add(roleRepository.findByName(RolesEnum.USER).get());
		user.setRoles(userRoles);
		
		userRepository.saveAndFlush(user);
		
		return ResponseEntity.ok(true);
	}
}
