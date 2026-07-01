package com.billingnotifier.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.Message;
import com.billingnotifier.enums.MessageStatus;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("""
		select m
		from Message m
		where m.billingStatement.id = :billingStatementId
		order by m.createdAt desc
		""")
	List<Message> findByBillingStatementId(@Param("billingStatementId") Long billingStatementId);

	@Query("""
		select m
		from Message m
		where m.status = :status
		order by m.createdAt
		""")
	List<Message> findByStatus(@Param("status") MessageStatus status);

	@Query("""
		select m
		from Message m
		where m.billingStatement.id = :billingStatementId
		order by m.createdAt desc
		""")
	List<Message> findLatestByBillingStatementId(@Param("billingStatementId") Long billingStatementId, Pageable pageable);

	default Optional<Message> findLatestMessageByBillingStatementId(Long billingStatementId) {
		return findLatestByBillingStatementId(billingStatementId, PageRequest.of(0, 1)).stream().findFirst();
	}

}
