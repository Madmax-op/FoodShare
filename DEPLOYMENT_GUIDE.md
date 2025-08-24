# 🚀 FoodShare Deployment Guide - Render

This guide will walk you through deploying the entire FoodShare application to Render using Docker.

## 📋 Prerequisites

- [Render Account](https://render.com) (Free tier available)
- [GitHub Repository](https://github.com) with your FoodShare code
- Docker knowledge (basic)

## 🏗️ Architecture Overview

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

## 🚀 Deployment Steps

### 1. Prepare Your Repository

Ensure your repository contains these files:
- `Dockerfile` - Multi-stage Docker build
- `render.yaml` - Render configuration
- `nginx.conf` - Nginx reverse proxy config
- `start-production.sh` - Production startup script
- `backend/src/main/resources/application-production.properties` - Production config

### 2. Connect to Render

1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Select the repository containing FoodShare

### 3. Configure the Web Service

**Basic Settings:**
- **Name**: `foodshare-app`
- **Environment**: `Docker`
- **Region**: Choose closest to your users
- **Branch**: `main` (or your default branch)

**Advanced Settings:**
- **Dockerfile Path**: `./Dockerfile`
- **Docker Context**: `.` (root directory)
- **Health Check Path**: `/health`

### 4. Set Environment Variables

Add these environment variables in Render:

```bash
# Database
DATABASE_URL=postgresql://foodshare_user:password@host:port/foodshare
DB_USERNAME=foodshare_user
DB_PASSWORD=your_secure_password

# JWT Security
JWT_SECRET=your_super_secure_jwt_secret_key_here

# CORS
CORS_ORIGINS=https://your-app-name.onrender.com

# ML Service (if deployed separately)
ML_SERVICE_URL=https://your-ml-service.onrender.com

# Redis (if using)
REDIS_URL=your-redis-url
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# Email (optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 5. Deploy

1. Click "Create Web Service"
2. Render will automatically build and deploy your application
3. Monitor the build logs for any issues
4. Wait for deployment to complete (usually 5-10 minutes)

## 🔧 Configuration Details

### Dockerfile Features

- **Multi-stage build** for optimized image size
- **Maven build** for Java backend
- **Python dependencies** for ML service
- **Nginx** for reverse proxy and static file serving
- **Health checks** for service monitoring

### Nginx Configuration

- **Reverse proxy** to backend and ML services
- **Static file serving** for frontend
- **Rate limiting** for API protection
- **Gzip compression** for performance
- **Security headers** for protection
- **Caching** for static assets

### Production Features

- **Service orchestration** with proper startup order
- **Health monitoring** and automatic restarts
- **Graceful shutdown** handling
- **Logging** and error handling
- **Environment-based configuration**

## 📊 Monitoring & Maintenance

### Health Checks

- **Frontend**: `/health` endpoint
- **Backend**: `/api/health` endpoint
- **ML Service**: `/ml/health` endpoint

### Logs

Access logs in Render dashboard:
- **Build logs**: During deployment
- **Runtime logs**: After deployment
- **Service logs**: Individual service logs

### Scaling

- **Free tier**: 750 hours/month
- **Paid tiers**: Auto-scaling available
- **Database**: Separate PostgreSQL service

## 🐛 Troubleshooting

### Common Issues

1. **Build Failures**
   - Check Dockerfile syntax
   - Verify file paths
   - Check Maven dependencies

2. **Service Startup Issues**
   - Check environment variables
   - Verify database connectivity
   - Check service dependencies

3. **Runtime Errors**
   - Check application logs
   - Verify API endpoints
   - Check CORS configuration

### Debug Commands

```bash
# Check container status
docker ps -a

# View logs
docker logs <container_id>

# Execute commands in container
docker exec -it <container_id> /bin/bash

# Check service health
curl https://your-app.onrender.com/health
```

## 🔒 Security Considerations

- **JWT Secret**: Use strong, unique secrets
- **Database**: Secure database credentials
- **CORS**: Restrict to trusted domains
- **Rate Limiting**: Prevent abuse
- **HTTPS**: Automatically provided by Render

## 📈 Performance Optimization

- **Gzip compression** enabled
- **Static asset caching** configured
- **Database connection pooling**
- **Service health monitoring**
- **Automatic restarts** on failure

## 🎯 Next Steps

After successful deployment:

1. **Test all features** thoroughly
2. **Set up monitoring** and alerts
3. **Configure custom domain** (optional)
4. **Set up CI/CD** for automatic deployments
5. **Monitor performance** and optimize

## 📞 Support

- **Render Documentation**: [docs.render.com](https://docs.render.com)
- **Render Community**: [community.render.com](https://community.render.com)
- **GitHub Issues**: Report bugs in your repository

---

**Happy Deploying! 🚀✨**
