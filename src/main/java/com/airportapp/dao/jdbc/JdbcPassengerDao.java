package com.airportapp.dao.jdbc;

import com.airportapp.dao.PassengerDao;
import com.airportapp.model.Passenger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcPassengerDao extends JdbcGenericDao<Passenger> implements PassengerDao {
    @Override protected String tableName() { return "passenger"; }
    @Override protected Passenger mapRow(ResultSet rs) throws SQLException {
        var p = new Passenger();
        p.setId(rs.getInt("id"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setPassportNumber(rs.getString("passport_number"));
        p.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return p;
    }
    @Override protected String insertSql() {
        return "INSERT INTO passenger(first_name,last_name,passport_number,date_of_birth) VALUES(?,?,?,?)";
    }
    @Override protected void setInsert(PreparedStatement ps, Passenger p) throws SQLException {
        ps.setString(1,p.getFirstName());
        ps.setString(2,p.getLastName());
        ps.setString(3,p.getPassportNumber());
        ps.setDate(4,java.sql.Date.valueOf(p.getDateOfBirth()));
    }
    @Override protected String updateSql() {
        return "UPDATE passenger SET first_name=?,last_name=?,passport_number=?,date_of_birth=? WHERE id=?";
    }
    @Override protected void setUpdate(PreparedStatement ps, Passenger p) throws SQLException {
        setInsert(ps,p);
        ps.setInt(5,p.getId());
    }
    @Override protected void setId(Passenger p, int id) { p.setId(id); }
}