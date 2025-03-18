# Unique IP Address Counter

This Java program counts the number of unique IPv4 addresses in a large text file. The file must contain one IPv4 
address per line, and the program is designed to handle files of unlimited size (up to hundreds of gigabytes) while 
using minimal memory.

### Running the Program

1. Generate the jar file within target directory.

```
./mvnw clean package
```

2. Run the program

```
java -jar .target/ip-address-counter-1.0.0-SNAPSHOT.jar "./path/to/your/file/ips.txt"
```

### Output

The program will output the number of unique IPs, for example:

```
The unique number of IPs: 1000
```

### Implementation Details

### Requirements

Java 21

Input File: A text file containing one IPv4 address per line. The program only supports IPv4 addresses.

### Example Input File

An example input file (XXX) is provided in the src/test/resources directory.
