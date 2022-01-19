package com.itcoke.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {
    /**
     * 读取 data.txt 文件中的内容
     */
    public static void testReadBuffer(){
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 从 channel 中读取数据，并写入 buffer,返回读取的实际字节数，等于 -1 时表示读取完毕
            //int read = channel.read(buffer);
            while(channel.read(buffer) != -1){
                // 将 buffer 切换至 读模式
                buffer.flip();
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    System.out.print((char)b);
                }
                // 将 buffer 切换至 写模式
                buffer.clear();
            }

        } catch (IOException e) {

        }
    }

    /**
     * 将文本写入到文件 write.txt 中
     */
    public static void testWriteBuffer(){
        try (RandomAccessFile file = new RandomAccessFile("write.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String str = "abcdefg你好";
            buffer.put(str.getBytes());
            buffer.flip();
            channel.write(buffer);

        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        testWriteBuffer();
        testReadBuffer();
    }

}
