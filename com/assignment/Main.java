package com.assignment;

import java.sql.SQLException;

public class Main {
    // this is a mysql server running on a oracle compute instance
    static String host = "155.248.215.20";
    static int port = 3306;
    static String user = "user8";
    static String pwd = "7dcGc#$q5@/,>n\"B";

    public static void main(String[] args) {
        try
        {
            WSDataSource.initConnections(String.format("jdbc:mysql://%s:%d/factor_hw", host, port), user, pwd);
        }
        catch (SQLException ex)
        {
            System.out.println("Error on Connecting Database: " + ex.getErrorCode());
            return;
        }

        try
        {
            WaterSample sample = WaterSample.find(2);

            System.out.format("find WaterSample: %d, %s, %f, %f, %f, %f\n", sample.id, sample.site,
            sample.chloroform, sample.bromoform, sample.bromodichloromethane, sample.dibromichloromethane);
            System.out.println("factor(3) = " + sample.factor(3));
            try {
                sample.factor(6);
            }
            catch (Exception ex)
            {
                System.out.println("factor(6) causes exception: " + ex.getMessage());
            }
            System.out.println("to_hash() = " + sample.to_hash());
            System.out.println("to_hash(true) = " + sample.to_hash(true));
        }
        catch (Exception ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}