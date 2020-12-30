package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@SpringBootApplication
@MapperScan("com.wx.*.mapper")
public class WxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
            "   ______    ___     __ \n" +
            "  |___  /   | \\ \\   / / \n" +
            "     / /    | |\\ \\_/ /  \n" +
            "    / / _   | | \\   /    \n" +
            "   / /_| |__| |  | |     \n" +
            "  /_____\\____/   |_|    \n" +
            "                       \n"
        );
    }
}
