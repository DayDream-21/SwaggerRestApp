CREATE TABLE navy.ports
(
    id       bigserial    NOT NULL,
    "name"   varchar(255) NOT NULL,
    capacity int4         NOT NULL,
    CONSTRAINT port_pk PRIMARY KEY (id)
);

CREATE TABLE navy.ships
(
    id      bigserial    NOT NULL,
    "name"  varchar(255) NOT NULL,
    port_id int8         NULL,
    status  varchar(255) NOT NULL,
    CONSTRAINT ship_pk PRIMARY KEY (id),
    CONSTRAINT ship_fk_port FOREIGN KEY (port_id) REFERENCES navy.ports (id)
);

INSERT INTO navy.ports (name, capacity)
VALUES ('Дальний', 5),
       ('Солнечный', 10),
       ('Восточный', 7);

INSERT INTO navy.ships (name, status, port_id)
VALUES ('Проворный', 'PORT', 1),
       ('Санта Мария', 'PORT', 1),
       ('Туман', 'PORT', 1),
       ('Буря', 'PORT', 1),
       ('Сказочный', 'PORT', 1),
       ('Грозный', 'PORT', 2),
       ('Восток', 'PORT', 2),
       ('Кудесник', 'PORT', 3),
       ('Арктика', 'SEA', NULL),
       ('Москва', 'PORT', 3);

ALTER TABLE navy.ships
    ALTER COLUMN status TYPE varchar(255);