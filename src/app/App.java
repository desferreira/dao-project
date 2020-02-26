package app;

import db.DB;
import model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Department dp = new Department(1, "Diego");
        System.out.println(dp);

    }
}
