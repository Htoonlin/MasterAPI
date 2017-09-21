/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Htoonlin
 */
@Provider
public class ClassNotFoundExceptionMapper extends DefaultExceptionMapper<ClassNotFoundException> {

	@Override
	public Response toResponse(ClassNotFoundException exception) {
		return this.buildResponse(501, exception);
	}

}
