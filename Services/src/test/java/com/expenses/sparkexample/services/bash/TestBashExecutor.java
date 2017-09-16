package com.expenses.sparkexample.services.bash;

import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Foten on 2/20/2017.
 */
public class TestBashExecutor
{
    @Test
    public void testExecutesPs()
    {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
        BashExecutor bash = new BashExecutor("ps | grep -o com.expenses.sparkexample.services.bash");
        List<BashResult> bashResults = bash.executeCommand();

        assertThat(bashResults).hasSize(1);

        BashResult bashResult = bashResults.get(0);
        assertThat(bashResult.getCommand()).isEqualTo("ps ");
        assertThat(bashResult.getGrepCommand()).isEqualTo("| grep -o com.expenses.sparkexample.services.bash");
        assertThat(bashResult.getResult()).isNotNull();
    }

    @Test
    public void testExecutesTwoGrepCommands()
    {
        BashExecutor bash = new BashExecutor("ps | grep com.expenses.sparkexample.services.bash | grep -o usr");
        List<BashResult> bashResults = bash.executeCommand();

        assertThat(bashResults).hasSize(1);

        BashResult bashResult = bashResults.get(0);
        assertThat(bashResult.getCommand()).isEqualTo("ps ");
        assertThat(bashResult.getGrepCommand()).isEqualTo("| grep com.expenses.sparkexample.services.bash | grep -o usr");
    }
}
