INSERT INTO navy.ports (capacity, name)
VALUES
    (5,  'Дальний'),
    (10, 'Солнечный'),
    (7,  'Восточный');

INSERT INTO navy.ships (max_crew_capacity, min_crew_capacity, name, status, port_id)
VALUES
    (5, 2, 'Проворный',   'SEA',  NULL),
    (5, 2, 'Санта Мария', 'PORT', 1),
    (5, 2, 'Туман',       'PORT', 2),
    (5, 2, 'Буря',        'PORT', 2),
    (5, 2, 'Сказочный',   'PORT', 2),
    (5, 2, 'Грозный',     'PORT', 3),
    (5, 2, 'Восток',      'PORT', 3),
    (5, 2, 'Кудесник',    'PORT', 3),
    (5, 2, 'Арктика',     'PORT', 3),
    (5, 2, 'Москва',      'PORT', 2),
    (5, 2, 'Беда',        'PORT', 2),
    (5, 2, 'Красивый',    'PORT', 2);

INSERT INTO navy.crew_members (status, name, role, ship_id)
VALUES
    ('ON_SHIP', 'Ivan',   'CAPITAN', 1),
    ('ON_LAND', 'Anton',  'CAPITAN', null),
    ('ON_SHIP', 'Slava',  'MATE',    1),
    ('ON_SHIP', 'Dima',   'MATE',    1),
    ('ON_SHIP', 'Maksim', 'MATE',    2),
    ('ON_SHIP', 'Jenya',  'MATE',    2);