package com.synergisticit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergisticit.domain.Book;

public interface BookService {
	Book saveBook(Book a);
	Book findBookById(Long aId);
	List<Book> findAllBooks();
	Page<Book> findAllBooks(Pageable p);
	Book updateBook(Book a);
	void deleteBook(Long aId);
	Long getMaxId();
}
