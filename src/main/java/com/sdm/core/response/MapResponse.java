/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.util.HashMap;


/**
 *
 * @author Htoonlin
 */
public class MapResponse<K, V> extends HashMap<K, V> implements IResponseContent{
    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.INFO;
    }
    
}
