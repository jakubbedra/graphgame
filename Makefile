
run:
	rm -r target && mvn clean && mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug"

