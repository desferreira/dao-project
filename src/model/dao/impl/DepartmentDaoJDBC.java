package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("insert into department " +
                    "(Id, Name) " +
                    "values " +
                    "(?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getName());

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
    public void update(Department obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("update department " +
                    "set Name = ?" +
                    "where Id = ?", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());

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
            ps = conn.prepareStatement("delete from department " +
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
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(
                    "SELECT * FROM department " +
                    "WHERE Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()){
                Department dp = this.instantiateDepartment(rs);
                return dp;
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

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dp = new Department();
        dp.setId(rs.getInt("Id"));
        dp.setName(rs.getString("Name"));
        return dp;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Department> departments = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select * from department " +
                    "order by Id"
            );
            rs = ps.executeQuery();

            while (rs.next()){
                Department dp = this.instantiateDepartment(rs);
                departments.add(dp);
            }
        }catch (SQLException e ){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
        return departments;
    }
}
