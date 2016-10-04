/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Htoonlin
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;
    private List<QuickReply> quickReplies;
    private IAttachment attachment;

    public Message(String text) {
        this.text = text;
    }

    public Message(IAttachment attachment) {
        this.attachment = attachment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    public IAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(IAttachment attachment) {
        this.attachment = attachment;
    }

    public void addQuickReply(QuickReply quickReply) {
        if (quickReplies == null) {
            quickReplies = new ArrayList<>();
        }        
        quickReplies.add(quickReply);
    }
}
