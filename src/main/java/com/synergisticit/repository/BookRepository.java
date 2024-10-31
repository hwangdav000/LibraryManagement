package com.synergisticit.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Category;

@Repository 
public interface BookRepository extends JpaRepository<Book, Long>{
	@Query("SELECT COALESCE(MAX(b.bookId), 0) FROM Book b")
	Long getMaxId();
	
	@Query(value = "SELECT b FROM Book b " +
	        "WHERE (:fTitle IS NULL OR b.title LIKE %:fTitle%) " +
	        "AND (:fCat IS NULL OR b.bookCategory = :fCat) " +
	        "AND (:fAuthor IS NULL OR EXISTS (" +
	        "SELECT a FROM b.bookAuthors a WHERE a LIKE %:fAuthor%))")
	Page<Book> findBooksWithPagination(
	    @Param("fTitle") String bookTitle,
	    @Param("fCat") Category bookCategory,
	    @Param("fAuthor") String authorName, 
	    Pageable pageable);

}
