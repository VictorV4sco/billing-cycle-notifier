package com.billingnotifier.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.billingnotifier.entities.BillingStatement;
import com.billingnotifier.entities.BillingStatementItem;
import com.billingnotifier.entities.Charge;
import com.billingnotifier.entities.Message;
import com.billingnotifier.entities.Person;
import com.billingnotifier.entities.User;
import com.billingnotifier.enums.MessageStatus;

@DataJpaTest
@ActiveProfiles("test")
class MessageAndBillingStatementItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BillingStatementItemRepository billingStatementItemRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Test
	void shouldFindStatementItemsAndMessagesWithExpectedOrdering() {
		User user = persistUser();
		Person person = persistPerson(user);
		Charge firstCharge = persistCharge(person, "Dinner", "25.00", LocalDate.of(2026, 6, 15));
		Charge secondCharge = persistCharge(person, "Market", "40.00", LocalDate.of(2026, 6, 18));
		BillingStatement billingStatement = persistBillingStatement(user, person);

		persistStatementItem(billingStatement, secondCharge);
		persistStatementItem(billingStatement, firstCharge);

		Message olderMessage = persistMessage(person, billingStatement, "First message", MessageStatus.FAILED,
			LocalDateTime.of(2026, 7, 9, 9, 0));
		Message latestMessage = persistMessage(person, billingStatement, "Latest message", MessageStatus.PENDING,
			LocalDateTime.of(2026, 7, 9, 10, 0));

		List<BillingStatementItem> items = billingStatementItemRepository.findByBillingStatementId(billingStatement.getId());
		List<Message> messages = messageRepository.findByBillingStatementId(billingStatement.getId());
		List<Message> pendingMessages = messageRepository.findByStatus(MessageStatus.PENDING);
		List<Message> latestMessages = messageRepository.findLatestByBillingStatementId(
			billingStatement.getId(),
			PageRequest.of(0, 1)
		);
		Optional<Message> latestMessageOptional = messageRepository.findLatestMessageByBillingStatementId(
			billingStatement.getId()
		);

		assertThat(items).extracting(item -> item.getCharge().getDescription()).containsExactly("Dinner", "Market");
		assertThat(messages).containsExactly(latestMessage, olderMessage);
		assertThat(pendingMessages).containsExactly(latestMessage);
		assertThat(latestMessages).containsExactly(latestMessage);
		assertThat(latestMessageOptional).contains(latestMessage);
	}

	private User persistUser() {
		User user = new User();
		user.setFullName("Victor Vasco");
		user.setEmail("messages@example.com");
		user.setBillingCycleOpeningDay(10);
		user.setBillingCycleClosingDay(9);
		user.setMessageSendTime(LocalTime.of(14, 0));
		user.setTimezone("America/Sao_Paulo");
		return entityManager.persistFlushFind(user);
	}

	private Person persistPerson(User user) {
		Person person = new Person();
		person.setUser(user);
		person.setFullName("Ana Costa");
		person.setWhatsappNumber("5511777777777");
		return entityManager.persistFlushFind(person);
	}

	private Charge persistCharge(Person person, String description, String amount, LocalDate chargeDate) {
		Charge charge = new Charge();
		charge.setPerson(person);
		charge.setDescription(description);
		charge.setAmount(new BigDecimal(amount));
		charge.setChargeDate(chargeDate);
		return entityManager.persistFlushFind(charge);
	}

	private BillingStatement persistBillingStatement(User user, Person person) {
		BillingStatement billingStatement = new BillingStatement();
		billingStatement.setUser(user);
		billingStatement.setPerson(person);
		billingStatement.setCycleStart(LocalDate.of(2026, 6, 10));
		billingStatement.setCycleEnd(LocalDate.of(2026, 7, 9));
		billingStatement.setTotalAmount(new BigDecimal("65.00"));
		return entityManager.persistFlushFind(billingStatement);
	}

	private BillingStatementItem persistStatementItem(BillingStatement billingStatement, Charge charge) {
		BillingStatementItem item = new BillingStatementItem();
		item.setBillingStatement(billingStatement);
		item.setCharge(charge);
		item.setDescriptionSnapshot(charge.getDescription());
		item.setAmountSnapshot(charge.getAmount());
		item.setChargeDateSnapshot(charge.getChargeDate());
		return entityManager.persistFlushFind(item);
	}

	private Message persistMessage(Person person, BillingStatement billingStatement, String content, MessageStatus status,
		LocalDateTime createdAt) {
		Message message = new Message();
		message.setPerson(person);
		message.setBillingStatement(billingStatement);
		message.setRecipient(person.getWhatsappNumber());
		message.setContent(content);
		message.setStatus(status);
		message.setCreatedAt(createdAt);
		message.setUpdatedAt(createdAt);
		return entityManager.persistFlushFind(message);
	}

}
