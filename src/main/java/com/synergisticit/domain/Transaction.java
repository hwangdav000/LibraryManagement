package com.synergisticit.domain;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends Auditable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long transactionId;
	
	private Long bookId;
	private Long userId;
	
	private Double amount;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate transactionDate;
	
	private TransactionType transactionType;
}
