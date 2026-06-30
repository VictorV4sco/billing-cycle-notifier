package com.billingnotifier.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.billingnotifier.enums.MessageStatus;

@DataJpaTest
@ActiveProfiles("test")
class MessageAndStatementItemPersistenceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldPersistBillingStatementItemSnapshotsAndMessageDefaults() {
		Person person = persistPerson();
		User user = person.getUser();
		Charge charge = persistCharge(person);
		BillingStatement billingStatement = persistBillingStatement(user, person);

		BillingStatementItem item = new BillingStatementItem();
		item.setBillingStatement(billingStatement);
		item.setCharge(charge);
		item.setDescriptionSnapshot(charge.getDescription());
		item.setAmountSnapshot(charge.getAmount());
		item.setChargeDateSnapshot(charge.getChargeDate());

		BillingStatementItem persistedItem = entityManager.persistFlushFind(item);

		Message message = new Message();
		message.setPerson(person);
		message.setBillingStatement(billingStatement);
		message.setRecipient(person.getWhatsappNumber());
		message.setContent("You owe 60.00 for the current billing cycle.");

		Message persistedMessage = entityManager.persistFlushFind(message);

		assertThat(persistedItem.getId()).isNotNull();
		assertThat(persistedItem.getDescriptionSnapshot()).isEqualTo("Groceries");
		assertThat(persistedItem.getAmountSnapshot()).isEqualByComparingTo("60.00");
		assertThat(persistedItem.getChargeDateSnapshot()).isEqualTo(LocalDate.of(2026, 6, 25));
		assertThat(persistedItem.getBillingStatement().getId()).isEqualTo(billingStatement.getId());
		assertThat(persistedItem.getCharge().getId()).isEqualTo(charge.getId());

		assertThat(persistedMessage.getId()).isNotNull();
		assertThat(persistedMessage.getStatus()).isEqualTo(MessageStatus.PENDING);
		assertThat(persistedMessage.getRecipient()).isEqualTo(person.getWhatsappNumber());
		assertThat(persistedMessage.getBillingStatement().getId()).isEqualTo(billingStatement.getId());
		assertThat(persistedMessage.getPerson().getId()).isEqualTo(person.getId());
		assertThat(persistedMessage.getCreatedAt()).isNotNull();
		assertThat(persistedMessage.getUpdatedAt()).isNotNull();
	}

	private BillingStatement persistBillingStatement(User user, Person person) {
		BillingStatement billingStatement = new BillingStatement();
		billingStatement.setUser(user);
		billingStatement.setPerson(person);
		billingStatement.setCycleStart(LocalDate.of(2026, 6, 10));
		billingStatement.setCycleEnd(LocalDate.of(2026, 7, 9));
		billingStatement.setTotalAmount(new BigDecimal("60.00"));
		return entityManager.persistFlushFind(billingStatement);
	}

	private Charge persistCharge(Person person) {
		Charge charge = new Charge();
		charge.setPerson(person);
		charge.setDescription("Groceries");
		charge.setAmount(new BigDecimal("60.00"));
		charge.setChargeDate(LocalDate.of(2026, 6, 25));
		return entityManager.persistFlushFind(charge);
	}

	private Person persistPerson() {
		User user = new User();
		user.setFullName("Victor Vasco");
		user.setEmail("victor-message@example.com");
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(14, 0));
		user.setTimezone("America/Sao_Paulo");

		entityManager.persist(user);

		Person person = new Person();
		person.setUser(user);
		person.setFullName("Ana Costa");
		person.setWhatsappNumber("5511777777777");
		return entityManager.persistFlushFind(person);
	}

}
