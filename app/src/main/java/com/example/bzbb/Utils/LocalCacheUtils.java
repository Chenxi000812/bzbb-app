package com.example.bzbb.Utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LocalCacheUtils {
    private static final String BASEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/cac";

    private static final String IMGPATH = BASEPATH+"/pic";

    public static void setImgCache(Bitmap bitmap,String fileName){
        File imgdir = new File(IMGPATH);
        if (!imgdir.exists()||!imgdir.isDirectory()){
            imgdir.mkdirs();
        }
        File imgfile = new File(IMGPATH,fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imgfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getImgCache(String fileName){
        File imgfile = new File(IMGPATH,fileName);
        if (!imgfile.exists()){
            return null;
        }
        try {
            return BitmapFactory.decodeStream(new FileInputStream(imgfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static boolean hasWRITE = false;


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else {
                hasWRITE = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
