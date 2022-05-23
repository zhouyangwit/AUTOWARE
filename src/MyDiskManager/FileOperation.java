package MyDiskManager;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class FileOperation {

    public static void main(String[] args) {
        new FileOperation();
    }

    DatabaseOperation database = new DatabaseOperation();
    boolean isDeleteFile=false;
    FileOperation()
    {
        if(isDeleteFile)
        {
            JOptionPane.showMessageDialog(null,"未开启保护模式，文件md5值重复则直接删除文件，请谨慎操作！！！");
        }else{
            JOptionPane.showMessageDialog(null,"已开启文件保护模式，本次操作不会执行文件删除操作~~~");
        }
        File startFile=selectFolder();
        getTotalFileNum(startFile);
        isDirectory(startFile);
        database.closeDatabase();
    }


    int TotalFileNum=0;
    int currentFileNo=0;
    int successFileNum=0;
    int deleteFileNum=0;
    int lastRecordNum=0;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    NameSpace ns=new NameSpace();

    public void getTotalFileNum(File file)
    {
        File[] fl=file.listFiles();
        for(int i=0;i<fl.length;i++)
        {
            if(fl[i].isFile())
            {
                TotalFileNum++;
            }else{
                getTotalFileNum(fl[i]);
            }
        }
    }


    public void isDirectory(File file)
    {
        File[] fl=file.listFiles();
        for(int i=0;i<fl.length;i++)
        {
            if(fl[i].isDirectory())
            {
                isDirectory(fl[i]);
            }
            else if(fl[i].isFile())
            {
                currentFileNo++;
                if(currentFileNo>=lastRecordNum)
                {
                    isFile(fl[i],isDeleteFile);
                }
            }
        }
    }

    public void isFile(File file,boolean isDeleteFile)
    {
        ns.filename=file.getName().replace("'","’").replace("\""," ");
        ns.lastmodified=sdf.format(file.lastModified());
        ns.length=file.length();
        ns.path=(file.getAbsolutePath()).replace("\\","\\\\").replace("'","’").replace("\"","");
        ns.hashcode=file.hashCode();
        ns.md5=new FileInformation().getMD5(file);
        ns.rank=0;
        if(!database.queryMD5(ns.md5))
        {
            if(database.insert(ns)){
                successFileNum++;
                System.out.println("[T:"+TotalFileNum+"][C:"+currentFileNo+"][R:"+successFileNum+"][D:"+deleteFileNum+"]\t"+ns.path+"\t文件数据录入成功~~");
            }
            else{
                System.out.println(ns.path+"\t文件数据录入失败！");
            }
        }
        else{
            if(isDeleteFile)
            {
                deleteFileNum++;
                System.out.println("[T:"+TotalFileNum+"][C:"+currentFileNo+"][R:"+successFileNum+"][D:"+deleteFileNum+"]\t"+ns.path+"\t文件数据重复，执行删除操作！");
                file.delete();
            }else
            {
                System.out.println("[T:"+TotalFileNum+"][C:"+currentFileNo+"][R:"+successFileNum+"][D:"+deleteFileNum+"]\t"+ns.path+"\t文件数据重复，由于开启保护模式，未执行删除操作！");
            }
            database.updateRank(ns.md5);

        }
        ns.resetData();
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

    public File selectFile()
    {
        File file = null;
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "open");
        file = jfc.getSelectedFile();
        return file;
    }

}
