package MyDiskManager;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

//本程序主要是解析JPG文件的EXIF信息，获得文件的尺寸、GPS、拍摄时间、拍摄设备信息
public class PictureInformation {
    public static void main(String[] args) {
        new PictureInformation();
    }

    PictureInformation()
    {
        File directory=selectFolder();
        isDirectory(directory);
    }



    /*TODO 找相关的jar包，来获取EXIF信息，将信息封装到一个二维数组中，再构造一个函数来解析这个二维数组
     */
    public void isDirectory(File directory)
    {
        File[] fl=directory.listFiles();
        for(int i=0;i<fl.length;i++)
        {
            if(fl[i].isFile())
            {
                isFile(fl[i]);
            }
            else if(fl[i].isDirectory())
            {
                isDirectory(fl[i]);
            }
        }
    }

    FileInformation fi=new FileInformation();
    DatabaseOperation database=new DatabaseOperation();
    public void isFile(File file)
    {
        if(file.getName().endsWith(".jpg"))
        {
            getEXIF(file);
            ik.md5=fi.getMD5(file);
            if(database.addEXIF(ik))
            {
                System.out.println(file.getName()+"\t数据录入成功~~~");
            }
            else
            {
                System.out.println(file.getName()+"数据录入出错！！！");
            }
        }
        else if(file.getName().endsWith(".jpeg"))
        {
            getEXIF(file);
            if(database.addEXIF(ik))
            {
                System.out.println(file.getName()+"\t数据录入成功~~~");
            }
            else
            {
                System.out.println(file.getName()+"数据录入出错！！！");
            }
        }
    }

    public void getEXIF(File image)
    {
        try {
            Metadata metadata= ImageMetadataReader.readMetadata(image);
            printMeta(metadata);
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void getFullEXIF(File image)
    {
        try {
            Metadata metadata=ImageMetadataReader.readMetadata(image);
            print(metadata);
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ImageKeyword ik=new ImageKeyword();
    public void printMeta(Metadata metadata)
    {
       ik.resetData();
        for(Directory directory: metadata.getDirectories())
        {
            for(Tag tag: directory.getTags())
            {
                if(tag.getTagName().equals("Image Height"))
                {
                    ik.Image_Height=tag.getDescription();
                }
                else if(tag.getTagName().equals("Image Width"))
                {
                    ik.Image_Width=tag.getDescription();
                }
                else if(tag.getTagName().equals("Model"))
                {
                    ik.model=tag.getDescription();
                }
                else if(tag.getTagName().equals("Make"))
                {
                    ik.make=tag.getDescription();
                }
                else if(tag.getTagName().equals("Date/Time"))
                {
                    ik.DateTime=tag.getDescription();
                }
                else if(tag.getTagName().equals("GPS Latitude"))
                {
                    ik.GPS_Latitude=tag.getDescription();
                    ik.GPS_Latitude=ik.GPS_Latitude.replace("\"","“").replace("\'","‘");
                }
                else if(tag.getTagName().equals("GPS Longitude"))
                {
                    ik.GPS_Longitude=tag.getDescription();
                    ik.GPS_Longitude=ik.GPS_Longitude.replace("\"","“").replace("\'","‘");
                }
                else if(tag.getTagName().equals("GPS Altitude"))
                {
                    ik.GPS_Altitude=tag.getDescription();
                }

            }
            if(directory.hasErrors())
            {
                for(String error:directory.getErrors())
                {
                    System.err.println("ERROR : "+error);
                }
            }
        }
    //    ik.show();
    }

    public void print(Metadata metadata)
    {
        for(Directory directory: metadata.getDirectories())
        {
            for(Tag tag:directory.getTags())
            {
                System.out.print(tag.getTagName()+"--->");
                System.out.println(tag.getDescription());
            }
            if(directory.hasErrors())
            {
                for(String error:directory.getErrors())
                {
                    System.err.println("ERROR : "+error);
                }
            }
        }
    }

    public File selectFolder()
    {
        File file=null;
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(new JLabel(), "choose");
        file=jfc.getSelectedFile();
        return file;
    }

}
