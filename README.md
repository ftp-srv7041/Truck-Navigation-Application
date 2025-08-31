# 🚛 Truck Navigation System

A comprehensive truck navigation and route optimization system designed specifically for Indian roads and regulations. This full-stack application provides intelligent routing for commercial vehicles while considering truck-specific restrictions, permits, and operational requirements.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🌟 Features

### Core Functionality
- **Truck-Specific Routing**: Calculate routes considering vehicle dimensions, weight, and axle load restrictions
- **Multi-Criteria Optimization**: Choose from fastest, shortest, fuel-efficient, or toll-free routes
- **Road Restriction Awareness**: Automatic detection and avoidance of restricted roads based on truck specifications
- **Cost Estimation**: Real-time calculation of fuel costs and toll charges
- **Traffic Integration**: Consider current traffic conditions in route planning

### User Management
- **JWT Authentication**: Secure user authentication and authorization
- **Role-Based Access**: Different access levels for drivers, fleet managers, and administrators
- **Profile Management**: Comprehensive user profile and truck fleet management

### Truck Profile Management
- **Multiple Profiles**: Support for up to 10 truck profiles per user
- **Detailed Specifications**: Height, width, length, weight, axle configuration
- **Permit Tracking**: National permit, oversize permit, and hazmat permit management
- **Indian Compliance**: Built-in validation for Indian road regulations

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.1.5
- **Security**: Spring Security with JWT authentication
- **Database**: Spring Data JPA with H2 (development) / PostgreSQL (production)
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Maven
- **Java Version**: 17

### Frontend
- **Framework**: React 18
- **Routing**: React Router DOM
- **UI Library**: React Bootstrap
- **HTTP Client**: Axios
- **Maps**: Leaflet (ready for integration)
- **Notifications**: React Toastify

## 🚀 Getting Started

### Prerequisites
- ☕ Java 17 or higher
- 📦 Node.js 16 or higher  
- 🔧 Maven 3.6 or higher
- 🌐 Modern web browser

### Quick Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ftp-srv7041/Truck-Navigation-Application.git
   cd Truck-Navigation-Application
   ```

2. **Backend Setup**
   ```bash
   cd truck-navigation-system/backend
   mvn clean install
   mvn spring-boot:run
   ```
   🚀 Backend will start on http://localhost:8080

3. **Frontend Setup** (In a new terminal)
   ```bash
   cd truck-navigation-system/frontend
   npm install
   npm start
   ```
   🌐 Frontend will start on http://localhost:3000

### 🔑 Test Credentials

The application comes with pre-configured test users:

| Role | Email | Password | Description |
|------|-------|----------|-------------|
| **Driver** | `test@example.com` | `password123` | Regular driver account |
| **Admin** | `admin@trucknavigation.com` | `admin123` | Administrator account |
| **Driver** | `driver1@example.com` | `driver123` | Another driver account |

## 📚 API Documentation

Visit http://localhost:8080/swagger-ui.html for interactive API documentation once the backend is running.

### Key Endpoints
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User authentication
- `GET /api/v1/truck-profiles` - Get user's truck profiles
- `POST /api/v1/truck-profiles` - Create new truck profile
- `POST /api/v1/routes/calculate` - Calculate optimized routes

## 🗺️ Usage Guide

### 1. User Registration
- Register with email, password, full name, and phone number
- Indian mobile number validation included

### 2. Create Truck Profile
- Add truck specifications (dimensions, weight, permits)
- System validates against Indian road regulations
- Support for various truck types (Mini, Light, Medium, Heavy, Multi-axle, Trailer)

### 3. Calculate Routes
- Enter start and end coordinates
- Select truck profile and optimization type
- Get multiple route options with cost estimates

### 4. Route Analysis
- Compare different route options
- View fuel costs, toll charges, and travel time
- Consider traffic levels and road restrictions

## 🧪 Testing

### Sample Coordinates for Testing
- **New Delhi**: 28.6139, 77.2090
- **Mumbai**: 19.0760, 72.8777
- **Bangalore**: 12.9716, 77.5946
- **Chennai**: 13.0827, 80.2707
- **Kolkata**: 22.5726, 88.3639
- **Hyderabad**: 17.3850, 78.4867

### Test User Flow
1. Register a new account
2. Create a truck profile with valid Indian specifications
3. Calculate route between major Indian cities
4. Compare different optimization options

## 🔐 Security Features

- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control
- CORS configuration for cross-origin requests
- Input validation and sanitization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 📦 Project Structure

```
truck-navigation-system/
├── backend/                 # Spring Boot Backend
│   ├── src/main/java/      # Java source code
│   │   └── com/trucknavigation/
│   │       ├── config/     # Configuration classes
│   │       ├── controller/ # REST controllers
│   │       ├── dto/        # Data transfer objects
│   │       ├── model/      # JPA entities
│   │       ├── repository/ # Data repositories
│   │       ├── security/   # Security configuration
│   │       └── service/    # Business logic
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml            # Maven dependencies
├── frontend/               # React Frontend
│   ├── public/            # Static assets
│   ├── src/               # React source code
│   │   ├── components/    # Reusable components
│   │   ├── contexts/      # React contexts
│   │   ├── pages/         # Page components
│   │   └── App.js         # Main app component
│   └── package.json       # NPM dependencies
└── README.md              # Project documentation
```

## 🔧 Development

### Backend Development
- **Hot Reload**: Use `mvn spring-boot:run` for automatic restart on changes
- **Database Console**: Access H2 console at http://localhost:8080/h2-console
- **API Testing**: Use Swagger UI at http://localhost:8080/swagger-ui.html

### Frontend Development
- **Hot Reload**: React dev server automatically reloads on changes
- **Build**: Run `npm run build` for production build
- **Testing**: Run `npm test` for unit tests

## 🚀 Deployment

### Production Build
```bash
# Backend
cd backend
mvn clean package
java -jar target/truck-navigation-backend-1.0.0.jar

# Frontend
cd frontend
npm run build
# Deploy build/ directory to web server
```

## 🆘 Support & Contributing

### Support
- 🐛 **Issues**: [GitHub Issues](https://github.com/ftp-srv7041/Truck-Navigation-Application/issues)
- 📖 **Documentation**: Check API docs at `/swagger-ui.html`
- 💬 **Discussions**: [GitHub Discussions](https://github.com/ftp-srv7041/Truck-Navigation-Application/discussions)

### Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

**Made with ❤️ for the Indian trucking industry**
