// Maps API Integration for FoodShare
class MapsManager {
    constructor() {
        this.map = null;
        this.markers = [];
        this.directionsService = null;
        this.directionsRenderer = null;
        this.geocoder = null;
        this.apiKey = null;
        this.isLoaded = false;
    }

    // Initialize Google Maps
    async init(apiKey, containerId = 'map') {
        this.apiKey = apiKey;
        
        // Load Google Maps API
        await this.loadGoogleMapsAPI();
        
        // Initialize map components
        this.initializeMap(containerId);
        this.initializeServices();
        
        this.isLoaded = true;
        console.log('Maps initialized successfully');
    }

    // Load Google Maps API dynamically
    loadGoogleMapsAPI() {
        return new Promise((resolve, reject) => {
            if (window.google && window.google.maps) {
                resolve();
                return;
            }

            const script = document.createElement('script');
            script.src = `https://maps.googleapis.com/maps/api/js?key=${this.apiKey}&libraries=places,geometry`;
            script.async = true;
            script.defer = true;
            
            script.onload = () => resolve();
            script.onerror = () => reject(new Error('Failed to load Google Maps API'));
            
            document.head.appendChild(script);
        });
    }

    // Initialize the map
    initializeMap(containerId) {
        const container = document.getElementById(containerId);
        if (!container) {
            throw new Error(`Map container with id '${containerId}' not found`);
        }

        this.map = new google.maps.Map(container, {
            zoom: 12,
            center: { lat: 40.7128, lng: -74.0060 }, // Default to NYC
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            styles: this.getMapStyles(),
            mapTypeControl: true,
            streetViewControl: false,
            fullscreenControl: true
        });
    }

    // Initialize Google Maps services
    initializeServices() {
        this.directionsService = new google.maps.DirectionsService();
        this.directionsRenderer = new google.maps.DirectionsRenderer({
            map: this.map,
            suppressMarkers: true
        });
        this.geocoder = new google.maps.Geocoder();
    }

    // Get optimized route between multiple points
    async getOptimizedRoute(waypoints) {
        if (!this.isLoaded) {
            throw new Error('Maps not initialized');
        }

        const request = {
            origin: waypoints[0],
            destination: waypoints[waypoints.length - 1],
            waypoints: waypoints.slice(1, -1).map(point => ({
                location: point,
                stopover: true
            })),
            optimizeWaypoints: true,
            travelMode: google.maps.TravelMode.DRIVING
        };

        try {
            const result = await this.directionsService.route(request);
            this.directionsRenderer.setDirections(result);
            return this.parseDirectionsResult(result);
        } catch (error) {
            console.error('Error getting optimized route:', error);
            throw error;
        }
    }

    // Get fastest route between two points
    async getFastestRoute(origin, destination, mode = 'driving') {
        if (!this.isLoaded) {
            throw new Error('Maps not initialized');
        }

        const request = {
            origin: origin,
            destination: destination,
            travelMode: this.getTravelMode(mode)
        };

        try {
            const result = await this.directionsService.route(request);
            this.directionsRenderer.setDirections(result);
            return this.parseDirectionsResult(result);
        } catch (error) {
            console.error('Error getting fastest route:', error);
            throw error;
        }
    }

    // Add marker to map
    addMarker(position, title, icon = null, infoWindow = null) {
        if (!this.isLoaded) {
            throw new Error('Maps not initialized');
        }

        const marker = new google.maps.Marker({
            position: position,
            map: this.map,
            title: title,
            icon: icon
        });

        if (infoWindow) {
            marker.addListener('click', () => {
                infoWindow.open(this.map, marker);
            });
        }

        this.markers.push(marker);
        return marker;
    }

    // Add NGO marker
    addNGOMarker(ngo) {
        const position = { lat: ngo.latitude, lng: ngo.longitude };
        const infoWindow = new google.maps.InfoWindow({
            content: `
                <div class="info-window">
                    <h3>${ngo.name}</h3>
                    <p><strong>Type:</strong> ${ngo.organizationType}</p>
                    <p><strong>Phone:</strong> ${ngo.phone}</p>
                    <p><strong>Address:</strong> ${ngo.city}</p>
                    <button onclick="selectNGO('${ngo.id}')" class="btn btn-primary">Select NGO</button>
                </div>
            `
        });

        return this.addMarker(position, ngo.name, this.getNGOMarkerIcon(), infoWindow);
    }

    // Add donor marker
    addDonorMarker(donor) {
        const position = { lat: donor.latitude, lng: donor.longitude };
        const infoWindow = new google.maps.InfoWindow({
            content: `
                <div class="info-window">
                    <h3>${donor.name}</h3>
                    <p><strong>Type:</strong> ${donor.donorType}</p>
                    <p><strong>Phone:</strong> ${donor.phone}</p>
                    <p><strong>Address:</strong> ${donor.city}</p>
                    <button onclick="selectDonor('${donor.id}')" class="btn btn-primary">View Details</button>
                </div>
            `
        });

        return this.addMarker(position, donor.name, this.getDonorMarkerIcon(), infoWindow);
    }

    // Add donation marker
    addDonationMarker(donation) {
        const position = { lat: donation.latitude, lng: donation.longitude };
        const infoWindow = new google.maps.InfoWindow({
            content: `
                <div class="info-window">
                    <h3>Food Donation</h3>
                    <p><strong>Type:</strong> ${donation.foodType}</p>
                    <p><strong>Quantity:</strong> ${donation.quantity} kg</p>
                    <p><strong>Expires:</strong> ${new Date(donation.expiryTime).toLocaleString()}</p>
                    <button onclick="acceptDonation('${donation.id}')" class="btn btn-success">Accept</button>
                </div>
            `
        });

        return this.addMarker(position, 'Food Donation', this.getDonationMarkerIcon(), infoWindow);
    }

    // Clear all markers
    clearMarkers() {
        this.markers.forEach(marker => marker.setMap(null));
        this.markers = [];
    }

    // Clear directions
    clearDirections() {
        this.directionsRenderer.setDirections({ routes: [] });
    }

    // Fit map to show all markers
    fitMapToMarkers() {
        if (this.markers.length === 0) return;

        const bounds = new google.maps.LatLngBounds();
        this.markers.forEach(marker => {
            bounds.extend(marker.getPosition());
        });
        this.map.fitBounds(bounds);
    }

    // Geocode address to coordinates
    async geocodeAddress(address) {
        if (!this.isLoaded) {
            throw new Error('Maps not initialized');
        }

        return new Promise((resolve, reject) => {
            this.geocoder.geocode({ address: address }, (results, status) => {
                if (status === 'OK') {
                    const location = results[0].geometry.location;
                    resolve({
                        lat: location.lat(),
                        lng: location.lng(),
                        formattedAddress: results[0].formatted_address
                    });
                } else {
                    reject(new Error(`Geocoding failed: ${status}`));
                }
            });
        });
    }

    // Reverse geocode coordinates to address
    async reverseGeocode(lat, lng) {
        if (!this.isLoaded) {
            throw new Error('Maps not initialized');
        }

        return new Promise((resolve, reject) => {
            this.geocoder.geocode({ location: { lat: lat, lng: lng } }, (results, status) => {
                if (status === 'OK') {
                    resolve({
                        formattedAddress: results[0].formatted_address,
                        components: results[0].address_components
                    });
                } else {
                    reject(new Error(`Reverse geocoding failed: ${status}`));
                }
            });
        });
    }

    // Get current location
    getCurrentLocation() {
        return new Promise((resolve, reject) => {
            if (!navigator.geolocation) {
                reject(new Error('Geolocation is not supported by this browser'));
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (position) => {
                    resolve({
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    });
                },
                (error) => {
                    reject(new Error(`Geolocation error: ${error.message}`));
                },
                {
                    enableHighAccuracy: true,
                    timeout: 10000,
                    maximumAge: 300000
                }
            );
        });
    }

    // Parse directions result
    parseDirectionsResult(result) {
        const route = result.routes[0];
        const leg = route.legs[0];
        
        return {
            distance: leg.distance.text,
            duration: leg.duration.text,
            startAddress: leg.start_address,
            endAddress: leg.end_address,
            polyline: route.overview_polyline.points,
            steps: leg.steps.map(step => ({
                instruction: step.instructions,
                distance: step.distance.text,
                duration: step.duration.text
            }))
        };
    }

    // Get travel mode
    getTravelMode(mode) {
        const modes = {
            'driving': google.maps.TravelMode.DRIVING,
            'walking': google.maps.TravelMode.WALKING,
            'bicycling': google.maps.TravelMode.BICYCLING,
            'transit': google.maps.TravelMode.TRANSIT
        };
        return modes[mode] || google.maps.TravelMode.DRIVING;
    }

    // Get custom map styles
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
            }
        ];
    }

    // Get NGO marker icon
    getNGOMarkerIcon() {
        return {
            url: 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(`
                <svg width="32" height="32" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="16" cy="16" r="14" fill="#10b981" stroke="#ffffff" stroke-width="2"/>
                    <text x="16" y="20" text-anchor="middle" fill="white" font-size="12" font-weight="bold">N</text>
                </svg>
            `),
            scaledSize: new google.maps.Size(32, 32),
            anchor: new google.maps.Point(16, 16)
        };
    }

    // Get donor marker icon
    getDonorMarkerIcon() {
        return {
            url: 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(`
                <svg width="32" height="32" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="16" cy="16" r="14" fill="#3b82f6" stroke="#ffffff" stroke-width="2"/>
                    <text x="16" y="20" text-anchor="middle" fill="white" font-size="12" font-weight="bold">D</text>
                </svg>
            `),
            scaledSize: new google.maps.Size(32, 32),
            anchor: new google.maps.Point(16, 16)
        };
    }

    // Get donation marker icon
    getDonationMarkerIcon() {
        return {
            url: 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(`
                <svg width="32" height="32" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="16" cy="16" r="14" fill="#f59e0b" stroke="#ffffff" stroke-width="2"/>
                    <text x="16" y="20" text-anchor="middle" fill="white" font-size="12" font-weight="bold">🍽️</text>
                </svg>
            `),
            scaledSize: new google.maps.Size(32, 32),
            anchor: new google.maps.Point(16, 16)
        };
    }
}

// Global maps instance
let mapsManager = null;

// Initialize maps
async function initMaps(apiKey, containerId = 'map') {
    try {
        mapsManager = new MapsManager();
        await mapsManager.init(apiKey, containerId);
        return mapsManager;
    } catch (error) {
        console.error('Failed to initialize maps:', error);
        throw error;
    }
}

// Utility functions for map interactions
function selectNGO(ngoId) {
    // Handle NGO selection
    console.log('Selected NGO:', ngoId);
    // Add your logic here
}

function selectDonor(donorId) {
    // Handle donor selection
    console.log('Selected donor:', donorId);
    // Add your logic here
}

function acceptDonation(donationId) {
    // Handle donation acceptance
    console.log('Accepted donation:', donationId);
    // Add your logic here
}

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { MapsManager, initMaps };
}
