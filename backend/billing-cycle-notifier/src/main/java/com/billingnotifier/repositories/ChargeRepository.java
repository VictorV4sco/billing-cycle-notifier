package com.billingnotifier.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.Charge;
import com.billingnotifier.enums.ChargeStatus;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

	@Query("""
		select c
		from Charge c
		where c.person.id = :personId
		  and c.status = :status
		  and c.chargeDate between :startDate and :endDate
		order by c.chargeDate, c.id
		""")
	List<Charge> findByPersonIdAndStatusWithinPeriod(
		@Param("personId") Long personId,
		@Param("status") ChargeStatus status,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate
	);

	@Query("""
		select c
		from Charge c
		where c.person.user.id = :userId
		  and c.status = :status
		  and c.chargeDate between :startDate and :endDate
		order by c.person.id, c.chargeDate, c.id
		""")
	List<Charge> findByUserIdAndStatusWithinPeriod(
		@Param("userId") Long userId,
		@Param("status") ChargeStatus status,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate
	);

	@Query("""
		select c
		from Charge c
		where c.id = :chargeId
		  and c.person.id = :personId
		""")
	Optional<Charge> findByIdAndPersonId(@Param("chargeId") Long chargeId, @Param("personId") Long personId);

}
