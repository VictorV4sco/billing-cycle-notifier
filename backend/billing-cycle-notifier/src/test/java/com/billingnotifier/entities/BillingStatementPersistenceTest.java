package com.billingnotifier.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.billingnotifier.enums.BillingStatementStatus;

import jakarta.persistence.PersistenceException;

@DataJpaTest
@ActiveProfiles("test")
class BillingStatementPersistenceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldPersistBillingStatementWithGeneratedDefaults() {
		Person person = persistPerson();
		User user = person.getUser();

		BillingStatement billingStatement = new BillingStatement();
		billingStatement.setUser(user);
		billingStatement.setPerson(person);
		billingStatement.setCycleStart(LocalDate.of(2026, 6, 10));
		billingStatement.setCycleEnd(LocalDate.of(2026, 7, 9));
		billingStatement.setTotalAmount(new BigDecimal("145.80"));

		BillingStatement persistedStatement = entityManager.persistFlushFind(billingStatement);

		assertThat(persistedStatement.getId()).isNotNull();
		assertThat(persistedStatement.getStatus()).isEqualTo(BillingStatementStatus.GENERATED);
		assertThat(persistedStatement.getGeneratedAt()).isNotNull();
		assertThat(persistedStatement.getCreatedAt()).isNotNull();
		assertThat(persistedStatement.getUpdatedAt()).isNotNull();
		assertThat(persistedStatement.getPerson().getId()).isEqualTo(person.getId());
		assertThat(persistedStatement.getUser().getId()).isEqualTo(user.getId());
	}

	@Test
	void shouldNotAllowDuplicateBillingStatementForSamePersonAndCycle() {
		Person person = persistPerson();
		User user = person.getUser();

		BillingStatement firstStatement = buildStatement(user, person);
		entityManager.persistAndFlush(firstStatement);

		BillingStatement duplicateStatement = buildStatement(user, person);

		assertThatThrownBy(() -> {
			entityManager.persist(duplicateStatement);
			entityManager.flush();
		}).isInstanceOf(PersistenceException.class);
	}

	private BillingStatement buildStatement(User user, Person person) {
		BillingStatement billingStatement = new BillingStatement();
		billingStatement.setUser(user);
		billingStatement.setPerson(person);
		billingStatement.setCycleStart(LocalDate.of(2026, 6, 10));
		billingStatement.setCycleEnd(LocalDate.of(2026, 7, 9));
		billingStatement.setTotalAmount(new BigDecimal("145.80"));
		return billingStatement;
	}

	private Person persistPerson() {
		User user = new User();
		user.setFullName("Victor Vasco");
		user.setEmail("victor@example.com");
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(10, 0));
		user.setTimezone("America/Sao_Paulo");

		entityManager.persist(user);

		Person person = new Person();
		person.setUser(user);
		person.setFullName("Carlos Lima");
		person.setWhatsappNumber("5511888888888");

		return entityManager.persistFlushFind(person);
	}

}
