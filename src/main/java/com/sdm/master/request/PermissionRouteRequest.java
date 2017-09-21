/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.master.entity.PermissionEntity;
import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author YOETHA
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PermissionRouteRequest implements Serializable{
    private static final long serialVersionUID = 4541648496908228267L;
    private Boolean checked;
    private PermissionEntity permission;

    @NotBlank(message = "Checked Can't be blank.")
    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public PermissionEntity getPermission() {
        return permission;
    }

    public void setPermission(PermissionEntity permission) {
        this.permission = permission;
    }
}
