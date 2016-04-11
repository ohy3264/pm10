package rest;

import android.util.Log;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.
 * @Time          : 오전 10:52:02
 * @Explanation   : RestAPI Request - 가까운 측정소
 * </pre>
 *
 */
public class CallRest_Nearby {
	static String addr = null;

	public static String restClient(String tmX, String tmY) throws Exception {

        String serviceKey = "9toUVsN1owp+fPheP/lrBRgUHBVmshXXJTXThUo0fA1Bvm42sjDn96m3JSEK6ZowojQdPMAh/FASfOfy/joCnQ==";
        serviceKey = URLEncoder.encode(serviceKey, "UTF-8");

        addr = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList"
				+ "?ServiceKey=";
		addr = addr + serviceKey + "&pageNo=1&numOfRows=80" + "&tmX=" + tmX
				+ "&tmY=" + tmY;
		Log.d("가까운 측정소", addr);

		URL url = new URL(addr);
		InputStream in = url.openStream();
		CachedOutputStream bos = new CachedOutputStream();
		IOUtils.copy(in, bos);
		in.close();
		bos.close();
		return bos.getOut().toString();
	}
}