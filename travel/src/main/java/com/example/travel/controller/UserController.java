package com.example.travel.controller;

import com.example.travel.entity.Team;
import com.example.travel.entity.User;
import com.example.travel.service.TeamService;
import com.example.travel.service.UserService;
import com.example.travel.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JwtUtil.extractUsername(token);

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Long userId = user.getId();
        List<Team> createdTeams = userService.getCreatedTeams(userId);
        List<Team> joinedTeams = userService.getJoinedTeams(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("createdTeams", createdTeams);
        System.out.println(1);
        response.put("joinedTeams", joinedTeams);
        System.out.println(1);

        for (String key : response.keySet()) {
            System.out.println(key+":"+response.get(key));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-team")
    public ResponseEntity<?> createTeam(@RequestBody Team team, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JwtUtil.extractUsername(token);
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            team.setAdmin(user); // 设置队伍管理员为当前用户
            Team createdTeam = teamService.createTeam(team); // 创建队伍
            if (createdTeam != null) {
                teamService.addTeamMember(createdTeam.getId(), user.getId(), "admin"); // 添加创建者到队伍成员列表中，角色为管理员
                return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating team");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating team: " + e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (Exception e) {
            // 打印异常信息到控制台
            e.printStackTrace();
            // 返回服务器内部错误状态码及错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        if (userService.authenticateUser(user)) {
            System.out.println(user.getUsername());
            String token = JwtUtil.generateToken(user.getUsername()); // 生成Token
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("token", token); // 将Token添加到响应中
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed. Invalid username or password.");
        }
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}