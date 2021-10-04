package com.mdxt.secureapi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdxt.secureapi.dto.request.RequestCreateDentalAndVision;
import com.mdxt.secureapi.dto.request.RequestCreateDentalPolicy;
import com.mdxt.secureapi.dto.request.RequestCreateLifeInsurancePolicy;
import com.mdxt.secureapi.dto.response.DentalAndVisionPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.DentalPolicyListResponse;
import com.mdxt.secureapi.dto.response.DentalPolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.LifeInsurancePolicyPurchaseResponse;
import com.mdxt.secureapi.dto.response.UserResponse;
import com.mdxt.secureapi.entity.DentalAndVisionPolicy;
import com.mdxt.secureapi.entity.DentalPolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.repository.DentalAndVisionPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.DentalAndVisionPolicyRepository;
import com.mdxt.secureapi.repository.DentalPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.DentalPolicyRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyRepository;
import com.mdxt.secureapi.repository.RoleRepository;
import com.mdxt.secureapi.repository.UserRepository;
import com.mdxt.secureapi.security.RolesEnum;

import ch.qos.logback.core.Context;

@RestController
@RequestMapping("api/admin")
public class AdminController {
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Value("${application.static-folder-path}")
	private String staticFolderPath;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private LifeInsurancePolicyRepository lifeInsurancePolicyRepository;
	
	@Autowired
	private DentalPolicyRepository dentalPolicyRepository;
	
	@Autowired
	private DentalAndVisionPolicyRepository dentalAndVisionPolicyRepository;
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;
	
	@Autowired
	private DentalPolicyPurchaseRepository dentalPolicyPurchaseRepository;
	
	@Autowired
	private DentalAndVisionPolicyPurchaseRepository dentalAndVisionPolicyPurchaseRepository;
	
	@GetMapping("checkAccess")
	public ResponseEntity<Boolean> checkAccess(){
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("usersMatching/{patternString}")
	public List<UserResponse> getUsersMatching(@PathVariable("patternString") @Nullable String patternString){
		
		return userRepository.findByEmailContaining(patternString)
							 .stream()
							 .limit(10)
							 .filter(user -> !user.getRoles().contains(roleRepository.findByName(RolesEnum.ADMIN).get())
									 		 && !user.getRoles().contains(roleRepository.findByName(RolesEnum.UNDERWRITER).get()))
							 .map(user -> new UserResponse(user.getEmail(), user.getRoles()))
							 .collect(Collectors.toList());
	}
	
	@PostMapping("setAsUnderwriter")
	public ResponseEntity<String> setAsUnderwriter(@RequestBody String email){
		if(!userRepository.existsByEmail(email)) return ResponseEntity.badRequest().body("No such user");
		
		User user = userRepository.findByEmail(email).get();
		if(user.getRoles().contains(roleRepository.findByName(RolesEnum.UNDERWRITER).get()))
			return ResponseEntity.ok("This user is already an underwriter");
	
		user.getRoles().add(roleRepository.findByName(RolesEnum.UNDERWRITER).get());
		userRepository.saveAndFlush(user);
		
		return ResponseEntity.ok("User is now an underwriter");
	}
	
	@PostMapping("uploadPDF")
	public ResponseEntity<String> uploadPDF(@RequestBody MultipartFile file){
		if (file == null || file.isEmpty()) {

            return ResponseEntity.badRequest().body("Failed to store empty file");
        }
		
		System.out.println("inside upload "+file.getOriginalFilename());
		

        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            
            String fileLocation = new File(staticFolderPath).getAbsolutePath() + "/" + fileName;
            
            FileOutputStream output = new FileOutputStream(fileLocation);

    		output.write(is.readAllBytes());

    		output.close();
            
        } catch (IOException e) {

            var msg = String.format("Failed to store file "+ file.getName());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(msg);
        }
        
        return ResponseEntity.ok("File uploaded successfully");
	}
	
	@PostMapping("create/LIFE")
	public ResponseEntity<String> createLifePolicy(@RequestBody @Valid RequestCreateLifeInsurancePolicy request){
		System.out.println(request.toString());
		
		try {
			LifeInsurancePolicy policy = mapper.convertValue(request, LifeInsurancePolicy.class);
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath());
			
			policy.setMultiplierCoverTillAge(request.getMultiplierCoverTillAge());
			policy.setMultiplierCoverValue(request.getMultiplierCoverValue());
			
			List<String> additionalFeatures = new ArrayList<String>();
			for(String i : request.getAdditionalFeaturesCSV().split(",")) {
				if(i==null || i.equals("")) continue;
				additionalFeatures.add(i.trim());
			}
			policy.setAdditionalFeatures(additionalFeatures.toArray(new String[0]));
			policy.setDocumentPath("/public/static/"+request.getDocumentPath());
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath()+','+Arrays.toString(policy.getAdditionalFeatures()));
			
			lifeInsurancePolicyRepository.saveAndFlush(policy);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error creating policy");
		}
		return ResponseEntity.ok("Policy successfully created");
	}
	
	@PostMapping("create/DENTAL")
	public ResponseEntity<String> createDentalPolicy(@RequestBody @Valid RequestCreateDentalPolicy request){
		System.out.println(request.toString());
		
		try {
			DentalPolicy policy = mapper.convertValue(request, DentalPolicy.class);
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath());
			
			policy.setMultiplierCoverValue(request.getMultiplierCoverValue());
			policy.setMultiplierCoverPeriod(request.getMultiplierCoverPeriod());
			policy.setMultiplierNumberCovered(request.getMultiplierNumberCovered());
			
			List<String> additionalFeatures = new ArrayList<String>();
			for(String i : request.getAdditionalFeaturesCSV().split(",")) {
				if(i==null || i.equals("")) continue;
				additionalFeatures.add(i.trim());
			}
			policy.setAdditionalFeatures(additionalFeatures.toArray(new String[0]));
			
			List<Long> coverValues = new ArrayList<Long>();
			for(String i : request.getCoverValuesCSV().split(",")) {
				coverValues.add(Long.parseLong(i));
			}
			policy.setCoverValues(coverValues.toArray(new Long[0]));
			
			policy.setDocumentPath("/public/static/"+request.getDocumentPath());
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath()+','+Arrays.toString(policy.getAdditionalFeatures()));
			
			dentalPolicyRepository.saveAndFlush(policy);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error creating policy");
		}
		return ResponseEntity.ok("Policy successfully created");
	}
	
	@PostMapping("create/DENTAL_AND_VISION")
	public ResponseEntity<String> createDentalAndVisionPolicy(@RequestBody @Valid RequestCreateDentalAndVision request){
		System.out.println(request.toString());
		
		try {
			DentalAndVisionPolicy policy = mapper.convertValue(request, DentalAndVisionPolicy.class);
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath());
			
			policy.setMultiplierCoverValue(request.getMultiplierCoverValue());
			policy.setMultiplierCoverPeriod(request.getMultiplierCoverPeriod());
			policy.setMultiplierNumberCovered(request.getMultiplierNumberCovered());
			
			List<String> additionalFeatures = new ArrayList<String>();
			for(String i : request.getAdditionalFeaturesCSV().split(",")) {
				if(i==null || i.equals("")) continue;
				additionalFeatures.add(i.trim());
			}
			policy.setAdditionalFeatures(additionalFeatures.toArray(new String[0]));
			
			List<Long> coverValues = new ArrayList<Long>();
			for(String i : request.getCoverValuesCSV().split(",")) {
				coverValues.add(Long.parseLong(i));
			}
			policy.setCoverValues(coverValues.toArray(new Long[0]));
			
			policy.setDocumentPath("/public/static/"+request.getDocumentPath());
			System.out.println(policy.toString()+','+policy.getInsurer()+','+policy.getName()+','+policy.getDocumentPath()+','+Arrays.toString(policy.getAdditionalFeatures()));
			
			dentalAndVisionPolicyRepository.saveAndFlush(policy);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error creating policy");
		}
		return ResponseEntity.ok("Policy successfully created");
	}
	
	
	@GetMapping("all/LIFE")
	public ResponseEntity<List<LifeInsurancePolicyPurchaseResponse>> getAllLifeInsurancePolicyPurchases(){
		return ResponseEntity.ok(lifeInsurancePolicyPurchaseRepository.findAll()
																	  .stream()
																	  .map(response -> new LifeInsurancePolicyPurchaseResponse(response))
																	  .collect(Collectors.toList()));
	}
	
	@GetMapping("all/DENTAL")
	public ResponseEntity<List<DentalPolicyPurchaseResponse>> getAllDentalInsurancePolicyPurchases(){
		return ResponseEntity.ok(dentalPolicyPurchaseRepository.findAll()
															  .stream()
															  .map(response -> new DentalPolicyPurchaseResponse(response))
															  .collect(Collectors.toList()));
	}
	
	@GetMapping("all/DENTAL_AND_VISION")
	public ResponseEntity<List<DentalAndVisionPolicyPurchaseResponse>> getAllDentalAndVisionInsurancePolicyPurchases(){
		return ResponseEntity.ok(dentalAndVisionPolicyPurchaseRepository.findAll()
															  .stream()
															  .map(response -> new DentalAndVisionPolicyPurchaseResponse(response))
															  .collect(Collectors.toList()));
	}
}
