package tests.remote.multiple;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class TestMultipleNodes {

    private static final String CACHE_NAME = "myCache";

    static Ignite ignite;

    @BeforeClass
    static public void setUpAll() {
        List<String> addressList = new ArrayList<>();
        addressList.add("127.0.0.1:47500");
        addressList.add("127.0.0.1:47501");


        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(addressList);
        ipFinder.setShared(true);

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);
        tcpDiscoverySpi.setClientReconnectDisabled(false);
        tcpDiscoverySpi.setReconnectDelay(100);

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(true);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        Ignition.setClientMode(true);
        ignite = Ignition.start(igniteConfiguration);
    }

    @AfterClass
    static public void tearDownAll() {
        Ignition.stopAll(false);
    }

    @Before
    public void setUp() {
        IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");
        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);
    }

    @Test
    public void testMultipleReads() {

        // place breakpoints

        System.out.println("verify with local.node 1 only");

        // write cache
        {
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");
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
        IgniteCache<String, Integer> cache = ignite.cache(CACHE_NAME);
        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));
    }

}
