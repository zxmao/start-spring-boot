package zxm.boot.template;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author JunXiong
 * @Date 2016/6/7
 */
@Component
public class ExcelTemplateDownload {

	Logger logger = LoggerFactory.getLogger(ExcelTemplateDownload.class);

	@Autowired
	FreeMarkerConfigurer freeMarker;

	private Template getExcelText(String templateName) throws Exception {
		Template tpl = null;
		try {
			tpl = freeMarker.getConfiguration().getTemplate("excel/" + templateName + ".ftl");
		} catch (Exception e) {
			logger.error("获取excel模板失败", e);
		}
		return tpl;
	}

	public void generateExcel(HttpServletResponse response, String templateName, Map<String, Object> map,
			String outName) {
		Template htmlExcel = null;
		try {
			htmlExcel = getExcelText(templateName);
		} catch (Exception e) {
			logger.info("获取excel 数据失败......");
		}
		String strExcel = null;
		try {
			strExcel = FreeMarkerTemplateUtils.processTemplateIntoString(htmlExcel, map);
		} catch (IOException e) {
			logger.error("获取excel文本内容失败", e);
		} catch (TemplateException e) {
			logger.error("获取excel文本内容失败", e);
		}

		if(StringUtils.isBlank(strExcel)){
			logger.error("读取不到excel模板信息");
			return;
		}

		try {
			response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(outName.getBytes(), "iso-8859-1") + ".xls");
		} catch (UnsupportedEncodingException e) {
			logger.error("excel 文件名编码异常......",e);
		}
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		try {
			OutputStream out = response.getOutputStream();
			byte b[] = strExcel.getBytes("UTF-8");
			for (int i = 0; i < b.length; i++) {
				out.write(b[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("下载excel文件失败", e);
		}
	}
}
