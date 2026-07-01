package com.billingnotifier.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.billingnotifier.entities.BillingStatement;
import com.billingnotifier.entities.Person;
import com.billingnotifier.entities.User;
import com.billingnotifier.enums.BillingStatementStatus;

@DataJpaTest
@ActiveProfiles("test")
class BillingStatementRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BillingStatementRepository billingStatementRepository;

	@Test
	void shouldFindStatementsByCycleUserAndPerson() {
		User user = persistUser("statement@example.com");
		Person person = persistPerson(user, "Maria Silva");
		BillingStatement older = persistStatement(user, person, LocalDate.of(2026, 5, 10), LocalDate.of(2026, 6, 9),
			"100.00", BillingStatementStatus.SENT);
		BillingStatement latest = persistStatement(user, person, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 7, 9),
			"250.00", BillingStatementStatus.GENERATED);

		boolean exists = billingStatementRepository.existsByPersonIdAndCycle(
			person.getId(),
			LocalDate.of(2026, 6, 10),
			LocalDate.of(2026, 7, 9)
		);
		Optional<BillingStatement> foundByCycle = billingStatementRepository.findByPersonIdAndCycle(
			person.getId(),
			LocalDate.of(2026, 6, 10),
			LocalDate.of(2026, 7, 9)
		);
		List<BillingStatement> generatedStatements = billingStatementRepository.findByUserIdAndStatus(
			user.getId(),
			BillingStatementStatus.GENERATED
		);
		List<BillingStatement> byPerson = billingStatementRepository.findByPersonIdOrderByCycleEndDesc(person.getId());

		assertThat(exists).isTrue();
		assertThat(foundByCycle).contains(latest);
		assertThat(generatedStatements).containsExactly(latest);
		assertThat(byPerson).containsExactly(latest, older);
	}

	private User persistUser(String email) {
		User user = new User();
		user.setFullName("User " + email);
		user.setEmail(email);
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(10, 0));
		user.setTimezone("America/Sao_Paulo");
		return entityManager.persistFlushFind(user);
	}

	private Person persistPerson(User user, String fullName) {
		Person person = new Person();
		person.setUser(user);
		person.setFullName(fullName);
		person.setWhatsappNumber("5511991111111");
		return entityManager.persistFlushFind(person);
	}

	private BillingStatement persistStatement(User user, Person person, LocalDate cycleStart, LocalDate cycleEnd,
		String totalAmount, BillingStatementStatus status) {
		BillingStatement billingStatement = new BillingStatement();
		billingStatement.setUser(user);
		billingStatement.setPerson(person);
		billingStatement.setCycleStart(cycleStart);
		billingStatement.setCycleEnd(cycleEnd);
		billingStatement.setTotalAmount(new BigDecimal(totalAmount));
		billingStatement.setStatus(status);
		return entityManager.persistFlushFind(billingStatement);
	}

}
