package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Util {
	private static String nodeInfos = "<%1$s>%2$s</%3$s>";

	public static String getNodeInfos() {
		return nodeInfos;
	}

	private static String nodeInfo = "<%1$s/>";

	public static String getNodeInfo() {
		return nodeInfo;
	}

	public static String manageNode(String info) {
		String result = "";
		if (info != null) {
			String[] infos = info.split("=");
			if (infos != null) {
				if (infos.length == 2) {
					result = String.format(getNodeInfos(), infos[0], infos[1],
							infos[0]);
				} else if (infos.length == 1) {
					result = String.format(getNodeInfo(), infos[0]);
				}
			}
		}
		return result;
	}

	public static boolean judgeStr(String level) {
		if (level != null && !level.equals("") && level.length() > 0
				&& !level.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/** 读取文件中数据 */
	public static String readFile(String fileName) {
		StringBuilder result = new StringBuilder();
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		try {
			String str = null;
			BufferedReader in = new BufferedReader(new FileReader(file));
			while ((str = in.readLine()) != null) {
				if (str != null) {
					result.append(str);
				}
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return result.toString();
	}

	/** 保存信息到文件中 */
	public static void writeFile(String info, String fileName) {
		try {
			System.out.println("writeFile======>" + info);
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(file, false);
			os.write((info + "\n").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------saveFilesName-------->" + e.toString());
		}
	}

}
