psql -U fomic -d fomic_db -h localhost -p 5432
psql -U postgres -h 192.168.178.135

--Task 1--

--Session 1--
CREATE DATABASE locks_objects;

\c locks_objects

CREATE TABLE accounts(acc_no integer PRIMARY KEY, amount numeric);

INSERT INTO accounts VALUES (1,1000.00),(2,2000.00),(3,3000.00);

--Session 2--
\c locks_objects

SELECT pg_backend_pid();

BEGIN;
SELECT * FROM accounts WHERE acc_no = 1;

--Session 1--

SELECT locktype, relation::REGCLASS, virtualxid AS virtxid, transactionid AS xid, mode, granted
FROM pg_locks WHERE pid = 124;

--Session 2--

SELECT locktype, relation::REGCLASS, virtualxid AS virtxid, transactionid AS xid, mode, granted
FROM pg_locks WHERE pid = 124;

COMMIT;


--Task 2--

--Session 2--
SELECT pg_backend_pid();
BEGIN ISOLATION LEVEL SERIALIZABLE;
SELECT * FROM accounts WHERE acc_no = 1;

--Session 1--
SELECT locktype, relation::REGCLASS, page, tuple, virtualxid AS virtxid, transactionid AS xid, mode, granted
FROM pg_locks WHERE pid = 124;

--Session 2--
Commit;


--Task 3--

--Session 1--
ALTER SYSTEM SET log_lock_waits = on;

ALTER SYSTEM SET deadlock_timeout = '100ms';

SELECT pg_reload_conf();

--Session 1--
BEGIN;

UPDATE accounts SET amount = 10.00 WHERE acc_no = 1;

--Session 2--

BEGIN;

UPDATE accounts SET amount = 100.00 WHERE acc_no = 1;

--Session 1--

SELECT pg_sleep(1);

COMMIT;

--Session 2--

COMMIT;