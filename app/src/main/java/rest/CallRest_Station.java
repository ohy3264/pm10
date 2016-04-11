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
 * @Time          : 오전 10:48:34
 * @Explanation   : RestAPI Request - 측정소별 대기오염  정보
 * </pre>
 *
 */
public class CallRest_Station {
	static String addr = null;

	public static String restClient(String stationName, String dataTerm)
			throws Exception {

        String serviceKey = "oxHnupvsc%2BHOqw0ykaqzt%2F3m07d1qqJEQfGG4Lkgf8UFbl%2F21YfHncUmRQjacukAhg%2Fodm94g%2BBd4BkFazT5yQ%3D%3D";
        //serviceKey = URLEncoder.encode(serviceKey, "UTF-8");
        stationName = URLEncoder.encode(stationName, "UTF-8");
        dataTerm = URLEncoder.encode(dataTerm, "UTF-8");

		addr = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty"
				+ "?ServiceKey=";
		addr = addr + serviceKey + "&pageNo=1&numOfRows=80" + "&stationName="
				+ stationName + "&dataTerm=" + dataTerm;
		Log.d("측정소별 대기오염정보", addr);

		URL url = new URL(addr);
		InputStream in = url.openStream();
		CachedOutputStream bos = new CachedOutputStream();
		IOUtils.copy(in, bos);
		in.close();
		bos.close();
		return bos.getOut().toString();
	}
}