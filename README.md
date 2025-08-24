# 🌍 Food Donation Management System

A comprehensive solution to reduce food waste by connecting donors with nearby NGOs using ML predictions and real-time matching.

## 🎯 Problem Statement
Millions of tons of edible food are wasted annually while millions go hungry. This system provides an efficient way to predict surplus food and connect donors with nearby NGOs in real-time, especially in low-internet rural regions.

## 🚀 Solution Overview

### Core Features
- **ML-Powered Prediction**: Python-based food surplus forecasting using restaurant/event/hostel data
- **Real-time Matching**: Connect donors with nearest NGOs using spatial queries
- **Offline Support**: SMS/USSD fallback for low-internet regions
- **Secure Authentication**: JWT-based security for all users

### Tech Stack
- **AI/ML**: Python, NumPy, Pandas, Scikit-learn
- **Backend**: Java 17, Spring Boot 3, Spring Security (JWT), REST APIs
- **Database**: PostgreSQL + PostGIS extension
- **Frontend**: HTML, CSS, JavaScript
- **Notifications**: SMS/USSD gateway (Twilio)
- **Deployment**: Docker + Cloud (Heroku/AWS)

## 🏗️ Project Structure
```
Ignithon/
├── ml-prediction/          # Python ML models
├── backend/                # Spring Boot application
├── frontend/               # HTML/CSS/JS pages
├── database/               # Database scripts and migrations
├── docker/                 # Docker configuration
├── docs/                   # Documentation
└── README.md
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Python 3.8+
- PostgreSQL 12+ with PostGIS
- Docker (optional)

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```

### ML Service Setup
```bash
cd ml-prediction
pip install -r requirements.txt
python app.py
```

### Frontend
```bash
cd frontend
# Open index.html in browser
```

## 📱 Features

### For NGOs
- Registration and authentication
- View incoming donations
- Accept/reject donations
- Location-based matching

### For Donors
- Registration and authentication
- Submit food donations
- View nearest NGOs
- ML-powered surplus prediction

### System Features
- Real-time spatial queries
- Offline notification support
- Secure JWT authentication
- RESTful API design

## 🔐 Security
- JWT-based authentication
- Role-based access control
- Secure password hashing
- CORS configuration

## 🌐 API Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User authentication
- `GET /api/ngos/nearby` - Find nearby NGOs
- `POST /api/donations` - Create donation
- `GET /api/predictions` - Get ML predictions

## 📊 Database Schema
- **NGO**: id, name, email, phone, city, latitude, longitude
- **Donor**: id, name, email, phone, city, latitude, longitude
- **Donation**: id, donorId, ngoId, foodDetails, quantity, timestamp
- **Prediction**: id, donorId, predictedQuantity, timestamp

## 🚀 Deployment
- Docker containerization
- Cloud deployment ready
- Environment-based configuration
- Health check endpoints

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details. 