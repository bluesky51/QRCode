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
	    
库中源码的基本说明：  
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


