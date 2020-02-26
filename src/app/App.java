package app;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


import java.text.ParseException;


public class App {
    public static void main(String[] args){

        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller sl = sellerDao.findById(3);
        System.out.println(sl);

    }
}
