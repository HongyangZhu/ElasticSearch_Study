package com.zhuhy;

import com.zhuhy.utlis.HtmlPareseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EsJdApplicationTests {

    @Test
    void contextLoads() throws IOException {
        new HtmlPareseUtil().parseJD("hello").forEach(System.out::println);
    }

}
