package tests.local;

import local.node.ServerNode;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSingleNode {

    private static final String CACHE_NAME = "myCache";

    private static ServerNode node;
    private static IgniteClient client;

    @BeforeClass
    public static void setUpAll() {

        {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("1");
            node = new ServerNode(configuration);
            node.startNode();
        }

        {
            ClientConfiguration configuration = new ClientConfiguration();
            configuration.setAddresses("127.0.0.1");
            configuration.setTimeout(2000);
            client = Ignition.startClient(configuration);
        }

    }

    @AfterClass
    public static void tearDownAll() {
        node.stopNode();
    }

    @Test
    public void testSingleWriteRead() {

        // write
        {
            ClientCache<Object, Object> cache = client.getOrCreateCache(CACHE_NAME);
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);
        }

        // read and test
        {
            ClientCache<Object, Object> cache = client.getOrCreateCache(CACHE_NAME);
            Assert.assertEquals(1, cache.get("one"));
            Assert.assertEquals(2, cache.get("two"));
            Assert.assertEquals(3, cache.get("three"));
        }

    }

}
