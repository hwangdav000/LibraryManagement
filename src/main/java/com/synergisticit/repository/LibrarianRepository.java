package com.synergisticit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synergisticit.domain.Librarian;


@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long>{
	@Query("SELECT COALESCE(MAX(l.librarianId), 0) FROM Librarian l")
	Long getMaxId();

}
