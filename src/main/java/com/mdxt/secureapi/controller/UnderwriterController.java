package com.mdxt.secureapi.controller;

import java.io.Console;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdxt.secureapi.dto.response.DentalPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyPurchaseResponse;
import com.mdxt.secureapi.entity.DentalPolicyPurchase;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.repository.DentalPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;
import com.mdxt.secureapi.repository.UserRepository;

@RestController
@RequestMapping("api/underwriter")
public class UnderwriterController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;
	
	@Autowired
	private DentalPolicyPurchaseRepository dentalPolicyPurchaseRepository;
	
	@GetMapping("checkAccess")
	public ResponseEntity<Boolean> checkAccess(){
		return ResponseEntity.ok(true);
	}

	@GetMapping("pending/LIFE")
	public ResponseEntity<List<LifeInsurancePolicyPurchaseResponse>> getOrderHistory(Principal principal){
		
		return ResponseEntity.ok(
				lifeInsurancePolicyPurchaseRepository.findByApplicationStateAndAssignedUnderwriter(ApplicationStateEnum.UNDERWRITER_REVIEW, 
																									userRepository.findByEmail(principal.getName()).get())
													 .stream()
													 .map(policyPurchase -> new LifeInsurancePolicyPurchaseResponse(policyPurchase))
													 .collect(Collectors.toList())
				);
	} 
	
	@PostMapping("approve/LIFE/{id}")
	public ResponseEntity<String> setApproval(@RequestBody String approved, @PathVariable("id") Long id){
		System.out.println("Received request to set underwriter approval of policy LIFE-"+id+" to "+approved);
		if(!lifeInsurancePolicyPurchaseRepository.existsById(id)) return ResponseEntity.badRequest().body("invalid policy id");
		
		LifeInsurancePolicyPurchase targetPolicy = lifeInsurancePolicyPurchaseRepository.findById(id).get();
		
		if(targetPolicy.getApplicationState() != ApplicationStateEnum.UNDERWRITER_REVIEW) return ResponseEntity.badRequest().body("Policy is not under underwriter review"); 
		
		if(approved.equals("true")) {
			System.out.println("setting policy approved");
			targetPolicy
						.setApplicationState(ApplicationStateEnum.APPROVED);
		} else if(approved.equals("false")){
			System.out.println("setting policy rejected");
			targetPolicy
			  			.setApplicationState(ApplicationStateEnum.REJECTED);
		}
		System.out.println(targetPolicy.toString());
		lifeInsurancePolicyPurchaseRepository.saveAndFlush(targetPolicy);
	
		return ResponseEntity.ok("approval status set");
	}
	
	@GetMapping("pending/DENTAL")
	public ResponseEntity<List<DentalPolicyPurchaseResponse>> getOrderHistoryDental(Principal principal){
		
		return ResponseEntity.ok(
				dentalPolicyPurchaseRepository.findByApplicationStateAndAssignedUnderwriter(ApplicationStateEnum.UNDERWRITER_REVIEW, 
																									userRepository.findByEmail(principal.getName()).get())
													 .stream()
													 .map(policyPurchase -> new DentalPolicyPurchaseResponse(policyPurchase))
													 .collect(Collectors.toList())
				);
	} 
	
	@PostMapping("approve/DENTAL/{id}")
	public ResponseEntity<String> setApprovalDental(@RequestBody String approved, @PathVariable("id") Long id){
		System.out.println("Received request to set underwriter approval of policy DENTAL-"+id+" to "+approved);
		if(!dentalPolicyPurchaseRepository.existsById(id)) return ResponseEntity.badRequest().body("invalid policy id");
		
		DentalPolicyPurchase targetPolicy = dentalPolicyPurchaseRepository.findById(id).get();
		
		if(targetPolicy.getApplicationState() != ApplicationStateEnum.UNDERWRITER_REVIEW) return ResponseEntity.badRequest().body("Policy is not under underwriter review"); 
		
		if(approved.equals("true")) {
			System.out.println("setting policy approved");
			targetPolicy
						.setApplicationState(ApplicationStateEnum.APPROVED);
		} else if(approved.equals("false")){
			System.out.println("setting policy rejected");
			targetPolicy
			  			.setApplicationState(ApplicationStateEnum.REJECTED);
		}
		System.out.println(targetPolicy.toString());
		dentalPolicyPurchaseRepository.saveAndFlush(targetPolicy);
	
		return ResponseEntity.ok("approval status set");
	}
}
