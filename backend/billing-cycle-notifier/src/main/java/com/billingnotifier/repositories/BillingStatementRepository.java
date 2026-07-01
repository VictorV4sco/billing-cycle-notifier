package com.billingnotifier.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.BillingStatement;
import com.billingnotifier.enums.BillingStatementStatus;

public interface BillingStatementRepository extends JpaRepository<BillingStatement, Long> {

	@Query("""
		select case when count(bs) > 0 then true else false end
		from BillingStatement bs
		where bs.person.id = :personId
		  and bs.cycleStart = :cycleStart
		  and bs.cycleEnd = :cycleEnd
		""")
	boolean existsByPersonIdAndCycle(
		@Param("personId") Long personId,
		@Param("cycleStart") LocalDate cycleStart,
		@Param("cycleEnd") LocalDate cycleEnd
	);

	@Query("""
		select bs
		from BillingStatement bs
		where bs.person.id = :personId
		  and bs.cycleStart = :cycleStart
		  and bs.cycleEnd = :cycleEnd
		""")
	Optional<BillingStatement> findByPersonIdAndCycle(
		@Param("personId") Long personId,
		@Param("cycleStart") LocalDate cycleStart,
		@Param("cycleEnd") LocalDate cycleEnd
	);

	@Query("""
		select bs
		from BillingStatement bs
		where bs.user.id = :userId
		  and bs.status = :status
		order by bs.generatedAt desc
		""")
	List<BillingStatement> findByUserIdAndStatus(
		@Param("userId") Long userId,
		@Param("status") BillingStatementStatus status
	);

	@Query("""
		select bs
		from BillingStatement bs
		where bs.person.id = :personId
		order by bs.cycleEnd desc
		""")
	List<BillingStatement> findByPersonIdOrderByCycleEndDesc(@Param("personId") Long personId);

}
