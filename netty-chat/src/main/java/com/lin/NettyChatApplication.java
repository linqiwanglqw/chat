package com.lin;

import com.lin.netty.chat.ChatServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyChatApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(NettyChatApplication.class, args);
        ChatServerApplication bean = run.getBean(ChatServerApplication.class);
        bean.statr();
    }

}
