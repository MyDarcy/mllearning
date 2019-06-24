package com.darcy.thrift;

import com.darcy.file.TFile;
import com.darcy.file.TMode;
import com.darcy.file.TStatus;
import org.apache.thrift.TException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 在 TFileServiceImpl 中 cmd + shift + t生成测试类;
 * 只需要导入JUnit即可;
 */
public class TFileServiceImplTest {

  TFileServiceImpl client;

  @Before
  public void setUp() throws Exception {
    client = new TFileServiceImpl();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void ls() {
    TFileServiceImpl demo = new TFileServiceImpl();
    List<TFile> tFileList = demo.ls("/Users/darcy");
    tFileList.stream().forEach(System.out::println);
  }

  @Test
  public void cat() throws TException {
    String result = client.cat("/Users/darcy/diff1.2-1.4.txt", TMode.TEXT);
    System.out.println(result);
    String content = client.cat("/Users/darcy/github.com.new.repo.png", TMode.BINARY);
    System.out.println(content);
  }

  @Test
  public void upload() throws TException {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    TStatus status = client.upload("test3.txt", byteBuffer.put("ByteBuffer byteBuffer = ByteBuffer.allocate(1024);333333".getBytes()));
    System.out.println(status);
  }

  @Test
  public void download() throws TException {
    ByteBuffer byteBuffer1 = client.download("/Users/darcy/Pictures");
    System.out.println(byteBuffer1.array().length);
    ByteBuffer byteBuffer2 = client.download("/Users/darcy/diff1.2-1.4.txt");
    byte[] array = byteBuffer2.array();
    System.out.println(array.length);
    System.out.println(new String(array, 0, array.length));
  }
}