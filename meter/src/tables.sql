CREATE TABLE subscription(
icao_code NVARCHAR(4) PRIMARY KEY,
active  BIT NOT NULL DEFAULT 1,
created_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE meter_raw_data (
id INT IDENTITY(1,1) PRIMARY KEY,
icao_code NVARCHAR(4) ,
raw_data NVARCHAR(MAX) NOT NULL,
created_at DATETIME DEFAULT GETDATE(),
CONSTRAINT fk_icao_r FOREIGN KEY (icao_code) REFERENCES subscription(
icao_code)
);


CREATE TABLE meter_detail_data (
    id INT IDENTITY(1,1) PRIMARY KEY,
    icao_code NVARCHAR(4),
    time_of_report DATETIME NOT NULL,

    wind_direction INT,
    wind_speed INT,
    wind_gust INT,
    visibility_in_meters INT,
    weather NVARCHAR(255),
    cloud_cover NVARCHAR(255),
    cloud_altitude_in_feet INT,
    temperature_in_celsius DECIMAL(5,2),
    dew_point_in_celsius DECIMAL(5,2),
    pressure_hpa INT,
    runway_data NVARCHAR(MAX),
    remarks NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_icao FOREIGN KEY (icao_code) REFERENCES subscription(icao_code)
);


