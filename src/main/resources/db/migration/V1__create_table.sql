create table public.author
(
    id          uuid
        primary key,
    birth_date  date,
    book_count  integer,
    description oid,
    location    varchar(255),
    name        varchar(255) not null
        constraint author_name_unique
            unique
);

create table public.book_metadata
(
    id               uuid
        primary key,
    description      oid,
    insert_date_time timestamp(6) not null,
    name             varchar(255) not null
        constraint book_metadata_unique
            unique,
    written_year     varchar(255),
    author_id        uuid
        constraint book_metadata_author_id_ref
            references public.author
);

create table public.book_file
(
    id          uuid
        primary key,
    body        oid,
    book_audio  oid,
    language    varchar(255),
    metadata_id uuid
        constraint book_file_metadata_id_ref
            references public.book_metadata
);
