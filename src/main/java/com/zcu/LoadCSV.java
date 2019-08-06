package com.zcu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import com.opencsv.*;
import org.apache.commons.lang3.StringUtils;



public class LoadCSV {
    private Connection connection;

    public LoadCSV(Connection connection){
        this.connection = connection;
    }

    public void insertData(String csvFile, String insertScript) throws Exception {
        String[] nextLine;
        Connection tmpCon = null;
        PreparedStatement ps = null;
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new FileReader(csvFile), ';');
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occured while executing file. "
                    + e.getMessage());
        }
        //read header, thrown away
        nextLine = csvReader.readNext();
        connection.setAutoCommit(false);

        while((nextLine = csvReader.readNext()) != null) {
            ps = connection.prepareStatement(insertScript);
            if (nextLine != null) {
                Integer index = 1;
                for (String string : nextLine) {
                    ps.setString(index++, string);
                }
            }
            ps.execute();
        }
        connection.commit();
    }
}
