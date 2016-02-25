package com.codechallenge.UserSessionAnalysis;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Index {
    static String outputFolder = ".";
    static List<BufferedWriter> writers = new ArrayList<BufferedWriter>();

    public static String getDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public static String getFileName (int hash) {
        return String.format("%s/%d.txt", outputFolder, hash);
    }

    public static void createWriters() {
        try {
            for (int i = 0; i < 100; i++) {
                File file = new File(getFileName(i));
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                writers.add(bw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeWriters() {
        try {
            for (int i = 0; i < 100; i++) {
                writers.get(i).close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertPath(HashMap<String, Set<String>> users, String user, String path, int n) {
        if (users.get(user) == null) {
            Set<String> paths = new HashSet<String>();
            paths.add(path);
            users.put(user, paths);
        } else if (users.get(user).size() > n) {
            users.get(user).clear();
        } else if (users.get(user).size() != 0 && !users.get(user).contains(path)) {
            users.get(user).add(path);
        }
    }

    public static void writeToOutput(String outputFile, int n) {
        try {
            File file = new File(String.format("%s/%s", outputFolder, outputFile));
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter output = new BufferedWriter(fw);

            for (int i = 0; i < 100; i++) {
                InputStream inputStream = new FileInputStream(getFileName(i));
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                HashMap<String, Set<String>> users = new HashMap<String, Set<String>>();
                String line;
                while((line = br.readLine()) != null) {
                    String[] lineCont = line.split(",");
                    insertPath(users, lineCont[1], lineCont[2], n);
                }
                for (Map.Entry<String, Set<String>> entry : users.entrySet()){
                    if (entry.getValue().size() == n) {
                        output.write(String.format("%s\n",entry.getKey()));
                    }
                }
                br.close();
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the users with n unique paths
     * @param args
     */
    public static void analyzeFile(String[] args) {
        int n = Integer.parseInt(args[0]);
        String file = args[1];
        String outputFile = args[2];
        outputFolder = getDate();
        createWriters();
        System.out.println(String.format("Output folder: %s",outputFolder));

        // Read in log file
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            // Read in and process file
            String line;
            while((line = br.readLine()) != null) {
                String[] lineCont = line.split(",");
                int mapIndex = lineCont[1].hashCode() % 100;
                writers.get(mapIndex).write(String.format("%s\n", line));
            }
            closeWriters();
            // Print the users with n unique paths
            writeToOutput(outputFile, n);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        analyzeFile(args);
    }
}
