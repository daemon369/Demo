package com.daemon.netty.client.pojo.time;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by daemon on 15-2-2.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    public TimeClientHandler() {
        System.out.println("TimeClientHandler constructor");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final UnixTime t = (UnixTime)msg;
        System.out.println("current time of server is : " + t);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
