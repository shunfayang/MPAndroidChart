package com.xxmassdeveloper.mpchartexample.moose;

import android.content.Context;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yangshunfa on 07/27/2018
 **/
public class JsonUtils {
    public static String getJsonData(Context context) {
        BufferedReader reader = null;
        String mockData = "";
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("data.TXT")));
            StringBuilder sb = new StringBuilder();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
            }
            mockData = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    reader = null;
                }
            }
        }
        return mockData;
    }
}
