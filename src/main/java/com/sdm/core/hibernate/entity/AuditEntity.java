package com.sdm.core.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.sdm.core.hibernate.audit.AuditListener;

@Entity
@Table(name = "tbl_audit_info")
@RevisionEntity(AuditListener.class)
public class AuditEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4287015242983266529L;

	@Id
	@GeneratedValue
	@RevisionNumber
	private long id;

	@RevisionTimestamp
	private long timestamp;

	private int userId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditEntity other = (AuditEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
