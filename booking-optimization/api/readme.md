# Java API

## Dependencies 
Into the `pom.xml` file, all the dependencies of the project are specified. 
List of dependencies along with download links from Maven Central Repo:     
- org.glassfish.jersey.bundles : `jaxrs-ri-3.1.3.zip`     
        https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/3.1.3/    
- org.glassfish.jersey.media : `jersey-media-multipart-3.1.3.jar`     
        https://repo1.maven.org/maven2/org/glassfish/jersey/media/jersey-media-multipart/3.1.3/   
   
*During build, all of the specified dependencies in the `pom.xml` will be downloaded automatically from the Maven repo. Besides that, you can manually download them from the links, in order to package them inside the source folder of the project as well.*

## Build 
Execute the following command inorder to build the artifact of the project. 
```
mvn clean install
```
After the building phase, make sure that the `targer` folder of the project contains the `.war` artifact.
## Deployment
You can either deploy the Java API using docker, kubernetes, or a locally installed Web Application Server (i.e. Glassfish, Tomacat, etc).
### Deploy with Docker
Build the docker image. In the current folder, run:
```
docker build -t bookings ./
``` 

Start the docker container.
```
docker run -d -p 8080:8080 --name bookings bookings:latest 
```

### Deploy with Kubernetes
Build the docker image. If not done, execute the `docker build` command in the previous Docker section.   
```
docker build -t bookings ./
``` 
Next, navigate through the `kubernetes` folder of the project and follow the instructions.
