package com.itcoke.blocking;

import com.itcoke.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 nio 来理解阻塞模式
 */
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        // 1、创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 2、绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3、连接集合
        List<SocketChannel> channels = new ArrayList<>();
        log.debug("12345");
        while(true){
            // 4、accept 建立与客户端的链接
            log.info("建立连接。。。。");
            SocketChannel accept = ssc.accept();
            log.info("链接成功。。。。");
            channels.add(accept);
            // 5、接收客户端的数据
            for(SocketChannel sc : channels){
                log.info("读取数据开始");
                sc.read(buffer);
                buffer.flip();
                ByteBufferUtil.debugRead(buffer);
                buffer.clear();
                log.info("读取数据结束");
            }
        }

    }

    public static void readBufferByChannel(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 从 channel 读取数据填充 ByteBuffer，返回值表示读到了多少字节，-1 表示到达了文件的末尾
        int read = channel.read(buffer);
        while (read != -1) {
            System.out.println(channel.getLocalAddress()+"读取的字节数为：" + read);
            // 切换到读模式
            buffer.flip();
            // 打印读取到 buffer 中的内容
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println();
            // 重置到写模式
            buffer.clear();
            read = channel.read(buffer);
            System.out.println(111);
        }
        log.info("读取结束endend");
    }
}
