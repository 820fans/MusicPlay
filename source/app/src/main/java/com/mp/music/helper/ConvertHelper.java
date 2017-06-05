package com.mp.music.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ConvertHelper {
	
	/**
	 * 图片模糊化
	 * @param bitmap
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap blurBitmap(Bitmap bitmap,Context context){  

		//Let's create an empty bitmap with the same size of the bitmap we want to blur  
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  

		//Instantiate a new Renderscript  
		RenderScript rs = RenderScript.create(context);  

		//Create an Intrinsic Blur Script using the Renderscript  
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));  

		//Create the Allocations (in/out) with the Renderscript and the in/out bitmaps  
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);  
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);  

		//Set the radius of the blur  
		blurScript.setRadius(25.f);  

		//Perform the Renderscript  
		blurScript.setInput(allIn);  
		blurScript.forEach(allOut);  

		//Copy the final bitmap created by the out Allocation to the outBitmap  
		allOut.copyTo(outBitmap);  

		//recycle the original bitmap  
		bitmap.recycle();  

		//After finishing everything, we destroy the Renderscript.  
		rs.destroy();  

		return outBitmap;  


	} 
}
