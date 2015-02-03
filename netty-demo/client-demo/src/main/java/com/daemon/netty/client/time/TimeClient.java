package com.daemon.netty.client.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by daemon on 15-2-2.
 */
public class TimeClient {
    public static void main(final String[] args) {
        if(2 != args.length) {
            System.out.println("Usage : TimeClient host port");
            return;
        }

        final String host = args[0];
        int port;

        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("port should be a integer");
            return;
        }

        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());
//                    ch.pipeline().addLast(new TimeClientHandler2());
//                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler2());
                    ch.pipeline().addLast(new TimeDecoder2(), new TimeClientHandler2());
                }
            });

            final ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
