package com.memonote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.common.Result;
import com.memonote.config.JwtUtil;
import com.memonote.dto.LoginDTO;
import com.memonote.dto.RegisterDTO;
import com.memonote.entity.User;
import com.memonote.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Result<Map<String, Object>> login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = getOne(wrapper);

        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("role", user.getRole());

        return Result.success(data);
    }

    public Result<Void> register(RegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (count(wrapper) > 0) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole("user");
        user.setAvatar("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=simple%20user%20avatar%20icon&image_size=square");

        save(user);
        return Result.success();
    }

    public Result<User> getUserInfo(Long userId) {
        User user = getById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    public Result<Void> updateUser(Long userId, User user) {
        user.setId(userId);
        user.setPassword(null);
        user.setRole(null);
        updateById(user);
        return Result.success();
    }
}
