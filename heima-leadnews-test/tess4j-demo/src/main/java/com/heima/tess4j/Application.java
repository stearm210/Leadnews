package com.heima.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.tess4j
 * @Author: yanhongwei
 * @CreateTime: 2025-01-18  20:35
 * @Description: TODO
 * @Version: 1.0
 */
public class Application {
     /*
      * @Title: main
      * @Author: pyzxW
      * @Date: 2025-01-18 20:36:04
      * @Params:
      * @Return: null
      * @Description: 识别图片中的文字
      * 注意，对应的路径不能有中文
      */
    public static void main(String[] args) throws TesseractException {
        //创建实例
        ITesseract tesseract = new Tesseract();
        //设置字体库路径
        tesseract.setDatapath("E:\\tessdata");
        //设置语言，简体中文
        tesseract.setLanguage("chi_sim");

        //图片路径
        File file = new File("E:\\1.png");
        //识别图片
        String result = tesseract.doOCR(file);
        System.out.println("识别的结果为："+result);
    }
}
