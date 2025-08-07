# JAVA-TUTOR


### Compile source-code

```shell
javac --module-source-path src/main/java -d target/modules --module m.A
javac --module-source-path src/main/java -d target/modules --module m.B
javac --module-source-path src/main/java -d target/modules --module m.C
javac --module-source-path src/main/java -d target/modules --module m.D
```

### Execute java program
```shell
java --module-path target/modules --module m.A/client.Test
```