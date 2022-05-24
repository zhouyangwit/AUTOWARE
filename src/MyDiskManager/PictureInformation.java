package MyDiskManager;

import java.io.File;

//本程序主要是解析JPG文件的EXIF信息，获得文件的尺寸、GPS、拍摄时间、拍摄设备信息
public class PictureInformation {
    PictureInformation()
    {
        getEXIF(new File(""));
    }

    /*TODO 找相关的jar包，来获取EXIF信息，将信息封装到一个二维数组中，再构造一个函数来解析这个二维数组
     */
    public void getEXIF(File image)
    {

    }
}
