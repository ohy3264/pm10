package xmlreader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.NearbyModel;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:44:21
 * @Explanation   : XmlDataReader - 가까운 측정소 
 * </pre>
 *
 */
public class XmlReader_Nearby {
	// Log
	private static final String TAG = "XmlReader_Nearby";
	private static final boolean DEBUG = true;
	private static final boolean INFO = true;
	public static ArrayList<NearbyModel> airpollution_parser(String XMLData) {
		ArrayList<NearbyModel> nearbyList = new ArrayList<NearbyModel>();
		try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();

			StringReader sr = new StringReader(XMLData);
			InputSource is = new InputSource(sr);

			Document doc = docBuild.parse(is);
			doc.getDocumentElement().normalize();

			// item 엘리먼트 리스트
			NodeList itemlist = doc.getElementsByTagName("item");
			for (int i = 0; i < itemlist.getLength(); i++) {
				NearbyModel model = new NearbyModel();

				Node itemNode = itemlist.item(i);

				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
					// 엘리먼트
					Element Elmnt = (Element) itemNode;
					// stationName 태그
					NodeList stationNameList = Elmnt
							.getElementsByTagName("stationName");
					if (stationNameList.getLength() != 0) {
						Element stationNameElmnt = (Element) stationNameList
								.item(0);
						Node stationName = stationNameElmnt.getFirstChild();
						if (stationNameElmnt.getFirstChild() != null) {
							model.setStationName(stationName.getNodeValue());
						}
					}
				}
				nearbyList.add(model);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nearbyList;

	}
}
