package tests.remote.single;

import org.apache.ignite.client.ClientCache;
import org.junit.Assert;
import org.junit.Test;
import tests.remote.setup.RemoteTestSetup;

public class TestSingleNode extends RemoteTestSetup {

    @Test
    public void testSingleRead() {

        ClientCache<String, Integer> cache = ignite.getOrCreateCache("myCache");

        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);


        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));

    }

}
