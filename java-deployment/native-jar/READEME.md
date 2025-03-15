- Compile source

```
javac -d target src/**/*.java
```

- Move to the `target` directory
```
cd target
```

- Package source-code to executable .jar file
```
jar cvfe myapp.jar com.tungnn.deployment.JavaApplication **/*.class
```

- Run the JAR
```
java -jar myapp.jar
```

- You can list the contents of a JAR file using:
```
jar tf myapp.jar
```

- Viewing the Manifest File
```
jar xf myapp.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
```

