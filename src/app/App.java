package app;

import db.DB;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    public static void main(String[] args) throws ParseException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Department dp = new Department(1, "Diego");
        System.out.println(dp);
        Seller sl = new Seller("Diego", "diegoferreira4@hotmail.com", new Date(), 3500.0, dp);
        System.out.println(sl);

    }
}
