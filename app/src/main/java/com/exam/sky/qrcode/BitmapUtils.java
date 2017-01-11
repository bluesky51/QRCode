package com.exam.sky.qrcode;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by BlueSky on 17/1/11.
 */

public class BitmapUtils {

    /**
     * 保存图片到本地
     * @param bmp:图片的bitmap对象
     * @param fileName:保存的图片的文件名称
     * @return
     */
    public static String  saveImageToSDCard(Bitmap bmp,String fileName) {
        // 首先保存图片
        File rootDir = new File(Environment.getExternalStorageDirectory(), "QRCode");
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        File file = new File(rootDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  file.getAbsolutePath();

    }

}
