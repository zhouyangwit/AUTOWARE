package MyDiskManager;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileInformation {

    public String getSHA1(File f)
    {
        String sha="";
        try{
            sha=getHash(f,"SHA-1");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get SHA-1 values failer");
        }
        return sha;
    }

    public String getSHA256(File f)
    {
        String sha="";
        try{
            sha=getHash(f,"SHA-256");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get SHA-256 values failer");
        }
        return sha;
    }

    public String getSHA384(File f)
    {
        String sha="";
        try{
            sha=getHash(f,"SHA-384");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get SHA-384 values failer");
        }
        return sha;
    }

    public String getSHA512(File f)
    {
        String sha256="";
        try{
            sha256=getHash(f,"SHA-512");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get SHA-256 values failer");
        }
        return sha256;
    }

    public String getMD2(File f)
    {
        String md="";
        try{
            md=getHash(f,"MD2");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get MD5 values failer");
        }

        return md;
    }

    public String getMD5(File f)
    {
        String md="";
        try{
            md=getHash(f,"MD5");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("get MD5 values failer");
        }

        return md;
    }

    private String getHash(File file, String hashType)
    {
        InputStream fis=null;
        byte buffer[]=new byte[1024];
        MessageDigest md5=null;
        try {
            fis=new FileInputStream(file);
            md5=MessageDigest.getInstance(hashType);
            for(int numRead=0;(numRead=fis.read(buffer))>0;)
            {
                md5.update(buffer,0,numRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Close file Error");
        }
        return toHexString(md5.digest());
    }

    private String toHexString(byte b[])
    {
        StringBuilder sb=new StringBuilder();
        String temp=null;
        for(byte aB:b)
        {
            temp=Integer.toHexString(aB & 0xFF);
            if(temp.length()==1){
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString().toLowerCase();
    }



}
