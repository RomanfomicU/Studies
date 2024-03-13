
#Остановка в режиме fast
/usr/lib/postgresql/14/bin/pg_ctl -w -l ~/logfile -D /var/lib/postgresql/14/main restart

#Остановка в режиме immidiate
/usr/lib/postgresql/14/bin/pg_ctl -w -D /var/lib/postgresql/14/main stop -m immediate
/usr/lib/postgresql/14/bin/pg_ctl -w -l ~/logfile -D /var/lib/postgresql/14/main start

psql -d postgres

SQL code;

create database wal_log;
\c wal_log;
create table t(id integer);
insert into t values (1);

create extension pageinspect;

select pg_current_wal_insert_lsn();

select pg_walfile_name('0/17603E8');

select * from pg_ls_waldir() LIMIT 10;

insert into t values(2);

insert into t values(3);

select pg_current_wal_insert_lsn();

select pg_walfile_name('0/1760630');

select * from pg_ls_waldir() LIMIT 10;

/usr/lib/postgresql/14/bin/pg_waldump -p /var/lib/postgresql/14/main2/pg_wal -s 0/17603E8 -e 0/1760630 000000010000000000000001