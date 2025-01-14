import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import com.heima.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: PACKAGE_NAME
 * @Author: yanhongwei
 * @CreateTime: 2025-01-14  15:05
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GreenImageScan greenImageScan;

//    测试文本内容审核
    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("我是好人");
        System.out.println(map);
    }

//    测试图片审核
    @Test
    public void testScanImage() throws Exception {
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.200.130:9000/leadnews/2025/01/11/94859a0e796c4164851f9f1351bfd261.png");
        Map map = greenImageScan.imageScan(Arrays.asList(bytes));
        System.out.println(map);
    }

}
