package tests.remote.multiple;

import org.apache.ignite.client.ClientCache;
import org.junit.Assert;
import org.junit.Test;
import tests.remote.setup.RemoteTestSetup;

public class TestMultipleNodes extends RemoteTestSetup {


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
