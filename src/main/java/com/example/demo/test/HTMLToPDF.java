package com.example.demo.test;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Description
 * @Author zhaokun
 * @Date 2021/5/20
 * @Version 1.0
 **/
@Slf4j
public class HTMLToPDF {
    public static void main(String[] args) {
    }

    /**
     * 不支持图片
     * 如果设置宽度不足等，则回显示空白pdf，hello1为显示不完全
     * 不推荐
     * @param htmlStr
     * @return
     */
    public static ByteArrayOutputStream htmlToPdf(String htmlStr){
        Document document = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    baos);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new ByteArrayInputStream(htmlStr.getBytes()), Charset.forName("UTF-8"),new AsianFontProvider());
        } catch (DocumentException e) {log.error("HtmlToPDF 获取实例pdfWriter失败....",e);

        }catch (IOException e) {
            log.error("HtmlToPDF 转换pdf失败....",e);

        } catch (RuntimeWorkerException e) {
            log.error("html标签未闭合...", e);

        } catch (Exception e) {
            log.error("HtmlToPDF 转换pdf失败....", e);

        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    log.error("document关闭异常...",e);

                }
            }
        }
        return baos;
    }
    /**
     *
     * @version 1.0
     * @date 2020/3/22 15:43
     * @description 解决pdf文件中文不显示的问题
     **/

}
@Slf4j
class AsianFontProvider extends XMLWorkerFontProvider {

    public Font getFont(final String fontname, final String encoding,
                        final boolean embedded, final float size, final int style,
                        final BaseColor color) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.error("生成pdf文件，添加字体失败",e);
        }
        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }
}
