package com.bonc.integrate;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class EmailIntegrate {
	@Autowired
	JavaMailSender jms;
	@Value("${spring.mail.from:}")
	String from;
	@RequestMapping("/email/{content}")
	public String sendSimpleEmail(@PathVariable String content) throws MessagingException, FileNotFoundException {
		if(!StringUtils.hasText(from))
			return "from is null";
		sendSimpleEmail(content,"1052023708@qq.com");
		sendEmail();
		return "Success";
	}
	public void sendSimpleEmail(String text,String... tos) {
		SimpleMailMessage sm=new SimpleMailMessage();
		sm.setText(text);
		sm.setTo(tos);
		sm.setFrom(from);
		jms.send(sm);
	}

	public void sendEmail() throws MessagingException, FileNotFoundException {
		MimeMessage message=jms.createMimeMessage();
		message.setFrom(from);
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo("1052023708@qq.com");

		// use the true flag to indicate the text included is HTML
		helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);

		// let's include the infamous windows Sample file (this time copied to c:/)
		ClassPathResource cpr = new ClassPathResource("application.yml");
		helper.addInline("identifier1234", cpr);
		helper.addAttachment("config", ResourceUtils.getFile("classpath:application.yml"));
		jms.send(message);
	}
}
