package com.synergisticit.domain;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends Auditable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long memberId;
	
	private String firstName;
	private String lastName;
	private String addressLine1;
	private String addressLine2;
	private String phone;
	private double fineBalance = 0.0;
	
	@Email
	private String email;
	
	private Date membershipDate;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id")
	private User user;
	
	// for usage in json
	private String username;
    private String password;
}
