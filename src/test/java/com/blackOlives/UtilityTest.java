package com.blackOlives;

import org.junit.Assert;
import org.junit.Test;

public class UtilityTest {

    @Test
    public void testAssignPlayersNotNull() {
        Utility util = new Utility();
        Assert.assertNotNull(util.assignPlayers(false));
    }
}