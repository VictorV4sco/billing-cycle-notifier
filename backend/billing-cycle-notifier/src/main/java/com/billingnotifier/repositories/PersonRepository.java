package com.billingnotifier.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billingnotifier.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	@Query("""
		select p
		from Person p
		where p.user.id = :userId
		  and p.active = true
		order by p.fullName
		""")
	List<Person> findActiveByUserId(@Param("userId") Long userId);

	@Query("""
		select p
		from Person p
		where p.id = :personId
		  and p.user.id = :userId
		""")
	Optional<Person> findByIdAndUserId(@Param("personId") Long personId, @Param("userId") Long userId);

	@Query("""
		select p
		from Person p
		where p.user.id = :userId
		  and p.whatsappNumber = :whatsappNumber
		  and p.active = true
		""")
	Optional<Person> findActiveByWhatsappNumberAndUserId(
		@Param("whatsappNumber") String whatsappNumber,
		@Param("userId") Long userId
	);

}
