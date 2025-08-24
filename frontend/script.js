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
                </div>
                <div class="donation-amount">${entry.donationAmount}</div>
                <div class="donation-value">${entry.donationValue}</div>
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
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "2,450 meals", donationValue: "₹24,500" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "1,890 meals", donationValue: "₹18,900" },
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "1,650 meals", donationValue: "₹16,500" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "1,420 meals", donationValue: "₹14,200" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "1,200 meals", donationValue: "₹12,000" }
            ],
            yearly: [
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "18,750 meals", donationValue: "₹187,500" },
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "16,200 meals", donationValue: "₹162,000" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "14,800 meals", donationValue: "₹148,000" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "12,600 meals", donationValue: "₹126,000" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "11,400 meals", donationValue: "₹114,000" }
            ],
            'all-time': [
                { donorName: "IIT Delhi Mess", donorCategory: "Educational Institution", donationAmount: "156,800 meals", donationValue: "₹1,568,000" },
                { donorName: "Delhi University Hostel #5", donorCategory: "Student Hostel", donationAmount: "142,300 meals", donationValue: "₹1,423,000" },
                { donorName: "Spice Garden Restaurant", donorCategory: "Restaurant", donationAmount: "128,900 meals", donationValue: "₹1,289,000" },
                { donorName: "Radisson Blu Hotel", donorCategory: "Hotel", donationAmount: "115,600 meals", donationValue: "₹1,156,000" },
                { donorName: "Vivek Raj Sahay", donorCategory: "Individual Donor", donationAmount: "98,400 meals", donationValue: "₹984,000" }
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
