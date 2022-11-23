
run:
	rm -r target
	mvn clean
	cp src/main/resources/application.properties.dev src/main/resources/application.properties
	mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug"

clone_ui:
	git clone https://github.com/jakubbedra/graphgame-ui.git ../ui

build_ui:
	cd ../ui && npm install
	cd ../ui && ng build

build:
	rm -rf src/main/resources/public/*
	rm -rf target
	cp ../ui/dist/graphgame-ui/* src/main/resources/public/
	mvn clean
	cp src/main/resources/application.properties.prod src/main/resources/application.properties
	mvn package -Dmaven.test.skip
	cp src/main/resources/application.properties.dev src/main/resources/application.properties

upload:
	scp target/graphgame-0.0.1-SNAPSHOT.jar admin@54.93.168.155:/home/admin/
	

