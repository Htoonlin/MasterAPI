package com.sdm.sample.entity;

import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.ui.UIInputType;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.*;
import com.sdm.sample.resource.CustomerResource;
import java.io.Serializable;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;
import com.sdm.core.ui.UIStructure;
import java.util.*;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import com.sdm.core.response.model.LinkModel;
import java.math.*;
import com.sdm.core.util.MyanmarFontManager;

/**
 * Sample Customer Entity for MasterAPI
 * 
 * @Author htoonlin
 * @Since 2018-07-12 17:02:43
 */
@Audited
@DynamicUpdate(value = true)
@Entity(name = "com.sdm.sample.entity.CustomerEntity")
@Table(name = "tbl_sample_customer")
public class CustomerEntity extends DefaultEntity implements Serializable {

    private static final long serialVersionUID = 1531390985615L;

    /**
     * Customer name
     */
    @JsonIgnore
    @Size(min = 1, max = 255)
    @UIStructure(order = 1, label = "Name", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * City of Customer address
     */
    @Size(max = 255)
    @UIStructure(order = 7, label = "City", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "city", nullable = true, columnDefinition = "VARCHAR(255)")
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Customer Bank Account No.
     */
    @Size(max = 34)
    @UIStructure(order = 5, label = "BankAccount", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "bankAccount", nullable = true, columnDefinition = "VARCHAR(34)")
    private String bankAccount;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Customer Address Line
     */
    @Size(max = 255)
    @UIStructure(order = 6, label = "Address", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "address", nullable = true, columnDefinition = "VARCHAR(255)")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Customer phone no.
     */
    @Size(max = 100)
    @UIStructure(order = 3, label = "Phone", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "phone", nullable = true, columnDefinition = "VARCHAR(100)")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Customer company name
     */
    @JsonIgnore
    @Size(max = 255)
    @UIStructure(order = 4, label = "Company Name", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "company", nullable = true, columnDefinition = "VARCHAR(255)")
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Unique ID of Customer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "Id", inputType = UIInputType.number, hideInGrid = false, readOnly = false)
    @Column(name = "id", columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @UIStructure(order = 10, label = "RegistrationDate", inputType = UIInputType.datetime, hideInGrid = false, readOnly = false)
    @Column(name = "registrationDate", nullable = true, columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Customer Email
     */
    @Pattern(regexp = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$")
    @Size(max = 255)
    @UIStructure(order = 2, label = "E-mail", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "email", nullable = true, columnDefinition = "VARCHAR(255)")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Country of Customer Address
     */
    @Size(max = 100)
    @UIStructure(order = 8, label = "Country", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "country", nullable = true, columnDefinition = "VARCHAR(100)")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Any remark for record
     */
    @JsonIgnore
    @Size(max = 500)
    @UIStructure(order = 9, label = "Remark", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "remark", nullable = true, columnDefinition = "VARCHAR(500)")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    @NotAudited
    @Formula(value = "concat(bankAccount, country, address, phone, city, name, remark, company, email)")
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @JsonGetter("&detail_link")
    public LinkModel getSelfLink() {
        String selfLink = UriBuilder.fromResource(CustomerResource.class).build().toString();
        selfLink += "/" + this.id + "/";
        return new LinkModel(selfLink);
    }

    @JsonGetter("remark")
    public Object getMMRemark() {
        if (MyanmarFontManager.isMyanmar(this.remark)) {
            Map<String, String> output = new HashMap<>();
            output.put("zg", MyanmarFontManager.toZawgyi(this.remark));
            output.put("uni", this.remark);
            return output;
        } else {
            return this.remark;
        }
    }

    @JsonSetter("remark")
    public void setMMRemark(String remark) {
        if (MyanmarFontManager.isMyanmar(remark) && MyanmarFontManager.isZawgyi(remark)) {
            this.remark = MyanmarFontManager.toUnicode(remark);
        } else {
            this.remark = remark;
        }
    }

    @JsonGetter("company")
    public Object getMMCompany() {
        if (MyanmarFontManager.isMyanmar(this.company)) {
            Map<String, String> output = new HashMap<>();
            output.put("zg", MyanmarFontManager.toZawgyi(this.company));
            output.put("uni", this.company);
            return output;
        } else {
            return this.company;
        }
    }

    @JsonSetter("company")
    public void setMMCompany(String company) {
        if (MyanmarFontManager.isMyanmar(company) && MyanmarFontManager.isZawgyi(company)) {
            this.company = MyanmarFontManager.toUnicode(company);
        } else {
            this.company = company;
        }
    }

    @JsonGetter("name")
    public Object getMMName() {
        if (MyanmarFontManager.isMyanmar(this.name)) {
            Map<String, String> output = new HashMap<>();
            output.put("zg", MyanmarFontManager.toZawgyi(this.name));
            output.put("uni", this.name);
            return output;
        } else {
            return this.name;
        }
    }

    @JsonSetter("name")
    public void setMMName(String name) {
        if (MyanmarFontManager.isMyanmar(name) && MyanmarFontManager.isZawgyi(name)) {
            this.name = MyanmarFontManager.toUnicode(name);
        } else {
            this.name = name;
        }
    }
}
