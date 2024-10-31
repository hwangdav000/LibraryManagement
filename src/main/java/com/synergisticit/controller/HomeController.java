package com.synergisticit.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Category;

import com.synergisticit.repository.BookRepository;

@Controller
public class HomeController {
	
	@Autowired BookRepository bRepo;

	@RequestMapping({"/","home"})
	public String home(@RequestParam(name="bookTitle", required=false) String bookTitle,
	        @RequestParam(name="bookCategory", required=false) String bookCategory,
	        @RequestParam(name="author", required=false) String author,
	        @RequestParam(name="sortBy", required=false, defaultValue="bookId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	    	@RequestParam(name="size", required=false, defaultValue="4") int size,
	        ModelMap mm) {
		
		if (bookTitle == null || bookTitle.equals("")) bookTitle = null;		
		if (bookCategory == null || bookCategory.equals("")) bookCategory = null;		
		if (author == null || author.equals("")) author = null;
		
		Category bCat = (bookCategory != null) ? Category.valueOf(bookCategory) : null;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Book>  homePage = bRepo.findBooksWithPagination(bookTitle, bCat, author, pageable);
		
		mm.addAttribute("homePage", homePage);
		mm.addAttribute("fTitle", bookTitle);
		mm.addAttribute("fCat", bookCategory);
		mm.addAttribute("fAuthor", author);
		mm.addAttribute("sortBy", sortBy);
		
		getModel(mm);
		
		return "home";
	}
	
	public ModelMap getModel(ModelMap mm) {
		mm.addAttribute("categories", Arrays.asList(Category.values()));
		return mm;
	}
}
