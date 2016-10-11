/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.response.MapResponse;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public interface IGeoIPCache {

    MapResponse<String, Object> getInfoByIP(String ipAddress);

    void saveInfo(Map<String, Object> info) throws Exception;
}
