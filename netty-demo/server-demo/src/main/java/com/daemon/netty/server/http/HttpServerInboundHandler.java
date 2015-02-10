package com.daemon.netty.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * Created by daemon on 15-2-8.
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;

    public HttpServerInboundHandler() {
        super();
        System.out.println("HttpServerInboundHandler()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");

        if(msg instanceof HttpRequest) {
            System.out.println("channelRead HttpRequest");
            final HttpRequest request = (HttpRequest) msg;

            System.out.println("messageType:" + request.headers().get("messageType"));
            System.out.println("businessType:" + request.headers().get("businessType"));

            if(HttpHeaders.isContentLengthSet(request)) {
                buf = ctx.alloc().buffer((int) HttpHeaders.getContentLength(request));
            }
        }

        if(msg instanceof HttpContent) {
            System.out.println("channelRead HttpContent");
            final HttpContent httpContent = (HttpContent) msg;
            final ByteBuf content = httpContent.content();

            if(null == buf) {
                System.out.println("error! content length is not set");
            }

            buf.writeBytes(content);
            content.release();

            if(buf.writableBytes() == 0) {
                final byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                final String contentStr = new String(data);
                System.out.println("client said:" + contentStr);

                final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.MULTI_STATUS.OK,
                        Unpooled.wrappedBuffer("Message received".getBytes()));
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);

                ctx.close();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.flush();
    }
}
