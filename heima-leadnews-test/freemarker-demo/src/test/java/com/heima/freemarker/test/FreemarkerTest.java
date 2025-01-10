package com.heima.freemarker.test;

import com.heima.freemarker.FreemarkerDemoApplication;
import com.heima.freemarker.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.freemarker.test
 * @Author: yanhongwei
 * @CreateTime: 2025-01-10  10:48
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = FreemarkerDemoApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {
    @Autowired
    private Configuration configuration;

    //获取静态文件的测试方法
    //根据代码生成对应文件的操作
    @Test
    public void test() throws IOException, TemplateException {
        Template template = configuration.getTemplate("02-list.ftl");
        
         /*
          * @Title: test
          * @Author: pyzxW
          * @Date: 2025-01-10 11:05:41
          * @Params: [] 
          * @Return: void
          * @Description: 合成方法
          */
        //第一个参数是模型数据，第二个参数是输出流
        template.process(getDate(),new FileWriter("E:\\java study\\knowledge_picture\\黑马头条\\Test_output"));
    }

    private Map getDate(){
        Map<String,Object> map = new HashMap<>();
        Student stu1 = new Student();
        stu1.setName("小强");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        //小红对象模型数据
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);

        //将两个对象模型数据存放到List集合中
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        //向model中存放List集合数据
        map.put("stus", stus);

        //------------------------------------

        //创建Map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        // 3.1 向model中存放Map数据
        map.put("stuMap", stuMap);
        //内置函数例子添加
        map.put("today", new Date());
        //常数值函数添加
        map.put("point", 12313421414L);

        return map;
    }
}
