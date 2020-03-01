package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()){
                Department dp = this.instantiateDepartment(rs);
                Seller obj = this.instantiateSeller(rs, dp);
                return obj;
            }
            return null;

        }catch (Exception e ) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dp) throws SQLException {
        Seller sl = new Seller();
        sl.setId(rs.getInt("Id"));
        sl.setBaseSalary(rs.getDouble("BaseSalary"));
        sl.setBirthDate(rs.getDate("BirthDate"));
        sl.setEmail(rs.getString("Email"));
        sl.setName(rs.getString("Name"));
        sl.setDepartment(dp);
        return sl;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dp = new Department();
        dp.setId(rs.getInt("DepartmentId"));
        dp.setName(rs.getString("DepName"));
        return dp;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
