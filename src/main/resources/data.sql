-- ===============================
-- Tippkategooria automargid
-- ===============================
INSERT INTO car_brand (name, parent_id) VALUES ('Mercedes-Benz', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('BMW', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('Audi', NULL);
INSERT INTO car_brand (name, parent_id) VALUES ('Citroën', NULL);

-- ===============================
-- Mercedes-Benzi mudelid
-- ===============================
-- C-klassi hierarhia
INSERT INTO car_brand (name, parent_id) VALUES ('C klass', 1);  -- parent_id=1 viitab Mercedes-Benzile
INSERT INTO car_brand (name, parent_id) VALUES ('C 160', 5);    -- parent_id=5 viitab C klassile
INSERT INTO car_brand (name, parent_id) VALUES ('C 180', 5);
INSERT INTO car_brand (name, parent_id) VALUES ('C 200', 5);
INSERT INTO car_brand (name, parent_id) VALUES ('C 220', 5);

-- ===============================
-- BMW mudelid
-- ===============================
-- 3. seeria hierarhia
INSERT INTO car_brand (name, parent_id) VALUES ('3 seeria', 2); -- parent_id=2 viitab BMW-le
INSERT INTO car_brand (name, parent_id) VALUES ('315', 10);     -- parent_id=10 viitab 3. seeriale
INSERT INTO car_brand (name, parent_id) VALUES ('317', 10);
INSERT INTO car_brand (name, parent_id) VALUES ('319', 10);

-- 4. seeria
INSERT INTO car_brand (name, parent_id) VALUES ('4 seeria', 2);

-- 5. seeria hierarhia
INSERT INTO car_brand (name, parent_id) VALUES ('5 seeria', 2);
INSERT INTO car_brand (name, parent_id) VALUES ('518', 15);     -- parent_id=15 viitab 5. seeriale
INSERT INTO car_brand (name, parent_id) VALUES ('523', 15);
INSERT INTO car_brand (name, parent_id) VALUES ('524', 15);

-- ===============================
-- Audi mudelid
-- ===============================
-- Elektriautod
INSERT INTO car_brand (name, parent_id) VALUES ('e-tron', 3);   -- parent_id=3 viitab Audile

-- Q-seeria hierarhia
INSERT INTO car_brand (name, parent_id) VALUES ('Q seeria', 3);
INSERT INTO car_brand (name, parent_id) VALUES ('Q2', 20);      -- parent_id=20 viitab Q seeriale
INSERT INTO car_brand (name, parent_id) VALUES ('Q4', 20);
INSERT INTO car_brand (name, parent_id) VALUES ('Q5', 20);
INSERT INTO car_brand (name, parent_id) VALUES ('Q7', 20);

-- RS-seeria hierarhia
INSERT INTO car_brand (name, parent_id) VALUES ('RS seeria', 3);
INSERT INTO car_brand (name, parent_id) VALUES ('RS4', 25);     -- parent_id=25 viitab RS seeriale
INSERT INTO car_brand (name, parent_id) VALUES ('RS5', 25);
INSERT INTO car_brand (name, parent_id) VALUES ('RS6', 25);

-- Muud mudelid
INSERT INTO car_brand (name, parent_id) VALUES ('TT', 3);
