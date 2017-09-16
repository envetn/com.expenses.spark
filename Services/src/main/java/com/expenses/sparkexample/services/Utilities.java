package com.expenses.sparkexample.services;

import com.google.gson.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Foten on 2/26/2017.
 */
public class Utilities
{
    public static String getPrettyJsonString(JsonObject jsonObject)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonObject.toString());
        return gson.toJson(je);
    }


    public static String createJsonHeader(String responseInfo, String message)
    {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("Response", responseInfo);
        jsonResponse.addProperty("Time", getTimestamp().toString());
        jsonResponse.addProperty("where", "REST interface");
        jsonResponse.addProperty("Reason", message);

        return jsonResponse.toString();
    }



    public static Date getTimestamp()
    {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Date.valueOf(dateFormat.format(calender.getTime()));
    }
}
