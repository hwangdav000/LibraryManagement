package com.synergisticit.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends Auditable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bookId;
	
	private String title;
	private String isbn;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate publicationDate;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Category bookCategory;
	
	@ElementCollection
	private List<String> bookAuthors = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;
	
	int quantity;
	
	@Enumerated(EnumType.STRING)
	private BookStatus status = BookStatus.AVAILABLE;
}
