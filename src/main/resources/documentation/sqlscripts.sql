create table bouquets
(
    id                bigint auto_increment
        primary key,
    description       varchar(255)   null,
    image_url         varchar(255)   null,
    name              varchar(255)   null,
    price             decimal(10, 2) null,
    category          varchar(255)   null,
    created_at        datetime(6)    null,
    featured          bit            not null,
    stock_quantity    int            not null,
    quantity_in_stock int            not null
);

create table carts
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) not null,
    user_id    bigint      not null,
    constraint UK64t7ox312pqal3p7fg9o503c2
        unique (user_id)
);

create table users
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    is_company bit          not null,
    password   varchar(255) null,
    role       varchar(255) null,
    username   varchar(255) null,
    created_at datetime(6)  null,
    cart_id    bigint       null,
    constraint UKpnp1baae4enifkkuq2cd01r9l
        unique (cart_id),
    constraint FKdv26y3bb4vdmsr89c9ppnx85w
        foreign key (cart_id) references carts (id)
);

create table cart_items
(
    id         bigint auto_increment
        primary key,
    bouquet_id bigint      null,
    quantity   int         not null,
    user_id    bigint      null,
    created_at datetime(6) not null,
    cart_id    bigint      not null,
    constraint FK709eickf3kc0dujx3ub9i7btf
        foreign key (user_id) references users (id),
    constraint FKpcttvuq4mxppo8sxggjtn5i2c
        foreign key (cart_id) references carts (id),
    constraint FKst8j6v28tsvay8afoao11fung
        foreign key (bouquet_id) references bouquets (id)
);

alter table carts
    add constraint FKb5o626f86h46m4s7ms6ginnop
        foreign key (user_id) references users (id);

create table orders
(
    id         bigint auto_increment
        primary key,
    order_date datetime(6)  null,
    status     varchar(255) null,
    total      double       not null,
    user_id    bigint       not null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references users (id)
);

create table order_bouquets
(
    order_id   bigint not null,
    bouquet_id bigint not null,
    constraint FK1mumfr3ah5v4jdiqeq9ycrict
        foreign key (order_id) references orders (id),
    constraint FKs996xahgwl54efihj91oh0bun
        foreign key (bouquet_id) references bouquets (id)
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

