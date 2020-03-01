package app;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class App {
    public static void main(String[] args){
        SellerDao sellerDao = DaoFactory.createSellerDao();
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

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

        System.out.println("==== TEST 4: Insert seller");
        sellerDao.insert(new Seller("Insert", "Insert@hotmail.com",  new Date(), 2500.0, new Department(1, "Computers")));

        System.out.println("==== TEST 5: Update seller");
        Seller old = sellerDao.findById(6);
        old.setName("Wallesca");
        sellerDao.update(old);

        System.out.println("==== TEST 6: Delete seller");
        sellerDao.deleteById(12);

        System.out.println("==== TEST 7: List departments");
        List<Department> deps =  departmentDao.findAll();
        for (Department d : deps){
            System.out.println(d);
        }

        System.out.println("==== TEST 8: Find department by ID");
        Department dp = departmentDao.findById(3);
        System.out.println(dp);

        System.out.println("==== TEST 9: Insert department");
        departmentDao.insert(new Department(5, "D1"));

        System.out.println("==== TEST 10: Delete department");
        departmentDao.deleteById(5);


    }
}
