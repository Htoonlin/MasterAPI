/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

/**
 *
 * @author Htoonlin
 */
public class FileManager {

    public static final String[] SIZE_CODES = new String[]{"", "K", "M", "G", "T", "P", "E", "Z", "Y"};

    public static String byteSize(long size) {
        if (size <= 1024) {
            return "1 KB";
        }
        float resultSize = size;
        String result = resultSize + " T";
        for (String code : SIZE_CODES) {
            if (resultSize < 1024) {
                result = (Math.round(resultSize * 100.0) / 100.0) + " " + code;
                break;
            }
            resultSize /= 1024;
        }
        return result + "B";
    }

    public static String[] fileNameSplitter(String fileName) {
        String[] fileInfo = fileName.split("\\.(?=[^\\.]+$)");
        if (fileInfo.length < 2) {
            return new String[]{fileName};
        }
        return fileInfo;
    }
}
