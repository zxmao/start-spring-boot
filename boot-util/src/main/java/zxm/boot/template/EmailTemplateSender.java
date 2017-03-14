/**
 *
 */
package zxm.boot.template;

import zxm.boot.handler.DateHandler;
import zxm.boot.properties.UtilProperties;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.util.Map;

@Component
public class EmailTemplateSender {
	Logger logger = LoggerFactory.getLogger(EmailTemplateSender.class);

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	FreeMarkerConfigurer freeMarker;

	/**
	 * 生成html模板字符串
	 *
	 *            存储动态数据的map
	 * @return
	 */
	public String getMailText(Map<String, ? extends Object> param, String templateName) {
		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = freeMarker.getConfiguration().getTemplate("email/" + templateName + ".ftl");
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, param);
		} catch (Exception e) {
			logger.error("获取email文本内容失败", e);
		}
		return htmlText;
	}

	/**
	 * 发送邮件
	 *
	 *            存储动态数据的map
	 * @param toEmail
	 *            邮件地址
	 * @param subject
	 *            邮件主题
	 * @return
	 */
	public boolean sendTemplateMail(Map<String, ? extends Object> param, String toEmail, String subject, String templateName, String[] filePath) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");// 由于是html邮件，不是mulitpart类型
			helper.setFrom(UtilProperties.getProperty("email.from.address", ""));
			helper.setTo(toEmail);
			helper.setSubject(subject + "（自动发送 请勿回复）");
			String htmlText = getMailText(param, templateName);// 使用模板生成html邮件内容
			logger.debug("--------->" + DateHandler.getCreateTime() + "<--------->" + toEmail + "<----------");
			logger.debug(htmlText);
			if(StringUtils.isNoneBlank(htmlText)){
				logger.info("开始发送邮件！");
				if (filePath != null) {
		            BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
		            mdp.setContent(htmlText, "text/html;charset=UTF-8");// 给BodyPart对象设置内容和格式/编码方式
		            Multipart mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放BodyPart对象
		            mm.addBodyPart(mdp);// 将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
		            // 把mm作为消息对象的内容
		            MimeBodyPart filePart;
		            FileDataSource filedatasource;
		            // 逐个加入附件
		            for (int j = 0; j < filePath.length; j++) {
		                filePart = new MimeBodyPart();
		                filedatasource = new FileDataSource(filePath[j]);
		                filePart.setDataHandler(new DataHandler(filedatasource));
		                try {
		                    filePart.setFileName(MimeUtility.encodeText(filedatasource.getName()));
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		                mm.addBodyPart(filePart);
		            }
		            msg.setContent(mm);
		        } else {
		        	helper.setText(htmlText, true);
		        }
				new Thread(() -> {
                    mailSender.send(msg);
                }).start();
			}else{
				logger.warn("邮件无内容，忽略发送！");
			}
			return true;
		} catch (Exception e) {
			logger.error("发送邮件异常！", e);
			return false;
		}
	}
}
