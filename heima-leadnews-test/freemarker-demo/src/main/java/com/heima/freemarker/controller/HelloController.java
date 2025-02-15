package com.heima.freemarker.controller;

import com.heima.freemarker.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.freemarker.controller
 * @Author: yanhongwei
 * @CreateTime: 2025-01-08  14:54
 * @Description: TODO
 * @Version: 1.0
 */

@Controller
public class HelloController {

    @GetMapping("/basic")
    public String hello(Model model){
        //对name进行赋值
        model.addAttribute("name","freemarker");
        //对stu进行赋值
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        //信息传入模版对象stu
        model.addAttribute("stu",student);
        return "01-basic";
    }

    @GetMapping("/list")
    public String list(Model model){
        //------------------------------------
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
        model.addAttribute("stus",stus);

        //------------------------------------

        //创建Map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        // 3.1 向model中存放Map数据
        model.addAttribute("stuMap", stuMap);

        //内置函数例子添加
        model.addAttribute("today", new Date());

        //常数值函数添加
        model.addAttribute("point", 12313421414L);




        return "02-list";
    }


}
