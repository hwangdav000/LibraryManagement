package com.synergisticit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synergisticit.domain.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	@Query("SELECT COALESCE(MAX(p.publisherId), 0) FROM Publisher p")
	Long getMaxId();
}
