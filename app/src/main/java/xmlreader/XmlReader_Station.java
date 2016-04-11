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

import model.StationModel;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:47:27
 * @Explanation   : XmlDataReader - 측정소 별 대기오염 정보
 * </pre>
 *
 */
public class XmlReader_Station {
	// 대기오염 정보
	public static ArrayList<StationModel> airpollution_parser(
			String XMLData, String station_name) {
		ArrayList<StationModel> airpollutionList = new ArrayList<StationModel>();
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
				StationModel model = new StationModel();

				Node itemNode = itemlist.item(i);

				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
					// 엘리먼트
					Element Elmnt = (Element) itemNode;
					// stationName 태그
					model.setStationName(station_name);
					// dataTime 태그
					NodeList dateList = Elmnt.getElementsByTagName("dataTime");
					if (dateList.getLength() != 0) {
						Element dateElmnt = (Element) dateList.item(0);
						Node date = dateElmnt.getFirstChild();
						if (dateElmnt.getFirstChild() != null) {
							model.setDataTime(date.getNodeValue());
						}
					}
					// so2Value 태그
					NodeList so2ValueList = Elmnt
							.getElementsByTagName("so2Value");
					if (so2ValueList.getLength() != 0) {
						Element so2ValueElmnt = (Element) so2ValueList.item(0);
						Node so2Value = so2ValueElmnt.getFirstChild();
						if (so2ValueElmnt.getFirstChild() != null) {
							model.setSo2Value(so2Value.getNodeValue());

						}
					}
					// coValue 태그
					NodeList coValueList = Elmnt
							.getElementsByTagName("coValue");
					if (coValueList.getLength() != 0) {
						Element coValueElmnt = (Element) coValueList.item(0);
						Node coValue = coValueElmnt.getFirstChild();
						if (coValueElmnt.getFirstChild() != null) {
							model.setCoValue(coValue.getNodeValue());
						}

					}
					// o3Value 태그
					NodeList o3ValueList = Elmnt
							.getElementsByTagName("o3Value");
					if (o3ValueList.getLength() != 0) {
						Element o3ValueElmnt = (Element) o3ValueList.item(0);
						Node o3Value = o3ValueElmnt.getFirstChild();
						if (o3ValueElmnt.getFirstChild() != null) {
							model.setO3Value(o3Value.getNodeValue());

						}
					}
					// no2Value 태그
					NodeList no2ValueList = Elmnt
							.getElementsByTagName("no2Value");
					if (no2ValueList.getLength() != 0) {
						Element no2ValueElmnt = (Element) no2ValueList.item(0);
						Node no2Value = no2ValueElmnt.getFirstChild();
						if (no2ValueElmnt.getFirstChild() != null) {
							model.setNo2Value(no2Value.getNodeValue());

						}
					}
					// pm10Value 태그
					NodeList pm10ValueList = Elmnt
							.getElementsByTagName("pm10Value");
					if (pm10ValueList.getLength() != 0) {
						Element pm10ValueElmnt = (Element) pm10ValueList
								.item(0);
						if (pm10ValueElmnt.getFirstChild() != null) {
							Node pm10Value = pm10ValueElmnt.getFirstChild();
							model.setPm10Value(pm10Value.getNodeValue());

						}
					}
					// khaiValue 태그
					NodeList khaiValueList = Elmnt
							.getElementsByTagName("khaiValue");
					if (khaiValueList.getLength() != 0) {
						Element khaiValueElmnt = (Element) khaiValueList
								.item(0);
						if (khaiValueElmnt.getFirstChild() != null) {
							Node khaiValue = khaiValueElmnt.getFirstChild();
							model.setKhaiValue(khaiValue.getNodeValue());

						}
					}
					// khaiGrade 태그
					NodeList khaiGradeList = Elmnt
							.getElementsByTagName("khaiGrade");
					if (khaiGradeList.getLength() != 0) {
						Element khaiGradeElmnt = (Element) khaiGradeList
								.item(0);
						if (khaiGradeElmnt.getFirstChild() != null) {
							Node khaiGrade = khaiGradeElmnt.getFirstChild();
							model.setKhaiGrade(khaiGrade.getNodeValue());
						}
					}

					// so2Grade 태그
					NodeList so2GradeList = Elmnt
							.getElementsByTagName("so2Grade");
					if (so2GradeList.getLength() != 0) {
						Element so2GradeElmnt = (Element) so2GradeList.item(0);
						if (so2GradeElmnt.getFirstChild() != null) {
							Node so2Grade = so2GradeElmnt.getFirstChild();
							model.setSo2Grade(so2Grade.getNodeValue());
						}
					}

					// coGrade 태그
					NodeList coGradeList = Elmnt
							.getElementsByTagName("coGrade");
					if (coGradeList.getLength() != 0) {
						Element coGradeElmnt = (Element) coGradeList.item(0);
						if (coGradeElmnt.getFirstChild() != null) {
							Node coGrade = coGradeElmnt.getFirstChild();
							model.setCoGrade(coGrade.getNodeValue());
						}
					}

					// o3Grade 태그
					NodeList o3GradeList = Elmnt
							.getElementsByTagName("o3Grade");
					if (o3GradeList.getLength() != 0) {
						Element o3GradeElmnt = (Element) o3GradeList.item(0);
						if (o3GradeElmnt.getFirstChild() != null) {
							Node o3Grade = o3GradeElmnt.getFirstChild();
							model.setO3Grade(o3Grade.getNodeValue());
						}
					}

					// no2Grade 태그
					NodeList no2GradeList = Elmnt
							.getElementsByTagName("no2Grade");
					if (no2GradeList.getLength() != 0) {
						Element no2GradeElmnt = (Element) no2GradeList.item(0);
						if (no2GradeElmnt.getFirstChild() != null) {
							Node no2Grade = no2GradeElmnt.getFirstChild();
							model.setNo2Grade(no2Grade.getNodeValue());
						}
					}
					// pm10Grade 태그
					NodeList pm10GradeList = Elmnt
							.getElementsByTagName("pm10Grade");
					if (pm10GradeList.getLength() != 0) {
						Element pm10GradeElmnt = (Element) pm10GradeList
								.item(0);
						if (pm10GradeElmnt.getFirstChild() != null) {
							Node pm10Grade = pm10GradeElmnt.getFirstChild();
							model.setPm10Grade(pm10Grade.getNodeValue());
						}
					}
				}
				airpollutionList.add(model);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return airpollutionList;
	}
}
