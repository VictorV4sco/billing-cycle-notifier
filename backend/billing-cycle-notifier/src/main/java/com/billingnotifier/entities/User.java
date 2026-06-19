package com.billingnotifier.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(nullable = false, length = 150)
	private String fullName;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false)
	private Integer billingCycleOpeningDay;

	@Column(nullable = false)
	private Integer billingCycleClosingDay;

	@Column(nullable = false)
	private LocalTime messageSendTime;

	@Column(nullable = false, length = 60)
	private String timezone;

	@Column(nullable = false)
	private boolean active = true;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Person> persons = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingStatement> billingStatements = new ArrayList<>();

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getBillingCycleOpeningDay() {
		return billingCycleOpeningDay;
	}

	public void setBillingCycleOpeningDay(Integer billingCycleOpeningDay) {
		this.billingCycleOpeningDay = billingCycleOpeningDay;
	}

	public Integer getBillingCycleClosingDay() {
		return billingCycleClosingDay;
	}

	public void setBillingCycleClosingDay(Integer billingCycleClosingDay) {
		this.billingCycleClosingDay = billingCycleClosingDay;
	}

	public LocalTime getMessageSendTime() {
		return messageSendTime;
	}

	public void setMessageSendTime(LocalTime messageSendTime) {
		this.messageSendTime = messageSendTime;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<BillingStatement> getBillingStatements() {
		return billingStatements;
	}

	public void setBillingStatements(List<BillingStatement> billingStatements) {
		this.billingStatements = billingStatements;
	}

}
