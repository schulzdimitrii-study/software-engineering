CREATE TABLE IF NOT EXISTS tall_stations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL
);