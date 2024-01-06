package MyDiskManager;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
public class FileOperation {
    private DatabaseOperation database = new DatabaseOperation();   //建立数据库连接
    private boolean isDeleteFile = true;    //文件保护模式，为true时，会删除重复文件，为false时，只记录扫面文件信息，不执行删除操作
    private int lastRecordNum = 0;  //当程序执行被打断时，将上一次执行的最终[currentFileNo-deleteFileNum]差值赋值给他即可继续扫描
    private String defaltScanFolder="D:\\UserData\\Downloads\\ZIG";   //默认要扫描的目录

    private int TotalFileNum = 0;   //用于统计系统需要扫描的文件总数，初始值为0，不要修改
    private int currentFileNo = 0;  //用于统计系统扫描进度的数据，初始值为0，不要修改
    private int successFileNum = 0; //用于统计系统扫描成功并录入的数量，初始值为0，不要修改
    private int deleteFileNum = 0;  //用于统计系统删除文件的数量，初始值为0，不要修改
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //用于标准化时间格式
    private NameSpace ns = new NameSpace();

    public static void main(String[] args) {
        new FileOperation();
    }

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
                if(currentFileNo>lastRecordNum)
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
        ns.recordtime=sdf.format(new Date());
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
        String defaultPath = defaltScanFolder;    //这里为默认扫描的文件地址
        jfc.setCurrentDirectory(new File(defaultPath));
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
