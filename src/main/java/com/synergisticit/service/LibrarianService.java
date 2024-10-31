package com.synergisticit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergisticit.domain.Librarian;

public interface LibrarianService {
	Librarian saveLibrarian(Librarian a);
	Librarian findLibrarianById(Long aId);
	List<Librarian> findAllLibrarians();
	Page<Librarian> findAllLibrarians(Pageable p);
	Librarian updateLibrarian(Librarian a);
	void deleteLibrarian(Long aId);
	Long getMaxId();
}
