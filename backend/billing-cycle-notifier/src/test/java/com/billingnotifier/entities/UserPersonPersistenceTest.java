package com.billingnotifier.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserPersonPersistenceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldPersistUserAndPersonRelationship() {
		User user = new User();
		user.setFullName("Victor Vasco");
		user.setEmail("victor@example.com");
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(9, 30));
		user.setTimezone("America/Sao_Paulo");

		User persistedUser = entityManager.persistFlushFind(user);

		Person person = new Person();
		person.setUser(persistedUser);
		person.setFullName("Maria Silva");
		person.setWhatsappNumber("5511999999999");
		person.setNotes("Main debtor");

		Person persistedPerson = entityManager.persistFlushFind(person);

		assertThat(persistedUser.getId()).isNotNull();
		assertThat(persistedUser.isActive()).isTrue();
		assertThat(persistedUser.getCreatedAt()).isNotNull();
		assertThat(persistedUser.getUpdatedAt()).isNotNull();
		assertThat(persistedUser.getBillingCycleOpeningDay()).isEqualTo(10);
		assertThat(persistedUser.getBillingCycleClosingDay()).isEqualTo(9);
		assertThat(persistedUser.getMessageSendTime()).isEqualTo(LocalTime.of(9, 30));

		assertThat(persistedPerson.getId()).isNotNull();
		assertThat(persistedPerson.isActive()).isTrue();
		assertThat(persistedPerson.getCreatedAt()).isNotNull();
		assertThat(persistedPerson.getUpdatedAt()).isNotNull();
		assertThat(persistedPerson.getUser().getId()).isEqualTo(persistedUser.getId());
	}

}
