package com.sdm.facebook.model.type;

/**
 * Set typing indicators or send read receipts using the Send API, to let users know you are processing their request. Ref : https://developers.facebook.com/docs/messenger-platform/send-api-reference/sender-actions
 *
 * @author htoonlin
 *
 */
public enum SenderAction {
    typing_on, // Turn typing indicators on
    typing_off, // Turn typing indicators off
    mark_seen; // Mark last message as read
}
