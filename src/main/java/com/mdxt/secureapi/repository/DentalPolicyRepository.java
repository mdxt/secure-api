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
			//+ "(:value in elements(p.coverValues)) and "
			+ "(:coverPeriod between p.minCoverPeriod and p.maxCoverPeriod) and "
			+ "(:dependents between p.minNumberCovered and p.maxNumberCovered)")
	List<DentalPolicy> findAvailablePolicies(@Param("coverPeriod") Integer coverPeriod, @Param("dependents") Integer dependents);
}
