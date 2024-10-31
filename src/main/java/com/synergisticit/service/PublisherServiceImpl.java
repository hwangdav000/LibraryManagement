package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Publisher;
import com.synergisticit.repository.PublisherRepository;

@Service
public class PublisherServiceImpl implements PublisherService{
	
	@Autowired PublisherRepository pRepo;

	@Override
	public Publisher savePublisher(Publisher b) {
		return pRepo.save(b);
	}

	@Override
	public Publisher findPublisherById(Long bId) {
		Optional<Publisher> publisher = pRepo.findById(bId);
		if (publisher.isPresent()) return publisher.get();
		return null;
	}

	@Override
	public List<Publisher> findAllPublishers() {
		return pRepo.findAll();
	}

	@Override
	public Publisher updatePublisher(Publisher b) {
		Optional<Publisher> publisher= pRepo.findById(b.getPublisherId());
		if (publisher.isEmpty()) {
			// publisher does not exist
			return null;
		}
		
		return pRepo.save(b);
	}

	@Override
	public void deletePublisher(Long bId) {
		pRepo.deleteById(bId);
	}

	@Override
	public Long getMaxId() {
		
		return pRepo.getMaxId();
	}

	@Override
	public Page<Publisher> findAllPublishers(Pageable p) {

		return pRepo.findAll(p);
	}

}
