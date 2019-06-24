package com.darcy.file;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Client {

  public static void main(String[] args) throws TException {
    TTransport transport;
    transport = new TSocket("localhost", 9090);
    TProtocol protocol = new TBinaryProtocol(transport);
    Hello.Client client = new Hello.Client(protocol);
    // 建立连接
    transport.open();

    System.out.println("java: " + client.helloString("java"));
    transport.close();
  }

}
