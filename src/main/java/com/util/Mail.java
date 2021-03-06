package com.util;

import java.io.File;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;



/**
 *邮件发送类  此类对发送邮件的东西进行封装
 * @author zyh
 *
 */
public class Mail {
      
	private static final String HOST = "smtp.qq.com";
    private static final String SMTP = "smtp";
    private static final String USERNAME = "785427346@qq.com";
    private static final String PASSWORD = "xxxxxxxxxx";
    private static final int PORT = 587;//587/465
    private static final String DEFAULTENCODING = "UTF-8";

    private static JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

    private static Properties prop = new Properties();

    static{
        // 设定mail server
        senderImpl.setHost(HOST);
        senderImpl.setProtocol(SMTP);
        senderImpl.setUsername(USERNAME);
        senderImpl.setPassword(PASSWORD);
        senderImpl.setPort(PORT);
        senderImpl.setDefaultEncoding(DEFAULTENCODING);

        // 设定properties
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        //设置调试模式可以在控制台查看发送过程
        prop.put("mail.debug", "true");

        senderImpl.setJavaMailProperties(prop);
    }
	
	
	/**
	 * 
	 * @param fjr 发件人
	 * @param sjr 收件人
	 * @param nr 内容
	 * @return
	 */
	public static void springQqMail( String fjr,String[] sjr,String nr) {
		
		   // 设置收件人，寄件人 用数组发送多个邮件
//      String[] array = new String[] {"88888@qq.com","666666@qq.com","999999999@qq.com",USERNAME};
        String[] array = sjr;
        String subject = "Cherish内嵌图片、音乐的邮件";



        String filePath = "E:/javaxmail.png";
        String html = "<html><head>"+
                    "</head><body>"+
                    "<audio src='http://m10.music.126.net/20160422225433/25b43b999bcdaf3425b9194514340596/ymusic/8c94/b9af/69e3/7ebe35b8e00154120822550b21b0c9c5.mp3' autoplay='autoplay' controls='controls' loop='-1'>爱你</audio>"+
                    "<h1>Hello,Nice to meet you!</h1>"+
                    "<span style='color:red;font-size:36px;'>并摸了一把你的小奶</span>"+
                    "<img src='cid:javaxmail.png'>"+
                    "</body></html>";
        boolean result = inlineFileMail(array, subject, html, filePath);

        if (result) {
            System.out.println("发送邮件成功。。。。");
        }
	}
	
	
	
	/**
     * 发送简单邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public static boolean singleMail(String to, String subject, String content){
        String[] array = new String[] {to};
        return singleMail(array, subject, content);
    }


    /**
     * 发送简单文本邮件
     * @param to 收件人邮箱数组
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public static boolean singleMail(String[] to, String subject, String content){
        boolean result = true;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        mailMessage.setTo(to);
        mailMessage.setFrom(USERNAME);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        // 发送邮件
        try {
            senderImpl.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    /**
     * 发送html邮件
     * @param to 收件人
     * @param subject 主题
     * @param html html代码
     * @return
     */
    public static boolean htmlMail(String[] to, String subject, String html){
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();  
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);  

        try {
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件  
            messageHelper.setText(html, true);  

            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送内嵌图片的邮件   （cid:资源名）
     * @param to 收件人邮箱
     * @param subject 主题
     * @param html html代码
     * @param imgPath 图片路径
     * @return
     */
    public static boolean inlineFileMail(String[] to, String subject, String html, String filePath){
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();  
        try {
            //设置true开启嵌入图片的功能
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);  
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件  
            messageHelper.setText(html, true);  

            FileSystemResource file = new FileSystemResource(new File(filePath));  
            messageHelper.addInline(file.getFilename(), file);  

            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param html
     * @param filePath
     * @return
     */
    public static boolean attachedFileMail(String[] to, String subject, String html, String filePath){
        boolean result = true;

        MimeMessage mailMessage = senderImpl.createMimeMessage();  

        try {
            // multipart模式 为true时发送附件 可以设置html格式
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");  
            // 设置收件人，寄件人 用数组发送多个邮件
            messageHelper.setTo(to);
            messageHelper.setFrom(USERNAME);
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件  
            messageHelper.setText(html, true);  

            FileSystemResource file = new FileSystemResource(new File(filePath));  
            // 这里的方法调用和插入图片是不同的。  
            messageHelper.addAttachment(file.getFilename(), file);

            // 发送邮件
            senderImpl.send(mailMessage);
        } catch (MessagingException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


}