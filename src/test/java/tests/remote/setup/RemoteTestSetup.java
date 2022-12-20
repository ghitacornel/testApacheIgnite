package tests.remote.setup;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class RemoteTestSetup {

    protected static final String CACHE_NAME = "myCache";

    static ClientConfiguration clientConfiguration;
    protected IgniteClient ignite;

    @BeforeClass
    static public void setUpAll() {
        clientConfiguration = new ClientConfiguration();
        clientConfiguration.setAddresses("127.0.0.1:10800", "127.0.0.1:10801", "127.0.0.1:10802");
        clientConfiguration.setTimeout(2000);
    }

    @Before
    public void setUp() {
        ignite = Ignition.startClient(clientConfiguration);
    }

    @After
    public void tearDown() {
        ignite.close();
    }

}
