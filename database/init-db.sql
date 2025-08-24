-- Enable PostGIS extension for spatial queries
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create database schema
CREATE SCHEMA IF NOT EXISTS public;

-- Set search path
SET search_path TO public;

-- Create users table (base table for inheritance)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ngos table (inherits from users)
CREATE TABLE IF NOT EXISTS ngos (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'NGO',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    organization_type VARCHAR(100) NOT NULL,
    description TEXT,
    accepts_donations BOOLEAN DEFAULT TRUE,
    max_donation_distance INTEGER DEFAULT 50,
    max_donation_quantity INTEGER DEFAULT 100
);

-- Create donors table (inherits from users)
CREATE TABLE IF NOT EXISTS donors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'DONOR',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    donor_type VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    average_donation_quantity INTEGER DEFAULT 0,
    total_donations INTEGER DEFAULT 0
);

-- Create donations table
CREATE TABLE IF NOT EXISTS donations (
    id BIGSERIAL PRIMARY KEY,
    donor_id BIGINT REFERENCES donors(id),
    ngo_id BIGINT REFERENCES ngos(id),
    food_details TEXT NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    food_type VARCHAR(50) NOT NULL,
    special_instructions TEXT,
    expiry_time TIMESTAMP NOT NULL,
    pickup_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create predictions table
CREATE TABLE IF NOT EXISTS predictions (
    id BIGSERIAL PRIMARY KEY,
    donor_id BIGINT REFERENCES donors(id),
    predicted_quantity DOUBLE PRECISION NOT NULL,
    confidence DOUBLE PRECISION NOT NULL,
    prediction_date TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    model_version VARCHAR(50),
    features TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create spatial indexes for location-based queries
CREATE INDEX IF NOT EXISTS idx_ngos_location ON ngos USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_donors_location ON donors USING GIST (ST_MakePoint(longitude, latitude));

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_donations_status ON donations(status);
CREATE INDEX IF NOT EXISTS idx_donations_donor_id ON donations(donor_id);
CREATE INDEX IF NOT EXISTS idx_donations_ngo_id ON donations(ngo_id);
CREATE INDEX IF NOT EXISTS idx_predictions_donor_id ON predictions(donor_id);
CREATE INDEX IF NOT EXISTS idx_predictions_status ON predictions(status);

-- Insert sample data for testing
INSERT INTO ngos (name, email, phone, city, latitude, longitude, password, organization_type, description) VALUES
('Community Food Bank', 'info@communityfoodbank.org', '+1234567890', 'New York', 40.7128, -74.0060, '$2a$10$dummy.hash.for.testing', 'FOOD_BANK', 'Providing food assistance to local communities'),
('Homeless Shelter Kitchen', 'kitchen@homelessshelter.org', '+1234567891', 'New York', 40.7589, -73.9851, '$2a$10$dummy.hash.for.testing', 'SHELTER', 'Serving meals to homeless individuals'),
('Senior Center Meals', 'meals@seniorcenter.org', '+1234567892', 'New York', 40.7505, -73.9934, '$2a$10$dummy.hash.for.testing', 'SENIOR_CENTER', 'Providing nutritious meals to seniors');

INSERT INTO donors (name, email, phone, city, latitude, longitude, password, donor_type, description) VALUES
('Downtown Restaurant', 'manager@downtownrestaurant.com', '+1234567893', 'New York', 40.7589, -73.9851, '$2a$10$dummy.hash.for.testing', 'RESTAURANT', 'Fine dining restaurant with daily surplus'),
('Corporate Cafeteria', 'cafeteria@corporate.com', '+1234567894', 'New York', 40.7505, -73.9934, '$2a$10$dummy.hash.for.testing', 'CORPORATE', 'Large corporate cafeteria serving employees'),
('Local Bakery', 'info@localbakery.com', '+1234567895', 'New York', 40.7128, -74.0060, '$2a$10$dummy.hash.for.testing', 'BAKERY', 'Fresh bread and pastries daily');

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_ngos_updated_at BEFORE UPDATE ON ngos FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_donors_updated_at BEFORE UPDATE ON donors FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_donations_updated_at BEFORE UPDATE ON donations FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres; 