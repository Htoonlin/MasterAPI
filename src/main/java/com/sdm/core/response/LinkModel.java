package com.sdm.core.response;

import java.io.Serializable;

/**
 * HEATEOAS Json Field
 *
 * @author htoonlin
 *
 */
public class LinkModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1489532404351163294L;
    private String href;
    private String type;
    private String title;

    public LinkModel() {

    }

    public LinkModel(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
