package com.synergisticit.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publisher extends Auditable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long publisherId;
	
	private String publisherName;
	private String addressLine1;
	private String addressLine2;
	
	private String email;
	
	@OneToMany(mappedBy="publisher")
	private Set<Book> books;
}
