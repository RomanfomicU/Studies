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

-- Session 3 --

BEGIN;
UPDATE example_table SET value = 'Updated 3 by Session 3' WHERE id = 3;

-- Session 1 --

UPDATE example_table SET value = 'Updated 2 by Session 1' WHERE id = 2;

-- Session 2 --

UPDATE example_table SET value = 'Updated 3 by Session 2' WHERE id = 3;

-- Session 3 --

UPDATE example_table SET value = 'Updated 1 by Session 3' WHERE id = 1;


-- Task 3 --
-- Session 1 --

psql -U fomic -d fomic_db -h localhost -p 5432

psql -U fomic -d fomic_db -h localhost -p 5432 -c "drop table test"
psql -U fomic -d fomic_db -h localhost -p 5432 -c "create table test(id integer primary key generated always as identity, n float)"
psql -U fomic -d fomic_db -h localhost -p 5432 -c "insert into test(n) select random() from generate_series(1,1000000)"

psql -U fomic -d fomic_db -h localhost -p 5432 << EOF
BEGIN ISOLATION LEVEL REPEATABLE READ;
UPDATE test SET n = (select id from test order by id asc limit 1 for update);
COMMIT;
EOF


-- Session 2 --

psql -U fomic -d fomic_db -h localhost -p 5432 << EOF
BEGIN ISOLATION LEVEL REPEATABLE READ;
UPDATE test SET n = (select id from test order by id desc limit 1 for update);
COMMIT;
EOF