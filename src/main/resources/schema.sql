-- Automarkide tabel
-- Võimaldab luua hierarhilise struktuuri (nt Mercedes-Benz -> C-Klass)
CREATE TABLE IF NOT EXISTS car_brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,        -- Automargi nimi
    parent_id BIGINT,                  -- Viide ülemkategooriale (null kui tippkategooria)
    FOREIGN KEY (parent_id) REFERENCES car_brand(id)
);

-- Kontaktide tabel
-- Sisaldab põhilisi kontaktandmeid
CREATE TABLE IF NOT EXISTS contact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,    -- Kontakti täisnimi
    phone VARCHAR(20) NOT NULL,         -- Kontakttelefon
    valid_driver_license BOOLEAN DEFAULT FALSE  -- Kas omab kehtivat juhiluba
);

-- Seosetabel kontaktide ja automarkide vahel
-- Võimaldab igal kontaktil valida mitu automarki
CREATE TABLE IF NOT EXISTS contact_carbrands (
    contact_id BIGINT NOT NULL,
    carbrand_id BIGINT NOT NULL,
    PRIMARY KEY (contact_id, carbrand_id),
    FOREIGN KEY (contact_id) REFERENCES contact(id),
    FOREIGN KEY (carbrand_id) REFERENCES car_brand(id)
);
