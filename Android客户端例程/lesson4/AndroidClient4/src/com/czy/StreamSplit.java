package com.czy;
/*
 * 参考源码http://ftp.teamsourcing.com.tw/Share/MobileVms/src/iveda/com/e200/smarttv/MjpegView/StreamSplit.java
 * 功能：用于视频流的处理
 */
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Hashtable;
//DataInputStream     流分割
public class StreamSplit                             
{
  public static final String BOUNDARY_MARKER_PREFIX = "--";
  public static final String BOUNDARY_MARKER_TERM = "--";
  private DataInputStream m_dis;
  private boolean m_streamEnd;

  public StreamSplit(DataInputStream paramDataInputStream)
  {
    this.m_dis = paramDataInputStream;
    this.m_streamEnd = false;
  }

  public Hashtable readHeaders()
    throws IOException
  {
    Hashtable localHashtable = new Hashtable();  //保存键值
    String str1 = null;
    int i = 0;
    while (true)
    {
      str1 = this.m_dis.readLine();
      if (str1 == null)
      {
        this.m_streamEnd = true;
        break;
      }
      if (str1.equals(""))
      {
        if (i != 0)
          break;
      }
      else
        i = 1;
      int j = str1.indexOf(":"); //返回指定字符':'在此字符串中第一次出现处的索引
      if (j == -1)
        continue;
      String str2 = str1.substring(0, j);    //返回一个新字符串，它是此字符串的一个子字符串。该子字符串从指定的 beginIndex 处开始，直到索引 endIndex - 1 处的字符
      String str3 = str1.substring(j + 1).trim();  //删除字符串中多余的空格，但会在英文字符串中保留一个作为词与词之间分隔的空格
      localHashtable.put(str2.toLowerCase(), str3);
    }
    return localHashtable;
  }

  public Hashtable readHeaders(URLConnection paramURLConnection)
  {
    Hashtable localHashtable = new Hashtable();
    for (int i = 1; ; i++)
    {
      String str1 = paramURLConnection.getHeaderFieldKey(i);
      if (str1 == null)
        break;
      String str2 = paramURLConnection.getHeaderField(i);
      localHashtable.put(str1.toLowerCase(), str2);
    }
    return localHashtable;
  }

  public void skipToBoundary(String paramString)
    throws IOException
  {
    readToBoundary(paramString);
  }

  public byte[] readToBoundary(String paramString)
    throws IOException
  {
    ResizableByteArrayOutputStream localResizableByteArrayOutputStream = new ResizableByteArrayOutputStream(); //创建它的实例时，程序内部创建一个byte型别数组的缓冲区
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = 0;
    while (true)
    {
      int k;
      try
      {
        k = this.m_dis.readByte();
      }
      catch (EOFException localEOFException)
      {
        this.m_streamEnd = true;
        break;
      }
      if ((k == 10) || (k == 13))
      {
        String str1 = localStringBuffer.toString();
        if ((str1.startsWith("--")) && (str1.startsWith(paramString)))
        {
          String str2 = str1.substring(paramString.length());
          if (str2.equals("--"))
            this.m_streamEnd = true;
          j = i;
          break;
        }
        localStringBuffer = new StringBuffer();
        i = j + 1;
      }
      else
      {
        localStringBuffer.append((char)k);
      }
      j++;
      localResizableByteArrayOutputStream.write(k);  //将指定的字节写入此 byte 数组输出流
    }
    localResizableByteArrayOutputStream.close();
    localResizableByteArrayOutputStream.resize(j);
    return localResizableByteArrayOutputStream.toByteArray();
  }

  public boolean isAtStreamEnd()
  {
    return this.m_streamEnd;
  }
}

/* Location:           C:\Users\chen_zhiyun\Desktop\cambozola.jar
 * Qualified Name:     com.charliemouse.cambozola.shared.StreamSplit
 * JD-Core Version:    0.6.0
 */