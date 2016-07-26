package com.czy;
//来自于http://ftp.teamsourcing.com.cn/Share/MobileVms/src/iveda/com/e200/smarttv/MjpegView/ResizableByteArrayOutputStream.java
import java.io.ByteArrayOutputStream;

class ResizableByteArrayOutputStream extends ByteArrayOutputStream
{
  public void resize(int paramInt)
  {
    this.count = paramInt;
  }
}

/* Location:           C:\Users\chen_zhiyun\Desktop\cambozola.jar
 * Qualified Name:     com.charliemouse.cambozola.shared.ResizableByteArrayOutputStream
 * JD-Core Version:    0.6.0
 */