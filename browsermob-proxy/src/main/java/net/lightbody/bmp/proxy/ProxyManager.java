package net.lightbody.bmp.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ProxyManager {
    private final AtomicInteger portCounter = new AtomicInteger(9090);
    private final Provider<ProxyServer> proxyServerProvider;
    private final Map<Integer, ProxyServer> proxies = new ConcurrentHashMap<Integer, ProxyServer>();

    @Inject
    public ProxyManager(final Provider<ProxyServer> proxyServerProvider) {
        this.proxyServerProvider = proxyServerProvider;
    }

    public ProxyServer create(final Map<String, String> options, final int port) throws Exception {
        ProxyServer proxy = proxyServerProvider.get();
        proxy.setPort(port);
        proxy.start(60000);
        proxy.setOptions(options);
        proxies.put(port, proxy);
        return proxy;
    }

    public ProxyServer create(final Map<String, String> options) throws Exception {
        int port = portCounter.incrementAndGet();
        ProxyServer proxy = proxyServerProvider.get();

        proxy.setPort(port);
        proxy.start(60000);
        proxy.setOptions(options);

        proxies.put(port, proxy);

        return proxy;
    }

    public ProxyServer get(final int port) {
        return proxies.get(port);
    }

    public void delete(final int port) throws Exception {
        ProxyServer proxy = proxies.remove(port);
        proxy.stop();
    }
}
