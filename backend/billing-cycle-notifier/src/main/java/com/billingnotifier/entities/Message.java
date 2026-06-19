package com.billingnotifier.entities;

import java.time.LocalDateTime;

import com.billingnotifier.enums.MessageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	@ManyToOne(optional = false)
	@JoinColumn(name = "billing_statement_id", nullable = false)
	private BillingStatement billingStatement;

	@Column(nullable = false, length = 20)
	private String recipient;

	@Lob
	@Column(nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private MessageStatus status = MessageStatus.PENDING;

	@Column(length = 120)
	private String providerMessageId;

	@Column(length = 500)
	private String errorMessage;

	private LocalDateTime sentAt;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public BillingStatement getBillingStatement() {
		return billingStatement;
	}

	public void setBillingStatement(BillingStatement billingStatement) {
		this.billingStatement = billingStatement;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public String getProviderMessageId() {
		return providerMessageId;
	}

	public void setProviderMessageId(String providerMessageId) {
		this.providerMessageId = providerMessageId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

}
