# [WildlifeTracker ](https://github.com/pkminor/WildlifeTracker)

Author [Patrick Maina](https://github.com/pkminor)

## Brief description
Wildlife tracker is an application that offers wildlife personnel tools to manage wildlife. They
are able to record new animals into the system, tag them as endangered and if so provide their health and status.
They are also able to record sighting of the animals they have entered into the system.

## User Requirements
- The user would like add a new animal to the system
- While adding a new animal the user would like to specify if the animal is endangered 
- If the animal is endangered the user would like to select the health and age from a predefined list
- The user would like to record new sightings of an animal
- While recording a sighting, the user would like to select the animal from the available list and
key in the location and name of the ranger who sighted the animal
- The user would like to view; 
  - all/sighted/unsighted wildlife, 
  - all/sighted/unsighted animals, 
  - all/sighted/unsighted endangered, 
  - all/animal/endangered sightings

## How it works

# Server Side
The application has three model classes;
- Animal
- EndangeredAnimal
- Sighting

The main class with the routes is App

The EndangeredAnimal inherits from the Animal class
Each model class has a corresponding DAO made of an interface and interface implementation class.
The SparkJava routing logic uses the corresponding DAO to service GET/POST requests.

The routes are;

- For all Wildlife
   - GET: "/"
   - GET: "/sighted"
   - GET: "/unsighted"
   
- For animals
   - GET: "/animals"
   - GET: "/animals/sighted"
   - GET: "/animals/unsighted"
   
- For endangered
   - GET: "/endangered"
   - GET: "/endangered/sighted"
   - GET: "/endangered/unsighted"
   
- For sightings
    - GET: "/sightings"
    - GET: "/sightings/animals"
    - GET: "/sightings/endangered"

- To create new entries
    - POST: "/animals/new"
    - POST: "/sightings/new"

# Client Side
The landing page presents users with two forms
- Add animal form
- Add Sighting form

The add animal form enables the user to key in details of an animal. The add sighting form allows the user to record
sightings of any of the recorded animals. They specify the location and the name of the ranger who sighted the animal.

To the left of the page are links to various categoris that the user might want to see, among them;

- All wildlife
- Sighted wildlife
- Unsighted wildlife
- Animals
- Sighted animals
- Unsighted animals
- Endangered
- Sighted Endangered
- Unsighted Endangered
- All sightings
- Animals Sightings
- Endangered Sightings

## Setup Instructions
Fork the project on github [WildlifeTracker ](https://github.com/pkminor/WildlifeTracker), then clone it to your local repo.

Run the following scripts to create the postgres database;
 - CREATE DATABASE wildlife_tacker;
 - CREATE TABLE animals (id int serial PRIMARY KEY, name varchar, health varchar, age varchar, type varchar);
 - CREATE TABLE sightings (id int serial PRIMARY KEY, aid int, location varchar,rangername varchar, sightdate timestamp);
 - CREATE DATABASE wildlife_tracker_test WITH TEMPLATE wildlife_tracker;
 
## Technology used
 - Java
 - SparkJava
 - Gradle
 - JUnit 4
 - Git VCS
 - IntelliJ IDEA Community Edition
 - Postgres
 - Sql2o

## Known Bugs
There are no known bugs. Please submit those you find for correction.

## Contributing
Pull requests are encouraged.

## License
This project is licensed under the MIT Open Source license.
