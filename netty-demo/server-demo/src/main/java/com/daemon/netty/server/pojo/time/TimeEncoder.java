package com.daemon.netty.server.pojo.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by daemon on 15-2-2.
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        final UnixTime t = (UnixTime)msg;
        final ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int) t.getTime());
        ctx.write(encoded, promise);
    }
}
