# ObjectRewriter

The main purpose of this application is to consume the REST API and save the downloaded objects to separate files.

## Supported objects

Currently, the application supports the following objects:

- `Post(long userId, long id, String title, String body)`

## Configuration

The application configuration is stored in the `application.properties` file. Currently, it is possible to define the
following properties:

- `api.url.posts` - address to the list of Post objects

Additional parameters are defined at the start of the application, a list of them is provided below. Example
usage: `-DoutputDir.posts=postOutput`

- `-DoutputDir.posts` - optional, destination folder for saving Post objects, default will be saved in posts folder

## Running the application

The application is designed to run using Java at a minimum version 21. The Maven build tool is used to compile the
source code.

### Compilation and startup

The condition for the following commands to work is the presence of Java and Maven in the environment variables.

It is possible to run the application immediately after compilation. To do this, run the following commands in the root
folder (the
one with the `pom.xml` file):

`mvn clean compile exec:java -Dexec.mainClass=com.kw.objectrewriter.ObjectRewriterStarter`

You can also generate a jar file with the application. Run the following command:

`mvn clean package`

This will generate `objectrewriter-0.1-SNAPSHOT-jar-with-dependencies.jar` archive inside `target` folder, which is
standalone application. Run it
with command

`java -jar target\objectrewriter-0.1-SNAPSHOT-jar-with-dependencies.jar`

If you prefer to generate JAR archive and keep dependencies separately run command:

`mvn clean package dependency:copy-dependencies -DoutputDirectory=target/lib/`

The result of this command will be an `objectrewriter-0.1-SNAPSHOT.jar` archive and a `lib` folder with the required
dependencies. You can run it with below command:

`java -jar target\objectrewriter-0.1-SNAPSHOT.jar`


