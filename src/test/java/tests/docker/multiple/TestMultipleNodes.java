package tests.docker.multiple;

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

    static Ignite ignite;

    @BeforeClass
    static public void setUpAll() {
        List<String> addressList = new ArrayList<>();
        addressList.add("127.0.0.1");


        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(addressList);

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);
        tcpDiscoverySpi.setClientReconnectDisabled(false);
        tcpDiscoverySpi.setReconnectDelay(100);

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(true);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

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

    @Ignore("does not work on docker with multiple containers")
    @Test
    public void testMultipleReads() {

        // place breakpoints

        // verify with 2 containers
        System.out.println("verify with 2 containers");
        {
           IgniteCache<String, Integer> cache = ignite.cache("myCache");
            Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
            Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
            Assert.assertEquals(Integer.valueOf(3), cache.get("three"));
        }

        // start killing containers

        // kill 1 container

        // verify
        System.out.println("verify with container 2");
        {
            IgniteCache<String, Integer> cache = ignite.cache("myCache");
            Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
            Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
            Assert.assertEquals(Integer.valueOf(3), cache.get("three"));
        }

        // start 3 container
        // kill 2 container

        // verify
        System.out.println("verify with container 3 and test replication");
        {
            IgniteCache<String, Integer> cache = ignite.cache("myCache");
            Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
            Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
            Assert.assertEquals(Integer.valueOf(3), cache.get("three"));
        }

    }

}
