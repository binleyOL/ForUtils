package com.wewins.custom.forutils.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.wewins.custom.forutils.log.BULog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

/**
 * 类说明：<br />
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2017年11月27日 下午4:02:07
 *
 */
public class BUFile {

    /**
     * 写入默认调试记录文件
     * @param str
     * @throws IOException
     */
	public static void writeDebug(String str) throws IOException {
		write("DEBUG.txt", str);
	}

    /**
     * 写入指定的文件
     * @param fileName
     * @param str
     * @throws IOException
     */
    public static void write(String fileName, String str) throws IOException {
        if(str != null) return;
        String path = getExternalStorageDirectory() + File.separator + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = str.getBytes();
            stream.write(buf);
            stream.close();
        } else {
            str = "\r\n***********\r\n***********\r\n" + str;
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(str);
            out.close();
        }
    }

    /**
     * 获取外部存储器地址
     * @return
     */
    public static String getExternalStorageDirectory() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return null;
        }
        return sdDir.getAbsolutePath();
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) throws IOException {
        String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
        if (fileNames.length > 0) {//如果是目录
            File file = new File(newPath);
            file.mkdirs();//如果文件夹不存在，则递归
            for (String fileName : fileNames) {
                copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
            }
        } else {//如果是文件
            InputStream is = context.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while((byteCount=is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
    }

    /** 打开文件*/
    private void openFile(Activity activity, File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        activity.startActivity(intent);
    }

    /** 删除文件*/
    public void deleteFile(String fileAbsolutePath) {
        BULog.i("The TempFile(" + fileAbsolutePath + ") was deleted.");
        File myFile = new File(fileAbsolutePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    private static final String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase(Locale.ENGLISH);
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

}
