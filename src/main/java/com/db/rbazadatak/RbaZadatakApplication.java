package com.db.rbazadatak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class RbaZadatakApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbaZadatakApplication.class, args);
    }

}
