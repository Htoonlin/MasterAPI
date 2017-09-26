package com.sdm.core.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sdm.core.hibernate.audit.AuditListener;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity(name = "AuditEntity")
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
    private long version;

    @RevisionTimestamp
    private long timestamp;

    private int userId;

    @ElementCollection
    @JoinTable(name = "tbl_audit_changes", joinColumns = @JoinColumn(name = "version"))
    @Column(name = "entity")
    @ModifiedEntityNames
    private Set<String> modifiedEntityNames;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    @JsonGetter("modified_count")
    public int getModifiedCount() {
        if (modifiedEntityNames == null) {
            return 0;
        }

        return modifiedEntityNames.size();
    }

    public Set<String> getModifiedEntityNames() {
        return modifiedEntityNames;
    }

    public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
        this.modifiedEntityNames = modifiedEntityNames;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (version ^ (version >>> 32));
        return result;
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
        AuditEntity other = (AuditEntity) obj;
        if (version != other.version) {
            return false;
        }
        return true;
    }

}
