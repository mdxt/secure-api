package com.mdxt.secureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.DentalPolicyPurchase;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.enums.ApplicationStateEnum;

@Repository
public interface DentalPolicyPurchaseRepository extends JpaRepository<DentalPolicyPurchase, Long>{
	List<DentalPolicyPurchase> findByUserId(Long userId);
	List<DentalPolicyPurchase> findByApplicationState(ApplicationStateEnum applicationState);
	List<DentalPolicyPurchase> findByApplicationStateAndAssignedUnderwriter(ApplicationStateEnum applicationState, User assignedUnderwriter);
}
