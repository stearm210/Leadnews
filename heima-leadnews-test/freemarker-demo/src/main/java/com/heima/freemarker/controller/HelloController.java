package com.heima.freemarker.controller;

import com.heima.freemarker.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
