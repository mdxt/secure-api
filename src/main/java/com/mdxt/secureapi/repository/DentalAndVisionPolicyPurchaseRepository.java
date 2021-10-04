package com.mdxt.secureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.DentalAndVisionPolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;

@Repository
public interface DentalAndVisionPolicyPurchaseRepository extends JpaRepository<DentalAndVisionPolicyPurchase, Long>{
	List<DentalAndVisionPolicyPurchase> findByUserId(Long userId);
	List<DentalAndVisionPolicyPurchase> findByApplicationState(ApplicationStateEnum applicationState);
	List<DentalAndVisionPolicyPurchase> findByApplicationStateAndAssignedUnderwriter(ApplicationStateEnum applicationState, User assignedUnderwriter);

}
