check-repo:
ifndef REPOSITORY
	$(error La variable de entorno REPOSITORY no está definida. )
endif

build: check-repo
	cd proleap-cobol-neo4j && mvn install -s ../settings.xml 

# Limpiar todos los proyectos
clean:
	cd proleap-cobol &&  mvn clean -s ../settings.xml -DskipTests

.PHONY: build clean check-repo