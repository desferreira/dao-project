package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import org.postgresql.core.SqlCommand;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("insert into seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "values " +
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            }else {
                throw new DbException("Erro! Nenhuma linha alterada");
            }

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
        }

    }

    @Override
    public void update(Seller obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("update seller " +
                    "set Name = ?, Email = ?, BirthDate= ?, BaseSalary = ?, DepartmentId = ? " +
                    "where Id = ?", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
            ps.setInt(6, obj.getId());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("delete from seller " +
                    "where Id = ?"
            );

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Foram deletadas "+rowsAffected+" linhas");
            }else{
                throw new DbException("Erro inesperado. Nenhuma linha afetada");
            }

        }catch (SQLException e ){
            throw new DbException(e.getMessage());
        }
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
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select seller.*, department.Name as depName " +
                    "from seller " +
                    "inner join department on seller.DepartmentId = department.Id "+
                    "order by Id"
                    );
            rs = ps.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department dp = map.get(rs.getInt("DepartmentId"));

                if (dp == null){
                    dp = this.instantiateDepartment(rs);
                }
                Seller sl = this.instantiateSeller(rs, dp);
                sellers.add(sl);
            }
        }catch (SQLException e ){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
        return sellers;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Seller> sellersFromDepartment = new ArrayList<>();
        try {
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.DepartmentId = ?");
            ps.setInt(1, department.getId());
            rs = ps.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department dp = map.get(rs.getInt("DepartmentId"));

                if (dp == null){
                    dp = this.instantiateDepartment(rs);
                }

                Seller obj = this.instantiateSeller(rs, dp);
                sellersFromDepartment.add(obj);
            }
        }catch (Exception e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
        return sellersFromDepartment;
    }
}
