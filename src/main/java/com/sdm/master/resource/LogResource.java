/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.DefaultResource;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

/**
 *
 * @author htoonlin
 */
@Path("log")
public class LogResource extends DefaultResource{
    private static final Logger logger = Logger.getLogger(LogResource.class.getName());
}
