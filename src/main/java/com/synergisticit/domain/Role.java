package com.synergisticit.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Role extends Auditable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long roleId;
	@NotEmpty
	private String roleName;
	
	@ManyToMany(mappedBy="userRoles")
	private List<User> users = new ArrayList<>();
}
