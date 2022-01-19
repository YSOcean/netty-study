package com.itcoke.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class SelectorTest {

    public static void createSelector() throws IOException {
        Selector open = Selector.open();
    }

    public static void socketChannel() {
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            channel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_READ);

        } catch (IOException e) {
        }
    }
}
