package com.airportapp.dao.jdbc;

import com.airportapp.dao.AircraftDao;
import com.airportapp.model.Aircraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAircraftDao extends JdbcGenericDao<Aircraft> implements AircraftDao {
    @Override protected String tableName() { return "aircraft"; }
    @Override protected Aircraft mapRow(ResultSet rs) throws SQLException {
        var a = new Aircraft();
        a.setId(rs.getInt("id"));
        a.setModel(rs.getString("model"));
        a.setManufacturer(rs.getString("manufacturer"));
        a.setCapacity(rs.getInt("capacity"));
        return a;
    }
    @Override protected String insertSql() {
        return "INSERT INTO aircraft(model,manufacturer,capacity) VALUES(?,?,?)";
    }
    @Override protected void setInsert(PreparedStatement ps, Aircraft a) throws SQLException {
        ps.setString(1,a.getModel());
        ps.setString(2,a.getManufacturer());
        ps.setInt(3,a.getCapacity());
    }
    @Override protected String updateSql() {
        return "UPDATE aircraft SET model=?,manufacturer=?,capacity=? WHERE id=?";
    }
    @Override protected void setUpdate(PreparedStatement ps, Aircraft a) throws SQLException {
        setInsert(ps,a);
        ps.setInt(4,a.getId());
    }
    @Override protected void setId(Aircraft a, int id) { a.setId(id); }
}