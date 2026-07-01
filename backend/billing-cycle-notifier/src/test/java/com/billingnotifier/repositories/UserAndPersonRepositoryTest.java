package com.billingnotifier.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.billingnotifier.entities.Person;
import com.billingnotifier.entities.User;

@DataJpaTest
@ActiveProfiles("test")
class UserAndPersonRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PersonRepository personRepository;

	@Test
	void shouldFindActiveUsersAndPersonsByConfiguredQueries() {
		User activeUser = persistUser("active@example.com", 9, true);
		User inactiveUser = persistUser("inactive@example.com", 9, false);

		Person activePerson = persistPerson(activeUser, "Maria Silva", "5511999999999", true);
		persistPerson(activeUser, "Joao Inactive", "5511888888888", false);
		persistPerson(inactiveUser, "Carlos Lima", "5511777777777", true);

		Optional<User> foundByEmail = userRepository.findByEmail("active@example.com");
		List<User> activeUsersByClosingDay = userRepository.findActiveUsersByClosingDay(9);
		Optional<User> foundActiveUser = userRepository.findActiveById(activeUser.getId());
		List<Person> activePersons = personRepository.findActiveByUserId(activeUser.getId());
		Optional<Person> foundPersonByOwner = personRepository.findByIdAndUserId(activePerson.getId(), activeUser.getId());
		Optional<Person> foundByWhatsapp = personRepository.findActiveByWhatsappNumberAndUserId("5511999999999",
			activeUser.getId());

		assertThat(foundByEmail).contains(activeUser);
		assertThat(activeUsersByClosingDay).extracting(User::getEmail).containsExactly("active@example.com");
		assertThat(foundActiveUser).contains(activeUser);
		assertThat(activePersons).extracting(Person::getFullName).containsExactly("Maria Silva");
		assertThat(foundPersonByOwner).contains(activePerson);
		assertThat(foundByWhatsapp).contains(activePerson);
	}

	private User persistUser(String email, int closingDay, boolean active) {
		User user = new User();
		user.setFullName("User " + email);
		user.setEmail(email);
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(closingDay);
		user.setMessageSendTime(LocalTime.of(9, 0));
		user.setTimezone("America/Sao_Paulo");
		user.setActive(active);
		return entityManager.persistFlushFind(user);
	}

	private Person persistPerson(User user, String fullName, String whatsappNumber, boolean active) {
		Person person = new Person();
		person.setUser(user);
		person.setFullName(fullName);
		person.setWhatsappNumber(whatsappNumber);
		person.setActive(active);
		return entityManager.persistFlushFind(person);
	}

}
