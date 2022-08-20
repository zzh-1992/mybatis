create table t_class
(
    id   int auto_increment
        primary key,
    name varchar(255) null
);

create table t_student
(
    id        int auto_increment
        primary key,
    age       int          null,
    class_id  varchar(200) null,
    name      varchar(255) null,
    favourite varchar(200) null,
    up_limit   varchar(200) null
);

create table t_student_class
(
    id   int auto_increment
        primary key,
    s_id int null,
    c_id int null,
    constraint student_class_id
        unique (s_id, c_id)
);

INSERT INTO grapefruit.t_class (id, name) VALUES (1, 'chinese语文');
INSERT INTO grapefruit.t_class (id, name) VALUES (2, 'math数学');
INSERT INTO grapefruit.t_class (id, name) VALUES (3, 'english英语');

INSERT INTO grapefruit.t_student (id, age, class_id, name, favourite, `up_limit`) VALUES (37, 22, '2', 'ZZH', null, null);
INSERT INTO grapefruit.t_student (id, age, class_id, name, favourite, `up_limit`) VALUES (38, 22, '2', 'ZZH', '[{"id":1,"name":"chinese语文"},{"id":2,"name":"math数学"},{"id":3,"name":"english英语"}]', null);

INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (47, 37, 1);
INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (48, 37, 2);
INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (49, 37, 3);
INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (50, 38, 1);
INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (51, 38, 2);
INSERT INTO grapefruit.t_student_class (id, s_id, c_id) VALUES (52, 38, 3);