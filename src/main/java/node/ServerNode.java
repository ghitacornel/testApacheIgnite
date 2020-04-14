package node;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ServerNode {

    IgniteConfiguration igniteConfiguration;

    private Ignite ignite;

    public ServerNode() {
    }

    public ServerNode(IgniteConfiguration igniteConfiguration) {
        this.igniteConfiguration = igniteConfiguration;
    }

    public void startNode() {
        ignite = Ignition.start(igniteConfiguration);
    }

    public void stopNode() {
        ignite.close();
    }

    public Ignite getIgnite() {
        return ignite;
    }

}
