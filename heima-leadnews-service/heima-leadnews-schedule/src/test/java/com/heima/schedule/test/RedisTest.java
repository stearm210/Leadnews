package com.heima.schedule.test;

import com.heima.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.schedule.test
 * @Author: yanhongwei
 * @CreateTime: 2025-01-20  15:20
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    @Test
    public void testList(){
    }

    @Test
    public void testZset(){

    }
}
