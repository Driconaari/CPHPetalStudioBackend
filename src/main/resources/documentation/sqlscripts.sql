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

INSERT INTO bouquet (id, name, created_at, description, featured, image_url, category, price, stock_quantity) VALUES
                                                                                                                  (1, 'Roses', '2024-12-12 13:52:56.000000', 'A classic bouquet of red roses.', true, 'https://www.post-a-rose.com/prod_cat/images/18-Select-Red-Roses__________wi850he850moletterboxbgwhite.jpg', 'Red Roses', 49.99, 20),
                                                                                                                  (2, 'Tulips', '2024-12-12 13:52:56.000000', 'Bright and cheerful tulip mix.', true, 'https://nectarflowersottawa.ca/cdn/shop/files/tulips-ottawa_720x.jpg?v=1710492753', 'Mixed Tulips', 39.99, 15),
                                                                                                                  (3, 'Lilies', '2024-12-12 13:52:56.000000', 'Elegant white lilies.', false, 'https://tigerlily.com/cdn/shop/files/classic-white-lilies-237359.jpg?v=1727720295', 'White Lilies', 59.99, 10),
                                                                                                                  (4, 'Orchids', '2024-12-12 13:52:56.000000', 'Exotic orchid arrangement.', false, 'https://tigerlily.com/cdn/shop/files/classic-white-lilies-237359.jpg?v=1727720295', 'Orchid Delight', 79.99, 8),
                                                                                                                  (5, 'Sunflowers', '2024-12-12 13:52:56.000000', 'Sunny and vibrant sunflowers.', true, 'https://www.floraly.com.au/cdn/shop/articles/pexels-susanne-jutzeler-1169084_2.jpg?v=1597813597', 'Sunflower Sunshine', 29.99, 25),
                                                                                                                  (6, 'Daisies', '2024-12-12 13:52:56.000000', 'Pure and simple daisies.', false, 'https://makingdream.ca/wp-content/uploads/2024/04/Premium-White-Daisies-Bouquet-938474561-1200x900.jpg', 'Daisy Dream', 19.99, 30),
                                                                                                                  (7, 'Mixed', '2024-12-12 13:52:56.000000', 'A beautiful mix of seasonal flowers.', true, 'https://www.flowerfix.com/cdn/shop/products/retro-colorful-mixed-flower-bouquet-C1161H_2000x.jpg?v=1626390544', 'Seasonal Mix', 44.99, 12),
                                                                                                                  (8, 'Peonies', '2024-12-12 13:52:56.000000', 'Romantic pink peonies.', false, 'https://www.surprose.com/media/catalog/product/cache/00fc3324f82f0edd130c68e9a6e51d4c/1/0/10-soft-pink-peonies-8720174083389-a.jpg', 'Pretty Peonies', 69.99, 6),
                                                                                                                  (9, 'Carnations', '2024-12-12 13:52:56.000000', 'Fragrant carnation bouquet.', false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZ4JiWQd_5vnrQlcM1fxamwLclQSPyefHWmmoskZo2JJUbwSxATxs-JTvfc_V8dRODJVs&usqp=CAU', 'Carnation Charm', 24.99, 18),
                                                                                                                  (10, 'Succulents', '2024-12-12 13:52:56.000000', 'A bouquet of hardy succulents.', true, 'https://c02.purpledshub.com/uploads/sites/40/2023/01/MG0462-Mixed-succulents-202797c.jpg?w=1029&webp=1', 'Succulent Surprise', 34.99, 20);

