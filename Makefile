
run:
	rm -r target
	mvn clean
	mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug"

clone_ui:
	git clone https://github.com/jakubbedra/graphgame-ui.git ../ui
	
build:
	rm src/main/resources/public/*
	rm -r target
	cd ../ui && npm install
	cd ../ui && ng build
	cp ../ui/dist/graphgame-ui/* src/main/resources/public/
	mvn clean
	mvn package

upload:
	scp target/graphgame-0.0.1-SNAPSHOT.jar admin@54.93.168.155:/home/admin/
	

