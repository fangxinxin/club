package admin.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by soholife on 2015/10/4.
 */
 public final class IOUtils
{
    /**
     * 写入内容到文件中，默认是append模式
     * @param filePath
     * @param fileName
     * @param content
     * @return
     */
    public static boolean AppendFile(String filePath,String fileName,String content)
    {
        return  AppendFile(  filePath,  fileName,  content,true);
    }

    /**
     * 写入内容到文件中
     * @param filePath
     * @param fileName
     * @param content
     * @return
     */
    public static boolean AppendFile(String filePath,String fileName,String content,boolean isAppend)
    {
        String file = filePath + fileName;
        //System.out.println(file);

        boolean value = false;
        FileWriter w = null;
        try {
            w = new FileWriter(file,isAppend);
            //w.write( zyfc.utils.DateUtils.DateTime2String(new Date()) + "\t" + content +  "\r\n");
            w.write( content);
            w.flush();
            value = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(w != null) {
                try {
                    w.close();
                } catch (IOException e) {}
            }
        }
        return value;
    }




    /**
     * 判断特定文件是否存在
     * @return
     */
    public static boolean isExistFile(String fileName)
    {
        boolean value = false;
        File file = new File(fileName);

        //先判断是否存在，如果存在，在判断是否是目录
        if( file.exists())
        {
            value = file.isFile();
        }

        return value;
    }

    /**
     * 判断特定文件是否存在
     * @return
     */
    public static boolean isExistPath(String pathName)
    {
        boolean value = false;
        try {
            File file = new File(pathName);

            //先判断是否存在，如果存在，在判断是否是目录
            if (file.exists()) {
                value = file.isDirectory();
            }
        }
        catch (Exception e){
            System.out.println( "异常:" + pathName + "," + e.getMessage());
        }
        catch (Error e){
            System.out.println( "错误:" + pathName + "," + e.getMessage());
        }

        return value;
    }

    /**
     * 判断特定文件是否存在
     * @return
     */
    public static boolean isExistPath(String pathName,boolean isNeedCreate)
    {
        boolean value = false;

        try {
            File file = new File(pathName);

            //先判断是否存在，如果存在，在判断是否是目录
            if (file.exists()) {
                value = file.isDirectory();
            }

            //目录不存在，并且
            if (!value && isNeedCreate) {
                //file.mkdir();
                value=file.mkdirs();
            }
        }
        catch (Exception e){
            System.out.println( "异常:" + pathName + "," + e.getMessage());
        }
        catch (Error e){
            System.out.println( "错误:" + pathName + "," + e.getMessage());
        }

        return value;
    }
}
