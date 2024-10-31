package com.synergisticit.controller;

import java.security.Principal;

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

import com.synergisticit.domain.Publisher;
import com.synergisticit.service.PublisherService;
import com.synergisticit.validation.PublisherValidator;

import jakarta.validation.Valid;

@Controller
public class PublisherController {
	
	@Autowired PublisherService pService;
	
	@Autowired PublisherValidator pValidator;
	
	@RequestMapping("publisherForm")
	public String publisherForm(Publisher publisher, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="publisherId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		publisher.setPublisherId(pService.getMaxId()+1);
		getModel(mm, sortBy, page, size);
		
		return "publisherForm";
	}
	
	@RequestMapping("savePublisher")
	public String savePublisher(@Valid @ModelAttribute Publisher publisher, BindingResult br, ModelMap mm) {
		pValidator.validate(publisher, br);
		
		if (br.hasErrors()) {
			publisher.setPublisherId(pService.getMaxId()+1);
			getModel(mm, "publisherId", 0, 4);
			return "publisherForm";
		}
		
	    pService.savePublisher(publisher);
	    
	    return "redirect:publisherForm";
	}
	
	@RequestMapping("updatePublisher")
	public String updatePublisher(Publisher publisher, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="publisherId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		
		
		Publisher uPublisher = pService.findPublisherById(publisher.getPublisherId());
		
		mm.addAttribute("p", uPublisher);
		getModel(mm, sortBy, page, size);
		
		return "publisherForm";
	}
	
	@RequestMapping("deletePublisher")
	public String deletePublisher(Publisher publisher, ModelMap mm) {
		pService.deletePublisher(publisher.getPublisherId());
		return "redirect:publisherForm";
	}
	
	public ModelMap getModel(ModelMap mm, String sortBy, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Publisher> publisherPage = pService.findAllPublishers(pageable);
		
		mm.addAttribute("publisherPage", publisherPage);
		mm.addAttribute("sortBy", sortBy);
		
		return mm;

	} 
}
