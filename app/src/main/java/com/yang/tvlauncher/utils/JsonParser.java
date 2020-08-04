package com.yang.tvlauncher.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class JsonParser {


    public static <T> T jsonFile2Bean(File file, Class<T> clazz) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            // 创建StringBuffer
            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            try {
                // 一行一行的读
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(temp);
                }
                String presonsString = stringBuffer.toString();
                // 解析,创建Gson,需要导入gson的jar包
                Gson gson = new Gson();
                T t = gson.fromJson(presonsString,clazz);
                return t;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonFile2Bean(InputStream stream, Class<T> clazz) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream));
        // 创建StringBuffer
        StringBuffer stringBuffer = new StringBuffer();
        String temp = "";
        try {
            // 一行一行的读
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }
            String presonsString = stringBuffer.toString();
            // 解析,创建Gson,需要导入gson的jar包
            Gson gson = new Gson();
            T t = gson.fromJson(presonsString,clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List jsonFile2List(InputStream stream, Type type) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(stream));
            // 创建StringBuffer
            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            try {
                    // 一行一行的读
                    while ((temp = bufferedReader.readLine()) != null) {
                            stringBuffer.append(temp);
                    }
                    String presonsString = stringBuffer.toString();
                    // 解析,创建Gson,需要导入gson的jar包
                    Gson gson = new Gson();
                    return gson.fromJson(presonsString, type);
            } catch (IOException e) {
                    e.printStackTrace();
                    return null;
            }
    }
}
