package com.ignithon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ignithon.dto.LeaderboardEntry;

@Service
public class LeaderboardService {

    public List<LeaderboardEntry> getMonthlyLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        
        entries.add(new LeaderboardEntry("1", "Delhi University Hostel #5", "Student Hostel", "2,450 meals", "₹24,500", "🥇"));
        entries.add(new LeaderboardEntry("2", "Spice Garden Restaurant", "Restaurant", "1,890 meals", "₹18,900", "🥈"));
        entries.add(new LeaderboardEntry("3", "IIT Delhi Mess", "Educational Institution", "1,650 meals", "₹16,500", "🥉"));
        entries.add(new LeaderboardEntry("4", "Radisson Blu Hotel", "Hotel", "1,420 meals", "₹14,200", "4"));
        entries.add(new LeaderboardEntry("5", "Vivek Raj Sahay", "Individual Donor", "1,200 meals", "₹12,000", "5"));
        
        return entries;
    }

    public List<LeaderboardEntry> getYearlyLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        
        entries.add(new LeaderboardEntry("1", "IIT Delhi Mess", "Educational Institution", "18,750 meals", "₹187,500", "🥇"));
        entries.add(new LeaderboardEntry("2", "Delhi University Hostel #5", "Student Hostel", "16,200 meals", "₹162,000", "🥈"));
        entries.add(new LeaderboardEntry("3", "Spice Garden Restaurant", "Restaurant", "14,800 meals", "₹148,000", "🥉"));
        entries.add(new LeaderboardEntry("4", "Radisson Blu Hotel", "Hotel", "12,600 meals", "₹126,000", "4"));
        entries.add(new LeaderboardEntry("5", "Vivek Raj Sahay", "Individual Donor", "11,400 meals", "₹114,000", "5"));
        
        return entries;
    }

    public List<LeaderboardEntry> getAllTimeLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        
        entries.add(new LeaderboardEntry("1", "IIT Delhi Mess", "Educational Institution", "156,800 meals", "₹1,568,000", "🥇"));
        entries.add(new LeaderboardEntry("2", "Delhi University Hostel #5", "Student Hostel", "142,300 meals", "₹1,423,000", "🥈"));
        entries.add(new LeaderboardEntry("3", "Spice Garden Restaurant", "Restaurant", "128,900 meals", "₹1,289,000", "🥉"));
        entries.add(new LeaderboardEntry("4", "Radisson Blu Hotel", "Hotel", "115,600 meals", "₹1,156,000", "4"));
        entries.add(new LeaderboardEntry("5", "Vivek Raj Sahay", "Individual Donor", "98,400 meals", "₹984,000", "5"));
        
        return entries;
    }
}
