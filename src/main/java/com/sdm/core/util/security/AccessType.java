/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.security;

/**
 *
 * @author Htoonlin
 */
public enum AccessType {

    READ(1), // 1 << 0
    CREATE(2), // 1 << 1
    UPDATE(4), // 1 << 2
    DELETE(8); // 1 << 3

    private final int value;

    private AccessType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
