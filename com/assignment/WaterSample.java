package com.assignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class WaterSample {
    int id;
    String site;
    double chloroform;
    double bromoform;
    double bromodichloromethane;
    double dibromichloromethane;

    static final String SQL_QUERY_ID = "SELECT * FROM water_samples WHERE id = %d";
    static final String SQL_QUERY_FACTOR = "SELECT * FROM factor_weights WHERE id = %d";
    static final String SQL_QUERY_ALL_FACTORS = "SELECT * FROM factor_weights ORDER BY id";
    static final double HASH_MULTIPLIER = 1000000000.0;

    public static WaterSample find(int sample_id) throws SQLException, Exception {
        Connection conn = null;
        Statement st = null;
        try
        {
            conn = WSDataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format(SQL_QUERY_ID, sample_id));
            if (rs.next())
            {
                WaterSample waterSample = new WaterSample();
                waterSample.id = rs.getInt("id");
                waterSample.site = rs.getString("site");
                waterSample.chloroform = rs.getDouble("chloroform");
                waterSample.bromoform = rs.getDouble("bromoform");
                waterSample.bromodichloromethane = rs.getDouble("bromodichloromethane");
                waterSample.dibromichloromethane = rs.getDouble("dibromichloromethane");
                return waterSample;
            }
            else
            {
                throw new Exception("sample id don't exist");
                // return null;
            }
        }
        finally
        {
            if (st != null)
            {
                try {
                    st.close();
                }
                catch (SQLException ex) {

                }
            }
            if (conn != null) {
                WSDataSource.releaseConnection(conn);
            }
        }
    }

    private double calculateFactor(ResultSet rs) throws SQLException {
        double chloroform_weight = rs.getDouble("chloroform_weight");
        double bromoform_weight = rs.getDouble("bromoform_weight");
        double bromodichloromethane_weight = rs.getDouble("bromodichloromethane_weight");
        double dibromichloromethane_weight = rs.getDouble("dibromichloromethane_weight");
        return chloroform * chloroform_weight + bromoform * bromoform_weight
            + bromodichloromethane * bromodichloromethane_weight + dibromichloromethane * dibromichloromethane_weight;
    }

    public double factor(int factor_weight_id) throws SQLException, Exception {
        Connection conn = null;
        Statement st = null;
        try
        {
            conn = WSDataSource.getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format(SQL_QUERY_FACTOR, factor_weight_id));
            if (rs.next())
            {
                return calculateFactor(rs);
            }
            else
            {
                throw new Exception("factor_weight id don't exist");
            }
        }
        finally
        {
            if (st != null)
            {
                try {
                    st.close();
                }
                catch (SQLException ex) {

                }
            }
            if (conn != null) {
                WSDataSource.releaseConnection(conn);
            }
        }
    }

    // I multiply the float point values with 10^9, then round it to an integer to ensure that we get the same hash value on different machines and different platforms.
    public int to_hash() {
        int h = id;
        h = hash_two_hash(h, site.hashCode());
        h = hash_two_hash(h, (int)Math.round(chloroform * HASH_MULTIPLIER));
        h = hash_two_hash(h, (int)Math.round(bromoform * HASH_MULTIPLIER));
        h = hash_two_hash(h, (int)Math.round(bromodichloromethane * HASH_MULTIPLIER));
        h = hash_two_hash(h, (int)Math.round(dibromichloromethane * HASH_MULTIPLIER));
        return h;
    }

    static private int hash_two_hash(int hash1, int hash2) {
        // System.out.println("hash_two_hash(), " + hash1 + ", " + hash2);
        return hash1 * 31 + hash2;  // there are overflows here, but it is ok as long as it is integer-wraparound
    }

    public int to_hash(boolean include_factors) throws SQLException, Exception {
        int h = to_hash();
        if (include_factors == true)
        {
            Connection conn = null;
            Statement st = null;
            try
            {
                    conn = WSDataSource.getConnection();
                    st = conn.createStatement();
                    ResultSet rs = st.executeQuery(SQL_QUERY_ALL_FACTORS);
                    // if there is no record in the factor_weights table the value of h will remain unchanged, which means to_hash(true) and to_hash(false) returns the same value
                    // if we want a different value we can simply hash an extra 1 here
                    while (rs.next())
                    {
                        h = hash_two_hash(h, (int)Math.round(calculateFactor(rs) * HASH_MULTIPLIER));
                    }
            }
            finally
            {
                if (st != null)
                {
                    try {
                        st.close();
                    }
                    catch (SQLException ex) {

                    }
                }
                if (conn != null) {
                    WSDataSource.releaseConnection(conn);
                }
            }
        }
        return h;
    }

    public int getID() {
        return id;
    }

    public String getSite() {
        return site;
    }

    public double getChloroform() {
        return chloroform;
    }

    public double getBromoform() {
        return bromoform;
    }

    public double getBromodichloromethane() {
        return bromodichloromethane;
    }

    public double getDibromichloromethane() {
        return dibromichloromethane;
    }
}
