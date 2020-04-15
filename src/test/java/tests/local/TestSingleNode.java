package tests.local;

import local.node.ServerNode;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.*;

public class TestSingleNode {

    private static ServerNode node;

    @BeforeClass
    public static void setUpAll() {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setIgniteInstanceName("1");
        node = new ServerNode(configuration);
        node.startNode();
    }

    @AfterClass
    public static void tearDownAll() {
        node.stopNode();
    }

    @Test
    public void testSingleRead() {

        Ignite ignite = node.getIgnite();
        IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");

        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);


        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));

    }

}
