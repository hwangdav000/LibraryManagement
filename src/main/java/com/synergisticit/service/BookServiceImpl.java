package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Book;
import com.synergisticit.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService{

	@Autowired BookRepository bRepo;

	@Override
	public Book saveBook(Book b) {
		return bRepo.save(b);
	}

	@Override
	public Book findBookById(Long bId) {
		Optional<Book> book = bRepo.findById(bId);
		if (book.isPresent()) return book.get();
		return null;
	}

	@Override
	public List<Book> findAllBooks() {
		return bRepo.findAll();
	}

	@Override
	public Book updateBook(Book b) {
		Optional<Book> book= bRepo.findById(b.getBookId());
		if (book.isEmpty()) {
			// book does not exist
			return null;
		}
		
		return bRepo.save(b);
	}

	@Override
	public void deleteBook(Long bId) {
		bRepo.deleteById(bId);
	}

	@Override
	public Long getMaxId() {

		return bRepo.getMaxId();
	}

	@Override
	public Page<Book> findAllBooks(Pageable p) {
		
		return bRepo.findAll(p);
	}

}
