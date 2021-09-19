package com.mdxt.secureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.dto.request.RequestLifeInsurancePolicyList;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;

@Repository
public interface LifeInsurancePolicyRepository extends JpaRepository<LifeInsurancePolicy, Long>{
	
	@Query("select p from LifeInsurancePolicy p where "
			+ "(:value between p.minCoverValue and p.maxCoverValue) and "
			+ "(:tillAge between p.minCoverTillAge and p.maxCoverTillAge)")
	List<LifeInsurancePolicy> findAvailablePolicies(@Param("value") Long value, @Param("tillAge") Integer tillAge);
}
