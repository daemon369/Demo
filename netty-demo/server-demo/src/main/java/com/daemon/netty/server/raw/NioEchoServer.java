package com.daemon.netty.server.raw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by daemon on 15-2-13.
 */
public class NioEchoServer {
    private final int port;

    public NioEchoServer(final int port) {
        this.port = port;
    }

    public void run() throws Exception {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        final ServerSocket serverSocket = serverChannel.socket();
        final InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        serverChannel.configureBlocking(false);
        final Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        try {
            while (true) {
                try {
                    selector.select();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                final Set<SelectionKey> readyKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();
                    try {
                        if(key.isAcceptable()) {
//                            System.out.println("is acceptable");
                            final ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            System.out.println("Accepted connected form : " + client);
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, ByteBuffer.allocate(100));
                        }

                        if(key.isReadable()) {
//                            System.out.println("is readable");
                            final SocketChannel client = (SocketChannel) key.channel();
                            final ByteBuffer output = (ByteBuffer) key.attachment();
                            client.read(output);
                        }

                        if(key.isWritable()) {
//                            System.out.println("is writable");
                            final SocketChannel client = (SocketChannel) key.channel();
                            final ByteBuffer output = (ByteBuffer) key.attachment();
                            output.flip();
                            client.write(output);
                            output.compact();
                        }
                    } catch (IOException e) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("main");

        try {
            new NioEchoServer(7895).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
