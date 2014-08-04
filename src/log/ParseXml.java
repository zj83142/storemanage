package log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class ParseXml {
	private static XmlPullParser parser;

	/** 解析 */
	public static LogFiles parserLogFiles(String info) {
		if (info == null)
			return null;
		System.out.println(info);
		InputStream ios = new ByteArrayInputStream(info.getBytes());
		parser = Xml.newPullParser();
		LogFiles logFiles = null;
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (name.equalsIgnoreCase("root")) {
						logFiles = new LogFiles();
					} else if (name.equalsIgnoreCase("OUT")) {
						logFiles.setFileByLevel(name, parser.nextText());
					} else if (name.equalsIgnoreCase("INFO")) {
						logFiles.setFileByLevel(name, parser.nextText());
					} else if (name.equalsIgnoreCase("DEBUG")) {
						logFiles.setFileByLevel(name, parser.nextText());
					} else if (name.equalsIgnoreCase("ERROR")) {
						logFiles.setFileByLevel(name, parser.nextText());
					} else if (name.equalsIgnoreCase("VERBOSE")) {
						logFiles.setFileByLevel(name, parser.nextText());
					} else if (name.equalsIgnoreCase("WARN")) {
						logFiles.setFileByLevel(name, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("-----解析出错---->");
		}
		return logFiles;
	}

}
