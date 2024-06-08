package com.example.travel.service;

import com.example.travel.entity.Team;
import com.example.travel.entity.TeamMember;
import com.example.travel.entity.User;
import com.example.travel.repository.TeamMemberRepository;
import com.example.travel.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team createTeam(Team team) {
        team.setCurrentMembers(0); // 初始成员数为0
        team.setClosed(false); // 默认队伍是开放的
        return teamRepository.save(team);
    }

    public void addTeamMember(Long teamId, Long userId, String role) {
        TeamMember teamMember = new TeamMember();
        teamMember.setTeamId(teamId);
        teamMember.setUserId(userId);
        teamMember.setRole(role);
        teamMemberRepository.save(teamMember);
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    public Team updateTeam(Team team) {
        // Retrieve existing team from the repository
        Team existingTeam = teamRepository.findById(team.getId()).orElse(null);

        if (existingTeam != null) {
            // Update team information
            existingTeam.setDestination(team.getDestination());
            existingTeam.setDepartureTime(team.getDepartureTime());
            existingTeam.setMaxMembers(team.getMaxMembers());

            // Save the updated team to the repository
            return teamRepository.save(existingTeam);
        } else {
            return null; // Team does not exist
        }
    }
}
