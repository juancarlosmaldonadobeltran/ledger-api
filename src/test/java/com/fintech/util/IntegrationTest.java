package com.fintech.util;

import com.fintech.common.JsonTransformer;
import com.fintech.repository.AccountRepository;
import org.junit.Before;

public abstract class IntegrationTest {

    protected static JsonTransformer transformer = new JsonTransformer();

    protected static TestUtil testUtil = new TestUtil();

    protected static final AccountRepository repository = new AccountRepository();

    @Before
    public void setUp() {
        repository.removeAll();
    }

}
