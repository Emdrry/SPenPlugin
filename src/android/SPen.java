package lt.sonaro.spenplugin;
import java.io.File;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.samsung.spensdk.SCanvasView;



public class SPen extends Activity  {
	public static  String temp_file_path = null;	
	private Context mContext = this;
    private RelativeLayout mCanvasContainer;
    private SCanvasView mSCanvas;
    static String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/SPenPluginSignatures";
    static File mFolder =  new File(sdcard_path);
    Button btnClose;
    Button btnSave ;
    Button btnClear;
    static int daysToSave = 3;
	
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("maniskis","onCreate");  
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.screen); 
        
     
       mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);
       mSCanvas = new SCanvasView(mContext);
       
      
       mSCanvas.setZoomFitToViewSize();
       mCanvasContainer.addView(mSCanvas); 
       
       ViewTreeObserver vto = mCanvasContainer.getViewTreeObserver();
       vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
           @Override
           public void onGlobalLayout() {
               ///getLocationOnScreen here
               ViewTreeObserver obs = mCanvasContainer.getViewTreeObserver();  
               mSCanvas.setCanvasZoomEnable(false);
               mSCanvas.setMultiTouchCancel(false);
               Bitmap bg = null;
               
               Bundle extras = getIntent().getExtras();
		        if(extras !=null) {
		        	if (extras.getString("PATH")!= "false"){
		        		loadCanvasImage(extras.getString("PATH"));
		        		Log.i("maniskis","UZLOADINO" + extras.getString("PATH") );
		        	}
		        	if (extras.getString("BACKGROUND_PATH")!= "false"){
		        		bg = BitmapFactory.decodeFile(extras.getString("BACKGROUND_PATH")); 
		        		Log.i("maniskis","background_path SPen " + extras.getString("BACKGROUND_PATH"));
		        		mSCanvas.setBGImage( bg );
		        	}
		        	
		        }
		        else 
		     	   Log.i("maniskis","NEUZLOADINO");
		        obs.removeGlobalOnLayoutListener(this);
           }
       });
        loadUI();
    }
    
    
    public void loadUI(){
    	Log.i("maniskis","loadUI()");
        
        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button btnSave  = (Button) findViewById(R.id.btnSave);
        Button btnClear  = (Button) findViewById(R.id.btnClear);
        
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {            	
            	Intent intent=new Intent();
				intent.putExtra("RESULT_STRING", "");
				setResult(RESULT_OK, intent);
            	Log.i("maniskis","atgal mygtukas");
               finish();
           }
       });      
        
        btnSave.setOnClickListener (new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String file_path = saveSAMMFile();
				Intent intent=new Intent();
				intent.putExtra("RESULT_STRING", file_path);
				setResult(RESULT_OK, intent);
				Log.i("maniskis","issaugoti mygtukas baige"+ file_path);				
				finish();
			}
		});
       
        btnClear.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				mSCanvas.clearSCanvasView();
				Bitmap bg = null;
	            Bundle extras = getIntent().getExtras();
			        if(extras !=null) {
			        	if (extras.getString("BACKGROUND_PATH")!= "false"){
			        		bg = BitmapFactory.decodeFile(extras.getString("BACKGROUND_PATH")); 
			        		mSCanvas.setBGImage( bg );
			        	}
			        }
				Log.i("maniskis","ištrinti mygtukas");
			}
		});
        
        
    }  
   
    private String saveSAMMFile() {    
    	if(!mFolder.exists())
    		mFolder.mkdirs();
    	deleteFilesOlderThanNdays(daysToSave);
    	Log.i("maniskis","sdcard_path: " + sdcard_path);
    	Random generator = new Random();
    	String savePath = mFolder.getPath() + "/signature" + generator.nextInt(100000) +".png";	
    	Log.i("maniskis","savePath: " + savePath);
    	if (mSCanvas.saveSAMMFile(savePath)){
    		return savePath;
    	}
    	else return null;
    }   
    
    // Loading
    private void loadCanvasImage(String loadPath){  
    	mSCanvas.loadSAMMFile(loadPath, true, true); 
    }
    
    public static void deleteFilesOlderThanNdays(int daysBack) {     	  
        
        if(mFolder.exists()){    
            File[] listFiles = mFolder.listFiles();              
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);  
            for(File listFile : listFiles) {  
                if(listFile.lastModified() < purgeTime) {  
                    if(!listFile.delete()) {  
                    	Log.i("maniskis","failas istrintas");
                    }  
                }  
            }  
        } 
    }  
    
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//      //super.onConfigurationChanged(newConfig);
//      //setContentView(R.layout.screen);
//      //loadCanvasImage(temp_file_path);
//      Log.i("maniskis", "OnConfigurationChanged" );
//    }
    
    
    
//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState){ 
//    	//mSCanvas.setDrawingCacheEnabled(true);
//    	//mSCanvas.buildDrawingCache();
//    	temp_file_path = saveSAMMFile();
//    	Log.i("maniskis", "OnSave" + temp_file_path);
//    	savedInstanceState.putString("temp", temp_file_path);
//    	super.onSaveInstanceState(savedInstanceState);
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState){     	 
//        super.onRestoreInstanceState(savedInstanceState); 
//        temp_file_path = savedInstanceState.getString("temp");
//        //mCanvasContainer.addView((View) getLastNonConfigurationInstance());   
//        Log.i("maniskis","mSCanvas image path onrestore() " + mSCanvas.getAlpha());
//        if (mSCanvas !=null && temp_file_path != null) {
//        	loadCanvasImage(temp_file_path);
//        }
//        Log.i("maniskis", "OnRestore " + temp_file_path); 
//    }
}





