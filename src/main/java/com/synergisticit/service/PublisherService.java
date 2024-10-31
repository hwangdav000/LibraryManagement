package com.synergisticit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergisticit.domain.Publisher;

public interface PublisherService {
	Publisher savePublisher(Publisher a);
	Publisher findPublisherById(Long aId);
	List<Publisher> findAllPublishers();
	Page<Publisher> findAllPublishers(Pageable p); 
	Publisher updatePublisher(Publisher a);
	void deletePublisher(Long aId);
	Long getMaxId();
}
