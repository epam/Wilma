package net.lightbody.bmp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProxyServerTest {

    private final ProxyServer server = new ProxyServer(0);

    @Before
    public void startServer() throws Exception {
        server.start(60000);
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void portAllocation() throws Exception {
        assertThat(server.getPort(), not(equalTo(0)));
    }
}
