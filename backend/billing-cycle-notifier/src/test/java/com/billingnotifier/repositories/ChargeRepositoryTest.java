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

import com.billingnotifier.entities.Charge;
import com.billingnotifier.entities.Person;
import com.billingnotifier.entities.User;
import com.billingnotifier.enums.ChargeStatus;

@DataJpaTest
@ActiveProfiles("test")
class ChargeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ChargeRepository chargeRepository;

	@Test
	void shouldFindChargesByPersonAndUserWithinPeriod() {
		User user = persistUser("charges@example.com");
		Person person = persistPerson(user, "Maria Silva");
		Charge includedFirst = persistCharge(person, "Dinner", "30.00", LocalDate.of(2026, 6, 12), ChargeStatus.PENDING);
		Charge includedSecond = persistCharge(person, "Market", "70.00", LocalDate.of(2026, 6, 20), ChargeStatus.PENDING);
		persistCharge(person, "Paid item", "15.00", LocalDate.of(2026, 6, 18), ChargeStatus.PAID);
		persistCharge(person, "Outside period", "99.00", LocalDate.of(2026, 7, 12), ChargeStatus.PENDING);

		List<Charge> byPerson = chargeRepository.findByPersonIdAndStatusWithinPeriod(
			person.getId(),
			ChargeStatus.PENDING,
			LocalDate.of(2026, 6, 10),
			LocalDate.of(2026, 7, 9)
		);

		List<Charge> byUser = chargeRepository.findByUserIdAndStatusWithinPeriod(
			user.getId(),
			ChargeStatus.PENDING,
			LocalDate.of(2026, 6, 10),
			LocalDate.of(2026, 7, 9)
		);

		Optional<Charge> foundByIdAndPerson = chargeRepository.findByIdAndPersonId(includedFirst.getId(), person.getId());

		assertThat(byPerson).extracting(Charge::getDescription).containsExactly("Dinner", "Market");
		assertThat(byUser).extracting(Charge::getDescription).containsExactly("Dinner", "Market");
		assertThat(foundByIdAndPerson).contains(includedFirst);
		assertThat(byPerson).extracting(Charge::getId).contains(includedSecond.getId());
	}

	private User persistUser(String email) {
		User user = new User();
		user.setFullName("User " + email);
		user.setEmail(email);
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(9, 0));
		user.setTimezone("America/Sao_Paulo");
		return entityManager.persistFlushFind(user);
	}

	private Person persistPerson(User user, String fullName) {
		Person person = new Person();
		person.setUser(user);
		person.setFullName(fullName);
		person.setWhatsappNumber("5511000000000");
		return entityManager.persistFlushFind(person);
	}

	private Charge persistCharge(Person person, String description, String amount, LocalDate chargeDate, ChargeStatus status) {
		Charge charge = new Charge();
		charge.setPerson(person);
		charge.setDescription(description);
		charge.setAmount(new BigDecimal(amount));
		charge.setChargeDate(chargeDate);
		charge.setStatus(status);
		return entityManager.persistFlushFind(charge);
	}

}
