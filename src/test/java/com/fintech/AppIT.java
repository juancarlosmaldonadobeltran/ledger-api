package com.fintech;

import com.fintech.util.IntegrationTest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static com.fintech.common.ApiResponse.StatusResponse;
import static com.fintech.util.TestUtil.TestResponse;
import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitInitialization;

public class AppIT extends IntegrationTest {

    @Before
    public void setUp() {
        String[] args = {};
        App.main(args);
        awaitInitialization();
    }

    @Test
    public void getPing() throws Exception {

        String path = "/ping";
        TestResponse res = testUtil.get(path);
        assertEquals(HttpStatus.OK_200, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        assertEquals("PONG", res.getBody().getData().getAsString());
    }

}
