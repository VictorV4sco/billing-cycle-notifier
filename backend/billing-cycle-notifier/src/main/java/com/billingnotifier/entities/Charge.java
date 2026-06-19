package com.billingnotifier.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.billingnotifier.enums.ChargeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "charges")
public class Charge extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	@Column(nullable = false, length = 255)
	private String description;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false)
	private LocalDate chargeDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private ChargeStatus status = ChargeStatus.PENDING;

	@Column(length = 500)
	private String notes;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(LocalDate chargeDate) {
		this.chargeDate = chargeDate;
	}

	public ChargeStatus getStatus() {
		return status;
	}

	public void setStatus(ChargeStatus status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
