DROP TABLE IF EXISTS categories CASCADE ;
DROP TABLE IF EXISTS product CASCADE ;
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS product (
    uuid           UUID PRIMARY KEY,
    name           VARCHAR NOT NULL,
    article_number VARCHAR NOT NULL,
    description    VARCHAR,
    category_id    BIGINT REFERENCES categories (category_id) ON DELETE RESTRICT NOT NULL ,
    price          NUMERIC(6,2) NOT NULL,
    quantity       INTEGER NOT NULL,
    last_update    TIMESTAMP(0),
    creation_date    TIMESTAMP(0) NOT NULL,
    CONSTRAINT uq_article_number UNIQUE (article_number)
);
