package com.mdxt.secureapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdxt.secureapi.dto.PolicyWithCost;
import com.mdxt.secureapi.dto.RequestPolicyList;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;
import com.mdxt.secureapi.entity.TestClass;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;

@RestController
@RequestMapping("api/public")
public class PublicController {
	
	@Autowired
	private LifeInsurancePolicyRepository lifeInsurancePolicyRepository;
	
	private static final List<TestClass> students = new ArrayList<>();
	static {
		students.add(new TestClass(1, "James Bond"));
		students.add(new TestClass(2, "Bruce Wayne"));
	}
	
	@GetMapping(path = "test/{id}")
	public TestClass getData(@PathVariable("id") Integer id) {
		return students.stream()
				.filter(testClass -> testClass.getId()==id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No TestClass of id "+id));
	}
	
	@PostMapping(path = "policies")
	public List<PolicyWithCost> getPolicies(@RequestBody RequestPolicyList request) {
		System.out.println("received policies list request - "+request);
		
		List<LifeInsurancePolicy> result = lifeInsurancePolicyRepository
											.findAvailablePolicies(request.getCoverValue(),
																	request.getCoverTillAge()
																	);
		
		return result.stream()
					.map(policy -> new PolicyWithCost(policy, calculateCost(policy, request)))
					.collect(Collectors.toList());
	}
	
	private Double calculateCost(LifeInsurancePolicy policy, RequestPolicyList request) {
		Double result = 3.14159;
		return result;
	}
	
	@Bean
	private void createSampleLifeInsurancePolicies() {
		System.out.println("creating sample Life Insurance Policies");
		
		LifeInsurancePolicy policy = new LifeInsurancePolicy();
		policy.setInsurer("mdxt");
		policy.setName("fantabulous policy");
		policy.setDocumentPath("http://www.africau.edu/images/default/sample.pdf");
		policy.setMinCoverValue(1000000l);
		policy.setMaxCoverValue(10000000l);
		policy.setMultiplierCoverValue(0.25);
		policy.setMinCoverTillAge(30);
		policy.setMaxCoverTillAge(99);
		policy.setMultiplierCoverTillAge(2.0);
		policy.setAdditionalFeatures(new String[] {"some other feature"});
		
		lifeInsurancePolicyRepository.saveAndFlush(policy);
	}
	
	
}
