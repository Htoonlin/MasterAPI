package com.sdm.master.entity;
// Generated 04-Mar-2016 00:48:07 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sdm.core.hibernate.entity.UIStructure;
import com.sdm.core.hibernate.entity.DefaultEntity;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "tbl_user")
@Audited
public class UserEntity extends DefaultEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final char ACTIVE = 'A';
    public static final char PENDING = 'P';
    public static final char INACTIVE = 'D';
    public static final int TOKEN_LENGTH = 8;

    @JsonIgnore
    @Formula(value = "concat(email, displayname)")
    @NotAudited
    private String search;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "#", readOnly = true)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "INT UNSIGNED")
    private long id;

    @UIStructure(order = 1, label = "E-mail")
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @UIStructure(order = 2, label = "Name")
    @Column(name = "displayName", nullable = false, length = 255)
    private String displayName;

    @UIStructure(order = 3, label = "Roles", hideInGrid = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_role",
            joinColumns = {
                @JoinColumn(name = "userId", columnDefinition = "INT UNSIGNED")},
            inverseJoinColumns = {
                @JoinColumn(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED")})
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<RoleEntity> roles;

    @Column(name = "password", nullable = false)
    private String password;

    @UIStructure(order = 4, label = "Is online?")
    @Column(name = "isOnline", nullable = false)
    private boolean online;

    @UIStructure(order = 5, label = "Country")
    @Column(name = "countryCode", nullable = false, length = 2)
    private String countryCode;

    @UIStructure(order = 6, label = "Image")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profileImage", columnDefinition = "BIGINT UNSIGNED", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private FileEntity profileImage;

    @JsonIgnore
    @Column(name = "facebookToken", length = 255)
    private String facebookToken;

    @JsonIgnore
    @Column(name = "otpToken", length = TOKEN_LENGTH)
    private String otpToken;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "otpExpired", length = 19)
    private Date otpExpired;

    @UIStructure(order = 7, label = "Status")
    @Column(name = "status", nullable = false, length = 1)
    private char status;

    @Transient
    private String currentToken;

    public UserEntity() {
    }

    public UserEntity(String email, String displayName, String password, boolean online, String countryCode, char status) {
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.online = online;
        this.countryCode = countryCode;
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Email(message = "Ivalid email format.")
    @NotBlank(message = "Email can't be blank.")
    @Size(min = 6, max = 255)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = "Display name is required.")
    @Size(min = 1, max = 255)
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @NotNull(message = "Password is required.")
    @Size(min = 6, max = 255)
    public String getPassword() {
        return this.password;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @NotNull(message = "Country is required.")
    @Size(min = 2, max = 2, message = "Invalid country code.")
    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public FileEntity getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileEntity profileImage) {
        this.profileImage = profileImage;
    }

    public String getFacebookToken() {
        return this.facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getOtpToken() {
        return this.otpToken;
    }

    public void setOtpToken(String otpToken) {
        this.otpToken = otpToken;
    }

    public Date getOtpExpired() {
        return this.otpExpired;
    }

    public void setOtpExpired(Date otpExpired) {
        this.otpExpired = otpExpired;
    }

    public char getStatus() {
        return this.status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
