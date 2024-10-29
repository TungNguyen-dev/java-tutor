<h3>Compile</h3>
```
javac -d target --module-source-path src $(find src -name "*.java")
```

<h3>Run</h3>
```
java -p target -m module.service/com.tungnn.java.tutor.modules.HelloWorld
```