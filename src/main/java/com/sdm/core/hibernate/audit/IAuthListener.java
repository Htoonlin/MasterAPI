/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.audit;

import com.sdm.core.hibernate.entity.AuthInfo;

/**
 *
 * @author htoonlin
 */
public interface IAuthListener {
    public AuthInfo getAuthInfo();
}
