package com.example.travel.repository;

import com.example.travel.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT tm FROM TeamMember tm WHERE tm.user.id = :userId AND tm.role = :role")
    List<TeamMember> findByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);
}
