package MyDiskManager;

public class ImageKeyword {
    String md5=null;
    String Image_Height=null;
    String Image_Width=null;
    String make=null;
    String model=null;
    String DateTime=null;
    String GPS_Latitude=null;
    String GPS_Longitude=null;
    String GPS_Altitude=null;
    public void resetData()
    {
        md5="";
        Image_Height="";
        Image_Width="";
        make="";
        model="";
        DateTime="";
        GPS_Latitude="";
        GPS_Longitude="";
        GPS_Altitude="";
    }

    ImageKeyword()
    {
        resetData();
    }

    public void show()
    {
        System.out.println("扫描文件数据如下：\n" +
                "尺寸：" +Image_Height+" X "+Image_Width+
                "\n设备供货商："+make+"\t" +
                "\n详细型号："+model+
                "\n项目日期："+DateTime+
                "\n经纬度信息："+GPS_Longitude+"\t"+GPS_Latitude+"\t"+GPS_Altitude);
    }
}
