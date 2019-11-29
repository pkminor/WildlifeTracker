CREATE DATABASE wildlife_tacker;
CREATE TABLE animals (id int serial PRIMARY KEY, name varchar, health varchar, age varchar, type varchar);
CREATE TABLE sightings (id int serial PRIMARY KEY, aid int, location varchar,rangername varchar, sightdate timestamp);
CREATE DATABASE wildlife_tracker_test WITH TEMPLATE wildlife_tracker;
