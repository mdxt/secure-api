package com.mdxt.secureapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.LifeInsurancePolicyPurchase;
import com.mdxt.secureapi.enums.ApplicationStateEnum;

@Repository
public interface LifeInsurancePolicyPurchaseRepository extends JpaRepository<LifeInsurancePolicyPurchase, Long>{
	List<LifeInsurancePolicyPurchase> findByUserId(Long userId);
	List<LifeInsurancePolicyPurchase> findByApplicationState(ApplicationStateEnum applicationState);
}
