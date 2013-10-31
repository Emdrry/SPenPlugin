package lt.sonaro.spenplugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class SPenPlugin extends CordovaPlugin { 
	
	public static final String NATIVE_ACTION_STRING="nativeAction"; 
	public static String FILE_PATH = null;    
      
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals(NATIVE_ACTION_STRING)) {
			String signature_path = args.getString(0); 
			String background_path = args.getString(1);
			Log.i("maniskis","spenplugin back_path" + background_path);	
			this.nativeAction(signature_path,background_path, callbackContext);
			
			return true;
		}
		return false;
	}
	
	private void nativeAction(String signature_path, String background_path, CallbackContext callbackContext) {
		if (signature_path != null && signature_path.length() > 0) { 
			Context context=this.cordova.getActivity().getApplicationContext();
			Intent SPenScreen = new Intent(context, SPen.class);
			SPenScreen.putExtra("PATH", signature_path);
			SPenScreen.putExtra("BACKGROUND_PATH", background_path);
			
			this.cordova.startActivityForResult(this,SPenScreen,1);
			Log.i("maniskis", "nativeaction() po activity starto  FILE_PATH - " + FILE_PATH);
				
			
		} else {
			callbackContext.error("Expected one non-empty string argument.");
		}
	}
	
	 public void onActivityResult(int requestCode, int resultCode,Intent data) {
		//Use Data to get string		 
		 	if (data != null){
		 		FILE_PATH = data.getStringExtra("RESULT_STRING");
		 		Log.i("maniskis", "onActivityResult grazintas adresas " + FILE_PATH);
		 		String funkc = "AddImage('"+FILE_PATH+"');";
		 		MainActivity.activity.sendJavascript(funkc);
		 	}
	   	
     }
	
	 
	
} 