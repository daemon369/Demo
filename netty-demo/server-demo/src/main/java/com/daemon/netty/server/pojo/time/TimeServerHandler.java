package com.daemon.netty.server.pojo.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by daemon on 15-2-2.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter{

    public TimeServerHandler() {
        System.out.println("TimeServerHandler constructor");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf in = (ByteBuf)msg;

        final ByteBuf temp = in.copy();

        try {
            while(temp.isReadable()) {
                System.out.print((char)temp.readByte());
                System.out.flush();
            }
        } finally {
            temp.release();
        }

        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        final ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        ctx.close();
    }
}
