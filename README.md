
# Graphgame

This project was made use spring-boot and Maven for it's compilation.

## Requirements

java 11
maven

## Running database

To run database you need to install Docker on your system and run the following command:

`sudo docker run -p 3306:3306 --name mariadb-graphgame --env MYSQL_ROOT_PASSWORD=root -d mariadb`

Which runs docker container with MariaDB server on localhost:3306

After running a database there is a requirement to create a new database namespace for application. Default database namespace in application is `graphsdb`.
To achieve this you can execute following commands in terminal:

`sudo docker exec -it mariadb-graphgame bash`

After that you are logged into database server as a root. Inside a database server you need to connect to MariaDB server with a command:

`mariadb -p root`

And then write a password given in first command:

`root`

Next you need to create new database namespace:

`create database graphsdb;`

After that the database is set.

## Development server

To run a local development server type following commands:


for linux users without gnu-make:

```bash
cp src/main/resources/application.properties.local.dev src/main/resources/application.properties
mvn spring-boot:run
```

for linux users with gnu-make installed:

```bash
make run_local
```

for windows users:

```bash
copy src\main\resources\application.properties.local.dev src\main\resources\application.properties
mvn spring-boot:run
```


