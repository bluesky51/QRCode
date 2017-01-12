package com.exam.sky.qrcode;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.zxing.library.activity.CaptureActivity;

/**
 * CaptureActivity:二维码的扫描界面：
 * 1.SurfaceView+Camera:二维码扫描界面(自定义相机的扩充)
 * 2.SurfaceView操作类SurfaceHolder以及SurfaceHolder.Callback
 * 3.Activity的生命周期：
 * <p>
 * 备注：Camera：聚焦，闪光灯，震动，音频
 * <p>
 * <p>
 * ViewfinderView：
 * 1.所在的包com.zxing.library.view.ViewfinderView
 * 2.二维码扫描界面上绘制的线条等图
 * 3.自定义View:一般重写：构造方法，onMeasure(),onLayout(),onDraw(),onTouchEvent()
 * 备注：invalidate()刷新界面，重新调用onDraw(),重新绘制
 * 4.重点是onDraw()；画8个角，该颜色的frameColor
 * 画中间波动的线；
 * <p>
 * <p>
 * DecodeHandler处理二维码扫描的解码信息：
 * 重点是  private void decode(byte[] data, int width, int height)：该方法中重点是for双层循环
 */
public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    //true: 成功 false:失败
    boolean isFlag = true;
    EditText editText;
    ImageView imageView;
    String imgPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnLongClickListener(this);
        int code = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (code == -1) {
            //授权失败
            ActivityCompat.requestPermissions(this, new String[]{
                     Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);
        } else {
            //授权成功
            isFlag = true;
        }
    }
    public void scanQRCode(View view) {
        if (isFlag == true) {
            //扫描二维码
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, 100);
        } else {
            Toast.makeText(this, "没有权限操作", Toast.LENGTH_LONG).show();
        }
    }
    public void generatedQRCode(View view) {
        Bitmap bmp = QRCodeUtils.GenerateQR(editText.getText().toString(), 300, 300);
        if (bmp!=null){
            imgPath = BitmapUtils.saveImageToSDCard(bmp, "qrcode1.jpg");
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        }
    }
    public void generatedQRCodeByImg(View view) {
        //生成二维码中心填充的图
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //生成带图的二维码
        Bitmap bmp = QRCodeUtils.GenerateQRCodeByLogo(editText.getText().toString(), 300, bmp1);
        if (bmp!=null){
             imgPath = BitmapUtils.saveImageToSDCard(bmp, "qrcode.jpg");
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    Toast.makeText(this, "扫描的结果是:" + scanResult, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if ((grantResults[0] == 0)&&(grantResults[1] == 0)&&(grantResults[2] == 0)){
                //授权成功
                isFlag = true;
            } else {
                //授权失败
                isFlag = false;
            }
        }
    }
    @Override
    public boolean onLongClick(View v) {
        if (!TextUtils.isEmpty(imgPath)) {
            //长按识别二维码
            Bitmap bmp = BitmapFactory.decodeFile(imgPath);
            Result result = ParseQRcodeBitmapUtils.parseQRcodeBitmap(bmp);
            Toast.makeText(this,"识别的结果是:"+result.getText(),Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
