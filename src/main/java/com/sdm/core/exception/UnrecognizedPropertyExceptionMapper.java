/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 *
 * @author Htoonlin
 */
@Provider
public class UnrecognizedPropertyExceptionMapper extends DefaultExceptionMapper<UnrecognizedPropertyException> {

	@Override
	public Response toResponse(UnrecognizedPropertyException exception) {
		return buildResponse(422, exception);
	}

}
