/**
 * Created by Foten on 12/7/2016.
 */
package com.expenses.spark.api;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class StartSpark
{
    private static final Logger LOGGER = Logger.getLogger(StartSpark.class);

    public StartSpark()
    {
        // Empty
    }

    /**
     * Only used for testing
     */
    public void fireUp()
    {
        BasicConfigurator.configure();
        System.setProperty("https.protocols", "TLSv1.2");

        LOGGER.info("Starting Register service API...");
        RegisterServiceAPI.start();

        LOGGER.info("Starting Expenses API...");
        ExpensesAPI.start();

        LOGGER.info("Starting Status API...");
        StatusAPI.start();
    }

    /**
     * Only used for testing
     */
    public void putOut()
    {
        Thread.currentThread().interrupt();
    }


}
