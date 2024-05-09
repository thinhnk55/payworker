package com.defi.util.json;

import com.google.gson.*;
import com.defi.util.file.FileUtil;

import java.util.HashSet;
import java.util.Set;

public class GsonUtil {
    public static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();
    public static Gson beautyGson = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    public static JsonObject getJsonObject(String filePath) {
        String data = FileUtil.getString(filePath);
        return GsonUtil.toJsonObject(data);
    }

    public static JsonArray getJsonArray(String filePath){
        String data = FileUtil.getString(filePath);
        return GsonUtil.toJsonArray(data);
    }

    public static JsonObject toJsonObject(String data) {
        try {
            return gson.fromJson(data, JsonObject.class);
        }catch (Exception e){
            return null;
        }
    }

    public static JsonArray toJsonArray(String data) {
        try {
            return gson.fromJson(data, JsonArray.class);
        }catch (Exception e){
            return null;
        }
    }
    public static Set<String> toSet(JsonArray array) {
        Set<String> set = new HashSet<>();
        for(int i = 0; i < array.size(); i++){
            set.add(array.get(i).getAsString());
        }
        return set;
    }

    public static double[] toDoubleArray(JsonArray array) {
        double[] values = new double[array.size()];
        for(int i = 0; i < array.size(); i++){
            values[i] = array.get(i).getAsDouble();
        }
        return values;
    }

    public static int[] toIntArray(JsonArray array) {
        int[] values = new int[array.size()];
        for(int i = 0; i < array.size(); i++){
            values[i] = array.get(i).getAsInt();
        }
        return values;
    }
    public static String[] toStringArray(JsonArray array) {
        String[] values = new String[array.size()];
        for(int i = 0; i < array.size(); i++){
            values[i] = array.get(i).getAsString();
        }
        return values;

    }

    public static JsonArray toJsonArray(int[][] intArray) {
        JsonArray array = new JsonArray();
        for(int i = 0; i < intArray.length; i++){
            JsonArray subArray = new JsonArray();
            for(int j = 0; j < intArray[i].length; j++){
                subArray.add(intArray[i][j]);
            }
            array.add(subArray);
        }
        return array;
    }

    public static int[][] toTwoDimensionIntArray(JsonArray array) {
        int[][] values = new int[array.size()][];
        for (int i = 0; i < array.size(); i++) {
            JsonArray innerArray = array.get(i).getAsJsonArray();
            values[i] = new int[innerArray.size()];
            for (int j = 0; j < innerArray.size(); j++) {
                values[i][j] = innerArray.get(j).getAsInt();
            }
        }
        return values;
    }

    public static JsonArray toJsonArray(double[] values) {
        JsonArray array = new JsonArray();
        for(int i = 0; i < values.length; i++) {
            array.add(values[i]);
        }
        return array;
    }


}
