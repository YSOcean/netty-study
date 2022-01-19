package com.itcoke.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    /**
     * 获取 FileChannel
     *
     * @throws Exception
     */
    public void getFileChannel() throws Exception {
        FileChannel channelRead = new FileInputStream("data.txt").getChannel();
        FileChannel channelWrite = new FileOutputStream("data.txt").getChannel();
        FileChannel channelRandom = new RandomAccessFile("data.txt", "rw").getChannel();
    }

    /**
     * 从 Channel 中读取数据
     */
    public static void readFileChannel() {
        try (FileChannel fileChannel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(5);
            // 从 channel 读取数据填充 ByteBuffer，返回值表示读到了多少字节，-1 表示到达了文件的末尾
            int read = fileChannel.read(buffer);
            while (read != -1) {
                System.out.println("读取的字节数为：" + read);
                // 切换到读模式
                buffer.flip();
                fileChannel.position(4);
                // 打印读取到 buffer 中的内容
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                System.out.println();
                // 重置到写模式
                buffer.clear();
                read = fileChannel.read(buffer);
            }
        } catch (IOException e) {
        }
    }

    /**
     * 将数据写入 Channel
     */
    public static void writeFileChannel() {
        try (FileChannel channel = new FileOutputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(5);
            // 重置写模式，默认刚创建的 buffer 就是写模式
            buffer.clear();
            buffer.put("12345".getBytes());
            // 切换到读模式
            buffer.flip();
            // 注意FileChannel.write()是在while循环中调用的。
            // 因为无法保证write()方法一次能向FileChannel写入多少字节，因此需要重复调用write()方法，
            // 直到Buffer中已经没有尚未写入通道的字节。
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            System.out.println(channel.size());
        } catch (IOException e) {
        }
    }


    /**
     * 两个 Channel 之间传输数据
     * 文件复制
     */
    public static void transferToChannel() {
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel()
        ) {
            // 底层使用零拷贝，效率比较高
            long size = from.size();
            // left 变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        transferToChannel();
    }
}
