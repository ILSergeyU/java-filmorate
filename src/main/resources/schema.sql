DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS frinds CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS film_user_likes CASCADE;

CREATE TABLE IF NOT EXISTS mpa(
                                     rating_id INTEGER GENERATED BY  DEFAULT AS IDENTITY PRIMARY KEY,
                                     rating_name VARCHAR
);

CREATE TABLE IF NOT EXISTS film(
                                   film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                   film_name VARCHAR,
                                   description VARCHAR,
                                   duration INTEGER,
                                   releaseDate DATE,
                                   rating_id INTEGER REFERENCES mpa (rating_id)
);




CREATE TABLE IF NOT EXISTS users(
                                    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    email VARCHAR NOT NULL,
                                    login VARCHAR,
                                    name VARCHAR,
                                    birthday date
);

CREATE TABLE IF NOT EXISTS film_user_likes(
                                              film_id INTEGER NOT NULL REFERENCES film (film_id),
                                              user_id INTEGER NOT NULL REFERENCES users (user_id)
);
CREATE TABLE IF NOT EXISTS genre(
                                     genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR
);
CREATE TABLE IF NOT EXISTS film_genre(
                                         film_id INTEGER  REFERENCES  film (film_id),
                                         genre_id INTEGER REFERENCES genre (genre_id)
);


CREATE TABLE IF NOT EXISTS frinds(
                                     friend_1 INTEGER  REFERENCES users (user_id),
                                     friend_2 INTEGER  REFERENCES users (user_id),
                                     cofirmation BOOLEAN
);
