package com.darcy.file;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.nio.ByteBuffer;
import java.util.List;

public class TFileClient {

  public static void main(String[] args) throws TException, InterruptedException {
    TTransport transport;
    transport = new TSocket("localhost", 9099);
    TProtocol protocol = new TBinaryProtocol(transport);
    TFileService.Client client = new TFileService.Client(protocol);
    // 建立连接
    transport.open();

    List<TFile> files = client.ls("/Users/darcy");
    files.stream().forEach(System.out::println);

    Thread.sleep(1000);

    String result = client.cat("/Users/darcy/diff1.2-1.4.txt", TMode.TEXT);
    System.out.println(result);
    String content = client.cat("/Users/darcy/github.com.new.repo.png", TMode.BINARY);
    System.out.println(content);

    Thread.sleep(1000);

    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    TStatus status = client.upload("test2.txt", byteBuffer.put("System.out.println(result);\nByteBuffer byteBuffer = ByteBuffer.allocate(1024);".getBytes()));
    System.out.println(status);

    Thread.sleep(1000);

    ByteBuffer byteBuffer1 = client.download("/Users/darcy/Pictures");
    System.out.println(byteBuffer1.array().length);
    ByteBuffer byteBuffer2 = client.download("/Users/darcy/diff1.2-1.4.txt");
    byte[] array = byteBuffer2.array();
    System.out.println(array.length);
    System.out.println(new String(array, 0, array.length));
    transport.close();
  }

}
