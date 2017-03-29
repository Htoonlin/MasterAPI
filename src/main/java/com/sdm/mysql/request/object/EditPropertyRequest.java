/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.object;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class EditPropertyRequest extends AddPropertyRequest implements Serializable {

    private String oldName;

    public EditPropertyRequest() {
    }

    @NotNull
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

}
