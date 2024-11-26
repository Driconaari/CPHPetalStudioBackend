create table bouquets
(
    id          bigint auto_increment
        primary key,
    description varchar(255)   null,
    image_url   varchar(255)   null,
    name        varchar(255)   null,
    price       decimal(38, 2) null
);

create table users
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    is_company bit          not null,
    password   varchar(255) null,
    role       varchar(255) null,
    username   varchar(255) null
);

create table subscriptions
(
    id         bigint auto_increment
        primary key,
    end_date   date         null,
    frequency  varchar(255) null,
    start_date date         null,
    bouquet_id bigint       null,
    user_id    bigint       null,
    constraint FK7s9cvy33d7kh7xob9h1evr2ep
        foreign key (bouquet_id) references bouquets (id),
    constraint FKhro52ohfqfbay9774bev0qinr
        foreign key (user_id) references users (id)
);

