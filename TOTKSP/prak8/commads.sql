psql -U fomic -d fomic_db -h localhost -p 5432

-- Task 1 --
-- Session 1 --

CREATE TABLE example_table (
    id SERIAL PRIMARY KEY,
    value VARCHAR(50)
);

INSERT INTO example_table (value) VALUES
('Row 1'),
('Row 2'),
('Row 3');

BEGIN;
UPDATE example_table SET value = 'Updated by Session 1' WHERE id = 1;

-- Session 2 --

BEGIN;
UPDATE example_table SET value = 'Updated by Session 2' WHERE id = 1;

-- Session 3 --

BEGIN;
UPDATE example_table SET value = 'Updated by Session 3' WHERE id = 1;

-- Session 4 --

SELECT * FROM pg_locks WHERE relation = 'example_table'::regclass;


-- Task 2 --
-- Session 1 --

BEGIN;
UPDATE example_table SET value = 'Updated 1 by Session 1' WHERE id = 1;

-- Session 2 --

BEGIN;
UPDATE example_table SET value = 'Updated 2 by Session 2' WHERE id = 2;

-- Session 1 --

UPDATE example_table SET value = 'Updated 2 by Session 1' WHERE id = 2;

-- Session 2 --

UPDATE example_table SET value = 'Updated 1 by Session 2' WHERE id = 1;


-- Task 3 --
-- Session 1 --

BEGIN;
UPDATE example_table SET value = 'Updated by Session 1' WHERE id = 1;

-- Session 2 --

BEGIN;
UPDATE example_table SET value = 'Updated by Session 2' WHERE id = 1;
