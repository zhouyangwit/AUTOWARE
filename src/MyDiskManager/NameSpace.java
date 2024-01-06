package MyDiskManager;

import java.text.SimpleDateFormat;

public class NameSpace {


    String filename;
    String lastmodified;
    String recordtime;
    long length;
    long hashcode;
    String md5;
    String path;
    int rank;

    NameSpace()
    {
        resetData();
    }

    public void resetData()
    {
      filename="";
      lastmodified="";
      recordtime="";
      length=0;
      hashcode=0;
      md5="";
      path="";
      rank=0;
    }

}
