-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create indexes for spatial queries
CREATE INDEX IF NOT EXISTS idx_users_location ON users USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_ngos_location ON ngos USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_donors_location ON donors USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_volunteers_location ON volunteers USING GIST (ST_MakePoint(longitude, latitude));
