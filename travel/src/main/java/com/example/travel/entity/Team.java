package com.example.travel.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private int maxMembers;

    @Column(nullable = false)
    private int currentMembers = 0;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Column(nullable = false)
    private boolean isClosed = false;

    public int getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
    }

    public boolean isClosed() {
        return isClosed;
    }

}
