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
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setAddresses("127.0.0.1");
            client = Ignition.startClient(clientConfiguration);
        }

    }

    @AfterClass
    public static void tearDownAll() {
        node.stopNode();
    }

    @Test
    public void testSingleWriteRead() {

        ClientCache<Object, Object> cache = client.getOrCreateCache("myCache");

        // write
        {
            cache.put("one", 1);
            cache.put("two", 2);
            cache.put("three", 3);
        }

        // read and test
        Assert.assertEquals(1, cache.get("one"));
        Assert.assertEquals(2, cache.get("two"));
        Assert.assertEquals(3, cache.get("three"));

    }

}
