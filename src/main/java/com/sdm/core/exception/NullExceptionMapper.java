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
public class NullExceptionMapper extends DefaultExceptionMapper<NullPointerException> {

	@Override
	public Response toResponse(NullPointerException exception) {
		return buildResponse(204, exception);
	}

}
