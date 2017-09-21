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
public class IllegalStateExceptionMapper extends DefaultExceptionMapper<IllegalStateException> {

	@Override
	public Response toResponse(IllegalStateException exception) {
		return buildResponse(500, exception);
	}

}
