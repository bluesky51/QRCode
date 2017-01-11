package com.exam.sky.qrcode;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BlueSky on 17/1/11.
 */

public class QRCodeUtils {

    //只生成简单的二维码

    /**
     *
     * @param data:需要生成二维码的文字信息
     * @param w:生成二维码的宽度
     * @param h:生成二维码的高度
     * @return :返回二维码的bitmap
     */
    public static  Bitmap GenerateQR(String data, int w, int h) {
        if (TextUtils.isEmpty(data)){
            return null;
        }
        Map<EncodeHintType, Object> map = new HashMap<>();
        //设置纠错级别(只含有重要信息)
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置空白边距的宽度
        map.put(EncodeHintType.MARGIN, 0);
        /**
         * 先根据输入的字符串信息得到BitMatrix位图矩阵
         *
         * 参数1:要生成二维码的字符串信息，文本输入
         * 参数2:码的格式，例如：条形码，一维码，二维码，
         * 参数3:生成位图矩阵的宽
         * 参数4:生成位图矩阵的高
         * 参数5:配置生成二维码矩阵的信息 Map<EncodeHintType, ?>
         */
        try {
            //Bit表示含有机器码0和1
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, w, h, map);
            //0和1转化成黑点和白点
            int w1 = bitMatrix.getWidth();
            int h1 = bitMatrix.getHeight();
            //一位数组存储数值代表的颜色值
            int[] values = new int[w1 * h1];
            //i控制列数
            for (int i = 0; i < w1; i++) {
                //j控制行数
                for (int j = 0; j < h1; j++) {
                    //黑色代表有值，1，白色代表无值，0

                    if (bitMatrix.get(i, j)) {
                        //表示有值
                        //[]表示某行某列的值
                        //[0,1][0,2][0,3][0,4][0,5]
                        //[1,0][1,1,][1,2][1,3][1,4]
                        values[j + i * h1] = 0xff000000;
                    } else {
                        //表示无值
                        values[j + i * h1] = 0xffffffff;
                    }
                }
            }

            /**
             * 参数1:把一位数组中的颜色值写入到bitmap
             * 参数2:写入颜色值数组中的第一个的索引index,从0开始
             * 参数3:一行的颜色个数
             * 参数4:一维数组中颜色值矩阵的宽度
             * 参数5:一维数组中颜色值矩阵的高度
             * 参数6:配置颜色基数Config
             */
            //方式1；
            Bitmap bmp = Bitmap.createBitmap(values, 0, w, w, h, Bitmap.Config.ARGB_8888);


            //方式2；
//            //先创建一个空的Bitmap
//            /**
//             *参数1:生成bitmap的宽度
//             * 参数2:生成bitmap的高度
//             * 参数3:配置颜色基数Config
//             */
//            Bitmap bmp = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
//            //把颜色值当作像素点绘制在上次的空的Bitmap
//            /**
//             * 参数1:把一位数组中的颜色值写入到bitmap
//             * 参数2:写入颜色值数组中的第一个的索引index,从0开始
//             * 参数3:一行的颜色个数
//             * 参数4:写入bitmap得x起点位置
//             * 参数5:写入bitmap得y起点位置
//             * 参数6:一维数组中颜色值矩阵的宽度
//             * 参数7:一维数组中颜色值矩阵的高度
//             */
//            bmp.setPixels(values,0,w,0,0,300,300);
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 生成中心带图片的二维码
     *
     * @param text：需要生成二维码的文字信息
     * @param widthAndHeight：生成二维码的宽和高
     * @param mBitmap：二维码中心的图片
     * @return 返回中心带图片的二维码的bitmap
     */
    public static  Bitmap GenerateQRCodeByLogo(String text, int widthAndHeight, Bitmap mBitmap) {
        if (TextUtils.isEmpty(text)){
            return null;
        }

        //中心图的宽高
        int IMAGE_HALFWIDTH = 40;
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);



        HashMap<EncodeHintType, Object> map = new HashMap<>();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        map.put(EncodeHintType.MARGIN, 0);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.QR_CODE,
                            widthAndHeight, widthAndHeight, map);
            int[] pixles = new int[widthAndHeight * widthAndHeight];
            int halfW = widthAndHeight / 2;
            int halfH = widthAndHeight / 2;
            //控制行数
            for (int i = 0; i < widthAndHeight; i++) {
                //控制列数
                for (int j = 0; j < widthAndHeight; j++) {
                    if (i > halfW - IMAGE_HALFWIDTH && i < halfW + IMAGE_HALFWIDTH
                            && j > halfH - IMAGE_HALFWIDTH
                            && j < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixles[i * widthAndHeight + j] = mBitmap.getPixel
                                (j - halfH + IMAGE_HALFWIDTH,
                                        i - halfW + IMAGE_HALFWIDTH);
                    } else{
                        if (bitMatrix.get(i, j)) {//如果有黑块点，记录信息
                            pixles[i * widthAndHeight + j] = 0xff000000;
                        }
                        else {
                            pixles[i * widthAndHeight + j] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bmp1 = Bitmap.createBitmap(widthAndHeight, widthAndHeight, Bitmap.Config.ARGB_8888);
            bmp1.setPixels(pixles, 0, widthAndHeight, 0, 0, widthAndHeight, widthAndHeight);
            return bmp1;

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


}
