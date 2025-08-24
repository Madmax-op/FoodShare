package com.ignithon.dto;

public class LeaderboardEntry {
    private String rank;
    private String donorName;
    private String donorCategory;
    private String donationAmount;
    private String donationValue;
    private String rankIcon;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String rank, String donorName, String donorCategory, String donationAmount, String donationValue, String rankIcon) {
        this.rank = rank;
        this.donorName = donorName;
        this.donorCategory = donorCategory;
        this.donationAmount = donationAmount;
        this.donationValue = donationValue;
        this.rankIcon = rankIcon;
    }

    // Getters and Setters
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }

    public String getDonorCategory() { return donorCategory; }
    public void setDonorCategory(String donorCategory) { this.donorCategory = donorCategory; }

    public String getDonationAmount() { return donationAmount; }
    public void setDonationAmount(String donationAmount) { this.donationAmount = donationAmount; }

    public String getDonationValue() { return donationValue; }
    public void setDonationValue(String donationValue) { this.donationValue = donationValue; }

    public String getRankIcon() { return rankIcon; }
    public void setRankIcon(String rankIcon) { this.rankIcon = rankIcon; }
}
