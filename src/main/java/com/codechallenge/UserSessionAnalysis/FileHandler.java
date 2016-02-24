package com.codechallenge.UserSessionAnalysis;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;

public class FileHandler {
    private File usersFile = null;
    private String outputFolder = null;

    public FileHandler(File usersFile, String outputFolderName) {
        this.usersFile = usersFile;
        this.outputFolder = outputFolderName;
    }

    /**
     * For each line check working directory to see if the user's file exists
     * If not, create a file and insert the path
     * If yes, write the path to the document
     * @param user
     * @param path
     */
    public void processUserInfo(String user, String path) {
        File f = new File(String.format("%s/%s.txt", outputFolder, user.hashCode()));
        try {
            if (!f.exists()) {
                FileUtils.writeStringToFile(usersFile, String.format("%s\n", user), true);
            }
            FileUtils.writeStringToFile(f, String.format("%s\n", path), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search the n Paths document for paths with n users
     * Prints the users that have n unique paths
     * @param n
     */
    public void printUsersWithNPaths(int n, String outputFileName) {
        try {
            File outputFile = new File(String.format("%s/%s", outputFolder, outputFileName));
            InputStream inputStream = new FileInputStream(usersFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String user;
            while ((user = br.readLine()) != null) {
                printUsersWithNPaths(n, outputFile, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printUsersWithNPaths(int n, File outputFile, String user) {
        try {
            File userFile = new File(String.format("%s/%s.txt", outputFolder, user.hashCode()));
            InputStream inputStream = new FileInputStream(userFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String path;
            int numPaths = 0;
            HashMap<String, Integer> uniquePaths = new HashMap<String, Integer>();

            while ((path = br.readLine()) != null && numPaths <= n) {
                if (!path.isEmpty() && uniquePaths.get(path) == null) {
                    uniquePaths.put(path, 0);
                    ++numPaths;
                }
            }
            if (numPaths == n) {
                FileUtils.writeStringToFile(outputFile, String.format("%s\n", user), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
