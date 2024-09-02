psql -U fomic -d fomic_db -h localhost -p 5432


-- Task 1 --

SELECT COUNT(*) FROM sales_order;

\timing on

DO $$
BEGIN
  FOR i IN 1..1000 LOOP
	EXECUTE 'SELECT avg(total) FROM SALES_ORDER';
  END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  FOR i IN 1..1000 LOOP
	PERFORM avg(total) FROM SALES_ORDER;
  END LOOP;
END;
$$ LANGUAGE plpgsql;

\timing off

-- Task 2 --

UPDATE SALES_ORDER SET total = 11111 where order_id = 70;

\timing on

DO $$
BEGIN
  FOR i IN 1..100000 LOOP
	EXECUTE 'SELECT * FROM SALES_ORDER WHERE total = 11111';
  END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  FOR i IN 1..100000 LOOP
	PERFORM * FROM SALES_ORDER WHERE total = 11111;
  END LOOP;
END;
$$ LANGUAGE plpgsql;

\timing off