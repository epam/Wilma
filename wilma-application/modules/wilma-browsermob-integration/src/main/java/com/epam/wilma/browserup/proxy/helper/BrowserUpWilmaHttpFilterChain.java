/*
 * Modifications Copyright (c) 2019 BrowserUp, Inc.
 *
 *
Copyright since 2020, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/


package com.epam.wilma.browserup.proxy.helper;

import com.browserup.bup.filters.BrowserUpHttpFilterChain;
import com.browserup.bup.filters.ModifiedRequestAwareFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The filter "driver" that delegates to all chained filters specified by the proxy server.
 */
public class BrowserUpWilmaHttpFilterChain extends HttpFiltersAdapter {
    private static final Logger log = LoggerFactory.getLogger(BrowserUpHttpFilterChain.class);

    private final BrowserUpProxyServer proxyServer;
    private final PreservedInformation preservedInformation;

    private final List<HttpFilters> filters;

    public BrowserUpWilmaHttpFilterChain(BrowserUpProxyServer proxyServer, HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
        preservedInformation = new PreservedInformation();
        this.proxyServer = proxyServer;

        if (proxyServer.getFilterFactories() != null) {
            filters = new ArrayList<>(proxyServer.getFilterFactories().size());

            // instantiate all HttpFilters using the proxy's filter factories
            // allow filter factories to avoid adding a filter on a per-request basis by returning a null
            // HttpFilters instance
            proxyServer.getFilterFactories().stream()
                    .map(filterFactory -> filterFactory.filterRequest(originalRequest, ctx))
                    .filter(Objects::nonNull).forEach(filters::add);
        } else {
            filters = Collections.emptyList();
        }
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject, PreservedInformation preservedInformation) {
        if (proxyServer.isStopped()) {
            log.warn("Aborting request to {} because proxy is stopped", originalRequest.getUri());
            HttpResponse abortedResponse = new DefaultFullHttpResponse(originalRequest.getProtocolVersion(), HttpResponseStatus.SERVICE_UNAVAILABLE);
            HttpHeaders.setContentLength(abortedResponse, 0L);
            return abortedResponse;
        }

        preservedInformation.informationMap.clear();

        for (HttpFilters filter : filters) {
            try {
                HttpResponse filterResponse = filter.clientToProxyRequest(httpObject, this.preservedInformation);
                if (filterResponse != null) {
                    // if we are short-circuiting the response to an HttpRequest, update ModifiedRequestAwareFilter instances
                    // with this (possibly) modified HttpRequest before returning the short-circuit response
                    if (httpObject instanceof HttpRequest) {
                        updateFiltersWithModifiedResponse((HttpRequest) httpObject);
                    }

                    return filterResponse;
                }
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        }

        // if this httpObject is the HTTP request, set the modified request object on all ModifiedRequestAwareFilter
        // instances, so they have access to all modifications the request filters made while filtering
        if (httpObject instanceof HttpRequest) {
            updateFiltersWithModifiedResponse((HttpRequest) httpObject);
        }

        return null;
    }

    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        for (HttpFilters filter : filters) {
            try {
                HttpResponse filterResponse = filter.proxyToServerRequest(httpObject);
                if (filterResponse != null) {
                    return filterResponse;
                }
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        }

        return null;
    }

    @Override
    public void proxyToServerRequestSending() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerRequestSending();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }


    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject, PreservedInformation preservedInformation) {
        HttpObject processedHttpObject = httpObject;

        preservedInformation.informationMap.clear();
        for (HttpFilters filter : filters) {
            try {
                processedHttpObject = filter.serverToProxyResponse(processedHttpObject, this.preservedInformation);
                if (processedHttpObject == null) {
                    return null;
                }
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        }

        return processedHttpObject;
    }

    @Override
    public void serverToProxyResponseTimedOut() {
        filters.forEach(filter -> {
            try {
                filter.serverToProxyResponseTimedOut();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void serverToProxyResponseReceiving() {
        filters.forEach(filter -> {
            try {
                filter.serverToProxyResponseReceiving();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
        InetSocketAddress overrideAddress = null;
        String newServerHostAndPort = resolvingServerHostAndPort;

        for (HttpFilters filter : filters) {
            try {
                InetSocketAddress filterResult = filter.proxyToServerResolutionStarted(newServerHostAndPort);
                if (filterResult != null) {
                    overrideAddress = filterResult;
                    newServerHostAndPort = filterResult.getHostString() + ":" + filterResult.getPort();
                }
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        }

        return overrideAddress;
    }

    @Override
    public void proxyToServerResolutionFailed(String hostAndPort) {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerResolutionFailed(hostAndPort);
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerResolutionSucceeded(serverHostAndPort, resolvedRemoteAddress);
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });

        super.proxyToServerResolutionSucceeded(serverHostAndPort, resolvedRemoteAddress);
    }

    @Override
    public void proxyToServerConnectionStarted() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerConnectionStarted();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void proxyToServerConnectionSSLHandshakeStarted() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerConnectionSSLHandshakeStarted();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void proxyToServerConnectionFailed() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerConnectionFailed();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerConnectionSucceeded(serverCtx);
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void proxyToServerRequestSent() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerRequestSent();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public void serverToProxyResponseReceived() {
        filters.forEach(filter -> {
            try {
                filter.serverToProxyResponseReceived();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject) {
        HttpObject processedHttpObject = httpObject;
        for (HttpFilters filter : filters) {
            try {
                processedHttpObject = filter.proxyToClientResponse(processedHttpObject);
                if (processedHttpObject == null) {
                    return null;
                }
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        }

        return processedHttpObject;
    }

    @Override
    public void proxyToServerConnectionQueued() {
        filters.forEach(filter -> {
            try {
                filter.proxyToServerConnectionQueued();
            } catch (RuntimeException e) {
                log.warn("Filter in filter chain threw exception. Filter method may have been aborted.", e);
            }
        });
    }

    /**
     * Updates {@link ModifiedRequestAwareFilter} filters with the final, modified request after all request filters have
     * processed the request.
     *
     * @param modifiedRequest the modified HttpRequest after all filters have finished processing it
     */
    private void updateFiltersWithModifiedResponse(HttpRequest modifiedRequest) {
        for (HttpFilters filter : filters) {
            if (filter instanceof ModifiedRequestAwareFilter) {
                ModifiedRequestAwareFilter requestCaptureFilter = (ModifiedRequestAwareFilter) filter;
                try {
                    requestCaptureFilter.setModifiedHttpRequest(modifiedRequest);
                } catch (RuntimeException e) {
                    log.warn("ModifiedRequestAwareFilter in filter chain threw exception while setting modified HTTP request.", e);
                }
            }
        }
    }
}
