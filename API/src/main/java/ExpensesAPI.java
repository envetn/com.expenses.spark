import com.expenses.sparkexample.services.Socket.SocketConnect;
import com.expenses.sparkexample.services.Utilities;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Foten on 12/7/2016.
 */
public class ExpensesAPI
{

    private static final Logger LOGGER = Logger.getLogger(ExpensesAPI.class);
    public static void start()
    {
        get("/expenses", (req, res) -> "Expenses API.\n Usage: ");

        // insert a post (using HTTP post method)
        post("/expenses", (request, response) ->
        {
            String stringResponse;
            try
            {
                LOGGER.info("******************************************\n");
                String body = request.body();
                LOGGER.info("REQUEST: " + body);

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(body).getAsJsonObject();
                LOGGER.info("REQUEST-JsonString: " + Utilities.getPrettyJsonString(jsonObject));
                SocketConnect connect = new SocketConnect(jsonObject.toString());

                stringResponse = connect.sendToServer();
                response.status(200);

                jsonObject = parser.parse(stringResponse).getAsJsonObject();
                LOGGER.info("RESPONSE-JsonString: " + Utilities.getPrettyJsonString(jsonObject));
                LOGGER.info("******************************************\n");
            }
            catch (Exception e)
            {
                LOGGER.error("Failed with the request: " + e.getMessage());
                stringResponse = Utilities.createJsonHeader("Failed", e.getMessage());
                response.redirect("REDIRECT", 418);
                //some failed
            }

            response.body(stringResponse);
            response.type("application/json");

            return stringResponse;
        });
    }

}
