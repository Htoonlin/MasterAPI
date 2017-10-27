/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import java.util.Map;
import org.jvnet.hk2.annotations.Contract;

/**
 *
 * @author Htoonlin
 */
@Contract
public interface ITemplateManager {

    String buildTemplate(String template, Map<String, Object> data);
}
