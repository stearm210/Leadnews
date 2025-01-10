package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import com.heima.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.minio.test
 * @Author: yanhongwei
 * @CreateTime: 2025-01-10  15:01
 * @Description: TODO
 * @Version: 1.0
 */

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-01-10 15:03:15
  * @Params:
  * @Return: null
  * @Description: 上传文件至minio中，并且可以在浏览器中访问
  */

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {

    //注入minioClient上传模块
    @Autowired
    private FileStorageService fileStorageService;

     /*
      * @Title: test
      * @Author: pyzxW
      * @Date: 2025-01-10 15:30:39
      * @Params:
      * @Return: null
      * @Description: 上传到对应的文件夹内
      */
    @Test
    public void test() throws FileNotFoundException {
        //上传文件，通过封装好的模版进行调用
       FileInputStream fileInputStream = new FileInputStream("E:\\java study\\knowledge_picture\\Test_output\\list.html");
       String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
       System.out.println(path);
    }



//    public static void main(String[] args) {
//        try {
//            //需要上传的文件路径
//            FileInputStream fileInputStream = new FileInputStream("E:\\java study\\knowledge_picture\\Test_output\\list.html");
//
//            //1.获取minio的连接信息，创建一个minio的客户端
//            MinioClient minioClient = MinioClient.builder().credentials("minio", "minio123").endpoint("http://192.168.200.130:9000").build();
//            //2.上传
//            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
//                    .object("list.html")//文件名称
//                    .contentType("text/html")//文件类型
//                    .bucket("leadnews")//已经创建的桶名称，与minio界面中创建的一致
//                    .stream(fileInputStream, fileInputStream.available(), -1).build();
//            minioClient.putObject(putObjectArgs);
//
//            //访问路径
//           System.out.println("http://192.168.200.130:9000/leadnews/list.html");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
