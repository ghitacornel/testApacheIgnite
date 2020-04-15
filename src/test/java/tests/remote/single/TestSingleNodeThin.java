package tests.remote.single;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSingleNodeThin {

    static IgniteClient client;

    @BeforeClass
    static public void setUpAll() {

        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setAddresses("127.0.0.1:47500");
        configuration.setTimeout(2000);

        client = Ignition.startClient(configuration);
    }

    @Test
    public void testSingleRead() {

        ClientCache<Object, Object> cache = client.getOrCreateCache("myCache");

        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);

        Assert.assertEquals(1, cache.get("one"));
        Assert.assertEquals(2, cache.get("two"));
        Assert.assertEquals(3, cache.get("three"));

    }

}
