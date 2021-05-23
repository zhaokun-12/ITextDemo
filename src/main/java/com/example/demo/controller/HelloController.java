package com.example.demo.controller;

import com.example.demo.test.HTMLToPDF;
import com.example.demo.utils.HtmlToPdfUtils;
import com.example.demo.utils.ITextRendererMargin;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @Description
 * @Author zhaokun
 * @Date 2021/5/19
 * @Version 1.0
 **/
@Controller
public class HelloController {
    private static String path = "C:\\Users\\45585\\Desktop/";
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HtmlToPdfUtils htmlToPdfUtils;
    @GetMapping("hello")
    public String jump() throws Exception {
        Context context = new Context();
        context.setVariables(new HashMap<>());
        String process = templateEngine.process("认申购确认书-模板一", context);
        System.out.println(process);

//        ITextRenderer renderer = new ITextRendererMargin();
        ITextRendererMargin renderer = new ITextRendererMargin();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //设置字体，否则不支持中文,在html中使用字体，html{ font-family: SimSun;}
        fontResolver.addFont("static/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.setDocumentFromString(process);
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(path + "9901.pdf");
        fileOutputStream.write(outputStream.toByteArray());
        fileOutputStream.close();
        outputStream.close();
        return "认申购确认书-模板一";
    }

    @GetMapping("hello2")
    public String jump2() throws Exception {
        Context context = new Context();
        context.setVariables(new HashMap<>());
        String process = templateEngine.process("认申购确认书-模板一", context);
        System.out.println(process);
        ByteArrayOutputStream byteArrayOutputStream = new HTMLToPDF().htmlToPdf(process);
        FileOutputStream fileOutputStream = new FileOutputStream(path + "9900.pdf");
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.close();
        return "认申购确认书-模板一";
    }

    @GetMapping("hello4")
    public String jump4() throws Exception {
        String outputFile = path + "9999.pdf";
        htmlToPdfUtils.htmlToPdfFindalPlus("认申购确认书-模板一", null, outputFile, PageSize.A4, "", true, null);
        return "认申购确认书-模板一";
    }



//    @GetMapping("hello3")
//    //itext7
//    public String jump3() throws Exception {
//        Context context = new Context();
//        context.setVariables(new HashMap<>());
//        String process = templateEngine.process("认申购确认书-模板一", context);
//        FontProvider fontProvider = new FontProvider();
//        PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
//        fontProvider.addFont(sysFont.getFontProgram(), "UniGB-UCS2-H");
//
//        ConverterProperties   converterProperties = new ConverterProperties();
//        converterProperties.setFontProvider(fontProvider);
//        HtmlConverter.convertToPdf(process, new FileOutputStream("C:/Users/meix/Desktop/999.pdf"), converterProperties);
//        return "认申购确认书-模板一";
//    }
}
