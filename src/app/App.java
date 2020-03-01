package app;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


import java.text.ParseException;
import java.util.List;


public class App {
    public static void main(String[] args){
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("==== TEST 1: Find seller by ID");
        Seller sl = sellerDao.findById(3);
        System.out.println(sl);

        System.out.println("==== TEST 2: Find seller by department");
        List<Seller> sellersByDepartment = sellerDao.findByDepartment(new Department(1, "Computers"));
        for (Seller s : sellersByDepartment){
            System.out.println(s);
        }

        System.out.println("==== TEST 3: Find all seller");
        List<Seller> sellers = sellerDao.findAll();
        for (Seller s : sellers){
            System.out.println(s);
        }

    }
}
