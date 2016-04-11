package utill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class ImgDownloader {

	
	public Bitmap DownLoadbitmapImgFile(String airflag, int imgNum) {
		Bitmap bmImg = null;
		URL myFileUrl = null;
		String num = "";
		if (imgNum < 10) {
			num = "0" + imgNum;
		} else {
			num = "" + imgNum;
		}

		String addr = "http://www.webairwatch.com/kaq/modelimg/";
		addr = addr + airflag + ".09km." + num + ".gif";
		Log.d("addr", addr);

		try {
			myFileUrl = new URL(addr);
		} catch (MalformedURLException e) {
			// Todo Auto-generated catch block
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		try {
			// 실질적인 통신이 이루어지는 부분
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);

			long endTime = System.currentTimeMillis();
			Log.d("다운로드 시간 : ", (endTime - startTime) / 1000.0 + " sec");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmImg;
	}

	public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath,
			String filename) {

		File file = new File(strFilePath);

		// If no folders
		if (!file.exists()) {
			file.mkdirs();
			// Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
		}

		File fileCacheItem = new File(strFilePath + "/" + filename);
		OutputStream out = null;

		try {
			long startTime = System.currentTimeMillis();
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.JPEG, 100, out);

			long endTime = System.currentTimeMillis();
			Log.d("저장 시간 : ", (endTime - startTime) / 1000.0 + " sec");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace(); 
			}
		}
	}

	public Bitmap LoadBitmapToFileCache(String strFilePath, String filename) {
		Bitmap bmImg = null;
		try {
			String imgpath = strFilePath + "/" +filename;
			bmImg = BitmapFactory.decodeFile(imgpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmImg;
	}
}
