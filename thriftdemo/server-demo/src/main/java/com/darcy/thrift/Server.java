package com.darcy.thrift;

import com.darcy.file.Hello;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Server {
  /**
   * 启动 Thrift 服务器
   * @param args
   */
  public static void main(String[] args) {
    try {
//      // 设置服务端口为 7911
//      TServerSocket serverTransport = new TServerSocket(7911);
//      // 设置协议工厂为 TBinaryProtocol.Factory
//      Factory proFactory = new TBinaryProtocol.Factory();
//      // 关联处理器与 Hello 服务的实现
//      TProcessor processor = new Hello.Processor(new HelloServiceImpl());
//
//      TServer server = new TThreadPoolServer(processor, serverTransport, proFactory);
//      System.out.println("Start server on port 7911...");
//      server.serve();

      TServerTransport serverTransport = new TServerSocket(9090);
      TProcessor processor = new Hello.Processor(new HelloServiceImpl());
      TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
      System.out.println("Starting the simple server......");
      server.serve();

//      TServerSocket serverTransport = new TServerSocket(7911);
//      TProcessor processor = new Hello.Processor(new HelloServiceImpl());
//      TServer server = new TSimpleServer(processor, serverTransport);
//      System.out.println("Start server on port 7911...");
//      server.serve();

    } catch (TTransportException e) {
      e.printStackTrace();
    }
  }
}
