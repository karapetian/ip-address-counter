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
As it is not efficient to read the input big file at once, it is divided into chunks. By default, each chunk contains 
5_000_000 bytes. To change this default value, pass it as the 2nd argument.  
For this purpose `java.nio.channels.FileChannel` is used, which lets you read from a file's specific position.

### Requirements

Java 21  
Input File: A text file containing one IPv4 address per line. The program only supports IPv4 addresses.

### Example Input File

Samples of input files are provided in the src/test/resources directory.
