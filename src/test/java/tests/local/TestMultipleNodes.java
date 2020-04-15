package tests.local;

import local.node.ServerNode;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMultipleNodes {

    private static final String CACHE_NAME = "myCache";

    private static ServerNode node1;
    private static ServerNode node2;
    private static IgniteClient client;

    @BeforeClass
    public static void setUpAll() {

        {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("1");
            node1 = new ServerNode(configuration);
            node1.startNode();
        }

        {
            ClientConfiguration configuration = new ClientConfiguration();
            configuration.setAddresses("127.0.0.1");
            client = Ignition.startClient(configuration);
        }

    }

    @AfterClass
    public static void tearDownAll() {
        node1.stopNode();
        node2.stopNode();
    }

    @Test
    public void testMultipleRead() {

        ClientCache<Object, Object> cache;

        // create and add to cache
        {

            ClientCacheConfiguration configuration = new ClientCacheConfiguration();
            configuration.setName(CACHE_NAME);
            configuration.setCacheMode(CacheMode.REPLICATED);

            // create cache
            cache = client.getOrCreateCache(configuration);

            // add to cache
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);

        }

        verifyCache();

        // create node 2
        {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("2");
            node2 = new ServerNode(configuration);
            node2.startNode();
        }

        verifyCache();

        // close node 1
        {
            node1.stopNode();
        }

        verifyCache();

    }

    private void verifyCache() {
        ClientCache<Object, Object> cache = client.getOrCreateCache(CACHE_NAME);
        Assert.assertEquals(1, cache.get("one"));
        Assert.assertEquals(2, cache.get("two"));
        Assert.assertEquals(3, cache.get("three"));
    }

}
