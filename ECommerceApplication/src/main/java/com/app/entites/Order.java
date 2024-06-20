package com.app.entites;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Email
	@Column(nullable = false)
	private String email;

	@JoinColumn(name = "customer_id")
	@ManyToOne
	private Customer customer;

	@JoinColumn(name = "employee_id")
	@ManyToOne
	private Employee employee;

	@OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("orderId ASC")
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	private Double totalAmount;

	@Column(nullable = false)
	private String orderStatus; // "PENDING", "COMPLETED", "CANCELLED"

	@ManyToOne
	@JoinColumn(name = "coupon_id", nullable = true)
	private Coupon coupon;
}