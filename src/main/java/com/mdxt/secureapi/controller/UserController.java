package com.mdxt.secureapi.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyList;
import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyPurchase;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyPurchaseResponse;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;
import com.mdxt.secureapi.repository.UserRepository;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LifeInsurancePolicyRepository lifeInsurancePolicyRepository;
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;

	@GetMapping("checkAccess")
	public ResponseEntity<Boolean> checkAccess(){
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("purchase/LIFE")
	public ResponseEntity<String> purchase(@RequestBody RequestLifeInsurancePolicyPurchase request, Principal principal){//@RequestBody Map<String, Object> request, Principal principal) {

		LifeInsurancePolicyPurchase requestLifeInsurancePolicyPurchase;
		System.out.println("purchase LIFE request - "+request.toString());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			requestLifeInsurancePolicyPurchase = mapper.convertValue(request, LifeInsurancePolicyPurchase.class);
			
			if(!lifeInsurancePolicyRepository.existsById(request.getPolicyId())) 
				return ResponseEntity.badRequest().body("No policy of given policy id");
			
			requestLifeInsurancePolicyPurchase.setPolicy(lifeInsurancePolicyRepository.getById(request.getPolicyId()));
			
			requestLifeInsurancePolicyPurchase.setApplicationState(ApplicationStateEnum.SUBMITTED);
			
			requestLifeInsurancePolicyPurchase.setUser(userRepository.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException()));
			
			lifeInsurancePolicyPurchaseRepository.saveAndFlush(requestLifeInsurancePolicyPurchase);
		} catch(IllegalArgumentException | ClassCastException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid request data");
		}
		
		return ResponseEntity.ok("Purchase request submitted");
	}
	
	@GetMapping("history/LIFE")
	public ResponseEntity<List<LifeInsurancePolicyPurchaseResponse>> getOrderHistory(Principal principal){
		try {
			User user = userRepository.findByEmail(principal.getName()).get();
			return ResponseEntity.ok(
						lifeInsurancePolicyPurchaseRepository.findByUserId(user.getId()).stream()
															 .map(policyRequest -> new LifeInsurancePolicyPurchaseResponse(policyRequest))
															 .collect(Collectors.toList())
					);
		} catch (NoSuchElementException | NullPointerException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}
