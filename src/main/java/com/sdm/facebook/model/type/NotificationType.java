package com.sdm.facebook.model.type;

/**
 * @author htoonlin
 *
 */
public enum NotificationType {
    REGULAR, // will emit a sound/vibration and a phone notification
    SILENT_PUSH, // will just emit a phone notification
    NO_PUSH; // will not emit either
}
