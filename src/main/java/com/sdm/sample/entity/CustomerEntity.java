/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.LinkModel;
import com.sdm.core.ui.UIStructure;
import com.sdm.sample.resource.CustomerResource;

/**
 *
 * @author Htoonlin
 */
@Audited
@Entity(name = "Sample_CustomerEntity")
@Table(name = "tbl_sample_customer")
public class CustomerEntity extends DefaultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4890976494684611076L;

	@NotAudited
	@JsonIgnore
	@Formula(value = "concat(name, email, phone, company, address, city, country, remark)")
	private String search;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@UIStructure(order = 0, label = "#", readOnly = true)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
	private int id;

	@UIStructure(order = 1, label = "Customer Name")
	@Column(name = "name", nullable = true, columnDefinition = "VARCHAR(255)", length = 255)
	private String name;

	@UIStructure(order = 2, label = "E-mail")
	@Column(name = "email", nullable = true, columnDefinition = "VARCHAR(255)", length = 255)
	private String email;

	@UIStructure(order = 3, label = "Phone")
	@Column(name = "phone", nullable = true, columnDefinition = "VARCHAR(100)", length = 100)
	private String phone;

	@UIStructure(order = 4, label = "Company Name")
	@Column(name = "company", nullable = true, columnDefinition = "VARCHAR(255)", length = 255)
	private String company;

	@UIStructure(order = 5, label = "Bank Account")
	@Column(name = "bankAccount", nullable = true, columnDefinition = "VARCHAR(34)", length = 34)
	private String bankAccount;

	@UIStructure(order = 6, label = "Address")
	@Column(name = "address", nullable = true, columnDefinition = "VARCHAR(255)", length = 255)
	private String address;

	@UIStructure(order = 7, label = "City")
	@Column(name = "city", nullable = true, columnDefinition = "VARCHAR(255)", length = 255)
	private String city;

	@UIStructure(order = 8, label = "Country")
	@Column(name = "country", nullable = true, columnDefinition = "VARCHAR(100)", length = 255)
	private String country;

	@UIStructure(order = 9, label = "Remark")
	@Column(name = "remark", nullable = true, columnDefinition = "TEXT")
	private String remark;

	@UIStructure(order = 10, label = "Registration Date")
	@Column(name = "registrationDate", nullable = true, columnDefinition = "DATETIME")
	private Date registrationDate;

	public CustomerEntity() {
	}

	@JsonGetter("&detail_link")
	public LinkModel getSelfLink() {
		String selfLink = UriBuilder.fromResource(CustomerResource.class)
				.path(Integer.toString(this.id)).build().toString();
		return new LinkModel(selfLink);
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CustomerEntity other = (CustomerEntity) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

}
