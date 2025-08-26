// Leaderboard Tab Functionality
document.addEventListener('DOMContentLoaded', function() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    // Load initial leaderboard data
    loadLeaderboardData('monthly');
    
    tabBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');
            
            // Remove active class from all buttons and contents
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Show corresponding content
            const targetContent = document.getElementById(targetTab);
            if (targetContent) {
                targetContent.classList.add('active');
            }
            
            // Load data for the selected tab
            loadLeaderboardData(targetTab);
        });
    });
    
    // Track loaded data to prevent duplicate loading
    const loadedData = new Set();
    
    // Function to load leaderboard data from backend
    async function loadLeaderboardData(period) {
        // Check if data is already loaded for this period
        if (loadedData.has(period)) {
            return;
        }
        
        try {
            const response = await fetch(`http://localhost:8080/api/leaderboard/${period}`);
            if (response.ok) {
                const data = await response.json();
                renderLeaderboard(period, data);
                loadedData.add(period);
            } else {
                console.error('Failed to fetch leaderboard data');
                // Fallback to sample data
                loadSampleData(period);
                loadedData.add(period);
            }
        } catch (error) {
            console.error('Error fetching leaderboard data:', error);
            // Fallback to sample data
            loadSampleData(period);
            loadedData.add(period);
        }
    }
    
    // Function to render leaderboard in horizontal grid
    function renderLeaderboard(period, data) {
        const gridElement = document.getElementById(`${period}-grid`);
        if (!gridElement) return;
        
        // Clear existing content to prevent duplicates
        gridElement.innerHTML = '';
        
        // Create a Set to track unique entries and prevent duplicates
        const uniqueEntries = new Set();
        
        data.forEach((entry, index) => {
            // Create a unique key for each entry
            const entryKey = `${entry.donorName}-${entry.donorCategory}`;
            
            // Skip if we've already processed this entry
            if (uniqueEntries.has(entryKey)) {
                return;
            }
            
            uniqueEntries.add(entryKey);
            
            const rankClass = index < 3 ? `rank-${index + 1}` : '';
            const rankIcon = entry.rankIcon || (index < 3 ? ['🥇', '🥈', '🥉'][index] : (index + 1).toString());
            
            const leaderboardItem = document.createElement('div');
            leaderboardItem.className = `leaderboard-item ${rankClass}`;
            leaderboardItem.innerHTML = `
                <div class="rank">${rankIcon}</div>
                <div class="donor-details">
                    <h4>${entry.donorName}</h4>
                    <p class="donor-category">${entry.donorCategory}</p>
                    <div class="donor-stats">
                        <span class="stat">📍 ${entry.location}</span>
                        <span class="stat">📅 ${entry.donations} donations</span>
                        <span class="stat">🌱 ${entry.impact}</span>
                    </div>
                </div>
                <div class="donation-stats">
                    <div class="donation-amount">${entry.donationAmount}</div>
                    <div class="donation-value">${entry.donationValue}</div>
                </div>
            `;
            
            gridElement.appendChild(leaderboardItem);
        });
        
        // Add animation to new items
        animateLeaderboardItems();
    }
    
    // Fallback sample data function
    function loadSampleData(period) {
        const sampleData = {
            monthly: [
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "2,450 meals", donationValue: "₹24,500", location: "Delhi", donations: "15", impact: "1.2 tons saved" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "1,890 meals", donationValue: "₹18,900", location: "Mumbai", donations: "12", impact: "0.9 tons saved" },
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "1,650 meals", donationValue: "₹16,500", location: "Delhi", donations: "18", impact: "0.8 tons saved" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "1,420 meals", donationValue: "₹14,200", location: "Bangalore", donations: "10", impact: "0.7 tons saved" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "1,200 meals", donationValue: "₹12,000", location: "Pune", donations: "8", impact: "0.6 tons saved" },
                { donorName: "Taj Palace Hotel", donorCategory: "Hotel", donationAmount: "1,150 meals", donationValue: "₹11,500", location: "Delhi", donations: "9", impact: "0.6 tons saved" },
                { donorName: "Amity University Canteen", donorCategory: "Educational Institution", donationAmount: "980 meals", donationValue: "₹9,800", location: "Noida", donations: "7", impact: "0.5 tons saved" },
                { donorName: "Pizza Hut Express", donorCategory: "Restaurant", donationAmount: "850 meals", donationValue: "₹8,500", location: "Chennai", donations: "6", impact: "0.4 tons saved" }
            ],
            yearly: [
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "18,750 meals", donationValue: "₹187,500", location: "Delhi", donations: "156", impact: "9.4 tons saved" },
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "16,200 meals", donationValue: "₹162,000", location: "Delhi", donations: "142", impact: "8.1 tons saved" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "14,800 meals", donationValue: "₹148,000", location: "Mumbai", donations: "128", impact: "7.4 tons saved" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "12,600 meals", donationValue: "₹126,000", location: "Bangalore", donations: "98", impact: "6.3 tons saved" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "11,400 meals", donationValue: "₹114,000", location: "Pune", donations: "95", impact: "5.7 tons saved" },
                { donorName: "Taj Palace Hotel", donorCategory: "Hotel", donationAmount: "10,800 meals", donationValue: "₹108,000", location: "Delhi", donations: "89", impact: "5.4 tons saved" },
                { donorName: "Amity University Canteen", donorCategory: "Educational Institution", donationAmount: "9,600 meals", donationValue: "₹96,000", location: "Noida", donations: "78", impact: "4.8 tons saved" },
                { donorName: "Pizza Hut Express", donorCategory: "Restaurant", donationAmount: "8,900 meals", donationValue: "₹89,000", location: "Chennai", donations: "72", impact: "4.5 tons saved" }
            ],
            'all-time': [
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "156,800 meals", donationValue: "₹1,568,000", location: "Delhi", donations: "1,245", impact: "78.4 tons saved" },
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "142,300 meals", donationValue: "₹1,423,000", location: "Delhi", donations: "1,156", impact: "71.2 tons saved" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "128,900 meals", donationValue: "₹1,289,000", location: "Mumbai", donations: "1,089", impact: "64.5 tons saved" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "115,600 meals", donationValue: "₹1,156,000", location: "Bangalore", donations: "987", impact: "57.8 tons saved" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "98,400 meals", donationValue: "₹984,000", location: "Pune", donations: "856", impact: "49.2 tons saved" },
                { donorName: "Taj Palace Hotel", donorCategory: "Hotel", donationAmount: "92,300 meals", donationValue: "₹923,000", location: "Delhi", donations: "789", impact: "46.2 tons saved" },
                { donorName: "Amity University Canteen", donorCategory: "Educational Institution", donationAmount: "87,600 meals", donationValue: "₹876,000", location: "Noida", donations: "745", impact: "43.8 tons saved" },
                { donorName: "Pizza Hut Express", donorCategory: "Restaurant", donationAmount: "76,800 meals", donationValue: "₹768,000", location: "Chennai", donations: "678", impact: "38.4 tons saved" }
            ]
        };
        
        renderLeaderboard(period, sampleData[period]);
    }
    
    // Ensure all components have proper end-to-end sizing
    function adjustComponentSizing() {
        const container = document.querySelector('.container');
        const sections = document.querySelectorAll('section');
        
        // Set container to full width
        container.style.maxWidth = '100%';
        container.style.padding = '0';
        
        // Ensure sections span full width
        sections.forEach(section => {
            section.style.width = '100%';
            section.style.marginLeft = '0';
            section.style.marginRight = '0';
        });
        
        // Adjust header for full width
        const header = document.querySelector('.header');
        if (header) {
            header.style.width = '100%';
            header.style.maxWidth = '100%';
        }
        
        // Adjust footer for full width
        const footer = document.querySelector('.footer');
        if (footer) {
            footer.style.width = '100%';
            footer.style.maxWidth = '100%';
        }
    }
    
    // Call sizing function on load and resize
    adjustComponentSizing();
    window.addEventListener('resize', adjustComponentSizing);
    
    // Add smooth scrolling for navigation links
    const navLinks = document.querySelectorAll('.nav-link[href^="#"]');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            
            if (targetSection) {
                targetSection.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

// Add animation for leaderboard items
function animateLeaderboardItems() {
    const items = document.querySelectorAll('.leaderboard-item');
    items.forEach((item, index) => {
        item.style.animationDelay = `${index * 0.1}s`;
        item.classList.add('fadeInUp');
    });
}

// Add CSS animation class
const style = document.createElement('style');
style.textContent = `
    .fadeInUp {
        animation: fadeInUp 0.6s ease-out forwards;
        opacity: 0;
    }
    
    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
`;
document.head.appendChild(style);

// Initialize animations when page loads
window.addEventListener('load', function() {
    animateLeaderboardItems();
});
