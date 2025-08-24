# 🌍 FoodShare - Food Donation Management System

A comprehensive food donation platform that connects donors with NGOs and volunteers to reduce food waste and help those in need.

## 🚀 Live Demo

**Frontend**: [https://foodshare-app.onrender.com](https://foodshare-app.onrender.com)  
**Backend API**: [https://foodshare-app.onrender.com/api](https://foodshare-app.onrender.com/api)  
**ML Service**: [https://foodshare-app.onrender.com/ml](https://foodshare-app.onrender.com/ml)

## ✨ Features

### 🍽️ **Core Functionality**
- **User Authentication** - Secure JWT-based login/registration
- **Donation Management** - Create, track, and manage food donations
- **NGO Connections** - Find and connect with nearby NGOs
- **Real-time Analytics** - Track your donation impact and achievements
- **ML Predictions** - AI-powered food spoilage prediction
- **Responsive Design** - Works on all devices

### 🎯 **User Roles**
- **Donors** - Individuals and businesses donating food
- **NGOs** - Organizations receiving and distributing food
- **Volunteers** - People helping with food collection and delivery
- **Admins** - System administrators and moderators

### 📊 **Dashboard Features**
- **Interactive Charts** - Visualize donation trends and impact
- **Achievement Badges** - Gamified donation tracking
- **Real-time Stats** - Live updates on donations and meals served
- **NGO Directory** - Comprehensive database of food organizations

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   ML Service    │
│   (HTML/CSS/JS) │◄──►│   (Spring Boot) │◄──►│   (Python)      │
│   Port 3000     │    │   Port 8080     │    │   Port 5000     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   Database      │
                    └─────────────────┘
```

## 🛠️ Technology Stack

### **Frontend**
- **HTML5** - Semantic markup
- **CSS3** - Modern styling with Flexbox/Grid
- **JavaScript (ES6+)** - Dynamic functionality
- **Chart.js** - Interactive data visualization

### **Backend**
- **Java 17** - Core application logic
- **Spring Boot 3.x** - RESTful API framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database

### **ML Service**
- **Python 3.9** - Machine learning algorithms
- **Flask** - Lightweight web framework
- **Scikit-learn** - ML model training and prediction
- **Pandas/NumPy** - Data processing

### **Infrastructure**
- **Docker** - Containerization
- **Nginx** - Reverse proxy and static file serving
- **Render** - Cloud deployment platform

## 🚀 Quick Start

### **Prerequisites**
- Java 17 or higher
- Maven 3.8+
- Python 3.9+
- Docker Desktop
- PostgreSQL (optional for local development)

### **Local Development**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/foodshare.git
   cd foodshare
   ```

2. **Start with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - ML Service: http://localhost:5000

### **Manual Setup**

1. **Backend Setup**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

2. **ML Service Setup**
   ```bash
   cd ml-prediction
   pip install -r requirements.txt
   python app.py
   ```

3. **Frontend Setup**
   ```bash
   cd frontend
   python -m http.server 3000
   ```

## 🌐 Deployment

### **Render Deployment**

This project is configured for easy deployment on Render:

1. **Fork/Clone** this repository
2. **Connect** to Render dashboard
3. **Create Web Service** with Docker environment
4. **Set Environment Variables** (see deployment guide)
5. **Deploy** automatically

### **Environment Variables**

```bash
# Database
DATABASE_URL=postgresql://user:pass@host:port/db
DB_USERNAME=foodshare_user
DB_PASSWORD=your_secure_password

# Security
JWT_SECRET=your_super_secure_jwt_secret_key

# CORS
CORS_ORIGINS=https://your-app-name.onrender.com

# ML Service
ML_SERVICE_URL=https://your-ml-service.onrender.com
```

### **Docker Deployment**

```bash
# Build the image
docker build -t foodshare .

# Run the container
docker run -p 80:80 -p 8080:8080 -p 5000:5000 foodshare
```

## 📁 Project Structure

```
FoodShare/
├── frontend/                 # Frontend application
│   ├── index.html           # Landing page
│   ├── login.html           # Login page
│   ├── register.html        # Registration page
│   ├── dashboard.html       # Main dashboard
│   ├── analytics.html       # Analytics and charts
│   ├── ngos.html           # NGO connections
│   ├── donations.html       # Donation management
│   ├── profile.html         # User profile
│   ├── styles.css           # Main stylesheet
│   └── js/                  # JavaScript files
├── backend/                  # Spring Boot application
│   ├── src/main/java/       # Java source code
│   ├── src/main/resources/  # Configuration files
│   └── pom.xml              # Maven configuration
├── ml-prediction/           # ML service
│   ├── app.py               # Flask application
│   └── requirements.txt     # Python dependencies
├── docker-compose.yml       # Local development setup
├── Dockerfile               # Production Docker build
├── render.yaml              # Render deployment config
├── nginx.conf               # Nginx configuration
└── start-production.sh      # Production startup script
```

## 🔧 Configuration

### **Database Configuration**
- **Development**: H2 in-memory database
- **Production**: PostgreSQL with connection pooling
- **Docker**: PostgreSQL container with persistent storage

### **Security Features**
- **JWT Authentication** - Secure token-based auth
- **CORS Protection** - Cross-origin request security
- **Rate Limiting** - API abuse prevention
- **Input Validation** - SQL injection protection

### **Performance Optimizations**
- **Gzip Compression** - Reduced bandwidth usage
- **Static Asset Caching** - Faster page loads
- **Database Indexing** - Optimized queries
- **Connection Pooling** - Efficient database connections

## 📊 API Endpoints

### **Authentication**
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Token refresh

### **Donations**
- `GET /api/donations` - List donations
- `POST /api/donations` - Create donation
- `PUT /api/donations/{id}` - Update donation
- `DELETE /api/donations/{id}` - Delete donation

### **NGOs**
- `GET /api/ngo/nearby` - Find nearby NGOs
- `GET /api/ngo/{id}` - Get NGO details
- `POST /api/ngo` - Register NGO

### **Analytics**
- `GET /api/donor/impact` - Donor impact metrics
- `GET /api/analytics/trends` - Donation trends

### **ML Service**
- `POST /ml/predict` - Food spoilage prediction
- `GET /ml/health` - Service health check

## 🧪 Testing

### **Backend Tests**
```bash
cd backend
mvn test
```

### **Frontend Testing**
- Manual testing of all user flows
- Cross-browser compatibility testing
- Mobile responsiveness testing

### **Integration Testing**
```bash
# Test all services
docker-compose up --build
# Navigate through the application
# Test API endpoints
```

## 🐛 Troubleshooting

### **Common Issues**

1. **Port Conflicts**
   - Ensure ports 3000, 8080, 5000 are available
   - Check for running services on these ports

2. **Database Connection**
   - Verify PostgreSQL is running
   - Check connection credentials
   - Ensure database exists

3. **Docker Issues**
   - Restart Docker Desktop
   - Clear Docker cache: `docker system prune`
   - Check Docker logs: `docker-compose logs`

### **Logs and Debugging**

```bash
# View all service logs
docker-compose logs

# View specific service logs
docker-compose logs backend
docker-compose logs frontend
docker-compose logs ml-service

# Follow logs in real-time
docker-compose logs -f
```

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch
3. **Make** your changes
4. **Test** thoroughly
5. **Submit** a pull request

### **Development Guidelines**
- Follow Java coding conventions
- Use meaningful commit messages
- Test all new features
- Update documentation

## 📈 Roadmap

### **Phase 1** ✅ (Completed)
- Basic authentication system
- Donation management
- NGO directory
- Simple analytics

### **Phase 2** 🚧 (In Progress)
- Advanced analytics dashboard
- ML-powered predictions
- Mobile app development
- Payment integration

### **Phase 3** 📋 (Planned)
- Real-time notifications
- Advanced ML models
- Multi-language support
- Advanced reporting

## 📞 Support

- **Documentation**: [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)
- **Issues**: [GitHub Issues](https://github.com/yourusername/foodshare/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/foodshare/discussions)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot Team** - Excellent Java framework
- **Chart.js** - Beautiful data visualization
- **Render** - Seamless cloud deployment
- **Open Source Community** - Continuous inspiration

---

**Made with ❤️ for a better world**

*Reducing food waste, one donation at a time* 🌱🍽️ 