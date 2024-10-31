package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Librarian;
import com.synergisticit.repository.LibrarianRepository;

@Service
public class LibrarianServiceImpl implements LibrarianService {
	@Autowired LibrarianRepository lRepo;

	@Override
	public Librarian saveLibrarian(Librarian b) {
		return lRepo.save(b);
	}

	@Override
	public Librarian findLibrarianById(Long bId) {
		Optional<Librarian> librarian = lRepo.findById(bId);
		if (librarian.isPresent()) return librarian.get();
		return null;
	}

	@Override
	public List<Librarian> findAllLibrarians() {
		return lRepo.findAll();
	}

	@Override
	public Librarian updateLibrarian(Librarian b) {
		Optional<Librarian> librarian= lRepo.findById(b.getLibrarianId());
		if (librarian.isEmpty()) {
			// librarian does not exist
			return null;
		}
		
		return lRepo.save(b);
	}

	@Override
	public void deleteLibrarian(Long bId) {
		lRepo.deleteById(bId);
	}

	@Override
	public Long getMaxId() {
		
		return lRepo.getMaxId();
	}

	@Override
	public Page<Librarian> findAllLibrarians(Pageable p) {
		
		return lRepo.findAll(p);
	}

}
