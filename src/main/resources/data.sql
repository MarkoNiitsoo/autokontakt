INSERT INTO car_brand (name, parent_id) VALUES ('Mercedes-Benz', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('C klass', (SELECT id FROM car_brand WHERE name='Mercedes-Benz'));
INSERT INTO car_brand (name, parent_id) VALUES ('C 160', (SELECT id FROM car_brand WHERE name='C klass'));
INSERT INTO car_brand (name, parent_id) VALUES ('C 180', (SELECT id FROM car_brand WHERE name='C klass'));
INSERT INTO car_brand (name, parent_id) VALUES ('C 200', (SELECT id FROM car_brand WHERE name='C klass'));
INSERT INTO car_brand (name, parent_id) VALUES ('C 220', (SELECT id FROM car_brand WHERE name='C klass'));

INSERT INTO car_brand (name, parent_id) VALUES ('BMW', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('3 seeria', (SELECT id FROM car_brand WHERE name='BMW'));
INSERT INTO car_brand (name, parent_id) VALUES ('315', (SELECT id FROM car_brand WHERE name='3 seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('317', (SELECT id FROM car_brand WHERE name='3 seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('319', (SELECT id FROM car_brand WHERE name='3 seeria'));

INSERT INTO car_brand (name, parent_id) VALUES ('4 seeria', (SELECT id FROM car_brand WHERE name='BMW'));

INSERT INTO car_brand (name, parent_id) VALUES ('5 seeria', (SELECT id FROM car_brand WHERE name='BMW'));
INSERT INTO car_brand (name, parent_id) VALUES ('518', (SELECT id FROM car_brand WHERE name='5 seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('523', (SELECT id FROM car_brand WHERE name='5 seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('524', (SELECT id FROM car_brand WHERE name='5 seeria'));

INSERT INTO car_brand (name, parent_id) VALUES ('Audi', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('e-tron', (SELECT id FROM car_brand WHERE name='Audi'));

INSERT INTO car_brand (name, parent_id) VALUES ('Q seeria', (SELECT id FROM car_brand WHERE name='Audi'));
INSERT INTO car_brand (name, parent_id) VALUES ('Q2', (SELECT id FROM car_brand WHERE name='Q seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('Q4', (SELECT id FROM car_brand WHERE name='Q seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('Q5', (SELECT id FROM car_brand WHERE name='Q seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('Q7', (SELECT id FROM car_brand WHERE name='Q seeria'));

INSERT INTO car_brand (name, parent_id) VALUES ('RS seeria', (SELECT id FROM car_brand WHERE name='Audi'));
INSERT INTO car_brand (name, parent_id) VALUES ('RS4', (SELECT id FROM car_brand WHERE name='RS seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('RS5', (SELECT id FROM car_brand WHERE name='RS seeria'));
INSERT INTO car_brand (name, parent_id) VALUES ('RS6', (SELECT id FROM car_brand WHERE name='RS seeria'));

INSERT INTO car_brand (name, parent_id) VALUES ('TT', (SELECT id FROM car_brand WHERE name='Audi'));

INSERT INTO car_brand (name, parent_id) VALUES ('Citroën', NULL);
