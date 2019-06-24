package com.darcy.thrift;

import com.darcy.file.TFile;
import com.darcy.file.TFileService;
import com.darcy.file.TMode;
import com.darcy.file.TStatus;
import org.apache.thrift.TException;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TFileServiceImpl implements TFileService.Iface {

  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
  private final static String fileBase = "/Users/darcy/mycode/mllearning/thriftdemo/server-demo/docs";


  /**
   * ls命令
   * @param path
   * @return
   */
  @Override
  public List<TFile> ls(String path) {
    List<TFile> tFileList = new ArrayList<>();
    try {
      File[] files = new File(path).listFiles();
      tFileList = Arrays.stream(files).map(file -> {
        long size = file.length();
        String filePath = file.getPath();
        String fileName = file.getName();
        String mime = null;
        if (file.isFile()) {
          mime = getFileMime(file);
        }
        TMode mode = null;
        if (file.isFile() && mime != null) {
          if (mime.startsWith("text")) {
            mode = TMode.TEXT;
          } else {
            mode = TMode.BINARY;
          }
        }
        TFile tFile = new TFile();
        tFile.setPath(filePath);
        tFile.setName(fileName);
        tFile.setMime(mime);
        tFile.setSize(size);
        tFile.setMode(mode);
        return tFile;
      }).collect(Collectors.toList());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tFileList;
  }

  /**
   * cat命令， 文本文件展示其内容，二进制文件展示其16进制编码内容；
   * @param path
   * @param mode
   * @return
   * @throws TException
   */
  @Override
  public String cat(String path, TMode mode) throws TException {
    String content = null;
    try {
      if (mode == TMode.TEXT) {
        List<String> list = Files.readAllLines(Paths.get(new File(path).toURI()), Charset.forName("utf-8"));
        if (list != null) {
          content = list.stream().collect(Collectors.joining("\n"));
        }
      } else {
        // Base64编码，不是16进制编码
        byte[] bytes = Files.readAllBytes(Paths.get(new File(path).toURI()));
//        content = Base64.encodeBase64String(bytes);

//        content = Hex.encodeHexString(bytes);
//        System.out.println(content);
        content = bytesToHex(bytes);
      }

    } catch (Exception e) {
      content = null;
      e.printStackTrace();
    }
    return content;
  }


  /**
   * 文件上传
   * @param name
   * @param data
   * @return
   * @throws TException
   */
  @Override
  public TStatus upload(String name, ByteBuffer data) throws TException {
    TStatus status;
    try {
      File file = new File(fileBase + "/" + name);
      if (!file.exists()) {
        file.createNewFile();
      }
      Files.write(Paths.get(file.toURI()), data.array(), StandardOpenOption.TRUNCATE_EXISTING);
      status = TStatus.SUCCESS;
    } catch (IOException e) {
      e.printStackTrace();
      status = TStatus.FAILED;
    }
    return status;
  }

  /**
   *
   * @param path
   * @return
   * @throws TException
   */
  @Override
  public ByteBuffer download(String path) throws TException {
    ByteBuffer byteBuffer = null;
    try {
      File file = new File(path);
      if (!file.exists() || file.isDirectory()) {
        byteBuffer = ByteBuffer.allocate(0);
        return byteBuffer;
      }
      if (file.length() > Integer.MAX_VALUE) {
        byteBuffer = ByteBuffer.allocate(0);
        return byteBuffer;
      }
      byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
      byteBuffer = ByteBuffer.allocate((int) file.length());
      byteBuffer.put(bytes);
      // 默认创建的bytebuffer只能读; flip可以转变读写模式；后续发送到网路上，应该是读模式了；
      byteBuffer.flip();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return byteBuffer;
  }


  /**
   * 文件文件的mime类型
   * <p>
   * 实现参考：https://www.baeldung.com/java-file-mime-type
   *
   * @param file
   * @return
   */
  private String getFileMime(File file) {
//    try {
//      MagicMatch magicMatch = Magic.getMagicMatch(file, false);
//      return magicMatch.getMimeType();
//    } catch (MagicException e) {
//      e.printStackTrace();
//    } catch (MagicParseException e) {
//      e.printStackTrace();
//    } catch (MagicMatchNotFoundException e) {
//      e.printStackTrace();
//    }
//    return null;

    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
    return mimeType;
  }

  /**
   * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
   *
   * @param bytes
   * @return
   */
  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static void main(String[] args) throws IOException, TException {
    TFileServiceImpl demo = new TFileServiceImpl();
    List<TFile> tFileList = demo.ls("/Users/darcy");
    tFileList.stream().forEach(System.out::println);

//    List<String> list = Files.readAllLines(Paths.get(new File("/Users/darcy/diff1.2-1.4.txt").toURI()), Charset.forName("utf-8"));
//    list.forEach(System.out::println);

    String result = demo.cat("/Users/darcy/diff1.2-1.4.txt", TMode.TEXT);
    System.out.println(result);
    String content = demo.cat("/Users/darcy/github.com.new.repo.png", TMode.BINARY);
    System.out.println(content);

    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    demo.upload("test.txt", byteBuffer.put("ByteBuffer byteBuffer = ByteBuffer.allocate(1024);".getBytes()));

    ByteBuffer byteBuffer1 = demo.download("/Users/darcy/Pictures");
    System.out.println(byteBuffer1.array().length);
    ByteBuffer byteBuffer2 = demo.download("/Users/darcy/diff1.2-1.4.txt");
    byte[] array = byteBuffer2.array();
    System.out.println(array.length);
    System.out.println(new String(array, 0, array.length));
  }

}
