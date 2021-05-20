package com.example.demo.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zhaokun
 * @Date 2021/5/20
 * @Version 1.0
 **/
@Service
@Slf4j
public class HtmlToPdfUtils {
    @Autowired
    TemplateEngine templateEngine;

    public  ByteArrayOutputStream htmlToPdfFindal(String template, String outFileName, Map<String, Object> map, Integer source, Long attachmentsId, Long companyCode ) throws IOException, DocumentException {
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
        return outputStream;

    }
}
