CREATE SEQUENCE IF NOT EXISTS passages_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS passages (
    id INTEGER NOT NULL DEFAULT nextval('passages_seq'),
    vehicle_id INTEGER NOT NULL,
    tall_station_id INTEGER NOT NULL,
    location VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    amount_paid DECIMAL(10, 2) NOT NULL,
    multiplier_applied DECIMAL(10, 2),
    
    PRIMARY KEY (id),
    CONSTRAINT fk_passages_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_passages_tall_station FOREIGN KEY (tall_station_id) REFERENCES tall_stations(id)
);
