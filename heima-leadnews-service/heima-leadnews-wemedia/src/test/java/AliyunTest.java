import com.heima.common.aliyun.GreenTextScan;
import com.heima.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

//    测试文本内容审核
    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("我是好人");
        System.out.println(map);
    }

//    测试图片审核
    @Test
    public void testScanImage(){

    }

}
