package com.billingnotifier.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
		select u
		from User u
		where u.email = :email
		""")
	Optional<User> findByEmail(@Param("email") String email);

	@Query("""
		select u
		from User u
		where u.active = true
		  and u.billingCycleClosingDay = :closingDay
		""")
	List<User> findActiveUsersByClosingDay(@Param("closingDay") Integer closingDay);

	@Query("""
		select u
		from User u
		where u.id = :userId
		  and u.active = true
		""")
	Optional<User> findActiveById(@Param("userId") Long userId);

}
