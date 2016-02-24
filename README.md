## Building and Running Program
### Building
* The jar is included in the package, so rebuilding the jar shouldn't be necessary
* Below code should work
```
mvn clean compile assembly:single
```
### Running
```
./users_unique_paths.sh number-of-paths logfile output-file-name
```

### Testing
#### Units tests (Not enough time)
* Would run using:
```
mvn test
```
#### Functional Tests
* Output folders are created using the date the program was run.
* Tests are located at src/test/files along with output files

## Approach
### Create temp files for users containing all paths (non-unique)
* Read in log file line by line
* For each line check working directory to see if the user's file exists
* If not, create a file and insert the path
* If yes, write the path to the document

### Find out the number of unique paths for all users
* For each user file, read the paths into a map
* If the path exists, skip
* If not increment counter i
* If i > n then move on to the next user file (n is number entered on the command line)
* If i < n then go to next user
* If i = n then print the user

### Why This Approach was Chosen
* Linear time solution (not counting opening and closing files on disk)
* Very little stored in memory. Maybe only file handles if we want to optimize.

### Resource Requirements
* Memory requirements: O(n) (n is the number entered on the command line)
* Space requirement: Hold m files on the system (m = number of users)

### Performance
* N = number of lines in original file, K = time to open/close files on disk
* Creating temp files
    * Reading the log file: O(N)
    * Creating/opening/closing temp files on system: O(K)
    * Conclusion: O(N * K)
* Reading unique paths for each user
    * Open file for each user: O(K)
    * At most read all N lines again: O(N)
    * Conclusion: O(N * K)
* Final Performance: O(N * K)

### Assumptions on Dataset Properties
* There aren't billions of users or beyond what the file system can hold
* We don't run out of integers to hash to
* n isn't so large that it overflows memory. This cannot always be guaranteed.

### Improvements
* Opening and closing files for each line in the dataset is expensive
    * We want to hold the maximum allowed file handles in the program possible.
    * This can probably be achieved using a least recently used algorithm.
        * Removes the least recently accessed file handle and replaces it with the file that was just opened.
* Writing fewer files to the file system.
* More command line options for output folders, input multiple log files

### Alternative Approaches
* MapReduce
    * Works better across multiple nodes. Distributed system approach.
* Indexing
    * Implement something similar to Lucene by creating a reverse index of number of paths as the key and the users that have that number of paths.
    * Problem: 
* Use serialized HashMaps
    * Key = hashed user key, Value = Set of user paths
    * Split keys into 100 maps since hashes are evenly distributed
    * Max number of maps in memory possible. Once some memory limit is reached, serialize least recently used and write to memory.
    * Problem: If number of paths for a group of users is too large to fit in hash this won't work.
