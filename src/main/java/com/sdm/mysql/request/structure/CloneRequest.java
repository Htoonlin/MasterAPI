/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.structure;

import com.sdm.core.request.DefaultRequest;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class CloneRequest extends DefaultRequest implements Serializable {

    private String destName;
    private boolean temporary;
    private boolean dataInclude;

    public CloneRequest() {
    }

    @NotNull
    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public boolean isDataInclude() {
        return dataInclude;
    }

    public void setDataInclude(boolean dataInclude) {
        this.dataInclude = dataInclude;
    }
}
