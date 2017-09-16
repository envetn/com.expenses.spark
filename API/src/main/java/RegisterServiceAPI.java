import com.expenses.sparkexample.services.Connections.RegisteredConnection;
import com.expenses.sparkexample.services.Socket.SocketConnect;
import com.expenses.sparkexample.services.Utilities;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import static spark.Spark.post;

/**
 * Created by Foten on 12/7/2016.
 */
public class RegisterServiceAPI
{
    private static final Logger logger = Logger.getLogger(RegisterServiceAPI.class);
    public static void start()
    {
        // insert a post (using HTTP post method)
        post("/register/json", (request, response) -> {

            String jsonData = request.body();
            logger.info("Received: " + jsonData);
            String stringResponse;

            // some sort of validating and maybe some sort of load balancing. Or have two different db?
            try
            {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();
                String ip = jsonObject.get("serverIp").getAsString();
                int port = jsonObject.get("serverPort").getAsInt();
                RegisteredConnection.addService(ip, port);

                stringResponse = Utilities.createJsonHeader("Success", "Service registered");
                logger.info("Response: " + stringResponse);
                logger.info("******************************************");
            }
            catch (Exception e)
            {
                stringResponse = Utilities.createJsonHeader("Failed", e.getMessage());
                // some failed
            }

            response.body(stringResponse);
            response.status(200);
            response.type("application/json");

            return 10;
        });
    }
}
