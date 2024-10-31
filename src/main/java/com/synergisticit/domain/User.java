package com.synergisticit.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class User extends Auditable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	
	@NotEmpty 
	private String username;
	
	@NotEmpty
	private String userPassword;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_role",
	joinColumns= {@JoinColumn(name="user_id")},
	inverseJoinColumns= {@JoinColumn(name="role_id")}
	)
	private List<Role> userRoles = new ArrayList<>();
	
	@OneToOne(mappedBy = "user")
	private Member member;
	
	@OneToOne(mappedBy = "user")
	private Librarian librarian;
}
