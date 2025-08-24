package com.ignithon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MapsService {
    
    private static final Logger logger = LoggerFactory.getLogger(MapsService.class);
    
    @Value("${app.maps.google.api-key:your-google-maps-api-key-here}")
    private String googleMapsApiKey;
    
    @Value("${app.maps.google.base-url:https://maps.googleapis.com/maps/api}")
    private String googleMapsBaseUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Simple health check method
     */
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "MapsService");
        response.put("timestamp", System.currentTimeMillis());
        response.put("apiKeyConfigured", googleMapsApiKey != null && !googleMapsApiKey.equals("your-google-maps-api-key-here"));
        return response;
    }
    
    /**
     * Get optimized route between multiple points
     */
    public Map<String, Object> getOptimizedRoute(List<Map<String, Object>> waypoints) {
        try {
            String url = buildDirectionsUrl(waypoints);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseDirectionsResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to get route: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error getting optimized route", e);
            return createErrorResponse("Failed to get route: " + e.getMessage());
        }
    }
    
    /**
     * Get fastest route between two points
     */
    public Map<String, Object> getFastestRoute(Double startLat, Double startLng, 
                                             Double endLat, Double endLng, 
                                             String mode) {
        try {
            String url = buildSingleRouteUrl(startLat, startLng, endLat, endLng, mode);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseSingleRouteResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to get route: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error getting fastest route", e);
            return createErrorResponse("Failed to get route: " + e.getMessage());
        }
    }
    
    /**
     * Get nearby places (NGOs, donors, etc.)
     */
    public Map<String, Object> getNearbyPlaces(Double lat, Double lng, String type, Integer radius) {
        try {
            String url = buildNearbySearchUrl(lat, lng, type, radius);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseNearbyPlacesResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to get nearby places: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error getting nearby places", e);
            return createErrorResponse("Failed to get nearby places: " + e.getMessage());
        }
    }
    
    /**
     * Get geocoding for address
     */
    public Map<String, Object> geocodeAddress(String address) {
        try {
            String url = buildGeocodingUrl(address);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseGeocodingResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to geocode address: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error geocoding address", e);
            return createErrorResponse("Failed to geocode address: " + e.getMessage());
        }
    }
    
    /**
     * Get reverse geocoding for coordinates
     */
    public Map<String, Object> reverseGeocode(Double lat, Double lng) {
        try {
            String url = buildReverseGeocodingUrl(lat, lng);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseReverseGeocodingResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to reverse geocode: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error reverse geocoding", e);
            return createErrorResponse("Failed to reverse geocode: " + e.getMessage());
        }
    }
    
    /**
     * Calculate distance matrix between multiple points
     */
    public Map<String, Object> getDistanceMatrix(List<Map<String, Object>> origins, 
                                                List<Map<String, Object>> destinations, 
                                                String mode) {
        try {
            String url = buildDistanceMatrixUrl(origins, destinations, mode);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            if ("OK".equals(jsonResponse.get("status").asText())) {
                return parseDistanceMatrixResponse(jsonResponse);
            } else {
                logger.error("Google Maps API error: {}", jsonResponse.get("status").asText());
                return createErrorResponse("Failed to get distance matrix: " + jsonResponse.get("status").asText());
            }
            
        } catch (Exception e) {
            logger.error("Error getting distance matrix", e);
            return createErrorResponse("Failed to get distance matrix: " + e.getMessage());
        }
    }
    
    // Private helper methods for building URLs
    private String buildDirectionsUrl(List<Map<String, Object>> waypoints) {
        StringBuilder url = new StringBuilder();
        url.append(googleMapsBaseUrl).append("/directions/json?");
        url.append("key=").append(googleMapsApiKey);
        url.append("&optimize=true");
        
        if (!waypoints.isEmpty()) {
            Map<String, Object> origin = waypoints.get(0);
            Map<String, Object> destination = waypoints.get(waypoints.size() - 1);
            
            url.append("&origin=").append(origin.get("lat")).append(",").append(origin.get("lng"));
            url.append("&destination=").append(destination.get("lat")).append(",").append(destination.get("lng"));
            
            if (waypoints.size() > 2) {
                url.append("&waypoints=");
                for (int i = 1; i < waypoints.size() - 1; i++) {
                    Map<String, Object> waypoint = waypoints.get(i);
                    url.append(waypoint.get("lat")).append(",").append(waypoint.get("lng"));
                    if (i < waypoints.size() - 2) url.append("|");
                }
            }
        }
        
        return url.toString();
    }
    
    private String buildSingleRouteUrl(Double startLat, Double startLng, 
                                     Double endLat, Double endLng, String mode) {
        return googleMapsBaseUrl + "/directions/json?" +
               "origin=" + startLat + "," + startLng +
               "&destination=" + endLat + "," + endLng +
               "&mode=" + mode +
               "&key=" + googleMapsApiKey;
    }
    
    private String buildNearbySearchUrl(Double lat, Double lng, String type, Integer radius) {
        return googleMapsBaseUrl + "/place/nearbysearch/json?" +
               "location=" + lat + "," + lng +
               "&radius=" + radius +
               "&type=" + type +
               "&key=" + googleMapsApiKey;
    }
    
    private String buildGeocodingUrl(String address) {
        return googleMapsBaseUrl + "/geocode/json?" +
               "address=" + address.replace(" ", "+") +
               "&key=" + googleMapsApiKey;
    }
    
    private String buildReverseGeocodingUrl(Double lat, Double lng) {
        return googleMapsBaseUrl + "/geocode/json?" +
               "latlng=" + lat + "," + lng +
               "&key=" + googleMapsApiKey;
    }
    
    private String buildDistanceMatrixUrl(List<Map<String, Object>> origins, 
                                        List<Map<String, Object>> destinations, 
                                        String mode) {
        StringBuilder url = new StringBuilder();
        url.append(googleMapsBaseUrl).append("/distancematrix/json?");
        url.append("key=").append(googleMapsApiKey);
        url.append("&mode=").append(mode);
        
        // Add origins
        url.append("&origins=");
        for (int i = 0; i < origins.size(); i++) {
            Map<String, Object> origin = origins.get(i);
            url.append(origin.get("lat")).append(",").append(origin.get("lng"));
            if (i < origins.size() - 1) url.append("|");
        }
        
        // Add destinations
        url.append("&destinations=");
        for (int i = 0; i < destinations.size(); i++) {
            Map<String, Object> destination = destinations.get(i);
            url.append(destination.get("lat")).append(",").append(destination.get("lng"));
            if (i < destinations.size() - 1) url.append("|");
        }
        
        return url.toString();
    }
    
    // Private helper methods for parsing responses
    private Map<String, Object> parseDirectionsResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode routes = response.get("routes");
        
        if (routes.size() > 0) {
            JsonNode route = routes.get(0);
            JsonNode legs = route.get("legs");
            
            List<Map<String, Object>> routeLegs = new ArrayList<>();
            double totalDistance = 0;
            int totalDuration = 0;
            
            for (JsonNode leg : legs) {
                Map<String, Object> legInfo = new HashMap<>();
                legInfo.put("distance", leg.get("distance").get("text").asText());
                legInfo.put("duration", leg.get("duration").get("text").asText());
                legInfo.put("startAddress", leg.get("start_address").asText());
                legInfo.put("endAddress", leg.get("end_address").asText());
                
                totalDistance += leg.get("distance").get("value").asDouble();
                totalDuration += leg.get("duration").get("value").asInt();
                
                routeLegs.add(legInfo);
            }
            
            result.put("success", true);
            result.put("legs", routeLegs);
            result.put("totalDistance", totalDistance);
            result.put("totalDuration", totalDuration);
            result.put("polyline", route.get("overview_polyline").get("points").asText());
        }
        
        return result;
    }
    
    private Map<String, Object> parseSingleRouteResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode routes = response.get("routes");
        
        if (routes.size() > 0) {
            JsonNode route = routes.get(0);
            JsonNode leg = route.get("legs").get(0);
            
            result.put("success", true);
            result.put("distance", leg.get("distance").get("text").asText());
            result.put("duration", leg.get("duration").get("text").asText());
            result.put("startAddress", leg.get("start_address").asText());
            result.put("endAddress", leg.get("end_address").asText());
            result.put("polyline", route.get("overview_polyline").get("points").asText());
        }
        
        return result;
    }
    
    private Map<String, Object> parseNearbyPlacesResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode places = response.get("results");
        
        List<Map<String, Object>> placesList = new ArrayList<>();
        for (JsonNode place : places) {
            Map<String, Object> placeInfo = new HashMap<>();
            placeInfo.put("name", place.get("name").asText());
            placeInfo.put("placeId", place.get("place_id").asText());
            
            JsonNode location = place.get("geometry").get("location");
            placeInfo.put("lat", location.get("lat").asDouble());
            placeInfo.put("lng", location.get("lng").asDouble());
            
            if (place.has("vicinity")) {
                placeInfo.put("address", place.get("vicinity").asText());
            }
            
            placesList.add(placeInfo);
        }
        
        result.put("success", true);
        result.put("places", placesList);
        
        return result;
    }
    
    private Map<String, Object> parseGeocodingResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode results = response.get("results");
        
        if (results.size() > 0) {
            JsonNode location = results.get(0).get("geometry").get("location");
            result.put("success", true);
            result.put("lat", location.get("lat").asDouble());
            result.put("lng", location.get("lng").asDouble());
            result.put("formattedAddress", results.get(0).get("formatted_address").asText());
        }
        
        return result;
    }
    
    private Map<String, Object> parseReverseGeocodingResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode results = response.get("results");
        
        if (results.size() > 0) {
            result.put("success", true);
            result.put("formattedAddress", results.get(0).get("formatted_address").asText());
        }
        
        return result;
    }
    
    private Map<String, Object> parseDistanceMatrixResponse(JsonNode response) {
        Map<String, Object> result = new HashMap<>();
        JsonNode rows = response.get("rows");
        
        List<List<Map<String, Object>>> matrix = new ArrayList<>();
        for (JsonNode row : rows) {
            List<Map<String, Object>> rowData = new ArrayList<>();
            JsonNode elements = row.get("elements");
            
            for (JsonNode element : elements) {
                Map<String, Object> elementData = new HashMap<>();
                if ("OK".equals(element.get("status").asText())) {
                    elementData.put("distance", element.get("distance").get("text").asText());
                    elementData.put("duration", element.get("duration").get("text").asText());
                } else {
                    elementData.put("status", element.get("status").asText());
                }
                rowData.add(elementData);
            }
            matrix.add(rowData);
        }
        
        result.put("success", true);
        result.put("matrix", matrix);
        
        return result;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", message);
        return result;
    }
}
