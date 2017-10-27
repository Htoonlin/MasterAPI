/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.hibernate.HibernateException;

/**
 *
 * @author Htoonlin
 */
@Provider
public class HibernateExceptionMapper extends DefaultExceptionMapper<HibernateException> {

    @Override
    public Response toResponse(HibernateException exception) {
        return buildResponse(500, exception);
    }

}
