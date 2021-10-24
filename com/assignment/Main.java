package com.assignment;

import java.sql.SQLException;

public class Main {
    // this is a mysql server running on a oracle compute instance
    static String host = "155.248.215.20";
    static int port = 3306;
    static String user = "user8";
    static String pwd = "7dcGc#$q5@/,>n\"B";

    public static void main(String[] args) {
        // connect to database
        try
        {
            System.out.println("Connecting to Database...");
            WSDataSource.initConnections(String.format("jdbc:mysql://%s:%d/factor_hw", host, port), user, pwd);
        }
        catch (SQLException ex)
        {
            System.out.println("Error on Connecting Database: " + ex.getErrorCode());
            return;
        }

        // call class functions
        try
        {
            System.out.println("Calling class functions...");
            // find
            WaterSample sample = WaterSample.find(2);
            System.out.format("WaterSample.find(2): %d, %s, %f, %f, %f, %f\n", sample.id, sample.site,
                sample.chloroform, sample.bromoform, sample.bromodichloromethane, sample.dibromichloromethane);
            // factor
            double factor3 = sample.factor(3);
            System.out.println("factor(3) = " + factor3);

            try {
                double factor6 = sample.factor(6);
                System.out.println("factor(6) = " + factor6);
            }
            catch (Exception ex)
            {
                System.out.println("factor(6) causes exception: " + ex.getMessage());
            }
            // to_hash
            int hash_no_factors = sample.to_hash();
            System.out.println("to_hash() = " + hash_no_factors);

            int hash_factors = sample.to_hash(true);
            System.out.println("to_hash(true) = " + hash_factors);
        }
        catch (Exception ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}