package tests.docker.single;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSingleNode {

    static Ignite ignite;

    @BeforeClass
    static public void setUpAll() {
        List<String> addressList = new ArrayList<>();
        addressList.add("127.0.0.1:47500");

        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(addressList);

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(true);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        ignite = Ignition.start(igniteConfiguration);
    }

    @AfterClass
    static public void tearDownAll() {
        Ignition.stopAll(false);
    }

    @Test
    public void testSingleRead() {

        IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");

        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);


        Assert.assertEquals(Integer.valueOf(1), cache.get("one"));
        Assert.assertEquals(Integer.valueOf(2), cache.get("two"));
        Assert.assertEquals(Integer.valueOf(3), cache.get("three"));

    }

}
