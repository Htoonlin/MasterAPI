/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.object;

import com.sdm.core.request.DefaultRequest;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Htoonlin
 */
public class RenameRequest extends DefaultRequest implements Serializable {

    private String oldName;
    private String newName;

    public RenameRequest(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    @NotNull
    @Size(min = 6, max = 255)
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @NotNull
    @Size(min = 6, max = 255)
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
