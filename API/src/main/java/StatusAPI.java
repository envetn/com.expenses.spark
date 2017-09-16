import com.expenses.sparkexample.services.Socket.SocketConnect;
import com.expenses.sparkexample.services.Utilities;
import com.expenses.sparkexample.services.bash.BashExecutor;
import com.expenses.sparkexample.services.bash.BashResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Foten on 1/3/2017.
 */
public class StatusAPI
{
    private static final Logger logger = Logger.getLogger(StatusAPI.class);
    public static void start()
    {
        get("/com/expenses/sparkexample/services/bash/status", (req, res) -> getHelpText());
        // insert a post (using HTTP post method)
        post("/com/expenses/sparkexample/services/bash/status", (request, response) ->
        {
            String jsonData = request.body();
            logger.info("Received: " + jsonData + " from " + request.ip());
            String stringResponse = "";
            try
            {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(jsonData)
                        .getAsJsonObject();
                String ip = request.ip();
                if (ip.matches("192.168.1.*"))
                {
                    List<BashResult> result = validateAndExeucte(jsonObject);
                    stringResponse = createResponse(result);
                }
            }
            catch (Exception e)
            {
                stringResponse = Utilities.createJsonHeader("Failed", e.getMessage());
                response.redirect("REDIRECT", 418);
                //some failed
            }
            logger.info("Response: " + stringResponse);
            response.body(stringResponse);
            response.type("application/jsonserver.common.json");

            return stringResponse;
        });
    }

    private static String createResponse(List<BashResult> results)
    {
        JsonObject object = new JsonObject();
        object.addProperty("Result", "Success");

        JsonArray jsonResults = new JsonArray();

        for (BashResult result : results)
        {
            JsonObject jsonSpot = new JsonObject();
            jsonSpot.addProperty("Status", "ok");
            jsonSpot.addProperty("commandResult", result.getCommand());
            jsonSpot.addProperty("grepCommand", result.getGrepCommand());
            jsonSpot.addProperty("result", result.getResult());
            jsonSpot.addProperty("isSuccessful", result.isSuccessfull());

            jsonResults.add(jsonSpot);
        }

        object.add("commandResults", jsonResults);

        return object.toString();
    }

    private static List<BashResult> validateAndExeucte(JsonObject jsonData)
    {
        JsonArray array = jsonData.getAsJsonArray("commands");
        List<BashResult> result = new ArrayList<>();

        for (JsonElement object : array)
        {
            for (JsonElement element : object.getAsJsonArray())
            {
                String spot = element.getAsString();
                List<BashResult> bashResutl = executeBash(spot);
                result.addAll(bashResutl);
            }
        }
        return result;
    }

    private static List<BashResult> executeBash(String command)
    {
        BashExecutor bashExecutor = new BashExecutor(command);
        return bashExecutor.executeCommand();
        //ps aux | grep "services/.*/target/Java.*.jar"
    }

    private static String getHelpText()
    {
        return "Usage: {\n" + "\t\"id\": {\n" + "\t\t\"requestId\": \"Status\"\n" + "\t},\n" + "\t\"requestDate\": \"2016-12-01\",\n" + "\t\"requestType\": \"Get\",\n" + "\t\"commands\": [\n" + "\t\t[\"ps | grep 'com.expenses.sparkexample.services.bash'\", \"ipconfig | grep '192.168.1.*'\"]\n" + "\t]\n" + "}";
    }
}
