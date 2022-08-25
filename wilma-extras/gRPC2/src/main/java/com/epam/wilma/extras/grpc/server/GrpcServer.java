package com.epam.wilma.extras.grpc.server;

import com.epam.wilma.extras.grpc.GRPCServiceCore;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static io.grpc.ServerInterceptors.intercept;
import static java.util.stream.Collectors.joining;

@Service
public class GrpcServer {
    private final Logger logger = LoggerFactory.getLogger(GrpcServer.class);
    private static GrpcServer self;

    private GrpcProperties grpcProperties;
    private List<BindableService> services;
    private Server server;

    @Autowired
    private CodecRegistry codecRegistry;

    private CountDownLatch latch;
    private boolean isStarted = false;

    public GrpcServer() {
        GrpcServer.self = this;
    }

    public void configureGrpcServer(GrpcProperties grpcProperties, List<BindableService> services) {
        this.grpcProperties = grpcProperties;
        this.services = services;
        this.latch = new CountDownLatch(1);
    }

    public static GrpcServer getInstance() {
        return self;
    }

    public void start() throws IOException {
        if (isStarted) { //avoid double start
            return;
        }
        isStarted = true;
        NettyServerBuilder builder = NettyServerBuilder.forPort(grpcProperties.getServer().getPort())
                .intercept(new ExceptionHandler(grpcProperties.getErrorCodeBy()))
                .compressorRegistry(codecRegistry.compressorRegistry())
                .decompressorRegistry(codecRegistry.decompressorRegistry())
                .addService(ProtoReflectionService.newInstance());

        setProperties(builder);
        services.forEach(s -> builder.addService(intercept(s, new HeaderPropagationInterceptor())));
        server = builder.build().start();
        logger.info(summary(server));
        startDaemonAwaitThread();
    }

    private void setProperties(NettyServerBuilder builder) {
        GrpcProperties.ServerProperties server = grpcProperties.getServer();
        if (server.getMaxHeaderListSize() != null) {
            int val = Math.toIntExact(server.getMaxHeaderListSize());
            logger.info("Set maxHeaderListSize = {}", val);
            builder.maxHeaderListSize(val);
        }
        if (server.getMaxMessageSize() != null) {
            int val = Math.toIntExact(server.getMaxMessageSize());
            logger.info("Set maxMessageSize = {}", val);
            builder.maxMessageSize(val);
        }
        if (server.getMaxInboundMetadataSize() != null) {
            int val = Math.toIntExact(server.getMaxInboundMetadataSize());
            logger.info("Set maxInboundMetadataSize = {}", val);
            builder.maxInboundMetadataSize(val);
        }
        if (server.getMaxInboundMessageSize() != null) {
            int val = Math.toIntExact(server.getMaxInboundMessageSize());
            logger.info("Set maxInboundMessageSize = {}", val);
            builder.maxInboundMessageSize(val);
        }
    }

    private String summary(Server server) {
        return "gRPC server is started: " + server + "\nRegistered services:\n" +
                server.getServices().stream().map(s -> " * " + s.getServiceDescriptor().getName()).collect(joining("\n"));
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("gRPC server awaiter interrupted.", e);
            }
        });
        awaitThread.setName("grpc-server-awaiter");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @PreDestroy
    public void destroy() {
        Optional.ofNullable(server).ifPresent(s -> {
            logger.info("Shutting down gRPC server ...");
            s.shutdown();
            try {
                s.awaitTermination();
            } catch (InterruptedException e) {
                logger.error("gRPC server interrupted during destroy.", e);
            } finally {
                latch.countDown();
            }
            logger.info("gRPC server stopped.");
        });
    }
}

