package com.example.demo.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.Charset;
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

    final String charsetName = "UTF-8";

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

    public  void htmlToPdfFindalPlus(String htmlTemplate, Map<String, Object> dataMap,
String targetPdf, Rectangle pageSize, String header, boolean isFooter, File watermark) throws IOException, DocumentException {
        Context context = new Context();
        context.setVariables(new HashMap<>());
        // 获取html文本
        String process = templateEngine.process(htmlTemplate, context);

        Document document = new Document(pageSize);
        OutputStream outputStream = new FileOutputStream(targetPdf);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        // 设置页边距
        document.setMargins(0,0,0,0);
        PDFBuilder builder = new PDFBuilder("页眉", 10, pageSize, null, true);
        // 事件回调
        writer.setPageEvent(builder);
        document.open();
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider(){
//            @Override
//            public Font getFont(String fontname, String encoding, float size, final int style) {
//                if (fontname == null) {
///**
//                * 操作系统需要有该字体, 没有则需要安装; 当然也可以将字体放到项目中， 再从项目中读取
//                */
//                    fontname = "simsun.ttf";
//                    encoding = "UniGB-UCS2-H";
//                }
//                Font font = null;
//                try {
//                    font = new Font(BaseFont.createFont(fontname, encoding, BaseFont.NOT_EMBEDDED), size, style);
//                } catch (Exception e) {
//                    log.error("", e);
//                }
//                return font;
//            }
        })){
            @Override
                public HtmlPipelineContext clone() throws CloneNotSupportedException {
                    HtmlPipelineContext context = super.clone();
                    ImageProvider imageProvider = this.getImageProvider();
                    context.setImageProvider(imageProvider);
                    return context;
            }
        };

        /**
              * 图片解析
              */
//        htmlContext.setImageProvider(new AbstractImageProvider() {
//            String rootPath = "C:\\Users\\Administrator\\Desktop\\刘亦菲\\";
//
//            @Override
//            public String getImageRootPath() {
//                return rootPath;
//            }
//
//            @Override
//            public Image retrieve(String src) {
//                if (StringUtils.isEmpty(src)) {
//                    return null;
//                }
//                try { Image image = Image.getInstance(new File(rootPath, src).toURI().toString());
//                    /**
//                                * 图片显示位置
//                                */
//                    image.setAbsolutePosition(400, 400);
//                    if (image != null) {
//                        store(src, image);
//                        return image;
//                    }
//                } catch (Exception e) {
//                    log.error("", e);
//                }
//                return super.retrieve(src);
//            }
//        });
        htmlContext.setAcceptUnknown(true).autoBookmark(true)
                .setTagFactory(Tags.getHtmlTagProcessorFactory());

        /**
              * css解析
              */
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        cssResolver.setFileRetrieve(new FileRetrieve() {
            @Override
            public void processFromStream(InputStream in, ReadingProcessor processor) throws IOException {
                try (InputStreamReader reader = new InputStreamReader(in, charsetName)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                }
            }

            /**
                  * 解析href
                  */
            @Override
            public void processFromHref(String href, ReadingProcessor processor) throws IOException {
                InputStream is = new ByteArrayInputStream(href.getBytes());
                try {
                    InputStreamReader reader = new InputStreamReader(is, charsetName);
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }

            }
        });

        HtmlPipeline htmlPipeline =
                new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = null;
        worker = new XMLWorker(pipeline, true);
        XMLParser parser = new XMLParser(true, worker, Charset.forName(charsetName));
        try (InputStream inputStream = new ByteArrayInputStream(process.getBytes())) {
            parser.parse(inputStream, Charset.forName(charsetName));
        }
        document.close();

    }
}
