package com.billingnotifier.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "billing_statement_items")
public class BillingStatementItem extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "billing_statement_id", nullable = false)
	private BillingStatement billingStatement;

	@ManyToOne(optional = false)
	@JoinColumn(name = "charge_id", nullable = false)
	private Charge charge;

	@Column(nullable = false, length = 255)
	private String descriptionSnapshot;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amountSnapshot;

	@Column(nullable = false)
	private LocalDate chargeDateSnapshot;

	public BillingStatement getBillingStatement() {
		return billingStatement;
	}

	public void setBillingStatement(BillingStatement billingStatement) {
		this.billingStatement = billingStatement;
	}

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

	public String getDescriptionSnapshot() {
		return descriptionSnapshot;
	}

	public void setDescriptionSnapshot(String descriptionSnapshot) {
		this.descriptionSnapshot = descriptionSnapshot;
	}

	public BigDecimal getAmountSnapshot() {
		return amountSnapshot;
	}

	public void setAmountSnapshot(BigDecimal amountSnapshot) {
		this.amountSnapshot = amountSnapshot;
	}

	public LocalDate getChargeDateSnapshot() {
		return chargeDateSnapshot;
	}

	public void setChargeDateSnapshot(LocalDate chargeDateSnapshot) {
		this.chargeDateSnapshot = chargeDateSnapshot;
	}

}
