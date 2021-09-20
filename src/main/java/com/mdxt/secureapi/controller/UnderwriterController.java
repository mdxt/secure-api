package com.mdxt.secureapi.controller;

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

import com.mdxt.secureapi.dto.response.LifeInsurancePolicyPurchaseResponse;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;

@RestController
@RequestMapping("api/underwriter")
public class UnderwriterController {
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;
	
	@GetMapping("checkAccess")
	public ResponseEntity<Boolean> checkAccess(){
		return ResponseEntity.ok(true);
	}

	@GetMapping("pending/LIFE")
	public ResponseEntity<List<LifeInsurancePolicyPurchaseResponse>> getOrderHistory(){
		return ResponseEntity.ok(
				lifeInsurancePolicyPurchaseRepository.findByApplicationState(ApplicationStateEnum.SUBMITTED).stream()
													 .map(policyPurchase -> new LifeInsurancePolicyPurchaseResponse(policyPurchase))
													 .collect(Collectors.toList())
				);
	}
	
	@PostMapping("approve/LIFE/{id}")
	public ResponseEntity<String> setApproval(@RequestBody boolean approved, @PathVariable("id") Long id){
		if(!lifeInsurancePolicyPurchaseRepository.existsById(id)) return ResponseEntity.badRequest().body("invalid policy id");
		
		if(approved) {
			lifeInsurancePolicyPurchaseRepository.findById(id).get()
															  .setApplicationState(ApplicationStateEnum.APPROVED);
		} else {
			lifeInsurancePolicyPurchaseRepository.findById(id).get()
			  												  .setApplicationState(ApplicationStateEnum.REJECTED);
		}
	
		return ResponseEntity.ok("approval status set");
	}
}
