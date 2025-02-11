-- Insert top-level brands first
INSERT INTO car_brand (name, parent_id) VALUES ('Mercedes-Benz', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('BMW', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('Audi', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('Citroën', NULL);

-- Then, insert children, referencing the parent IDs
INSERT INTO car_brand (name, parent_id) VALUES ('C klass', 1);
INSERT INTO car_brand (name, parent_id) VALUES ('C 160', 5);
INSERT INTO car_brand (name, parent_id) VALUES ('C 180', 5);
INSERT INTO car_brand (name, parent_id) VALUES ('C 200', 5);
INSERT INTO car_brand (name, parent_id) VALUES ('C 220', 5);

INSERT INTO car_brand (name, parent_id) VALUES ('3 seeria', 2);
INSERT INTO car_brand (name, parent_id) VALUES ('315', 10);
INSERT INTO car_brand (name, parent_id) VALUES ('317', 10);
INSERT INTO car_brand (name, parent_id) VALUES ('319', 10);

INSERT INTO car_brand (name, parent_id) VALUES ('4 seeria', 2);

INSERT INTO car_brand (name, parent_id) VALUES ('5 seeria', 2);
INSERT INTO car_brand (name, parent_id) VALUES ('518', 15);
INSERT INTO car_brand (name, parent_id) VALUES ('523', 15);
INSERT INTO car_brand (name, parent_id) VALUES ('524', 15);

INSERT INTO car_brand (name, parent_id) VALUES ('e-tron', 3);

INSERT INTO car_brand (name, parent_id) VALUES ('Q seeria', 3);
INSERT INTO car_brand (name, parent_id) VALUES ('Q2', 20);
INSERT INTO car_brand (name, parent_id) VALUES ('Q4', 20);
INSERT INTO car_brand (name, parent_id) VALUES ('Q5', 20);
INSERT INTO car_brand (name, parent_id) VALUES ('Q7', 20);

INSERT INTO car_brand (name, parent_id) VALUES ('RS seeria', 3);
INSERT INTO car_brand (name, parent_id) VALUES ('RS4', 25);
INSERT INTO car_brand (name, parent_id) VALUES ('RS5', 25);
INSERT INTO car_brand (name, parent_id) VALUES ('RS6', 25);

INSERT INTO car_brand (name, parent_id) VALUES ('TT', 3);
