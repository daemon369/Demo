package com.daemon.netty.client.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * Created by daemon on 15-2-10.
 */
public class HttpClient {
    private final String host;
    private final int port;

    public HttpClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        final EventLoopGroup worker = new NioEventLoopGroup();

        try {
            final Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpResponseDecoder());

                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler());
                }
            });

            final ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        final HttpClient client = new HttpClient("127.0.0.1", 7893);

        try {
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}