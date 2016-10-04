/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import com.sdm.core.request.AuthorizeRequest;
import java.lang.reflect.Method;

/**
 *
 * @author Htoonlin
 */
public interface IAccessManager {
    boolean validateToken(AuthorizeRequest request);

    boolean checkPermission(AuthorizeRequest request, Method method, String httpMethod);
}
