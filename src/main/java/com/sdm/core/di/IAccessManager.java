/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import java.lang.reflect.Method;

import io.jsonwebtoken.Claims;

/**
 *
 * @author Htoonlin
 */
public interface IAccessManager {

    boolean validateToken(Claims request);

    boolean checkPermission(Claims request, Method method, String httpMethod);
}
