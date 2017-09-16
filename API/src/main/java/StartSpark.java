/**
 * Created by Foten on 12/7/2016.
 */

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import static spark.Spark.get;

public class StartSpark
{
    private static final Logger logger = Logger.getLogger(StartSpark.class);

    public static void main(String[] argv)
    {

        get("/hello", (req, res) -> "Hello World");


        get("/hi", (req, res) -> "hi World");

        fireUp();
    }

    /**
     * Only used for testing
     */
    public static void fireUp()
    {
        BasicConfigurator.configure();
        System.setProperty("https.protocols", "TLSv1.2");

        RegisterServiceAPI.start();
        ExpensesAPI.start();
        StatusAPI.start();
    }

    /**
     * Only used for testing
     */
    public static void putOut()
    {
        Thread.currentThread().interrupt();
    }

}
