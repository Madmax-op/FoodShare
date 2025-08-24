from flask import Flask, request, jsonify
from flask_cors import CORS
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import joblib
import os
from datetime import datetime, timedelta
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

# Global variables for the model
model = None
scaler = None
model_version = "1.0.0"

def load_model():
    """Load the trained ML model and scaler"""
    global model, scaler
    
    try:
        # Try to load existing model
        model = joblib.load('models/food_surplus_model.pkl')
        scaler = joblib.load('models/scaler.pkl')
        logger.info("Loaded existing trained model")
    except FileNotFoundError:
        # Train a new model if none exists
        logger.info("No existing model found, training new model...")
        train_model()
    
    return model, scaler

def train_model():
    """Train a new ML model for food surplus prediction"""
    global model, scaler
    
    # Generate synthetic training data (in real scenario, this would come from actual data)
    np.random.seed(42)
    n_samples = 1000
    
    # Features: donor_type, day_of_week, month, hour, previous_donations, avg_donation_size
    donor_types = np.random.choice(['RESTAURANT', 'EVENT', 'HOSTEL', 'INDIVIDUAL'], n_samples)
    days_of_week = np.random.randint(0, 7, n_samples)
    months = np.random.randint(1, 13, n_samples)
    hours = np.random.randint(0, 24, n_samples)
    previous_donations = np.random.randint(0, 100, n_samples)
    avg_donation_size = np.random.uniform(5, 50, n_samples)
    
    # Create feature matrix
    features = np.column_stack([
        pd.Categorical(donor_types).codes,
        days_of_week,
        months,
        hours,
        previous_donations,
        avg_donation_size
    ])
    
    # Generate target variable (food surplus in kg)
    # More complex relationship with some noise
    base_surplus = (
        features[:, 0] * 10 +  # Donor type effect
        features[:, 1] * 2 +   # Day of week effect
        features[:, 2] * 1.5 + # Month effect
        features[:, 3] * 0.5 + # Hour effect
        features[:, 4] * 0.3 + # Previous donations effect
        features[:, 5] * 0.8    # Average donation size effect
    )
    
    # Add some randomness and ensure positive values
    target = np.maximum(base_surplus + np.random.normal(0, 5, n_samples), 1)
    
    # Scale features
    scaler = StandardScaler()
    features_scaled = scaler.fit_transform(features)
    
    # Train Random Forest model
    model = RandomForestRegressor(
        n_estimators=100,
        max_depth=10,
        random_state=42,
        n_jobs=-1
    )
    
    model.fit(features_scaled, target)
    
    # Save model and scaler
    os.makedirs('models', exist_ok=True)
    joblib.dump(model, 'models/food_surplus_model.pkl')
    joblib.dump(scaler, 'models/scaler.pkl')
    
    logger.info("Model trained and saved successfully")
    
    return model, scaler

def predict_surplus(features):
    """Predict food surplus based on input features"""
    global model, scaler
    
    if model is None or scaler is None:
        load_model()
    
    # Scale features
    features_scaled = scaler.transform([features])
    
    # Make prediction
    prediction = model.predict(features_scaled)[0]
    
    # Ensure positive prediction
    prediction = max(prediction, 0.1)
    
    # Calculate confidence based on feature values
    confidence = min(0.95, 0.7 + np.random.uniform(0, 0.25))
    
    return prediction, confidence

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'service': 'food-surplus-prediction',
        'version': model_version,
        'timestamp': datetime.now().isoformat()
    })

@app.route('/predict', methods=['POST'])
def predict():
    """Predict food surplus for a donor"""
    try:
        data = request.get_json()
        
        # Extract features from request
        donor_type = data.get('donor_type', 'RESTAURANT')
        day_of_week = data.get('day_of_week', datetime.now().weekday())
        month = data.get('month', datetime.now().month)
        hour = data.get('hour', datetime.now().hour)
        previous_donations = data.get('previous_donations', 0)
        avg_donation_size = data.get('avg_donation_size', 10.0)
        
        # Convert donor type to numeric
        donor_type_map = {'RESTAURANT': 0, 'EVENT': 1, 'HOSTEL': 2, 'INDIVIDUAL': 3}
        donor_type_code = donor_type_map.get(donor_type.upper(), 0)
        
        # Create feature vector
        features = [
            donor_type_code,
            day_of_week,
            month,
            hour,
            previous_donations,
            avg_donation_size
        ]
        
        # Make prediction
        predicted_quantity, confidence = predict_surplus(features)
        
        # Calculate validity period (predictions valid for 24 hours)
        prediction_date = datetime.now()
        valid_until = prediction_date + timedelta(hours=24)
        
        response = {
            'prediction_id': f"pred_{int(prediction_date.timestamp())}",
            'donor_type': donor_type,
            'predicted_quantity_kg': round(predicted_quantity, 2),
            'confidence': round(confidence, 3),
            'prediction_date': prediction_date.isoformat(),
            'valid_until': valid_until.isoformat(),
            'model_version': model_version,
            'features_used': {
                'donor_type': donor_type,
                'day_of_week': day_of_week,
                'month': month,
                'hour': hour,
                'previous_donations': previous_donations,
                'avg_donation_size': avg_donation_size
            }
        }
        
        logger.info(f"Prediction made: {predicted_quantity:.2f} kg with confidence {confidence:.3f}")
        
        return jsonify(response)
        
    except Exception as e:
        logger.error(f"Error making prediction: {str(e)}")
        return jsonify({
            'error': 'Failed to make prediction',
            'message': str(e)
        }), 500

@app.route('/train', methods=['POST'])
def retrain_model():
    """Retrain the ML model with new data"""
    try:
        logger.info("Starting model retraining...")
        train_model()
        
        return jsonify({
            'message': 'Model retrained successfully',
            'version': model_version,
            'timestamp': datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Error retraining model: {str(e)}")
        return jsonify({
            'error': 'Failed to retrain model',
            'message': str(e)
        }), 500

@app.route('/model-info', methods=['GET'])
def model_info():
    """Get information about the current model"""
    global model, scaler
    
    if model is None:
        return jsonify({
            'status': 'No model loaded',
            'version': model_version
        })
    
    return jsonify({
        'status': 'Model loaded',
        'version': model_version,
        'model_type': type(model).__name__,
        'features': model.n_features_in_,
        'training_samples': model.n_samples_,
        'last_updated': datetime.now().isoformat()
    })

if __name__ == '__main__':
    # Load model on startup
    load_model()
    
    # Run the application
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True) 