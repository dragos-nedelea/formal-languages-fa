# How to Run Lab 6

To compile and run the Parser/AST project, execute the following commands from the project root (`6_ParserASTBuild` directory).

### 1. Compile the Project
This command creates a `bin` directory and compiles all source files while maintaining the package structure.
```bash
javac -d bin src/*.java
```

### 2. Run the Main Class
This command runs the application using the compiled classes in the `bin` directory.
```bash
java -cp bin lfa.parser.Main
```

### 3. (Optional) Cleanup
To remove the compiled files:
```bash
rm -rf bin
```
