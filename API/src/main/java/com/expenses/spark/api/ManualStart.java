package com.expenses.spark.api;

import static spark.Spark.get;

/**
 * Created by lofie on 2017-09-17.
 */
public class ManualStart
{
    public static void main(String[] args)
    {
        get("/hello", (req, res) -> "Hello World");


        get("/hi", (req, res) -> "hi World");

        StartSpark startSpark = new StartSpark();
        startSpark.fireUp();
//        StartSpark.SparkHandler handler = new StartSpark.SparkHandler();
//        handler.fireUp();
    }
}
