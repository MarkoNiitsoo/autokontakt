CREATE TABLE IF NOT EXISTS car_brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES car_brand(id)
);

CREATE TABLE IF NOT EXISTS contact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    valid_driver_license BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS contact_carbrands (
    contact_id BIGINT NOT NULL,
    carbrand_id BIGINT NOT NULL,
    PRIMARY KEY (contact_id, carbrand_id),
    FOREIGN KEY (contact_id) REFERENCES contact(id),
    FOREIGN KEY (carbrand_id) REFERENCES car_brand(id)
);
