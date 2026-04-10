package com.memonote.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.memonote.entity.User;
import com.memonote.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initAdminUser();
    }
    
    private void initAdminUser() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, "admin");
        
        if (userMapper.selectCount(wrapper) == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNickname("系统管理员");
            admin.setEmail("admin@memonote.com");
            admin.setRole(1);
            admin.setStatus(1);
            
            userMapper.insert(admin);
            System.out.println("======================================");
            System.out.println("管理员账号初始化完成");
            System.out.println("用户名: admin");
            System.out.println("密码: admin123");
            System.out.println("======================================");
        }
    }
}
