package com.synergisticit.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Librarian extends Auditable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long librarianId;
	
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id")
	private User user;
	
	// for usage in json
	private String username;
    private String password;
}
