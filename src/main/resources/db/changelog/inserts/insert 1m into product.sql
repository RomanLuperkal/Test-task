INSERT INTO categories(name)
values ('fruit'),
       ('vegetables'),
       ('household goods');


INSERT INTO product
select gen_random_uuid(), 'name' || generate_series(1, 1000000), 'article' || generate_series(1, 1000000),
       'description' || generate_series(1, 1000000), floor(random() * (3 - 1 + 1) + 1)::int, 10.0, 50,
       now()::timestamp(0), now()::timestamp(0) + INTERVAL '1 DAY';
