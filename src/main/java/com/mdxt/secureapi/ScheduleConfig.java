package com.mdxt.secureapi;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mdxt.secureapi.entity.DentalAndVisionPolicyPurchase;
import com.mdxt.secureapi.entity.DentalPolicyPurchase;
import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.entity.Role;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;
import com.mdxt.secureapi.repository.DentalAndVisionPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.DentalPolicyPurchaseRepository;
import com.mdxt.secureapi.repository.LifeInsurancePolicyPurchaseRepository;
import com.mdxt.secureapi.repository.RoleRepository;
import com.mdxt.secureapi.repository.UserRepository;
import com.mdxt.secureapi.security.RolesEnum;

@Configuration
@EnableScheduling
public class ScheduleConfig {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private LifeInsurancePolicyPurchaseRepository lifeInsurancePolicyPurchaseRepository;
	
	@Autowired
	private DentalPolicyPurchaseRepository dentalPolicyPurchaseRepository;
	
	@Autowired
	private DentalAndVisionPolicyPurchaseRepository dentalAndVisionPolicyPurchaseRepository;
	
	@Scheduled(fixedRate = 20 * 1000)
	public void scheduleFixedRateTask() throws NoSuchElementException{
	    System.out.println(
	      "Fixed rate task - " + System.currentTimeMillis() / 1000);
	    
	    Role underwriterRole = roleRepository.findByName(RolesEnum.UNDERWRITER).get();
	    
	    User[] underwriterUsers = userRepository.findByRolesContaining(underwriterRole).toArray(User[]::new);
	    if(underwriterUsers == null || underwriterUsers.length == 0) {
	    	throw new NoSuchElementException("No underwriter users found");
	    }
	    
	    int i = new Random().nextInt(underwriterUsers.length);//TODO: Long?
	    
	    for(LifeInsurancePolicyPurchase policy: lifeInsurancePolicyPurchaseRepository.findByApplicationState(ApplicationStateEnum.SUBMITTED)) { //TODO: change to UNDERWRITER_REVIEW
	    	policy.setAssignedUnderwriter(underwriterUsers[(i++) % underwriterUsers.length]);
	    	policy.setApplicationState(ApplicationStateEnum.UNDERWRITER_REVIEW);
	    	System.out.println("assigned underwriter to policy "+policy.toString());
	    	lifeInsurancePolicyPurchaseRepository.save(policy);
	    }
	    lifeInsurancePolicyPurchaseRepository.flush();
	    
	    for(DentalPolicyPurchase policy: dentalPolicyPurchaseRepository.findByApplicationState(ApplicationStateEnum.SUBMITTED)) { //TODO: change to UNDERWRITER_REVIEW
	    	policy.setAssignedUnderwriter(underwriterUsers[(i++) % underwriterUsers.length]);
	    	policy.setApplicationState(ApplicationStateEnum.UNDERWRITER_REVIEW);
	    	System.out.println("assigned underwriter to policy "+policy.toString());
	    	dentalPolicyPurchaseRepository.save(policy);
	    }
	    dentalPolicyPurchaseRepository.flush();
	    
	    for(DentalAndVisionPolicyPurchase policy: dentalAndVisionPolicyPurchaseRepository.findByApplicationState(ApplicationStateEnum.SUBMITTED)) { //TODO: change to UNDERWRITER_REVIEW
	    	policy.setAssignedUnderwriter(underwriterUsers[(i++) % underwriterUsers.length]);
	    	policy.setApplicationState(ApplicationStateEnum.UNDERWRITER_REVIEW);
	    	System.out.println("assigned underwriter to policy "+policy.toString());
	    	dentalAndVisionPolicyPurchaseRepository.save(policy);
	    }
	    dentalPolicyPurchaseRepository.flush();
	}
	
}
