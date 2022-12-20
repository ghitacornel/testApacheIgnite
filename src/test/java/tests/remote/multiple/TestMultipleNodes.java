package tests.remote.multiple;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestMultipleNodes {

    private static final String CACHE_NAME = "myCache";

    static IgniteClient ignite;

    @BeforeClass
    static public void setUpAll() {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setAddresses("127.0.0.1:47500", "127.0.0.1:47501", "127.0.0.1:47502");
        clientConfiguration.setTimeout(2000);

        ignite = Ignition.startClient(clientConfiguration);
    }

    @Ignore("doesn't work")
    @Test
    public void testMultipleReads() {

        // place breakpoints

        System.out.println("verify with local.node 1 only");

        // write cache
        {
            ClientCache<Object, Object> cache = ignite.getOrCreateCache(CACHE_NAME);
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);
        }

        verifyCache();

        System.out.println("start local.node 2");

        verifyCache();

        System.out.println("stop local.node 1");

        verifyCache();

        System.out.println("done");

    }

    private void verifyCache() {
        ClientCache<Object, Object> cache = ignite.cache(CACHE_NAME);
        Assert.assertEquals(1, cache.get("one"));
        Assert.assertEquals(2, cache.get("two"));
        Assert.assertEquals(3, cache.get("three"));
    }

}
