package com.billingnotifier.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.BillingStatementItem;

public interface BillingStatementItemRepository extends JpaRepository<BillingStatementItem, Long> {

	@Query("""
		select bsi
		from BillingStatementItem bsi
		where bsi.billingStatement.id = :billingStatementId
		order by bsi.chargeDateSnapshot, bsi.id
		""")
	List<BillingStatementItem> findByBillingStatementId(@Param("billingStatementId") Long billingStatementId);

}
