create or replace function get_pg_info()
returns text
language plpgsql
as
$$
declare
ver text;
   tm text;
   pid text;
   slp double precision;
begin
--    select 0.2 + (random() ) into slp;
--    raise notice 'Sleep = %',slp;
--    perform pg_sleep(slp);
select version() into ver;
select now()::text into tm;
select pg_backend_pid()::text into pid;
return concat(ver,' - ', pid,' - ',tm);
end;
$$;

