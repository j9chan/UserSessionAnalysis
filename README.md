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
#### Units tests (Not enough time to create these)
* Would run using:
```
mvn test
```

#### Functional Tests
* Output folders are created using the date the program was run
* Tests are located at src/test/files along with output files

## Approach
### Create temp files for users containing all paths (non-unique)
* Chunks original input file into 100 smaller files based on user.hashCode() % 100 (creates an even distribution for each file)

### Find out the number of unique paths for all users
* For each of the 100 files read into a map
* Add to the map if the path was not seen before
* Set the corresponding set for a user to 0 if the number of paths for a user exceeds n

### Why This Approach was Chosen
* Linear time solution
* Less IO overhead since buffers can be kept open

### Resource Requirements
* Memory requirements: O(n) (n is the number entered on the command line)
* Space requirement: Hold 100 extra temp files so O(N) more data

### Performance
* N = number of lines in original file
* Creating temp files : O(N)
* Reading unique paths for each user: O(N)
* Final Performance: O(N)

### Assumptions on Dataset Properties
* n isn't so large that holding all the unique paths for one user will overflow memory

### Improvements
* Generalize for really large files since the current 100 files may overflow memory if the original file was extremely large
    * Divide original file into manageable chunks
    * Will end up with lots of files stored on disk

### Alternative Approaches
* Each user has a file with all the paths
    * Too much IO overhead. Extremely slow.
