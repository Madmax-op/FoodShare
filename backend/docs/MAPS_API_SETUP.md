# 🗺️ Maps API Integration Guide

This guide explains how to set up and use the Google Maps API integration for the FoodShare application.

## 🚀 Features

- **Route Optimization**: Find fastest routes between multiple points
- **Real-time Directions**: Turn-by-turn navigation with traffic data
- **Geocoding**: Convert addresses to coordinates and vice versa
- **Nearby Search**: Find nearby NGOs, donors, and places
- **Distance Matrix**: Calculate distances between multiple points
- **Interactive Maps**: Visual representation with custom markers

## ⚙️ Setup Instructions

### 1. Google Maps API Key Setup

1. **Go to Google Cloud Console**:
   - Visit [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one

2. **Enable Required APIs**:
   - Go to "APIs & Services" > "Library"
   - Enable the following APIs:
     - **Maps JavaScript API**
     - **Directions API**
     - **Geocoding API**
     - **Places API**
     - **Distance Matrix API**

3. **Create API Key**:
   - Go to "APIs & Services" > "Credentials"
   - Click "Create Credentials" > "API Key"
   - Copy the generated API key

4. **Restrict API Key** (Recommended):
   - Click on the created API key
   - Under "Application restrictions", select "HTTP referrers"
   - Add your domain(s): `http://localhost:3000/*`, `https://yourdomain.com/*`
   - Under "API restrictions", select "Restrict key"
   - Select the APIs you enabled above

### 2. Environment Configuration

Add your Google Maps API key to your environment variables:

```bash
# Linux/Mac
export GOOGLE_MAPS_API_KEY=your-api-key-here

# Windows
set GOOGLE_MAPS_API_KEY=your-api-key-here
```

Or add to your `.env` file:
```
GOOGLE_MAPS_API_KEY=your-api-key-here
```

### 3. Application Properties

The Maps API configuration is already set up in `application.properties`:

```properties
# Google Maps API Configuration
app.maps.google.api-key=${GOOGLE_MAPS_API_KEY:your-google-maps-api-key-here}
app.maps.google.base-url=https://maps.googleapis.com/maps/api
```

## 📍 API Endpoints

### Backend Maps API

#### 1. Get Optimized Route
```http
POST /api/maps/route/optimized
Content-Type: application/json

[
  {"lat": 40.7128, "lng": -74.0060},
  {"lat": 40.7589, "lng": -73.9851},
  {"lat": 40.7505, "lng": -73.9934}
]
```

#### 2. Get Fastest Route
```http
GET /api/maps/route/fastest?startLat=40.7128&startLng=-74.0060&endLat=40.7589&endLng=-73.9851&mode=driving
```

#### 3. Get Nearby Places
```http
GET /api/maps/places/nearby?lat=40.7128&lng=-74.0060&type=food&radius=5000
```

#### 4. Geocode Address
```http
GET /api/maps/geocode?address=New%20York,%20NY
```

#### 5. Reverse Geocode
```http
GET /api/maps/geocode/reverse?lat=40.7128&lng=-74.0060
```

#### 6. Distance Matrix
```http
POST /api/maps/distance-matrix
Content-Type: application/json

{
  "origins": [{"lat": 40.7128, "lng": -74.0060}],
  "destinations": [
    {"lat": 40.7589, "lng": -73.9851},
    {"lat": 40.7505, "lng": -73.9934}
  ],
  "mode": "driving"
}
```

#### 7. Volunteer Pickup Optimization
```http
POST /api/maps/volunteer/optimize-pickup
Content-Type: application/json

{
  "volunteerLocation": {"lat": 40.7128, "lng": -74.0060},
  "pickupPoints": [
    {"lat": 40.7589, "lng": -73.9851},
    {"lat": 40.7505, "lng": -73.9934}
  ]
}
```

#### 8. NGO to Donor Route
```http
GET /api/maps/ngo-to-donor?ngoLat=40.7128&ngoLng=-74.0060&donorLat=40.7589&donorLng=-73.9851
```

## 🎯 Frontend Integration

### 1. Include Maps JavaScript

Add the Maps JavaScript file to your HTML:

```html
<script src="maps.js"></script>
```

### 2. Initialize Maps

```javascript
// Initialize maps with your API key
const mapsManager = await initMaps('your-api-key-here', 'map-container');

// Get current location
const currentLocation = await mapsManager.getCurrentLocation();

// Add NGO markers
const ngo = {
    id: '1',
    name: 'Food Bank NYC',
    latitude: 40.7128,
    longitude: -74.0060,
    organizationType: 'FOOD_BANK',
    phone: '+1234567890',
    city: 'New York'
};
mapsManager.addNGOMarker(ngo);

// Get optimized route
const waypoints = [
    {lat: 40.7128, lng: -74.0060},
    {lat: 40.7589, lng: -73.9851},
    {lat: 40.7505, lng: -73.9934}
];
const route = await mapsManager.getOptimizedRoute(waypoints);
```

### 3. HTML Map Container

```html
<div id="map" style="width: 100%; height: 400px;"></div>
```

## 🔧 Usage Examples

### 1. NGO Dashboard - Find Nearby Donors

```javascript
// Get nearby donors for an NGO
async function findNearbyDonors(ngoLat, ngoLng) {
    try {
        const response = await fetch(`/api/maps/ngo/nearby-donors?ngoLat=${ngoLat}&ngoLng=${ngoLng}&radius=10000`);
        const data = await response.json();
        
        // Clear existing markers
        mapsManager.clearMarkers();
        
        // Add donor markers
        data.places.forEach(donor => {
            mapsManager.addDonorMarker(donor);
        });
        
        // Fit map to show all markers
        mapsManager.fitMapToMarkers();
        
    } catch (error) {
        console.error('Error finding nearby donors:', error);
    }
}
```

### 2. Volunteer Dashboard - Optimize Pickup Route

```javascript
// Optimize pickup route for volunteer
async function optimizePickupRoute(volunteerLocation, pickupPoints) {
    try {
        const response = await fetch('/api/maps/volunteer/optimize-pickup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                volunteerLocation: volunteerLocation,
                pickupPoints: pickupPoints
            })
        });
        
        const route = await response.json();
        
        // Display route on map
        const waypoints = [volunteerLocation, ...pickupPoints];
        await mapsManager.getOptimizedRoute(waypoints);
        
        return route;
        
    } catch (error) {
        console.error('Error optimizing pickup route:', error);
    }
}
```

### 3. Donor Dashboard - Find Nearby NGOs

```javascript
// Find nearby NGOs for a donor
async function findNearbyNGOs(donorLat, donorLng) {
    try {
        const response = await fetch(`/api/maps/donor/nearby-ngos?donorLat=${donorLat}&donorLng=${donorLng}&radius=10000`);
        const data = await response.json();
        
        // Clear existing markers
        mapsManager.clearMarkers();
        
        // Add NGO markers
        data.places.forEach(ngo => {
            mapsManager.addNGOMarker(ngo);
        });
        
        // Fit map to show all markers
        mapsManager.fitMapToMarkers();
        
    } catch (error) {
        console.error('Error finding nearby NGOs:', error);
    }
}
```

## 🎨 Customization

### 1. Custom Map Styles

Modify the `getMapStyles()` method in `maps.js`:

```javascript
getMapStyles() {
    return [
        {
            featureType: 'poi',
            elementType: 'labels',
            stylers: [{ visibility: 'off' }]
        },
        {
            featureType: 'transit',
            elementType: 'labels',
            stylers: [{ visibility: 'off' }]
        },
        {
            featureType: 'landscape',
            elementType: 'geometry',
            stylers: [{ color: '#f5f5f5' }]
        }
    ];
}
```

### 2. Custom Marker Icons

Create custom marker icons by modifying the icon methods:

```javascript
getNGOMarkerIcon() {
    return {
        url: 'path/to/your/ngo-icon.png',
        scaledSize: new google.maps.Size(32, 32),
        anchor: new google.maps.Point(16, 16)
    };
}
```

## 🔒 Security Considerations

1. **API Key Restrictions**: Always restrict your API key to specific domains and APIs
2. **Rate Limiting**: Implement rate limiting for Maps API calls
3. **Error Handling**: Handle API errors gracefully
4. **Caching**: Cache geocoding results to reduce API calls

## 💰 Cost Optimization

1. **Caching**: Cache frequently used geocoding results
2. **Batch Requests**: Use distance matrix API for multiple calculations
3. **Rate Limiting**: Implement client-side rate limiting
4. **Monitoring**: Monitor API usage to optimize costs

## 🧪 Testing

### 1. Health Check

Test if the Maps API is working:

```bash
curl http://localhost:8080/api/maps/health
```

### 2. Test Geocoding

```bash
curl "http://localhost:8080/api/maps/geocode?address=New%20York,%20NY"
```

### 3. Test Route

```bash
curl "http://localhost:8080/api/maps/route/fastest?startLat=40.7128&startLng=-74.0060&endLat=40.7589&endLng=-73.9851"
```

## 🚨 Troubleshooting

### Common Issues

1. **API Key Not Working**:
   - Check if API key is correctly set in environment variables
   - Verify API key restrictions
   - Ensure required APIs are enabled

2. **CORS Issues**:
   - Check CORS configuration in SecurityConfig
   - Verify frontend domain is allowed

3. **Rate Limiting**:
   - Implement proper error handling
   - Add retry logic with exponential backoff

4. **Map Not Loading**:
   - Check browser console for errors
   - Verify Google Maps API is loaded
   - Check network connectivity

### Debug Mode

Enable debug logging in `application.properties`:

```properties
logging.level.com.ignithon.service.MapsService=DEBUG
```

## 📚 Additional Resources

- [Google Maps JavaScript API Documentation](https://developers.google.com/maps/documentation/javascript)
- [Google Maps Directions API](https://developers.google.com/maps/documentation/directions)
- [Google Maps Geocoding API](https://developers.google.com/maps/documentation/geocoding)
- [Google Maps Places API](https://developers.google.com/maps/documentation/places)

---

**Note**: The Maps API integration provides real-time routing, geocoding, and location services to enhance the FoodShare application's functionality for efficient food donation management.
