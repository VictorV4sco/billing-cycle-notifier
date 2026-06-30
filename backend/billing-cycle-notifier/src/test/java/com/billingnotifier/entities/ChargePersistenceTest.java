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

import com.billingnotifier.enums.ChargeStatus;

import jakarta.persistence.PersistenceException;

@DataJpaTest
@ActiveProfiles("test")
class ChargePersistenceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldPersistChargeWithPendingStatusByDefault() {
		Person person = persistPerson();

		Charge charge = new Charge();
		charge.setPerson(person);
		charge.setDescription("Dinner");
		charge.setAmount(new BigDecimal("89.90"));
		charge.setChargeDate(LocalDate.of(2026, 6, 30));

		Charge persistedCharge = entityManager.persistFlushFind(charge);

		assertThat(persistedCharge.getId()).isNotNull();
		assertThat(persistedCharge.getStatus()).isEqualTo(ChargeStatus.PENDING);
		assertThat(persistedCharge.getCreatedAt()).isNotNull();
		assertThat(persistedCharge.getUpdatedAt()).isNotNull();
		assertThat(persistedCharge.getPerson().getId()).isEqualTo(person.getId());
	}

	@Test
	void shouldNotPersistChargeWithoutDescription() {
		Person person = persistPerson();

		Charge charge = new Charge();
		charge.setPerson(person);
		charge.setAmount(new BigDecimal("25.00"));
		charge.setChargeDate(LocalDate.of(2026, 6, 30));

		assertThatThrownBy(() -> {
			entityManager.persist(charge);
			entityManager.flush();
		}).isInstanceOf(PersistenceException.class);
	}

	private Person persistPerson() {
		User user = new User();
		user.setFullName("Victor Vasco");
		user.setEmail("victor@example.com");
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(9, 0));
		user.setTimezone("America/Sao_Paulo");

		entityManager.persist(user);

		Person person = new Person();
		person.setUser(user);
		person.setFullName("Maria Silva");
		person.setWhatsappNumber("5511999999999");
		person.setNotes("Shared expenses");

		return entityManager.persistFlushFind(person);
	}

}
