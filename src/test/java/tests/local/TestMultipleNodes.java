package tests.local;

import local.node.ServerNode;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMultipleNodes {

    private static ServerNode node1;
    private static ServerNode node2;

    @BeforeClass
    public static void setUpAll() {

        {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("1");
            node1 = new ServerNode(configuration);
        }

        {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("2");
            node2 = new ServerNode(configuration);
        }

        node1.startNode();
        node2.startNode();
    }

    @AfterClass
    public static void tearDownAll() {
        node1.stopNode();
        node2.stopNode();
    }

    @Test
    public void testSingleRead() {

        {// add to cache
            CacheConfiguration<String, Integer> cacheConfiguration = new CacheConfiguration<>("myCache");
            cacheConfiguration.setName("myCache");
            cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
            IgniteCache<String, Integer> cache = node1.getIgnite().getOrCreateCache(cacheConfiguration);


            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);
        }

        IgniteCache<String, Integer> cache;
        {// get cache from second instance
            cache = node2.getIgnite().getOrCreateCache("myCache");
        }

        // verify cache was distributed
        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));


        ServerNode server3;
        {// create a third local.node
            IgniteConfiguration igniteConfiguration3 = new IgniteConfiguration();
            igniteConfiguration3.setIgniteInstanceName("3");
            server3 = new ServerNode(igniteConfiguration3);
            server3.startNode();
        }

        {// close previous opened nodes
            node1.stopNode();
            node2.stopNode();
        }

        {// get cache from third instance
            cache = server3.getIgnite().getOrCreateCache("myCache");
        }

        // verify cache was replicated
        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));

        {// close third instance
            server3.stopNode();
        }

    }

}
