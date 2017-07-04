/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Htoonlin
 * @param <K>
 * @param <V>
 */
public class MapResponse<K, V> extends HashMap<K, V> implements Serializable{

    public MapResponse() {
    }

    public MapResponse(Map<? extends K, ? extends V> m) {
        super(m);
    }
}
