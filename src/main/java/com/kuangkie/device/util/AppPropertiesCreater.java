package com.kuangkie.device.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 	加载配置文件内容
 * @author lhb
 *
 */
public class AppPropertiesCreater {

	public AppPropertiesCreater() {

	}

	public String create(Long programCode) throws IOException {
		StringBuilder sb = new StringBuilder();

		InputStream resourceAsStream = AppPropertiesCreater.class.getResourceAsStream("/sae/application.properties");
		String readFileToString = IOUtils.toString(resourceAsStream);
		String appPropertiesPattern ="(\\$\\{([programCode\\}]+)\\})";

		String replaceFirst = readFileToString.replaceFirst(appPropertiesPattern, programCode + "");
		sb.append(replaceFirst);
		return sb.toString();
	}

}
