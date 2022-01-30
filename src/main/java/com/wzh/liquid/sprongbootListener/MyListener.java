package com.wzh.liquid.sprongbootListener;

import com.wzh.liquid.service.TcpSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author WZH
 * @Description
 * @date 2022/1/22 - 13:18
 **/
@Component
public class MyListener implements CommandLineRunner {
    @Autowired
    TcpSocketService tcpSocketService;
    @Override
    public void run(String... args) throws Exception {
        tcpSocketService.getTcpSocket();
    }
}
