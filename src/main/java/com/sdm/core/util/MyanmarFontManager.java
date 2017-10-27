package com.sdm.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Converter => saturngod on 23/1/15. Zawgyi Detector => Technomation Studio
 */
public class MyanmarFontManager {

    private static final String UNI_TO_ZG = "[ { \"from\": \"\u1004\u103a\u1039\", \"to\": \"\u1064\" }, { \"from\": \"\u1039\u1010\u103d\", \"to\": \"\u1096\" }, { \"from\": \"\u1014(?=[\u1030\u103d\u103e\u102f\u1039])\", \"to\": \"\u108f\" }, { \"from\": \"\u102b\u103a\", \"to\": \"\u105a\" }, { \"from\": \"\u100b\u1039\u100c\", \"to\": \"\u1092\" }, { \"from\": \"\u102d\u1036\", \"to\": \"\u108e\" }, { \"from\": \"\u104e\u1004\u103a\u1038\", \"to\": \"\u104e\" }, { \"from\": \"[\u1025\u1009](?=[\u1039\u102f\u1030])\", \"to\": \"\u106a\" }, { \"from\": \"[\u1025\u1009](?=[\u103a])\", \"to\": \"\u1025\" }, { \"from\": \"\u100a(?=[\u1039\u102f\u1030\u103d])\", \"to\": \"\u106b\" }, { \"from\": \"(\u1039[\u1000-\u1021])\u102f\", \"to\": \"$1\u1033\" }, { \"from\": \"(\u1039[\u1000-\u1021])\u1030\", \"to\": \"$1\u1034\" }, { \"from\": \"\u1039\u1000\", \"to\": \"\u1060\" }, { \"from\": \"\u1039\u1001\", \"to\": \"\u1061\" }, { \"from\": \"\u1039\u1002\", \"to\": \"\u1062\" }, { \"from\": \"\u1039\u1003\", \"to\": \"\u1063\" }, { \"from\": \"\u1039\u1005\", \"to\": \"\u1065\" }, { \"from\": \"\u1039\u1006\", \"to\": \"\u1066\" }, { \"from\": \"\u1039\u1007\", \"to\": \"\u1068\" }, { \"from\": \"\u1039\u1008\", \"to\": \"\u1069\" }, { \"from\": \"\u100a(?=[\u1039\u102f\u1030])\", \"to\": \"\u106b\" }, { \"from\": \"\u1039\u100b\", \"to\": \"\u106c\" }, { \"from\": \"\u1039\u100c\", \"to\": \"\u106d\" }, { \"from\": \"\u100d\u1039\u100d\", \"to\": \"\u106e\" }, { \"from\": \"\u100e\u1039\u100d\", \"to\": \"\u106f\" }, { \"from\": \"\u1039\u100f\", \"to\": \"\u1070\" }, { \"from\": \"\u1039\u1010\", \"to\": \"\u1071\" }, { \"from\": \"\u1039\u1011\", \"to\": \"\u1073\" }, { \"from\": \"\u1039\u1012\", \"to\": \"\u1075\" }, { \"from\": \"\u1039\u1013\", \"to\": \"\u1076\" }, { \"from\": \"\u1039\u1013\", \"to\": \"\u1076\" }, { \"from\": \"\u1039\u1014\", \"to\": \"\u1077\" }, { \"from\": \"\u1039\u1015\", \"to\": \"\u1078\" }, { \"from\": \"\u1039\u1016\", \"to\": \"\u1079\" }, { \"from\": \"\u1039\u1017\", \"to\": \"\u107a\" }, { \"from\": \"\u1039\u1018\", \"to\": \"\u107b\" }, { \"from\": \"\u1039\u1019\", \"to\": \"\u107c\" }, { \"from\": \"\u1039\u101c\", \"to\": \"\u1085\" }, { \"from\": \"\u103f\", \"to\": \"\u1086\" }, { \"from\": \"(\u103c)\u103e\", \"to\": \"$1\u1087\" }, { \"from\": \"\u103d\u103e\", \"to\": \"\u108a\" }, { \"from\": \"(\u1064)([\u1031]?)([\u103c]?)([\u1000-\u1021])\u102d\", \"to\": \"$2$3$4\u108b\" }, { \"from\": \"(\u1064)([\u1031]?)([\u103c]?)([\u1000-\u1021])\u102e\", \"to\": \"$2$3$4\u108c\" }, { \"from\": \"(\u1064)([\u1031]?)([\u103c]?)([\u1000-\u1021])\u1036\", \"to\": \"$2$3$4\u108d\" }, { \"from\": \"(\u1064)([\u1031]?)([\u103c]?)([\u1000-\u1021])\", \"to\": \"$2$3$4$1\" }, { \"from\": \"\u101b(?=[\u102f\u1030\u103d\u108a])\", \"to\": \"\u1090\" }, { \"from\": \"\u100f\u1039\u100d\", \"to\": \"\u1091\" }, { \"from\": \"\u100b\u1039\u100b\", \"to\": \"\u1097\" }, { \"from\": \"([\u1000-\u1021\u108f\u1029\u1090])([\u1060-\u1069\u106c\u106d\u1070-\u107c\u1085\u108a])?([\u103b-\u103e]*)?\u1031\", \"to\": \"\u1031$1$2$3\" }, { \"from\": \"([\u1000-\u1021\u1029])([\u1060-\u1069\u106c\u106d\u1070-\u107c\u1085])?(\u103c)\", \"to\": \"$3$1$2\" }, { \"from\": \"\u103a\", \"to\": \"\u1039\" }, { \"from\": \"\u103b\", \"to\": \"\u103a\" }, { \"from\": \"\u103c\", \"to\": \"\u103b\" }, { \"from\": \"\u103d\", \"to\": \"\u103c\" }, { \"from\": \"\u103e\", \"to\": \"\u103d\" }, { \"from\": \"\u103d\u102f\", \"to\": \"\u1088\" }, { \"from\": \"([\u102f\u1030\u1014\u101b\u103c\u108a\u103d\u1088])([\u1032\u1036]{0,1})\u1037\", \"to\": \"$1$2\u1095\" }, { \"from\": \"\u102f\u1095\", \"to\": \"\u102f\u1094\" }, { \"from\": \"([\u1014\u101b])([\u1032\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\u1037\", \"to\": \"$1$2\u1095\" }, { \"from\": \"\u1095\u1039\", \"to\": \"\u1094\u1039\" }, { \"from\": \"([\u103a\u103b])([\u1000-\u1021])([\u1036\u102d\u102e\u108b\u108c\u108d\u108e]?)\u102f\", \"to\": \"$1$2$3\u1033\" }, { \"from\": \"([\u103a\u103b])([\u1000-\u1021])([\u1036\u102d\u102e\u108b\u108c\u108d\u108e]?)\u1030\", \"to\": \"$1$2$3\u1034\" }, { \"from\": \"\u103a\u102f\", \"to\": \"\u103a\u1033\" }, { \"from\": \"\u103a([\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\u102f\", \"to\": \"\u103a$1\u1033\" }, { \"from\": \"([\u103a\u103b])([\u1000-\u1021])\u1030\", \"to\": \"$1$2\u1034\" }, { \"from\": \"\u103a\u1030\", \"to\": \"\u103a\u1034\" }, { \"from\": \"\u103a([\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\u1030\", \"to\": \"\u103a$1\u1034\" }, { \"from\": \"\u103d\u1030\", \"to\": \"\u1089\" }, { \"from\": \"\u103b([\u1000\u1003\u1006\u100f\u1010\u1011\u1018\u101a\u101c\u101a\u101e\u101f])\", \"to\": \"\u107e$1\" }, { \"from\": \"\u107e([\u1000\u1003\u1006\u100f\u1010\u1011\u1018\u101a\u101c\u101a\u101e\u101f])([\u103c\u108a])([\u1032\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\", \"to\": \"\u1084$1$2$3\" }, { \"from\": \"\u107e([\u1000\u1003\u1006\u100f\u1010\u1011\u1018\u101a\u101c\u101a\u101e\u101f])([\u103c\u108a])\", \"to\": \"\u1082$1$2\" }, { \"from\": \"\u107e([\u1000\u1003\u1006\u100f\u1010\u1011\u1018\u101a\u101c\u101a\u101e\u101f])([\u1033\u1034]?)([\u1032\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\", \"to\": \"\u1080$1$2$3\" }, { \"from\": \"\u103b([\u1000-\u1021])([\u103c\u108a])([\u1032\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\", \"to\": \"\u1083$1$2$3\" }, { \"from\": \"\u103b([\u1000-\u1021])([\u103c\u108a])\", \"to\": \"\u1081$1$2\" }, { \"from\": \"\u103b([\u1000-\u1021])([\u1033\u1034]?)([\u1032\u1036\u102d\u102e\u108b\u108c\u108d\u108e])\", \"to\": \"\u107f$1$2$3\" }, { \"from\": \"\u103a\u103d\", \"to\": \"\u103d\u103a\" }, { \"from\": \"\u103a([\u103c\u108a])\", \"to\": \"$1\u107d\" }, { \"from\": \"([\u1033\u1034])\u1094\", \"to\": \"$1\u1095\" }, {  \"from\": \"\u108F\u1071\",  \"to\" : \"\u108F\u1072\" }, {  \"from\": \"([\u1000-\u1021])([\u107B\u1066])\u102C\",  \"to\": \"$1\u102C$2\" }, {  \"from\": \"\u102C([\u107B\u1066])\u1037\",  \"to\": \"\u102C$1\u1094\" }]";
    private static final String ZG_TO_UNI = "[ { \"from\":\"\u200B\", \"to\" : \"\" }, { \"from\": \"(\u103d|\u1087)\", \"to\": \"\u103e\" }, { \"from\": \"\u103c\", \"to\": \"\u103d\" }, { \"from\": \"(\u103b|\u107e|\u107f|\u1080|\u1081|\u1082|\u1083|\u1084)\", \"to\": \"\u103c\" }, { \"from\": \"(\u103a|\u107d)\", \"to\": \"\u103b\" }, { \"from\": \"\u1039\", \"to\": \"\u103a\" }, { \"from\": \"(\u1066|\u1067)\", \"to\": \"\u1039\u1006\" }, { \"from\": \"\u106a\", \"to\": \"\u1009\" }, { \"from\": \"\u106b\", \"to\": \"\u100a\" }, { \"from\": \"\u106c\", \"to\": \"\u1039\u100b\" }, { \"from\": \"\u106d\", \"to\": \"\u1039\u100c\" }, { \"from\": \"\u106e\", \"to\": \"\u100d\u1039\u100d\" }, { \"from\": \"\u106f\", \"to\": \"\u100d\u1039\u100e\" }, { \"from\": \"\u1070\", \"to\": \"\u1039\u100f\" }, { \"from\": \"(\u1071|\u1072)\", \"to\": \"\u1039\u1010\" }, { \"from\": \"\u1060\", \"to\": \"\u1039\u1000\" }, { \"from\": \"\u1061\", \"to\": \"\u1039\u1001\" }, { \"from\": \"\u1062\", \"to\": \"\u1039\u1002\" }, { \"from\": \"\u1063\", \"to\": \"\u1039\u1003\" }, { \"from\": \"\u1065\", \"to\": \"\u1039\u1005\" }, { \"from\": \"\u1068\", \"to\": \"\u1039\u1007\" }, { \"from\": \"\u1069\", \"to\": \"\u1039\u1008\" }, { \"from\": \"(\u1073|\u1074)\", \"to\": \"\u1039\u1011\" }, { \"from\": \"\u1075\", \"to\": \"\u1039\u1012\" }, { \"from\": \"\u1076\", \"to\": \"\u1039\u1013\" }, { \"from\": \"\u1077\", \"to\": \"\u1039\u1014\" }, { \"from\": \"\u1078\", \"to\": \"\u1039\u1015\" }, { \"from\": \"\u1079\", \"to\": \"\u1039\u1016\" }, { \"from\": \"\u107a\", \"to\": \"\u1039\u1017\" }, { \"from\": \"\u107c\", \"to\": \"\u1039\u1019\" }, { \"from\": \"\u1085\", \"to\": \"\u1039\u101c\" }, { \"from\": \"\u1033\", \"to\": \"\u102f\" }, { \"from\": \"\u1034\", \"to\": \"\u1030\" }, { \"from\": \"\u103f\", \"to\": \"\u1030\" }, { \"from\": \"\u1086\", \"to\": \"\u103f\" }, { \"from\": \"\u1036\u1088\", \"to\": \"\u1088\u1036\" }, { \"from\": \"\u1088\", \"to\": \"\u103e\u102f\" }, { \"from\": \"\u1089\", \"to\": \"\u103e\u1030\" }, { \"from\": \"\u108a\", \"to\": \"\u103d\u103e\" }, { \"from\": \"([\u1000-\u1021])\u1064\", \"to\": \"\u1004\u103a\u1039$1\" }, { \"from\": \"([\u1000-\u1021])\u108b\", \"to\": \"\u1004\u103a\u1039$1\u102d\" }, { \"from\": \"([\u1000-\u1021])\u108c\", \"to\": \"\u1004\u103a\u1039$1\u102e\" }, { \"from\": \"([\u1000-\u1021])\u108d\", \"to\": \"\u1004\u103a\u1039$1\u1036\" }, { \"from\": \"\u108e\", \"to\": \"\u102d\u1036\" }, { \"from\": \"\u108f\", \"to\": \"\u1014\" }, { \"from\": \"\u1090\", \"to\": \"\u101b\" }, { \"from\": \"\u1091\", \"to\": \"\u100f\u1039\u100d\" }, { \"from\": \"\u1019\u102c(\u107b|\u1093)\", \"to\": \"\u1019\u1039\u1018\u102c\" }, { \"from\": \"(\u107b|\u1093)\", \"to\": \"\u1039\u1018\" }, { \"from\": \"(\u1094|\u1095)\", \"to\": \"\u1037\" }, { \"from\": \"\u1096\", \"to\": \"\u1039\u1010\u103d\" }, { \"from\": \"\u1097\", \"to\": \"\u100b\u1039\u100b\" }, { \"from\": \"\u103c([\u1000-\u1021])([\u1000-\u1021])?\", \"to\": \"$1\u103c$2\" }, { \"from\": \"([\u1000-\u1021])\u103c\u103a\", \"to\": \"\u103c$1\u103a\" }, { \"from\": \"\u1031([\u1000-\u1021])(\u103e)?(\u103b)?\", \"to\": \"$1$2$3\u1031\" }, { \"from\": \"([\u1000-\u1021])\u1031([\u103b\u103c\u103d\u103e]+)\", \"to\": \"$1$2\u1031\" }, { \"from\": \"\u1032\u103d\", \"to\": \"\u103d\u1032\" }, { \"from\": \"\u103d\u103b\", \"to\": \"\u103b\u103d\" }, { \"from\": \"\u103a\u1037\", \"to\": \"\u1037\u103a\" }, { \"from\": \"\u102f(\u102d|\u102e|\u1036|\u1037)\u102f\", \"to\": \"\u102f$1\" }, { \"from\": \"\u102f\u102f\", \"to\": \"\u102f\" }, { \"from\": \"(\u102f|\u1030)(\u102d|\u102e)\", \"to\": \"$2$1\" }, { \"from\": \"(\u103e)(\u103b|\u1037)\", \"to\": \"$2$1\" }, { \"from\": \"\u1025(\u103a|\u102c)\", \"to\": \"\u1009$1\" }, { \"from\": \"\u1025\u102e\", \"to\": \"\u1026\" }, { \"from\": \"\u1005\u103b\", \"to\": \"\u1008\" }, { \"from\": \"\u1036(\u102f|\u1030)\", \"to\": \"$1\u1036\" }, { \"from\": \"\u1031\u1037\u103e\", \"to\": \"\u103e\u1031\u1037\" }, { \"from\": \"\u1031\u103e\u102c\", \"to\": \"\u103e\u1031\u102c\" }, { \"from\": \"\u105a\", \"to\": \"\u102b\u103a\" }, { \"from\": \"\u1031\u103b\u103e\", \"to\": \"\u103b\u103e\u1031\" }, { \"from\": \"(\u102d|\u102e)(\u103d|\u103e)\", \"to\": \"$2$1\" }, { \"from\": \"\u102c\u1039([\u1000-\u1021])\", \"to\": \"\u1039$1\u102c\" }, { \"from\": \"\u103c\u1004\u103a\u1039([\u1000-\u1021])\", \"to\": \"\u1004\u103a\u1039$1\u103c\" }, { \"from\": \"\u1039\u103c\u103a\u1039([\u1000-\u1021])\", \"to\": \"\u103a\u1039$1\u103c\" }, { \"from\": \"\u103c\u1039([\u1000-\u1021])\", \"to\": \"\u1039$1\u103c\" }, { \"from\": \"\u1036\u1039([\u1000-\u1021])\", \"to\": \"\u1039$1\u1036\" }, { \"from\": \"\u1092\", \"to\": \"\u100b\u1039\u100c\" }, { \"from\": \"\u104e\", \"to\": \"\u104e\u1004\u103a\u1038\" }, { \"from\": \"\u1040(\u102b|\u102c|\u1036)\", \"to\": \"\u101d$1\" }, { \"from\": \"\u1025\u1039\", \"to\": \"\u1009\u1039\" }, { \"from\": \"([\u1000-\u1021])\u103c\u1031\u103d\", \"to\": \"$1\u103c\u103d\u1031\" }, { \"from\": \"([\u1000-\u1021])\u103b\u1031\u103d(\u103e)?\", \"to\": \"$1\u103b\u103d$2\u1031\" }, { \"from\": \"([\u1000-\u1021])\u103d\u1031\u103b\", \"to\": \"$1\u103b\u103d\u1031\" }, { \"from\": \"([\u1000-\u1021])\u1031(\u1039[\u1000-\u1021])\", \"to\": \"$1$2\u1031\" }, {  \"from\" : \" \u1037\",  \"to\": \"\u1037\" }]";

    private static final Pattern ZAWGYI_DETECT_PATTERN = Pattern.compile(
            // A regular expression matched if text is Zawgyi encoding.
            // Using the ranges 1033-1034 or 1060-1097 will report Shan, Karen,
            // etc. as Zawgyi.
            "[\u105a\u1060-\u1097]|" // Zawgyi characters outside Unicode range
            + "[\u1033\u1034]|" // These are Mon characters
            + "\u1031\u108f|" + "\u1031[\u103b-\u103e]|" // Medial right after \u1031
            + "[\u102b-\u1030\u1032]\u1031|" // Vowel sign right after before \u1031
            + " \u1031| \u103b|" // Unexpected characters after a space
            + "^\u1031|^\u103b|\u1038\u103b|\u1038\u1031|"
            + "[\u102d\u102e\u1032]\u103b|\u1039[^\u1000-\u1021]|\u1039$"
            + "|\u1004\u1039[\u1001-\u102a\u103f\u104e]" // Missing ASAT in Kinzi
            + "|\u1039[^u1000-\u102a\u103f\u104e]" // 1039 not before a consonant
            // Out of order medials
            + "|\u103c\u103b|\u103d\u103b" + "|\u103e\u103b|\u103d\u103c" + "|\u103e\u103c|\u103e\u103d"
            // Bad medial combos
            + "|\u103b\u103c"
            // Out of order vowel signs
            + "|[\u102f\u1030\u102b\u102c][\u102d\u102e\u1032]" + "|[\u102b\u102c][\u102f\u102c]"
            // Digit before diacritic
            + "|[\u1040-\u1049][\u102b-\u103e\u102b-\u1030\u1032\u1036\u1037\u1038\u103a]"
            // Single digit 0, 7 at start
            + "|^[\u1040\u1047][^\u1040-\u1049]"
            // Second 1039 with bad followers
            + "|[\u1000-\u102a\u103f\u104e]\u1039[\u101a\u101b\u101d\u101f\u1022-\u103f]"
            // Other bad combos.
            + "|\u103a\u103e" + "|\u1036\u102b]"
            // multiple upper vowels
            + "|\u102d[\u102e\u1032]|\u102e[\u102d\u1032]|\u1032[\u102d\u102e]"
            // Multiple lower vowels
            + "|\u102f\u1030|\u1030\u102f"
            // Multiple A vowels
            + "|\u102b\u102c|\u102c\u102b"
            // Shan digits with vowels or medials or other signs
            + "|[\u1090-\u1099][\u102b-\u1030\u1032\u1037\u103a-\u103e]"
            // Isolated Shan digit
            + "|[\u1000-\u10f4][\u1090-\u1099][\u1000-\u104f]"
            + "|^[\u1090-\u1099][\u1000-\u102a\u103f\u104e\u104a\u104b]" + "|[\u1000-\u104f][\u1090-\u1099]$"
            // Diacritics with non-Burmese vowel signs
            + "|[\u105e-\u1060\u1062-\u1064\u1067-\u106d\u1071-\u1074\u1082-\u108d" + "\u108f\u109a-\u109d]"
            + "[\u102b-\u103e]"
            // Consonant 103a + some vowel signs
            + "|[\u1000-\u102a]\u103a[\u102d\u102e\u1032]"
            // 1031 after other vowel signs
            + "|[\u102b-\u1030\u1032\u1036-\u1038\u103a]\u1031"
            // Using Shan combining characters with other languages.
            + "|[\u1087-\u108d][\u106e-\u1070\u1072-\u1074]"
            // Non-Burmese diacritics at start, following space, or following sections
            + "|^[\u105e-\u1060\u1062-\u1064\u1067-\u106d\u1071-\u1074" + "\u1082-\u108d\u108f\u109a-\u109d]"
            + "|[\u0020\u104a\u104b][\u105e-\u1060\u1062-\u1064\u1067-\u106d"
            + "\u1071-\u1074\u1082-\u108d\u108f\u109a-\u109d]"
            // Wrong order with 1036
            + "|[\u1036\u103a][\u102d-\u1030\u1032]"
            // Odd stacking
            + "|[\u1025\u100a]\u1039"
            // More mixing of non-Burmese languages
            + "|[\u108e-\u108f][\u1050-\u108d]"
            // Bad diacritic combos.
            + "|\u102d-\u1030\u1032\u1036-\u1037]\u1039]"
            // Dot before subscripted consonant
            + "|[\u1000-\u102a\u103f\u104e]\u1037\u1039"
            // Odd subscript + vowel signs
            + "|[\u1000-\u102a\u103f\u104e]\u102c\u1039[\u1000-\u102a\u103f\u104e]"
            // Medials after vowels
            + "|[\u102b-\u1030\u1032][\u103b-\u103e]"
            // Medials
            + "|\u1032[\u103b-\u103e]"
            // Medial with 101b
            + "|\u101b\u103c"
            // Stacking too deeply: consonant 1039 consonant 1039 consonant
            + "|[\u1000-\u102a\u103f\u104e]\u1039[\u1000-\u102a\u103f\u104e]\u1039"
            + "[\u1000-\u102a\u103f\u104e]"
            // Stacking pattern consonant 1039 consonant 103a other vowel signs
            + "|[\u1000-\u102a\u103f\u104e]\u1039[\u1000-\u102a\u103f\u104e]" + "[\u102b\u1032\u103d]"
            // Odd stacking over u1021, u1019, and u1000
            + "|[\u1000\u1005\u100f\u1010\u1012\u1014\u1015\u1019\u101a]\u1039\u1021"
            + "|[\u1000\u1010]\u1039\u1019" + "|\u1004\u1039\u1000" + "|\u1015\u1039[\u101a\u101e]"
            + "|\u1000\u1039\u1001\u1036" + "|\u1039\u1011\u1032"
            // Vowel sign in wrong order
            + "|\u1037\u1032" + "|\u1036\u103b"
            // Duplicated vowel
            + "|\u102f\u102f");

    // These Pattern Ref => http://novasteps.com/shake-n-break.html
    private static final Pattern IS_MYANMAR_PATTERN = Pattern.compile("[\u1000-\u1021]+|[\u1025-\u1027]");
    private static final Pattern IS_UNICODE_PATTERN = Pattern.compile("[ဃငစဆဇဈဉညတဋဌဍဎဏဒဓနဘရဝဟဠအ]်|"
            + "ျ[က-အ]ါ|ျ[ါ-း]|\u103e|\u103f|\u1031[^\u1000-\u1021\u103b\u106a\u106b\u107e-\u1084\u108f\u1090]|"
            + "\u1031$|\u100b\u1039|\u1031[က-အ]\u1032|\u1025\u102f|\u103c\u103d");

    private static String convert(String rule, String output) {
        JSONArray rule_array = new JSONArray(rule);
        int max_loop = rule_array.length();

        // because of JDK 7 bugs in Android
        output = output.replace("null", "\uFFFF\uFFFF");

        for (int i = 0; i < max_loop; i++) {

            JSONObject obj = rule_array.getJSONObject(i);
            String from = obj.get("from").toString();
            String to = obj.get("to").toString();

            output = output.replaceAll(from, to);
            output = output.replace("null", "");

        }

        output = output.replace("\uFFFF\uFFFF", "null");

        return output;

    }

    public static boolean isMyanmar(String input) {
        Matcher matcher = IS_MYANMAR_PATTERN.matcher(input);
        return matcher.find();
    }

    public static boolean isUnicode(String input) {
        Matcher matcher = IS_UNICODE_PATTERN.matcher(input);
        return matcher.find();
    }

    public static boolean isZawgyi(String input) {
        Matcher matcher = ZAWGYI_DETECT_PATTERN.matcher(input);
        return matcher.find();
    }

    public static String toUnicode(String zawgyiInput) {
        if (isZawgyi(zawgyiInput)) {
            return convert(ZG_TO_UNI, zawgyiInput);
        }
        return zawgyiInput;
    }

    public static String toZawgyi(String unicodeInput) {
        if (isZawgyi(unicodeInput)) {
            return unicodeInput;
        }
        return convert(UNI_TO_ZG, unicodeInput);
    }
}
