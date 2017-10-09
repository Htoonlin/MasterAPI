package com.sdm.master.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.LinkModel;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;
import com.sdm.core.util.MyanmarFontManager;
import com.sdm.master.resource.UserResource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;

/**
 * User generated by hbm2java
 */
@Audited
@DynamicUpdate(value = true)
@Entity(name = "UserEntity")
@Table(name = "tbl_user")
public class UserEntity extends DefaultEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1939600458371706458L;
    public static final char ACTIVE = 'A';
    public static final char PENDING = 'P';
    public static final char INACTIVE = 'D';
    public static final int TOKEN_LENGTH = 8;

    @JsonIgnore
    @NotAudited
    @Formula(value = "concat(email, displayname)")
    private String search;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "#", readOnly = true)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    @UIStructure(order = 1, label = "E-mail", inputType = UIInputType.email)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @JsonIgnore
    @UIStructure(order = 2, label = "Name", inputType = UIInputType.text)
    @Column(name = "displayName", nullable = false, length = 255)
    private String displayName;

    @UIStructure(order = 7, label = "Roles", hideInGrid = true, inputType = UIInputType.objectlist)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_role", joinColumns = {
        @JoinColumn(name = "userId", columnDefinition = "MEDIUMINT UNSIGNED")}, inverseJoinColumns = {
        @JoinColumn(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED")})
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<RoleEntity> roles = new HashSet<>();

    @UIStructure(order = 3, label = "Password", inputType = UIInputType.password)
    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false, length = 255)
    private String password;

    @NotAudited
    @UIStructure(order = 4, label = "Is online?", inputType = UIInputType.checkbox)
    @Column(name = "isOnline", nullable = false)
    private boolean online;

    @UIStructure(order = 6, label = "Image", inputType = UIInputType.image)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profileImage", columnDefinition = "BIGINT UNSIGNED", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private FileEntity profileImage;

    @Column(name = "facebookId", columnDefinition = "VARCHAR(255)", unique = true, nullable = true, length = 255)
    private String facebookId;

    @JsonIgnore
    @Column(name = "otpToken", length = TOKEN_LENGTH)
    private String otpToken;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "otpExpired", length = 19)
    private Date otpExpired;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "name")
    @CollectionTable(name = "tbl_user_extra", joinColumns = @JoinColumn(name = "userId"))
    @Column(name = "value", nullable = false, columnDefinition = "VARCHAR(500)", length = 500)
    @MapKeyColumn(name = "name", nullable = false, columnDefinition = "VARCHAR(255)", length = 255)
    @UIStructure(order = 8, label = "Extras", inputType = UIInputType.map)
    private Map<String, String> extra = new HashMap<>();

    @UIStructure(order = 7, label = "Status", inputType = UIInputType.radio)
    @Column(name = "status", nullable = false, length = 1)
    private char status;

    @Transient
    @NotAudited
    private String currentToken;

    public UserEntity() {
    }

    public UserEntity(String email, String displayName, String password, boolean online, char status) {
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.online = online;
        this.status = status;
    }

    @JsonGetter("&detail_link")
    public LinkModel getSelfLink() {
        String selfLink = UriBuilder.fromResource(UserResource.class).path(Integer.toString(this.id)).build()
                .toString();
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

    @Email(message = "Ivalid email format.")
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

    @JsonGetter("display_name")
    public Object getMMDisplayName() {
        if (MyanmarFontManager.isMyanmar(this.displayName)) {
            Map<String, String> output = new HashMap<>();
            output.put("zg", MyanmarFontManager.toZawgyi(this.displayName));
            output.put("uni", this.displayName);
            return output;
        } else {
            return this.displayName;
        }
    }

    @JsonSetter("display_name")
    public void setMMDisplayName(String displayName) {
        if (MyanmarFontManager.isMyanmar(displayName)
                && MyanmarFontManager.isZawgyi(displayName)) {
            this.displayName = MyanmarFontManager.toUnicode(displayName);
        } else {
            this.displayName = displayName;
        }
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @JsonIgnore
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

    public FileEntity getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileEntity profileImage) {
        this.profileImage = profileImage;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookID) {
        this.facebookId = facebookID;
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

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public void addExtra(String key, String value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, value);
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
