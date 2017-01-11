# QRCode
二维码的生成和扫描
####1.添加zxinglibrary模版依赖库  
####2.清单文件配置：  
权限：  
  
	  <!--调用系统相机进行自定义拍照-->
	    <uses-permission android:name="android.permission.CAMERA"></uses-permission>
	    <uses-feature android:name="android.hardware.Camera"></uses-feature>
	    <uses-feature android:name="android.hardware.Camera.autofocus"></uses-feature>
	
	   <!--申请震动器的权限-->
	    <uses-permission android:name="android.permission.VIBRATE"></uses-permission>
####3. 声明二维码扫描界面的Activity,该类在zxinglibrary包中  

	     <activity android:name="com.zxing.library.activity.CaptureActivity"></activity>
	     
####4.二维码扫描：  
启动扫描界面即可：   

		 //扫描二维码
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivityForResult(intent, 100);
获得扫描结果：

	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == 100) {
	            if (resultCode == RESULT_OK) {
	                if (data != null) {
	                    Bundle bundle = data.getExtras();
	                    String scanResult = bundle.getString("result");
	                    Log.e("======", "=====扫描的结果是======" + scanResult);
	                }
	
	            }
	        }
	    }
####5.二维码的生成:  
&emsp;&emsp;1>生成一个简单的二维码(只包含字符串信息)  
&emsp;&emsp;&emsp;&emsp;《1》使用Map<EncodeHintType, Object>对象进行配置生成二维码的信息，一般如下:
             
             //设置纠错级别(只含有重要信息)
             map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
             //设置编码
             map.put(EncodeHintType.CHARACTER_SET, "utf-8");
             //设置空白边距的宽度
             map.put(EncodeHintType.MARGIN, 0);
     
               
&emsp;&emsp;&emsp;&emsp;《2》使用MultiFormatWriter对象调用encode()，具体如下:
  
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
&emsp;&emsp;&emsp;&emsp;《3》使用双层for循环将得到的结果信息用颜色表示在一维数组中，关键示例如下：
                          
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
&emsp;&emsp;&emsp;&emsp;《4》借助Bitmap的createBitmap进行一维数组颜色值填充到Bitmap占据的区域中，如:
            
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
             
&emsp;&emsp;2>生成一个含logo图的二维码

####带logo图标的二维码是对普通二维码的改造，把普通二维码中心空出一块区域进行logo图标显示，关键的点如下：

    备注:1.将要填入二维码中心的logo缩放到指定大小
        //中心图logo的要显示宽高
        int IMAGE_HALFWIDTH = 40;
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);
####备注2:使用双层for循环的时候对于中心位置单独判断将logo图片的像素颜色值进行填充，最终得到结果信息用颜色表示在一维数组中，关键示例如下：
    
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
&emsp;&emsp;3.识别图片二维码,关键代码如下：
    
      try {
                //初始化解析对象
                MultiFormatReader multiFormatReader = new MultiFormatReader();
                //新建一个BitmapLuminanceSource对象，将bitmap图片传给此对象
                BitmapLuminanceSource source = new BitmapLuminanceSource(bitmap);
                //将图片转换成二进制图片
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                Result result1 = multiFormatReader.decode(bitmap1);
                Log.e("======","======="+result1.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
	      	       
###库中源码的基本说明：  
####CaptureActivity:二维码的扫描界面：    
 &emsp;&emsp;1.SurfaceView+Camera:二维码扫描界面(自定义相机的扩充)      
 &emsp;&emsp;2.SurfaceView操作类SurfaceHolder以及SurfaceHolder.Callback      
 &emsp;&emsp;3.Activity的生命周期      

&emsp;&emsp;&emsp;&emsp;&emsp;备注：Camera：聚焦，闪光灯，震动，音频        
####ViewfinderView      
 &emsp;&emsp;1.所在的包com.zxing.library.view.ViewfinderView      
 &emsp;&emsp;2.二维码扫描界面上绘制的线条等图     
 &emsp;&emsp;3.自定义View:一般重写：构造方法，onMeasure(),onLayout(),onDraw(),onTouchEvent()      
 &emsp;&emsp;备注：invalidate()刷新界面，重新调用onDraw(),重新绘制      
 &emsp;&emsp;4.重点是onDraw()；画8个角，该颜色的frameColor画中间波动的线    

####DecodeHandler处理二维码扫描的解码信息：  
&emsp;&emsp;&emsp;&emsp;&emsp;重点是  private void decode(byte[] data, int width, int height)：该方法中重点是for双层循环


