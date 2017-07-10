/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook;

/**
 *
 * @author Htoonlin
 */
public interface IFacebookListener {
	void success(int status, String responseBody);

	void failed(int status, String responseError);
}
