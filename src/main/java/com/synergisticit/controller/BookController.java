package com.synergisticit.controller;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Category;
import com.synergisticit.domain.Publisher;
import com.synergisticit.service.BookService;
import com.synergisticit.service.PublisherService;
import com.synergisticit.validation.BookValidator;

import jakarta.validation.Valid;

@Controller
public class BookController {
	
	@Autowired PublisherService pService;
	@Autowired BookService bService;
	
	@Autowired BookValidator bValidator;
	
	@RequestMapping("bookForm")
	public String bookForm(Book book, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="bookId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		
		book.setBookId(bService.getMaxId()+1);
		getModel(mm, sortBy, page, size);
		
		return "bookForm";
	}
	
	@RequestMapping("saveBook")
	public String saveBook(@Valid @ModelAttribute Book book, BindingResult br, ModelMap mm) {
		bValidator.validate(book , br);
		
		if (br.hasErrors()) {
			book.setBookId(bService.getMaxId()+1);
			getModel(mm, "bookId", 0, 4);
			return "bookForm";
		}
		
	    bService.saveBook(book);
	    
	    return "redirect:bookForm";
	}
	
	@RequestMapping("updateBook")
	public String updateBook(Book book, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="bookId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		Book b = bService.findBookById(book.getBookId());
		
		mm.addAttribute("b", b);
		getModel(mm, sortBy, page, size);
		
		return "bookForm";
	}
	
	@RequestMapping("deleteBook")
	public String deleteBook(Book book, ModelMap mm) {
		bService.deleteBook(book.getBookId());
		return "bookForm";
	}
	
	public ModelMap getModel(ModelMap mm, String sortBy, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Book> bookPage = bService.findAllBooks(pageable);
		
		mm.addAttribute("bookPage", bookPage);
		mm.addAttribute("sortBy", sortBy);
		
		//
		mm.addAttribute("categories", Arrays.asList(Category.values()));
		mm.addAttribute("publishers", pService.findAllPublishers());
		mm.addAttribute("books", bService.findAllBooks());
		return mm;
	}
}
