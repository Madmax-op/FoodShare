// FoodShare Authentication JavaScript
class FoodShareAuth {
    constructor() {
        this.baseUrl = 'http://localhost:8080/api';
        this.currentUser = null;
        this.token = localStorage.getItem('foodshare_token');
        
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupTabSwitching();
        this.checkAuthStatus();
    }

    setupEventListeners() {
        // Login form
        const loginForm = document.getElementById('loginForm');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => this.handleLogin(e));
        }

        // Donor registration form
        const donorForm = document.getElementById('donorForm');
        if (donorForm) {
            donorForm.addEventListener('submit', (e) => this.handleDonorRegistration(e));
        }

        // NGO registration form
        const ngoForm = document.getElementById('ngoForm');
        if (ngoForm) {
            ngoForm.addEventListener('submit', (e) => this.handleNGORegistration(e));
        }

        // Password toggles
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('password-toggle')) {
                this.togglePassword(e.target);
            }
        });
    }

    setupTabSwitching() {
        const tabBtns = document.querySelectorAll('.tab-btn');
        const forms = document.querySelectorAll('.registration-form');

        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const targetTab = btn.dataset.tab;
                
                // Update active tab button
                tabBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                
                // Show corresponding form
                forms.forEach(form => {
                    form.classList.remove('active');
                    if (form.dataset.type === targetTab) {
                        form.classList.add('active');
                    }
                });
            });
        });
    }

    async handleLogin(e) {
        e.preventDefault();
        
        const form = e.target;
        const submitBtn = form.querySelector('.auth-submit-btn');
        const formData = new FormData(form);
        
        const loginData = {
            email: formData.get('email'),
            password: formData.get('password')
        };

        // Validation
        if (!this.validateLoginData(loginData)) {
            return;
        }

        try {
            this.setLoading(submitBtn, true);
            
            const response = await this.login(loginData);
            
            if (response.success) {
                this.showMessage('Login successful! Redirecting...', 'success');
                this.storeAuthData(response.data);
                
                // Redirect to dashboard after short delay
                setTimeout(() => {
                    window.location.href = 'dashboard.html';
                }, 1500);
            } else {
                this.showMessage(response.message || 'Login failed', 'error');
            }
        } catch (error) {
            console.error('Login error:', error);
            this.showMessage('Login failed. Please try again.', 'error');
        } finally {
            this.setLoading(submitBtn, false);
        }
    }

    async handleDonorRegistration(e) {
        e.preventDefault();
        
        const form = e.target;
        const submitBtn = form.querySelector('.auth-submit-btn');
        const formData = new FormData(form);
        
        const donorData = {
            name: formData.get('name'),
            email: formData.get('email'),
            phone: formData.get('phone'),
            city: formData.get('city'),
            latitude: parseFloat(formData.get('latitude')),
            longitude: parseFloat(formData.get('longitude')),
            password: formData.get('password'),
            donorType: formData.get('donorType'),
            description: formData.get('description')
        };

        // Validation
        if (!this.validateDonorData(donorData)) {
            return;
        }

        try {
            this.setLoading(submitBtn, true);
            
            const response = await this.registerDonor(donorData);
            
            if (response.success) {
                this.showMessage('Registration successful! Please log in.', 'success');
                form.reset();
                
                // Redirect to login after short delay
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                this.showMessage(response.message || 'Registration failed', 'error');
            }
        } catch (error) {
            console.error('Registration error:', error);
            this.showMessage('Registration failed. Please try again.', 'error');
        } finally {
            this.setLoading(submitBtn, false);
        }
    }

    async handleNGORegistration(e) {
        e.preventDefault();
        
        const form = e.target;
        const submitBtn = form.querySelector('.auth-submit-btn');
        const formData = new FormData(form);
        
        const ngoData = {
            name: formData.get('name'),
            email: formData.get('email'),
            phone: formData.get('phone'),
            city: formData.get('city'),
            latitude: parseFloat(formData.get('latitude')),
            longitude: parseFloat(formData.get('longitude')),
            password: formData.get('password'),
            organizationType: formData.get('organizationType'),
            description: formData.get('description')
        };

        // Validation
        if (!this.validateNGOData(ngoData)) {
            return;
        }

        try {
            this.setLoading(submitBtn, true);
            
            const response = await this.registerNGO(ngoData);
            
            if (response.success) {
                this.showMessage('Registration successful! Please log in.', 'success');
                form.reset();
                
                // Redirect to login after short delay
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                this.showMessage(response.message || 'Registration failed', 'error');
            }
        } catch (error) {
            console.error('Registration error:', error);
            this.showMessage('Registration failed. Please try again.', 'error');
        } finally {
            this.setLoading(submitBtn, false);
        }
    }

    // Validation methods
    validateLoginData(data) {
        if (!data.email || !data.password) {
            this.showMessage('Please fill in all required fields', 'error');
            return false;
        }
        
        if (!this.isValidEmail(data.email)) {
            this.showMessage('Please enter a valid email address', 'error');
            return false;
        }
        
        return true;
    }

    validateDonorData(data) {
        if (!data.name || !data.email || !data.phone || !data.city || 
            !data.latitude || !data.longitude || !data.password || !data.donorType) {
            this.showMessage('Please fill in all required fields', 'error');
            return false;
        }
        
        if (!this.isValidEmail(data.email)) {
            this.showMessage('Please enter a valid email address', 'error');
            return false;
        }
        
        if (!this.isValidPhone(data.phone)) {
            this.showMessage('Please enter a valid phone number', 'error');
            return false;
        }
        
        if (!this.isValidCoordinates(data.latitude, data.longitude)) {
            this.showMessage('Please enter valid coordinates', 'error');
            return false;
        }
        
        return true;
    }

    validateNGOData(data) {
        if (!data.name || !data.email || !data.phone || !data.city || 
            !data.latitude || !data.longitude || !data.password || !data.organizationType) {
            this.showMessage('Please fill in all required fields', 'error');
            return false;
        }
        
        if (!this.isValidEmail(data.email)) {
            this.showMessage('Please enter a valid email address', 'error');
            return false;
        }
        
        if (!this.isValidPhone(data.phone)) {
            this.showMessage('Please enter a valid phone number', 'error');
            return false;
        }
        
        if (!this.isValidCoordinates(data.latitude, data.longitude)) {
            this.showMessage('Please enter valid coordinates', 'error');
            return false;
        }
        
        return true;
    }

    isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    isValidPhone(phone) {
        const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
        return phoneRegex.test(phone.replace(/\s/g, ''));
    }

    isValidCoordinates(lat, lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }

    // API calls
    async login(loginData) {
        try {
            const response = await fetch(`${this.baseUrl}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData)
            });

            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Login failed' };
            }
        } catch (error) {
            throw new Error('Network error during login');
        }
    }

    async registerDonor(donorData) {
        try {
            const response = await fetch(`${this.baseUrl}/auth/register/donor`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(donorData)
            });

            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Registration failed' };
            }
        } catch (error) {
            throw new Error('Network error during registration');
        }
    }

    async registerNGO(ngoData) {
        try {
            const response = await fetch(`${this.baseUrl}/auth/register/ngo`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(ngoData)
            });

            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Registration failed' };
            }
        } catch (error) {
            throw new Error('Network error during registration');
        }
    }

    // Utility methods
    setLoading(button, loading) {
        if (loading) {
            button.classList.add('loading');
            button.disabled = true;
        } else {
            button.classList.remove('loading');
            button.disabled = false;
        }
    }

    showMessage(message, type = 'info') {
        const messageContainer = document.getElementById('messageContainer');
        if (!messageContainer) return;

        messageContainer.textContent = message;
        messageContainer.className = `message-container ${type}`;
        messageContainer.style.display = 'block';

        // Auto-hide after 5 seconds
        setTimeout(() => {
            messageContainer.style.display = 'none';
        }, 5000);
    }

    storeAuthData(data) {
        this.token = data.token;
        this.currentUser = {
            id: data.userId,
            role: data.role
        };
        
        localStorage.setItem('foodshare_token', data.token);
        localStorage.setItem('foodshare_user', JSON.stringify(this.currentUser));
    }

    checkAuthStatus() {
        if (this.token) {
            // User is logged in, redirect to dashboard if on auth pages
            if (window.location.pathname.includes('login.html') || 
                window.location.pathname.includes('register.html')) {
                window.location.href = 'dashboard.html';
            }
        }
    }

    logout() {
        this.token = null;
        this.currentUser = null;
        localStorage.removeItem('foodshare_token');
        localStorage.removeItem('foodshare_user');
        window.location.href = 'index.html';
    }

    // Get current user info
    async getCurrentUser() {
        if (!this.token) return null;

        try {
            const response = await fetch(`${this.baseUrl}/auth/me`, {
                headers: {
                    'Authorization': `Bearer ${this.token}`
                }
            });

            if (response.ok) {
                const userData = await response.json();
                this.currentUser = userData;
                return userData;
            } else {
                this.logout();
                return null;
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
            this.logout();
            return null;
        }
    }
}

// Password toggle functionality
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const toggleBtn = input.parentNode.querySelector('.password-toggle');
    
    if (input.type === 'password') {
        input.type = 'text';
        toggleBtn.textContent = '🙈';
    } else {
        input.type = 'password';
        toggleBtn.textContent = '👁️';
    }
}

// Initialize authentication
document.addEventListener('DOMContentLoaded', () => {
    window.foodShareAuth = new FoodShareAuth();
    
    // Add some interactive enhancements
    addFormEnhancements();
});

// Form enhancements
function addFormEnhancements() {
    // Auto-fill coordinates with current location (if user allows)
    const locationBtns = document.querySelectorAll('[data-action="get-location"]');
    locationBtns.forEach(btn => {
        btn.addEventListener('click', getCurrentLocation);
    });

    // Real-time form validation
    const inputs = document.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.addEventListener('blur', validateField);
        input.addEventListener('input', clearFieldError);
    });
}

// Get current location
function getCurrentLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;
                
                // Find the closest form and fill coordinates
                const activeForm = document.querySelector('.registration-form.active');
                if (activeForm) {
                    const latInput = activeForm.querySelector('input[name="latitude"]');
                    const lngInput = activeForm.querySelector('input[name="longitude"]');
                    
                    if (latInput && lngInput) {
                        latInput.value = lat.toFixed(6);
                        lngInput.value = lng.toFixed(6);
                        
                        // Show success message
                        window.foodShareAuth.showMessage('Location detected and filled automatically!', 'success');
                    }
                }
            },
            (error) => {
                console.error('Error getting location:', error);
                window.foodShareAuth.showMessage('Could not detect location. Please enter coordinates manually.', 'error');
            }
        );
    } else {
        window.foodShareAuth.showMessage('Geolocation is not supported by this browser.', 'error');
    }
}

// Field validation
function validateField(e) {
    const field = e.target;
    const value = field.value.trim();
    
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, 'This field is required');
    } else if (field.type === 'email' && value && !window.foodShareAuth.isValidEmail(value)) {
        showFieldError(field, 'Please enter a valid email address');
    } else if (field.type === 'tel' && value && !window.foodShareAuth.isValidPhone(value)) {
        showFieldError(field, 'Please enter a valid phone number');
    }
}

function showFieldError(field, message) {
    clearFieldError(field);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    errorDiv.style.cssText = `
        color: #ef4444;
        font-size: 0.8rem;
        margin-top: 4px;
        animation: fadeIn 0.3s ease;
    `;
    
    field.parentNode.appendChild(errorDiv);
    field.style.borderColor = '#ef4444';
}

function clearFieldError(field) {
    const errorDiv = field.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
    field.style.borderColor = '';
}

// Add CSS for field errors
const errorStyles = `
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-5px); }
        to { opacity: 1; transform: translateY(0); }
    }
`;

const styleSheet = document.createElement('style');
styleSheet.textContent = errorStyles;
document.head.appendChild(styleSheet); 