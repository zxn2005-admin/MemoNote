package com.memonote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.memonote.mapper")
public class MemoNoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemoNoteApplication.class, args);
    }
}
