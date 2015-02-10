package com.daemon.netty.client.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Created by daemon on 15-2-10.
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        final URI uri = new URI("http://127.0.0.1:7893");
        final String msg = "Are you kidding me?";

        final Channel channel = ctx.channel();
        final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        final String hostName = address.getHostName();
        final int port = address.getPort();

        final URI uri = new URI(hostName + ":" + port);

        final DefaultFullHttpRequest r = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes()));

        r.headers().set(HttpHeaders.Names.HOST, address.getHostName());
        r.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        r.headers().set(HttpHeaders.Names.CONTENT_LENGTH, r.content().readableBytes());
        r.headers().set("messageType", "msg_type_1");
        r.headers().set("businessType", "msg_type_1");

        ctx.channel().write(r);
        ctx.channel().flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse) msg;

            System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));

            if(HttpHeaders.isContentLengthSet(response)) {
                buf = ctx.alloc().buffer((int) HttpHeaders.getContentLength(response));
            }
        }

        if(msg instanceof HttpContent) {
            final HttpContent httpContent = (HttpContent) msg;
            final ByteBuf content = httpContent.content();

            if(null == buf) {
                System.out.println("error! content length is not set");
                return;
            }

            buf.writeBytes(content);
            content.release();

            if(0 == buf.writableBytes()) {
                final byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                final String contentStr = new String(data);
                System.out.println("Server said:" + contentStr);
                ctx.close();
            }
        }
    }
}
