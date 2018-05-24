/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import io.jsonwebtoken.Claims;
import java.lang.reflect.Method;
import org.jvnet.hk2.annotations.Contract;

/**
 *
 * @author Htoonlin
 */
@Contract
public interface IAccessManager {

    boolean validateToken(Claims request);

    boolean checkPermission(Claims request, Method method, String httpMethod, Class<?> resourceClass);
}
