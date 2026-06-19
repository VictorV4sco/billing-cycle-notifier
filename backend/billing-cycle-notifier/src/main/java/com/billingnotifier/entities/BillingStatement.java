package com.billingnotifier.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.billingnotifier.enums.BillingStatementStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "billing_statements",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_billing_statement_person_cycle",
		columnNames = {"person_id", "cycle_start", "cycle_end"}
	)
)
public class BillingStatement extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	@Column(name = "cycle_start", nullable = false)
	private LocalDate cycleStart;

	@Column(name = "cycle_end", nullable = false)
	private LocalDate cycleEnd;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private BillingStatementStatus status = BillingStatementStatus.GENERATED;

	@Column(nullable = false)
	private LocalDateTime generatedAt;

	private LocalDateTime sentAt;

	private LocalDateTime paidAt;

	@OneToMany(mappedBy = "billingStatement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingStatementItem> items = new ArrayList<>();

	@OneToMany(mappedBy = "billingStatement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	@PrePersist
	protected void onGenerate() {
		if (generatedAt == null) {
			generatedAt = LocalDateTime.now();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public LocalDate getCycleStart() {
		return cycleStart;
	}

	public void setCycleStart(LocalDate cycleStart) {
		this.cycleStart = cycleStart;
	}

	public LocalDate getCycleEnd() {
		return cycleEnd;
	}

	public void setCycleEnd(LocalDate cycleEnd) {
		this.cycleEnd = cycleEnd;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BillingStatementStatus getStatus() {
		return status;
	}

	public void setStatus(BillingStatementStatus status) {
		this.status = status;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}

	public List<BillingStatementItem> getItems() {
		return items;
	}

	public void setItems(List<BillingStatementItem> items) {
		this.items = items;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
