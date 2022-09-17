 	package com.torres.springboot.backend.washer.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="rents")
public class Rent implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="status_rent", nullable=false)
	private Boolean statusRent;
	
	@NotNull
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Client client;
	
	@NotNull
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="washer_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Washer washer;
	
	@NotNull
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getStatusRent() {
		return statusRent;
	}

	public void setStatusRent(Boolean statusRent) {
		this.statusRent = statusRent;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Washer getWasher() {
		return washer;
	}

	public void setWasher(Washer washer) {
		this.washer = washer;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	private static final long serialVersionUID = 1L;
}
