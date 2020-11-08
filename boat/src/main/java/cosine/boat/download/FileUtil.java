package cosine.boat.download;

import java.util.zip.ZipFile;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.nio.charset.*;
public class FileUtil
{
    public static boolean dirCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        for (File s : src.listFiles()) {
            if (s.isFile()) {
                fileCopy(s.getPath(), destPath + File.separator + s.getName());
            } else {
                dirCopy(s.getPath(), destPath + File.separator + s.getName());
            }
        }
        return true;
    }

    public static boolean fileCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        try
        {
            InputStream is = new BufferedInputStream(new FileInputStream(src));
            OutputStream out =new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                out.write(flush, 0, len);
            }
            out.flush();
            out.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 删除文件或文件夹以及文件夹下所有文件
    public static boolean delDir(String dirPath) {
        try{
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                return false;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return true;
            }
            File[] files = dirFile.listFiles();
            if(files==null){
                return false;
            }
            for (int i = 0; i < files.length; i++) {
                delDir(files[i].toString());
            }
            dirFile.delete();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    //第一个参数就是需要解压的文件，第二个就是解压的目录
    public static boolean unZip(String zipFile, String folderPath) {
        ZipFile zfile= null;
        try {
            //转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            //列举的压缩文件里面的各个文件，判断是否为目录
            if(ze.isDirectory()){
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os= null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is= null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int readLen=0;
            //进行一些内容复制操作
            try {
                while ((readLen=is.read(buf, 0, 1024))!=-1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;

        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                substr = dirs[i];
                ret=new File(ret, substr);
            }

            if(!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length-1];
            ret=new File(ret, substr);
            return ret;
        }else{
            ret = new File(ret,absFileName);
        }
        return ret;
    }

}
