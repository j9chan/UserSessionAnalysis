package com.codechallenge.UserSessionAnalysis;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Index {

    public static String getDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }
    /**
     * Prints the users with n unique paths
     * @param args
     */
    public static void analyzeFile(String[] args) {
        int n = Integer.parseInt(args[0]);
        String file = args[1];
        String outputFile = args[2];

        String outputFolderName = getDate();
        System.out.println(String.format("Output folder: %s",outputFolderName));
        File userFile = new File(String.format("%s/%s", outputFolderName, "users.txt"));
        FileHandler fileHandler = new FileHandler(userFile, outputFolderName);

        // Read in log file
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            // Read in and process file
            String line;
            while((line = br.readLine()) != null) {
                String[] lineCont = line.split(",");
                fileHandler.processUserInfo(lineCont[1], lineCont[2]);
            }

            // Print the users with n unique paths
            fileHandler.printUsersWithNPaths(n, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        analyzeFile(args);
    }
}
