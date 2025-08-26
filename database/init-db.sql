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
    max_donation_quantity INTEGER DEFAULT 100,
    rating DECIMAL(3,2) DEFAULT 4.5,
    meals_delivered INTEGER DEFAULT 0
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

-- Insert comprehensive sample data for FoodShare application

-- Insert NGOs with Indian locations and realistic data
INSERT INTO ngos (name, email, phone, city, latitude, longitude, password, organization_type, description, rating, meals_delivered) VALUES
('Delhi Food Bank', 'info@delhifoodbank.org', '+91-11-2345-6789', 'Delhi', 28.6139, 77.2090, '$2a$10$dummy.hash.for.testing', 'FOOD_BANK', 'Serving the community since 2010, we provide meals to underprivileged families and homeless individuals.', 4.8, 156),
('Community Kitchen', 'kitchen@community.org', '+91-11-3456-7890', 'Delhi', 28.7041, 77.1025, '$2a$10$dummy.hash.for.testing', 'SHELTER', 'Emergency food relief and community kitchen serving daily meals to those in need.', 4.6, 89),
('Hunger Relief NGO', 'help@hungerrelief.org', '+91-11-4567-8901', 'Delhi', 28.5355, 77.3910, '$2a$10$dummy.hash.for.testing', 'NGO', 'Comprehensive community support center providing food, education, and healthcare services.', 4.9, 234),
('Amity University Canteen', 'canteen@amity.edu', '+91-120-2345-6789', 'Noida', 28.5355, 77.3910, '$2a$10$dummy.hash.for.testing', 'EDUCATIONAL', 'University canteen serving students and staff with surplus food donation program.', 4.7, 312),
('Taj Palace Hotel', 'donations@tajpalace.com', '+91-11-2345-6789', 'Delhi', 28.6139, 77.2090, '$2a$10$dummy.hash.for.testing', 'HOTEL', 'Luxury hotel with comprehensive food donation program for local communities.', 4.9, 445),
('Pizza Hut Express', 'donations@pizzahut.in', '+91-44-2345-6789', 'Chennai', 13.0827, 80.2707, '$2a$10$dummy.hash.for.testing', 'RESTAURANT', 'Fast food chain with daily surplus food donation initiative.', 4.5, 67),
('Spice Garden Restaurant', 'info@spicegarden.com', '+91-22-2345-6789', 'Mumbai', 19.0760, 72.8777, '$2a$10$dummy.hash.for.testing', 'RESTAURANT', 'Fine dining restaurant with comprehensive food donation program.', 4.6, 128),
('Radisson Blu Hotel', 'donations@radissonblu.com', '+91-80-2345-6789', 'Bangalore', 12.9716, 77.5946, '$2a$10$dummy.hash.for.testing', 'HOTEL', 'International hotel chain with sustainable food donation practices.', 4.7, 198);

-- Insert donors with Indian locations and realistic data
INSERT INTO donors (name, email, phone, city, latitude, longitude, password, donor_type, description, total_donations, average_donation_quantity) VALUES
('Delhi University Hostel #5', 'hostel5@du.ac.in', '+91-11-2345-6789', 'Delhi', 28.6139, 77.2090, '$2a$10$dummy.hash.for.testing', 'STUDENT_HOSTEL', 'Student hostel with regular surplus food from mess.', 15, 50),
('IIT Delhi Mess', 'mess@iitd.ac.in', '+91-11-3456-7890', 'Delhi', 28.5450, 77.1925, '$2a$10$dummy.hash.for.testing', 'EDUCATIONAL', 'IIT Delhi mess serving students with daily surplus food.', 156, 75),
('Vivek Raj Sahay', 'vivek@email.com', '+91-98765-43210', 'Pune', 18.5204, 73.8567, '$2a$10$dummy.hash.for.testing', 'INDIVIDUAL', 'Individual donor committed to reducing food waste.', 8, 25),
('Amity University Canteen', 'canteen@amity.edu', '+91-120-2345-6789', 'Noida', 28.5355, 77.3910, '$2a$10$dummy.hash.for.testing', 'EDUCATIONAL', 'University canteen with regular food donation program.', 78, 40),
('Taj Palace Hotel', 'donations@tajpalace.com', '+91-11-2345-6789', 'Delhi', 28.6139, 77.2090, '$2a$10$dummy.hash.for.testing', 'HOTEL', 'Luxury hotel with comprehensive food donation program.', 89, 60),
('Pizza Hut Express', 'donations@pizzahut.in', '+91-44-2345-6789', 'Chennai', 13.0827, 80.2707, '$2a$10$dummy.hash.for.testing', 'RESTAURANT', 'Fast food chain with daily surplus food donation.', 72, 35),
('Spice Garden Restaurant', 'info@spicegarden.com', '+91-22-2345-6789', 'Mumbai', 19.0760, 72.8777, '$2a$10$dummy.hash.for.testing', 'RESTAURANT', 'Fine dining restaurant with food donation initiative.', 128, 45),
('Radisson Blu Hotel', 'donations@radissonblu.com', '+91-80-2345-6789', 'Bangalore', 12.9716, 77.5946, '$2a$10$dummy.hash.for.testing', 'HOTEL', 'International hotel with sustainable food practices.', 98, 55);

-- Insert sample donations
INSERT INTO donations (donor_id, ngo_id, food_details, quantity, status, food_type, special_instructions, expiry_time, pickup_time) VALUES
(1, 1, 'Cooked rice, dal, vegetables', 50.0, 'DELIVERED', 'COOKED_FOOD', 'Vegetarian meals only', CURRENT_TIMESTAMP + INTERVAL '2 hours', CURRENT_TIMESTAMP + INTERVAL '1 hour'),
(2, 2, 'Packaged bread, fruits, snacks', 100.0, 'IN_TRANSIT', 'PACKAGED_FOOD', 'Handle with care', CURRENT_TIMESTAMP + INTERVAL '24 hours', CURRENT_TIMESTAMP + INTERVAL '2 hours'),
(3, 3, 'Fresh vegetables and fruits', 25.0, 'DELIVERED', 'FRESH_PRODUCE', 'Organic produce', CURRENT_TIMESTAMP + INTERVAL '48 hours', CURRENT_TIMESTAMP + INTERVAL '3 hours'),
(4, 4, 'Cooked meals from canteen', 75.0, 'PENDING', 'COOKED_FOOD', 'Hot meals', CURRENT_TIMESTAMP + INTERVAL '4 hours', CURRENT_TIMESTAMP + INTERVAL '1 hour'),
(5, 5, 'Hotel buffet surplus', 60.0, 'DELIVERED', 'COOKED_FOOD', 'High quality meals', CURRENT_TIMESTAMP + INTERVAL '6 hours', CURRENT_TIMESTAMP + INTERVAL '2 hours'),
(6, 6, 'Pizza and bread items', 40.0, 'IN_TRANSIT', 'BAKERY', 'Fresh baked items', CURRENT_TIMESTAMP + INTERVAL '12 hours', CURRENT_TIMESTAMP + INTERVAL '1 hour'),
(7, 7, 'Restaurant surplus meals', 80.0, 'PENDING', 'COOKED_FOOD', 'Spicy food available', CURRENT_TIMESTAMP + INTERVAL '3 hours', CURRENT_TIMESTAMP + INTERVAL '1 hour'),
(8, 8, 'Hotel kitchen surplus', 70.0, 'DELIVERED', 'COOKED_FOOD', 'International cuisine', CURRENT_TIMESTAMP + INTERVAL '5 hours', CURRENT_TIMESTAMP + INTERVAL '2 hours');

-- Insert sample predictions
INSERT INTO predictions (donor_id, predicted_quantity, confidence, prediction_date, valid_until, status, model_version, features) VALUES
(1, 45.0, 0.85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'ACTIVE', 'v1.0', '{"donor_type": "STUDENT_HOSTEL", "day_of_week": 1, "month": 1}'),
(2, 70.0, 0.92, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'ACTIVE', 'v1.0', '{"donor_type": "EDUCATIONAL", "day_of_week": 2, "month": 1}'),
(3, 30.0, 0.78, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'ACTIVE', 'v1.0', '{"donor_type": "INDIVIDUAL", "day_of_week": 3, "month": 1}'),
(4, 50.0, 0.88, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'ACTIVE', 'v1.0', '{"donor_type": "EDUCATIONAL", "day_of_week": 4, "month": 1}'),
(5, 65.0, 0.91, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'ACTIVE', 'v1.0', '{"donor_type": "HOTEL", "day_of_week": 5, "month": 1}');

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