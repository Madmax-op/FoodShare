# 🔐 FoodShare Authentication System

This document explains the frontend authentication system that connects to the Spring Security backend.

## 📁 Files Overview

- **`login.html`** - User login form
- **`register.html`** - User registration with tabs for NGO/Donor
- **`dashboard.html`** - Post-login dashboard
- **`auth-styles.css`** - Beautiful styling for authentication forms
- **`auth.js`** - JavaScript functionality and API integration

## 🚀 Features

### ✨ Beautiful UI/UX
- **Modern Design**: Glassmorphism effects with backdrop blur
- **Responsive Layout**: Works perfectly on all device sizes
- **Smooth Animations**: Hover effects, loading states, and transitions
- **Interactive Elements**: Password toggles, form validation, and real-time feedback

### 🔒 Security Features
- **JWT Integration**: Connects to Spring Security backend
- **Form Validation**: Client-side and server-side validation
- **Secure Storage**: Tokens stored in localStorage with proper handling
- **Auto-redirect**: Prevents authenticated users from accessing auth pages

### 📱 User Experience
- **Tab-based Registration**: Easy switching between NGO and Donor forms
- **Location Detection**: Automatic GPS coordinate filling
- **Real-time Validation**: Instant feedback on form inputs
- **Loading States**: Visual feedback during API calls

## 🔗 Backend Integration

### API Endpoints
The frontend connects to these Spring Security endpoints:

```javascript
// Base URL (configurable)
baseUrl: 'http://localhost:8080/api'

// Authentication endpoints
POST /auth/login              // User login
POST /auth/register/donor     // Donor registration
POST /auth/register/ngo       // NGO registration
GET  /auth/me                 // Get current user info
```

### Request/Response Format

#### Login Request
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Login Response
```json
{
  "message": "Login successful",
  "userId": 123,
  "token": "jwt_token_here",
  "role": "DONOR"
}
```

#### Registration Request (Donor)
```json
{
  "name": "Restaurant Name",
  "email": "restaurant@example.com",
  "phone": "+1234567890",
  "city": "New York",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "password": "password123",
  "donorType": "RESTAURANT",
  "description": "Fine dining restaurant"
}
```

## 🎨 Customization

### Styling
The authentication system uses a custom CSS file (`auth-styles.css`) with:

- **Color Scheme**: Green primary (#10b981) with gradient backgrounds
- **Typography**: Inter font family for modern readability
- **Spacing**: Consistent 8px grid system
- **Shadows**: Subtle depth with box-shadow properties

### Themes
You can easily customize colors by modifying CSS variables:

```css
:root {
  --primary-color: #10b981;
  --secondary-color: #059669;
  --error-color: #ef4444;
  --success-color: #10b981;
}
```

## 📱 Responsive Design

### Breakpoints
- **Desktop**: 1200px+ (Full layout with side-by-side forms)
- **Tablet**: 768px - 1199px (Stacked form rows)
- **Mobile**: 480px - 767px (Single column layout)
- **Small Mobile**: <480px (Compact spacing)

### Mobile Optimizations
- Touch-friendly button sizes (44px minimum)
- Optimized form spacing for mobile keyboards
- Swipe-friendly tab navigation
- Responsive typography scaling

## 🔧 Configuration

### Environment Variables
Set your backend URL in `auth.js`:

```javascript
constructor() {
    this.baseUrl = process.env.BACKEND_URL || 'http://localhost:8080/api';
    // ... rest of constructor
}
```

### CORS Settings
Ensure your Spring Boot backend allows requests from your frontend domain:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // ... other CORS settings
    }
}
```

## 🚀 Getting Started

### 1. Start Backend
```bash
cd backend
./mvnw spring-boot:run
```

### 2. Open Frontend
```bash
cd frontend
# Open login.html in browser
# Or serve with a local server
python -m http.server 8000
```

### 3. Test Authentication
1. **Register**: Create a new NGO or Donor account
2. **Login**: Sign in with your credentials
3. **Dashboard**: View your personalized dashboard
4. **Logout**: Sign out and return to home

## 🧪 Testing

### Manual Testing
- Test form validation with invalid inputs
- Verify API error handling
- Check responsive design on different screen sizes
- Test location detection (requires HTTPS in production)

### Browser Testing
- **Chrome**: Full feature support
- **Firefox**: Full feature support
- **Safari**: Full feature support
- **Edge**: Full feature support

## 🐛 Troubleshooting

### Common Issues

#### CORS Errors
```
Access to fetch at 'http://localhost:8080/api/auth/login' from origin 'http://localhost:8000' has been blocked by CORS policy
```
**Solution**: Ensure backend CORS configuration allows your frontend origin.

#### JWT Token Issues
```
JWT signature does not match locally computed signature
```
**Solution**: Verify JWT secret configuration in backend `application.properties`.

#### Form Not Submitting
**Check**: Browser console for JavaScript errors, network tab for failed requests.

### Debug Mode
Enable debug logging in `auth.js`:

```javascript
// Add this to constructor
this.debug = true;

// Add logging to API calls
if (this.debug) {
    console.log('API Request:', { url, method, data });
}
```

## 🔮 Future Enhancements

### Planned Features
- **Social Login**: Google, Facebook integration
- **Two-Factor Authentication**: SMS/Email verification
- **Password Reset**: Email-based password recovery
- **Profile Management**: Edit user information
- **Session Management**: Multiple device handling

### Technical Improvements
- **Service Workers**: Offline support
- **Progressive Web App**: Installable app experience
- **Advanced Validation**: Custom validation rules
- **Accessibility**: ARIA labels and screen reader support

## 📚 Resources

### Documentation
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - JWT token debugging
- [MDN Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)

### Design Inspiration
- [Glassmorphism CSS](https://css.glass/)
- [Modern Form Design](https://www.smashingmagazine.com/2011/11/extensive-guide-web-form-usability/)
- [Responsive Design Patterns](https://www.lukew.com/ff/entry.asp?1514)

---

**Note**: This authentication system is designed to work seamlessly with the Spring Boot backend. Ensure both services are running and properly configured for full functionality. 