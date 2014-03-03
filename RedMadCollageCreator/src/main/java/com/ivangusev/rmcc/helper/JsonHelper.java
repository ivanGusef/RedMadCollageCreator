package com.ivangusev.rmcc.helper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ivan on 21.02.14.
 */
public class JsonHelper {

    public static final String DOT = "\\.";

    public static JSONObject getJSONObject(JSONObject jsonObject, String path) {
        final String[] parts = path.split(DOT);
        if (parts.length == 1) return jsonObject.optJSONObject(parts[0]);
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i]).append(DOT);
            }
            sb.delete(sb.length() - 1, sb.length());
            return getJSONObject(jsonObject.optJSONObject(parts[0]), sb.toString());
        }
        return null;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String path) {
        final String[] parts = path.split(DOT);
        if (parts.length == 1) return jsonObject.optJSONArray(parts[0]);
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i]).append(DOT);
            }
            sb.delete(sb.length() - 1, sb.length());
            return getJSONArray(jsonObject.optJSONObject(parts[0]), sb.toString());
        }
        return null;
    }

    public static String getString(JSONObject jsonObject, String path) {
        final String[] parts = path.split(DOT);
        if (parts.length == 1) return jsonObject.optString(parts[0]);
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i]).append(DOT);
            }
            sb.delete(sb.length() - 1, sb.length());
            return getString(jsonObject.optJSONObject(parts[0]), sb.toString());
        }
        return null;
    }

    public static Integer getInt(JSONObject jsonObject, String path) {
        final String[] parts = path.split(DOT);
        if (parts.length == 1) return jsonObject.optInt(parts[0]);
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                sb.append(parts[i]).append(".");
            }
            sb.delete(sb.length() - 1, sb.length());
            return getInt(jsonObject.optJSONObject(parts[0]), sb.toString());
        }
        return null;
    }
}
