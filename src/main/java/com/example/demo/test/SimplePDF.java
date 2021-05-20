package com.example.demo.test;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description
 * @Author zhaokun
 * @Date 2021/5/20
 * @Version 1.0
 **/
public class SimplePDF {

    public final static String path = "C:/Users/meix/Desktop";
    public static void main(String[] args) throws IOException, DocumentException {
        new SimplePDF().createPdf2(path + "/" + "a.pdf");
    }
    /**
     *   创建PDF文档.
     * @param filename 新PDF文档的路径
     * @throws    DocumentException
     * @throws    IOException
     */
    public void createPdf(String filename) throws DocumentException, IOException {
        // 第一步 创建文档实例
        Document document = new Document();
        // 第二步 获取PdfWriter实例
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // 第三步 打开文档
        document.open();
        // 第四步 添加段落内容
        document.add(new Paragraph("Hello World!"));
        // 第五部 操作完成后必须执行文档关闭操作。
        document.close();
    }

    public void createPdf2(String filename) throws DocumentException, IOException {
        // 第一步 创建文档实例
        // 自定义页面大小使用
//        Rectangle pagesize = new Rectangle(216f, 720f);
//        Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
        Document document = new Document(PageSize.LETTER);
        // 第二步 获取PdfWriter实例
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // 第三步 打开文档
        document.open();
        //margin 单双页对称
//        document.setMarginMirroring()
        // 第四步 添加段落内容
        document.add(new Paragraph("Hello World!"));
        // 第五部 操作完成后必须执行文档关闭操作。
        document.close();
    }
}
