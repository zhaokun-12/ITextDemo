package com.example.demo.controller;

import com.itextpdf.text.pdf.BaseFont;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @Description
 * @Author zhaokun
 * @Date 2021/5/19
 * @Version 1.0
 **/
@Controller
public class HelloController {

    @Autowired
    private TemplateEngine templateEngine;
    @GetMapping("hello")
    public String jump() throws Exception {
        Context context = new Context();
        context.setVariables(new HashMap<>());
        String process = templateEngine.process("认申购份额确认书模板(1)", context);
        System.out.println(process);
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //设置字体，否则不支持中文,在html中使用字体，html{ font-family: SimSun;}
        fontResolver.addFont("static/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.setDocumentFromString(process);
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\meix\\Desktop/9900.pdf");
        fileOutputStream.write(outputStream.toByteArray());
        fileOutputStream.close();
        outputStream.close();
        return "认申购份额确认书模板(1)";
    }
}
