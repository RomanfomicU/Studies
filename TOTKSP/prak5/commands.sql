pg_dump -U fomic -d fomic_db -h localhost -p 5432 > backup.sql

createdb -U fomic -h localhost -p 5432 tmp_fomic_db

psql -U fomic -d tmp_fomic_db -h localhost -p 5432 < backup.sql

psql -U fomic -d tmp_fomic_db -h localhost -p 5432

BEGIN;
INSERT INTO SALES_ORDER (order_date, customer_id, ship_date, total) VALUES('2024-02-01', 2, '2024-02-05', 180.00);
COMMIT;

BEGIN;
INSERT INTO SALES_ORDER (order_date, customer_id, ship_date, total) VALUES('2024-02-02', 2, '2024-02-03', 100.00);
COMMIT;

SELECT * FROM sales_order;

dropdb -U fomic -h localhost -p 5432 tmp_fomic_db

createdb -U fomic -h localhost -p 5432 tmp_fomic_db

psql -U fomic -d tmp_fomic_db -h localhost -p 5432 < backup.sql

psql -U fomic -d tmp_fomic_db -h localhost -p 5432

SELECT * FROM sales_order;