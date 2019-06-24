package com.darcy.thrift;

import com.darcy.file.Hello;

public class HelloServiceImpl implements Hello.Iface {
  @Override
  public String helloString(String para) throws org.apache.thrift.TException {
    return "hello," + para;
  }

  @Override
  public int helloInt(int para) throws org.apache.thrift.TException {
    return para;
  }

  @Override
  public boolean helloBoolean(boolean para) throws org.apache.thrift.TException {
    return para;
  }

  @Override
  public void helloVoid() throws org.apache.thrift.TException {
    System.out.println("helloVoid.");
  }

  @Override
  public String helloNull() throws org.apache.thrift.TException {
    System.out.println("helloNull.");
    return "hello from helloNull";
  }
}
