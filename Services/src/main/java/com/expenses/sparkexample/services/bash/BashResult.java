package com.expenses.sparkexample.services.bash;

/**
 * Created by Foten on 2/17/2017.
 */
public class BashResult
{
    private final String myExecutedCommand;
    private final String myGrepCommand;
    private final String myResult;
    private final Boolean myIsSuccess;

    public BashResult(String executedCommand, String grepCommand, String result, Boolean isSuccess)
    {
        myExecutedCommand = executedCommand;
        myGrepCommand = grepCommand;
        myResult = result;
        myIsSuccess = isSuccess;
    }

    public String getCommand()
    {
        return myExecutedCommand;
    }

    public String getGrepCommand()
    {
        return myGrepCommand;
    }

    public Boolean isSuccessfull()
    {
        return myIsSuccess;
    }


    public String getResult()
    {
        return myResult;
    }

    @Override
    public String toString()
    {
        return "com.expenses.sparkexample.services.bash.BashResult{" + "myExecutedCommand='" + myExecutedCommand + '\'' + ", myGrepCommand='" + myGrepCommand + '\'' + ", myResult='" + myResult + '\'' + ", myIsSuccess=" + myIsSuccess + '}';
    }
}
