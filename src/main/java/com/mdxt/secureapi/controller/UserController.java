package com.mdxt.secureapi.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

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
import com.mdxt.secureapi.dto.request.RequestDentalAndVisionPolicyPurchase;
import com.mdxt.secureapi.dto.request.RequestDentalPolicyPurchase;
import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyList;
import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyPurchase;
import com.mdxt.secureapi.dto.response.DentalAndVisionPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.DentalPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyPurchaseResponse;
import com.mdxt.secureapi.entity.DentalAndVisionPolicyPurchase;
import com.mdxt.secureapi.entity.DentalPolicyPurchase;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.repository.DentalAndVisionPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.DentalAndVisionPolicyRepository;
import com.mdxt.secureapi.repository.DentalPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.DentalPolicyRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;
import com.mdxt.secureapi.repository.UserRepository;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LifeInsurancePolicyRepository lifeInsurancePolicyRepository;
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;
	
	@Autowired
	private DentalPolicyRepository dentalPolicyRepository;
	
	@Autowired
	private DentalPolicyPurchaseRepository dentalPolicyPurchaseRepository;
	
	@Autowired
	private DentalAndVisionPolicyRepository dentalAndVisionPolicyRepository;
	
	@Autowired
	private DentalAndVisionPolicyPurchaseRepository dentalAndVisionPolicyPurchaseRepository;

	@GetMapping("checkAccess")
	public ResponseEntity<Boolean> checkAccess(){
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("purchase/LIFE")
	public ResponseEntity<String> purchase(@RequestBody @Valid RequestLifeInsurancePolicyPurchase request, Principal principal){//@RequestBody Map<String, Object> request, Principal principal) {
		System.out.println("\n\n\n purchase request "+request.toString()+"\n\n\n");
		
		LifeInsurancePolicyPurchase requestLifeInsurancePolicyPurchase;
		System.out.println(request.getTobaccoUser()+"purchase LIFE request - "+request.toString());
		//ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			requestLifeInsurancePolicyPurchase = mapper.convertValue(request, LifeInsurancePolicyPurchase.class);
			
			if(!lifeInsurancePolicyRepository.existsById(request.getPolicyId())) 
				return ResponseEntity.badRequest().body("No policy of given policy id");
			
			requestLifeInsurancePolicyPurchase.setPolicy(lifeInsurancePolicyRepository.getById(request.getPolicyId()));
			
			requestLifeInsurancePolicyPurchase.setApplicationState(ApplicationStateEnum.SUBMITTED);
			
			requestLifeInsurancePolicyPurchase.setUser(userRepository.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException()));
			
			System.out.println(requestLifeInsurancePolicyPurchase.getTobaccoUser()+requestLifeInsurancePolicyPurchase.toString());
			
			lifeInsurancePolicyPurchaseRepository.saveAndFlush(requestLifeInsurancePolicyPurchase);
		} catch(IllegalArgumentException | ClassCastException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid request data");
		}
		
		return ResponseEntity.ok("Purchase request submitted");
	}
	
	@GetMapping("history/LIFE")
	public ResponseEntity<List<LifeInsurancePolicyPurchaseResponse>> getOrderHistoryForLife(Principal principal){
		try {
			User user = userRepository.findByEmail(principal.getName()).get();
			System.out.println("accessing history of LIFE purchases of user "+user.getId()+"/"+user.getEmail());
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
	
	@PostMapping("purchase/DENTAL")
	public ResponseEntity<String> purchase(@RequestBody @Valid RequestDentalPolicyPurchase request, Principal principal){//@RequestBody Map<String, Object> request, Principal principal) {

		DentalPolicyPurchase requestDentalPolicyPurchase;
		System.out.println("purchase DENTAL request - "+request.toString());
		//ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
		try {
			requestDentalPolicyPurchase = mapper.convertValue(request, DentalPolicyPurchase.class);
			
			if(!dentalPolicyRepository.existsById(request.getPolicyId())) 
				return ResponseEntity.badRequest().body("No policy of given policy id");
			
			requestDentalPolicyPurchase.setPolicy(dentalPolicyRepository.getById(request.getPolicyId()));
			
			requestDentalPolicyPurchase.setApplicationState(ApplicationStateEnum.SUBMITTED);
			
			requestDentalPolicyPurchase.setUser(userRepository.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException()));
			
			dentalPolicyPurchaseRepository.saveAndFlush(requestDentalPolicyPurchase);
		} catch(IllegalArgumentException | ClassCastException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid request data");
		}
		
		return ResponseEntity.ok("Purchase request submitted");
	}
	
	@GetMapping("history/DENTAL")
	public ResponseEntity<List<DentalPolicyPurchaseResponse>> getOrderHistoryForDental(Principal principal){
		try {
			User user = userRepository.findByEmail(principal.getName()).get();
			System.out.println("accessing history of DENTAL purchases of user "+user.getId()+"/"+user.getEmail());
			return ResponseEntity.ok(
						dentalPolicyPurchaseRepository.findByUserId(user.getId()).stream()
															 .map(policyRequest -> new DentalPolicyPurchaseResponse(policyRequest))
															 .collect(Collectors.toList())
					);
		} catch (NoSuchElementException | NullPointerException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@PostMapping("purchase/DENTAL_AND_VISION")
	public ResponseEntity<String> purchase(@RequestBody @Valid RequestDentalAndVisionPolicyPurchase request, Principal principal){//@RequestBody Map<String, Object> request, Principal principal) {

		DentalAndVisionPolicyPurchase requestDentalAndVisionPolicyPurchase;
		System.out.println("purchase DENTAL_AND_VISION request - "+request.toString());
		//ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
		try {
			requestDentalAndVisionPolicyPurchase = mapper.convertValue(request, DentalAndVisionPolicyPurchase.class);
			
			if(!dentalAndVisionPolicyRepository.existsById(request.getPolicyId())) 
				return ResponseEntity.badRequest().body("No policy of given policy id");
			
			requestDentalAndVisionPolicyPurchase.setPolicy(dentalAndVisionPolicyRepository.getById(request.getPolicyId()));
			
			requestDentalAndVisionPolicyPurchase.setApplicationState(ApplicationStateEnum.SUBMITTED);
			
			requestDentalAndVisionPolicyPurchase.setUser(userRepository.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException()));
			
			dentalAndVisionPolicyPurchaseRepository.saveAndFlush(requestDentalAndVisionPolicyPurchase);
		} catch(IllegalArgumentException | ClassCastException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid request data");
		}
		
		return ResponseEntity.ok("Purchase request submitted");
	}
	
	@GetMapping("history/DENTAL_AND_VISION")
	public ResponseEntity<List<DentalAndVisionPolicyPurchaseResponse>> getOrderHistoryForDentalAndVision(Principal principal){
		try {
			User user = userRepository.findByEmail(principal.getName()).get();
			System.out.println("accessing history of DENTAL_AND_VISION purchases of user "+user.getId()+"/"+user.getEmail());
			return ResponseEntity.ok(
						dentalAndVisionPolicyPurchaseRepository.findByUserId(user.getId()).stream()
															 .map(policyRequest -> new DentalAndVisionPolicyPurchaseResponse(policyRequest))
															 .collect(Collectors.toList())
					);
		} catch (NoSuchElementException | NullPointerException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}
