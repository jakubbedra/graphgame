
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

## Alternative to running database

If you want to test application localy, without worrying about setting up database, you may connect to our database hosted on graphgame.work.gd:3306 (which should be up and running until 2023.02). To achievie this, you need to copy file `src/main/resources/application.properties.dev` to `src/main/resources/application.properties`.

## Running development server

To run a local development server you need to configure server application to use local database by copying file `src/main/resources/application.properties.local.dev` to `src/main/resources/application.properties`.

To run server type a command:


```bash
mvn spring-boot:run
```

for linux users with GNU Make installed (clean run with server configured to connect to local database localhost:3306):

```bash
make run_local
```

If you are linux user and don't want to run local database and use our resources (database located at graphgame.work.gd:3306), you can run following command:

```bash
make run
```




