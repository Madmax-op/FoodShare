package com.ignithon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignithon.dto.LeaderboardEntry;
import com.ignithon.service.LeaderboardService;

@RestController
@RequestMapping("/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/monthly")
    public ResponseEntity<List<LeaderboardEntry>> getMonthlyLeaderboard() {
        try {
            List<LeaderboardEntry> leaderboard = leaderboardService.getMonthlyLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/yearly")
    public ResponseEntity<List<LeaderboardEntry>> getYearlyLeaderboard() {
        try {
            List<LeaderboardEntry> leaderboard = leaderboardService.getYearlyLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all-time")
    public ResponseEntity<List<LeaderboardEntry>> getAllTimeLeaderboard() {
        try {
            List<LeaderboardEntry> leaderboard = leaderboardService.getAllTimeLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
