
create table test
with (autovacuum_enabled = false)
as (
    select id, random() as v, point(random(), random()) as p
    from generate_series(1, 1000) as id
);

insert into test
(
    select id, random() as v, point(random(), random()) as p
    from generate_series(1001, 999000) as id
);


alter table test add primary key (id);
create index idx_v on test (v, id);
create index idx_p on test using gist (p);

-- drop table test;
-- drop index idx_v;
-- drop index idx_p;


explain ANALYSE
select * from test where id = 200000;


explain ANALYSE
select count(*) from test where  v between 0.1 and 0.9;


VACUUM ANALYSE test;

ANALYSE VERBOSE test;


update test
set v = random()
where id = 300;



-- diagnostic statistics table
select * from pg_stat_user_tables;


select * from pg_statistic;