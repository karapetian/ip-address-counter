# Unique IP Address Counter

This Java program counts the number of unique IPv4 addresses in a large text file. The file must contain one IPv4
address per line, and the program is designed to handle files of huge size while using minimal memory.

### Running the Program

1. Generate the jar file within target directory.

```
./mvnw clean package
```

2. Run the program

```
java -jar target/ip-address-counter-1.0.0-SNAPSHOT.jar "./path/to/your/file/ips.txt"
```

or if you want to set custom chunk size

```
java -jar target/ip-address-counter-1.0.0-SNAPSHOT.jar "./path/to/your/file/ips.txt" "10000"
```

### Output

The program will output the number of unique IPs and the execution time, for example:

```
The number of unique IPs: 1000
Execution time: 7472 ms
```

### Implementation Details

#### File Processing

To efficiently handle large input files, the file is divided into chunks. By default, each chunk contains 5.000.000
bytes.
This value can be customized by passing it as the second argument to the program.  
The `java.nio.channels.FileChannel` class is used to read from specific positions in the file, for efficient processing
of large files without loading the entire file into memory.

#### Data Structure: BitSet

To deal with large amount of data, BitSet data structure is used.
A BitSet requires only 512MB of memory (2^23 bits = 512 MB) to track all possible IPv4 addresses.  
But to avoid concurrency issues and the overloading of one BitSet, 256 BitSet instances are kept in a ConcurrentHashMap.
Each processed IP is put in this map, in a bucket matching to its first octet (ex. 220.230.144.205 goes to bucket 220).

The total number of unique IP addresses is calculated as the sum of the cardinalities of all BitSet instances in the
ConcurrentHashMap.

#### Edge Cases

- If the input file is empty, the program processes it without issues and returns 0 unique IP addresses.
- A file with blank lines is processed with no issue. The blank lines are just skipped.
- Invalid IPv4 ips and other invalid lines are logged and skipped.
- If the input file is absent, the program throws an exception.

#### Tests

Unit tests are included to check the number of IPs and some edge cases.  
Samples of input files are provided in the src/test/resources directory.

### CI Pipeline

Basic pipeline as added in GitHub Actions that builds the project and runs the tests.

### Requirements

- Java 21
- Input File: A text file containing one IPv4 address per line.
