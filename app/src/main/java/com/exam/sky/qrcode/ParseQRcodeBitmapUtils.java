package com.exam.sky.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * Created by BlueSky on 17/1/11.
 */

public class ParseQRcodeBitmapUtils {

    //解析二维码图片,返回结果封装在Result对象中
    public   static Result  parseQRcodeBitmap(Bitmap bitmap){
        try {
            //初始化解析对象
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            //新建一个BitmapLuminanceSource对象，将bitmap图片传给此对象
            BitmapLuminanceSource source = new BitmapLuminanceSource(bitmap);
            //将图片转换成二进制图片
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            Result result1 = multiFormatReader.decode(bitmap1);
            return result1;
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
}
