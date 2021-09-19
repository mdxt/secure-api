package com.mdxt.secureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.DentalPolicy;
import com.mdxt.secureapi.entity.LifeInsurancePolicy;

@Repository
public interface DentalPolicyRepository extends JpaRepository<DentalPolicy, Long>{
	
	@Query("select p from DentalPolicy p where "
			+ "(:value between p.minCoverValue and p.maxCoverValue) and "
			+ "(:tillAge between p.minCoverTillAge and p.maxCoverTillAge) and "
			+ "(:dependents between p.minNumberCovered and p.maxNumberCovered)")
	List<DentalPolicy> findAvailablePolicies(@Param("value") Long value, @Param("tillAge") Integer tillAge, @Param("dependents") Integer dependents);
}
