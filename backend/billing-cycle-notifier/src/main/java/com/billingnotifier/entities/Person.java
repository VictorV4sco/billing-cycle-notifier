package com.billingnotifier.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "persons")
public class Person extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, length = 150)
	private String fullName;

	@Column(nullable = false, length = 20)
	private String whatsappNumber;

	@Column(length = 500)
	private String notes;

	@Column(nullable = false)
	private boolean active = true;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Charge> charges = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingStatement> billingStatements = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getWhatsappNumber() {
		return whatsappNumber;
	}

	public void setWhatsappNumber(String whatsappNumber) {
		this.whatsappNumber = whatsappNumber;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public List<BillingStatement> getBillingStatements() {
		return billingStatements;
	}

	public void setBillingStatements(List<BillingStatement> billingStatements) {
		this.billingStatements = billingStatements;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
