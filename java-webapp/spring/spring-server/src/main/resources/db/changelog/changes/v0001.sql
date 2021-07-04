create table users (
    id serial not null,
    login text not null,
    password text not null,
    role text not null,

    primary key (id)
);

create table book (
    id serial not null,
    name text not null,
    author text not null,
    lang text not null,

    primary key (id)
);

create table book_stats (
    id serial not null,
    amount integer not null,
    total_requests integer not null,
    rate double precision not null,
    book_id serial not null,

    primary key (id),
    foreign key (book_id) references book (id)
);

create table delivery_type (
    id serial not null,
    description text not null,

    primary key (id)
);

create table book_request (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    delivery_type_id serial not null,
    contact text not null,
    state text not null,

    primary key (id),
    foreign key (book_id) references book (id),
    foreign key (delivery_type_id) references delivery_type (id)
);

create table book_request_return (
    id serial not null,
    datetime timestamp with time zone not null,
    request_id serial not null,
    state text not null,

    primary key (id),
    foreign key (request_id) references book_request (id)
);

create table book_rate_changelog (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    user_id serial not null,
    contribution double precision not null,

    primary key (id),
    foreign key (book_id) references book (id),
    foreign key (user_id) references users (id)
);

create table book_balance_changelog (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    amount integer not null,
    comment text not null,

    primary key (id),
    foreign key (book_id) references book (id)
);