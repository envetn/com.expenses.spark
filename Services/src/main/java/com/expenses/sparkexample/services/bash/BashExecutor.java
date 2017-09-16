package com.expenses.sparkexample.services.bash;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Foten on 1/3/2017.
 */
public class BashExecutor
{
    private static final Logger LOGGER = Logger.getLogger(BashExecutor.class);
    private final String myCommand;

    public BashExecutor(String command)
    {
        myCommand = command;
    }

    public List<BashResult> executeCommand()
    {
        File tempFile = createTempFile();

        return executeScript(tempFile);
    }

    private File createTempFile()
    {
        // create a temporary bashfile and put the wanted command into it.
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("script", null);
            Writer steamWriter = new OutputStreamWriter(new FileOutputStream(tempFile));
            PrintWriter printWriter = new PrintWriter(steamWriter);
            printWriter.println("#!/bin/com.expenses.sparkexample.services.bash");
            printWriter.println(myCommand);
            printWriter.close();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to create temporary bashfile: ", e);
        }
        return tempFile;
    }

    private List<BashResult> executeScript(File tempFile)
    {
        List<BashResult> resultAsList = new ArrayList<>();
        if(tempFile != null)
        {
            try
            {
                Process process = new ProcessBuilder("bash", tempFile.toString()).start();
                resultAsList = getOutputFromProcess(process.getInputStream());
                process.waitFor();
                if(!tempFile.delete())
                {
                    LOGGER.error("Failed to cleanup after bash execution.");
                }
            }
            catch (IOException | InterruptedException e)
            {
                LOGGER.error("Failed to execute bashFile.", e);
            }
        }


        return resultAsList;
    }

    private List<BashResult> getOutputFromProcess(InputStream inputStream)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<BashResult> resultAsList = new ArrayList<>();
        try
        {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                builder.append(line)
                        .append("@");
            }

            String result = builder.toString();

            String[] splitResult = myCommand.split("\\|");
            String actualCommand = splitResult[0];
            String grepCommand = appendGrepCommand(splitResult);
            Boolean success = result.length() < 1;

            BashResult bashResult = new BashResult(actualCommand, grepCommand, result, success);
            resultAsList.add(bashResult);
            LOGGER.info("-- Creating bashResult: " + bashResult);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return resultAsList;

    }

    private String appendGrepCommand(String[] splitResult)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < splitResult.length; i++)
        {
            builder.append("|")
                    .append(splitResult[i]);
        }
        return builder.toString();
    }
}
