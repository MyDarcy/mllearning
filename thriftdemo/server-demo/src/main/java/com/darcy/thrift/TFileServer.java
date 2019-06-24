package com.darcy.thrift;


import com.darcy.file.TFileService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class TFileServer {

  public static void main(String[] args) throws TTransportException {
    TServerTransport serverTransport = new TServerSocket(9099);
    TProcessor processor = new TFileService.Processor<>(new TFileServiceImpl());
    TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
    System.out.println("Starting the simple server......");
    server.serve();
  }

}
