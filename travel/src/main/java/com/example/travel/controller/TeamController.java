package com.example.travel.controller;

import com.example.travel.entity.Team;
import com.example.travel.entity.User;
import com.example.travel.service.TeamService;
import com.example.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping("/createteams")
    public ResponseEntity<String> createTeam(@RequestBody Team team) {
        // 设置队伍的管理员为当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User admin = userService.findByUsername(currentPrincipalName);
        team.setAdmin(admin);

        Team createdTeam = teamService.createTeam(team);
        if (createdTeam != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("队伍创建成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("队伍创建失败，请重试。");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable Long id, @RequestBody Team updatedTeam) {
        Team existingTeam = teamService.getTeamById(id);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到指定的队伍。");
        }

        // 检查当前用户是否是队伍的管理员
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User admin = userService.findByUsername(currentPrincipalName);
        if (!existingTeam.getAdmin().equals(admin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您没有权限修改该队伍信息。");
        }

        // 更新队伍信息
        existingTeam.setDestination(updatedTeam.getDestination());
        existingTeam.setDepartureTime(updatedTeam.getDepartureTime());
        existingTeam.setMaxMembers(updatedTeam.getMaxMembers());

        Team updatedTeamEntity = teamService.updateTeam(existingTeam);
        if (updatedTeamEntity != null) {
            return ResponseEntity.status(HttpStatus.OK).body("队伍信息更新成功！");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("队伍信息更新失败，请重试。");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        Team existingTeam = teamService.getTeamById(id);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到指定的队伍。");
        }

        // 检查当前用户是否是队伍的管理员
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User admin = userService.findByUsername(currentPrincipalName);
        if (!existingTeam.getAdmin().equals(admin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("您没有权限删除该队伍。");
        }

        teamService.deleteTeam(id);
        return ResponseEntity.status(HttpStatus.OK).body("队伍删除成功！");
    }

}