# 📁 FoodShare Project Structure

## 🏗️ **Root Directory Overview**

```
FoodShare/
├── 📁 backend/                    # Spring Boot Backend Application
├── 📁 frontend/                   # HTML/CSS/JS Frontend
├── 📁 ml-prediction/             # Python ML Service
├── 📁 database/                   # Database scripts and migrations
├── 📁 docker/                     # Docker development configuration
├── 🐳 Dockerfile                  # Production Docker build
├── 🐳 docker-compose.yml          # Local development setup
├── 🌐 nginx.conf                  # Production Nginx configuration
├── 🚀 start-production.sh         # Production startup script
├── ☁️ render.yaml                 # Render deployment configuration
├── 📖 README.md                   # Main project documentation
├── 📖 DEPLOYMENT_GUIDE.md         # Render deployment guide
└── 📖 PROJECT_STRUCTURE.md        # This file
```

## 🔧 **Core Application Files**

### **Backend (Spring Boot)**
- **Port**: 8080
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (production), H2 (development)
- **Features**: REST API, JWT Authentication, JPA/Hibernate

### **Frontend (Static Web)**
- **Port**: 3000 (development), 80 (production)
- **Technology**: HTML5, CSS3, JavaScript (ES6+)
- **Framework**: Vanilla JS with Chart.js for analytics
- **Features**: Responsive design, Progressive Web App

### **ML Service (Python)**
- **Port**: 5000
- **Language**: Python 3.9
- **Framework**: Flask
- **Features**: Food spoilage prediction, ML model training

## 🐳 **Docker Configuration**

### **Development (docker-compose.yml)**
- **PostgreSQL**: Database service
- **Backend**: Spring Boot application
- **Frontend**: Nginx static file server
- **ML Service**: Python Flask application

### **Production (Dockerfile)**
- **Multi-stage build** for optimized image size
- **Single container** running all services
- **Nginx reverse proxy** for service routing
- **Health checks** for service monitoring

## 🌐 **Deployment Files**

### **Render Configuration (render.yaml)**
- **Web Service**: Main application container
- **PostgreSQL**: Managed database service
- **Redis**: Caching service (optional)
- **Environment Variables**: Production configuration

### **Nginx Configuration (nginx.conf)**
- **Reverse Proxy**: Routes API calls to backend
- **Static Files**: Serves frontend assets
- **Security Headers**: CORS, XSS protection
- **Rate Limiting**: API abuse prevention
- **Gzip Compression**: Performance optimization

### **Production Startup (start-production.sh)**
- **Service Orchestration**: Starts services in order
- **Health Monitoring**: Checks service availability
- **Graceful Shutdown**: Proper cleanup on exit
- **Logging**: Service status and error reporting

## 📊 **Service Architecture**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   ML Service    │
│   (Nginx)       │◄──►│   (Spring Boot) │◄──►│   (Python)      │
│   Port 80       │    │   Port 8080     │    │   Port 5000     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   Database      │
                    └─────────────────┘
```

## 🚀 **Deployment Options**

### **1. Local Development**
```bash
docker-compose up --build
```

### **2. Production Docker**
```bash
docker build -t foodshare .
docker run -p 80:80 -p 8080:8080 -p 5000:5000 foodshare
```

### **3. Render Cloud**
- Connect GitHub repository
- Configure environment variables
- Automatic deployment on push

## 📁 **Key Directories Explained**

### **`backend/`**
- **Java source code** for the Spring Boot application
- **Configuration files** for different environments
- **Maven POM** for dependency management
- **Entity classes** for database models
- **Controller classes** for REST API endpoints
- **Service classes** for business logic

### **`frontend/`**
- **HTML pages** for all user interfaces
- **CSS stylesheets** for responsive design
- **JavaScript files** for dynamic functionality
- **Static assets** (images, icons, fonts)
- **Chart.js integration** for analytics

### **`ml-prediction/`**
- **Python application** for ML predictions
- **Requirements file** for dependencies
- **Trained models** for food spoilage prediction
- **Data processing** scripts
- **API endpoints** for ML services

### **`database/`**
- **SQL scripts** for database initialization
- **Migration files** for schema updates
- **Sample data** for development
- **Database configuration** files

### **`docker/`**
- **Development Docker** configurations
- **Service definitions** for local development
- **Volume mappings** for persistent data
- **Network configurations** for service communication

## 🔒 **Security & Configuration**

### **Environment Variables**
- **Database credentials** and connection strings
- **JWT secrets** for authentication
- **CORS origins** for cross-origin requests
- **API keys** for external services
- **Feature flags** for functionality control

### **Security Features**
- **JWT Authentication** with secure token handling
- **CORS Protection** for cross-origin security
- **Rate Limiting** to prevent API abuse
- **Input Validation** for SQL injection protection
- **HTTPS Enforcement** in production

## 📈 **Performance & Monitoring**

### **Optimization Features**
- **Gzip compression** for reduced bandwidth
- **Static asset caching** for faster loads
- **Database connection pooling** for efficiency
- **Service health monitoring** for reliability
- **Automatic restarts** on failure

### **Monitoring Endpoints**
- **`/health`** - Overall application health
- **`/api/health`** - Backend service health
- **`/ml/health`** - ML service health
- **Log aggregation** for debugging

## 🎯 **Development Workflow**

1. **Local Development**: Use `docker-compose.yml`
2. **Testing**: Run services locally for development
3. **Commit**: Push changes to GitHub
4. **Deploy**: Automatic deployment on Render
5. **Monitor**: Check logs and health endpoints

## 📚 **Documentation Files**

- **`README.md`** - Main project overview and setup
- **`DEPLOYMENT_GUIDE.md`** - Detailed deployment instructions
- **`PROJECT_STRUCTURE.md`** - This file with project organization
- **Inline code comments** for technical details

---

**This structure provides a clean, organized, and production-ready FoodShare application! 🚀✨**
