package log;

import java.io.File;


public class LogFiles {

	private String out;
	private String info;
	private String debug;
	private String error;
	private String verbose;
	private String warn;

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		if(Util.judgeStr(out)){
			this.out = out;
		}
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		if(Util.judgeStr(info)){
			this.info = info;
		}
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		if(Util.judgeStr(debug)){
			this.debug = debug;
		}
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		if(Util.judgeStr(error)){
			this.error = error;
		}
	}

	public String getVerbose() {
		return verbose;
	}

	public void setVerbose(String verbose) {
		if(Util.judgeStr(verbose)){
			this.verbose = verbose;
		}
	}

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		if(Util.judgeStr(warn)){
			this.warn = warn;
		}
	}

	public void setFileByLevel(String level,String filename) {
		if (level.equalsIgnoreCase("OUT")) {
			setOut(filename);
		} else if (level.equalsIgnoreCase("INFO")) {
			setInfo(filename);
		} else if (level.equalsIgnoreCase("DEBUG")) {
			setDebug(filename);
		} else if (level.equalsIgnoreCase("ERROR")) {
			setError(filename);
		} else if (level.equalsIgnoreCase("VERBOSE")) {
			setVerbose(filename);
		} else if (level.equalsIgnoreCase("WARN")) {
			setWarn(filename);
		}
		Log.map.put(level, new File(Log.getPath()+"/"+level,filename));
	}
	
	public String getFileName(String level) {
		String fileName = null;
		if (level.equalsIgnoreCase("OUT")) {
			fileName = getOut();
		} else if (level.equalsIgnoreCase("INFO")) {
			fileName = getInfo();
		} else if (level.equalsIgnoreCase("DEBUG")) {
			fileName = getDebug();
		} else if (level.equalsIgnoreCase("ERROR")) {
			fileName = getError();
		} else if (level.equalsIgnoreCase("VERBOSE")) {
			fileName = getVerbose();
		} else if (level.equalsIgnoreCase("WARN")) {
			fileName = getWarn();
		}
		return fileName;
	}

	public static String getSendParam(LogFiles logFiles) {
		String name = null; 
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<root>");
		name = logFiles.getOut();
		if(Util.judgeStr(name)){
			stringBuffer.append("<OUT>"+name+"</OUT>");
		}else{
			stringBuffer.append("<OUT />");
		}
		name = logFiles.getInfo();
		if(Util.judgeStr(name)){
			stringBuffer.append("<INFO>"+name+"</INFO>");
		}else{
			stringBuffer.append("<INFO />");
		}
		name = logFiles.getDebug();
		if(Util.judgeStr(name)){
			stringBuffer.append("<DEBUG>"+name+"</DEBUG>");
		}else{
			stringBuffer.append("<DEBUG />");
		}
		name = logFiles.getError();
		if(Util.judgeStr(name)){
			stringBuffer.append("<ERROR>"+name+"</ERROR>");
		}else{
			stringBuffer.append("<ERROR />");
		}
		name = logFiles.getVerbose();
		if(Util.judgeStr(name)){
			stringBuffer.append("<VERBOSE>"+name+"</VERBOSE>");
		}else{
			stringBuffer.append("<VERBOSE />");
		}
		name = logFiles.getWarn();
		if(Util.judgeStr(name)){
			stringBuffer.append("<WARN>"+name+"</WARN>");
		}else{
			stringBuffer.append("<WARN />");
		}
		stringBuffer.append("</root>");
		return stringBuffer.toString();
	}

	

}
