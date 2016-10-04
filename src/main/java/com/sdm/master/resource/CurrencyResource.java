/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.RestResource;
import com.sdm.master.entity.CurrencyEntity;
import org.apache.log4j.Logger;
import javax.ws.rs.Path;

/**
 *
 * @author Htoonlin
 */
@Path("currency")
public class CurrencyResource extends RestResource<CurrencyEntity, Integer> {

    private static final Logger LOG = Logger.getLogger(CurrencyResource.class.getName());
}
