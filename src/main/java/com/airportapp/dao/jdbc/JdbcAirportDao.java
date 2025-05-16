package com.airportapp.dao.jdbc;

import com.airportapp.dao.AirportDao;
import com.airportapp.model.Airport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAirportDao extends JdbcGenericDao<Airport> implements AirportDao {

    @Override protected String getTableName() { return "airport"; }

    @Override protected Airport mapRow(ResultSet rs) throws SQLException {
        Airport a = new Airport();
        a.setId(rs.getInt("id"));
        a.setName(rs.getString("name"));
        a.setCity(rs.getString("city"));
        a.setCountry(rs.getString("country"));
        a.setIataCode(rs.getString("iata_code"));
        return a;
    }

    @Override protected String getInsertSql() {
        return "INSERT INTO airport(name,city,country,iata_code) VALUES(?,?,?,?)";
    }
    @Override protected void setInsertParams(PreparedStatement ps, Airport a) throws SQLException {
        ps.setString(1,a.getName());
        ps.setString(2,a.getCity());
        ps.setString(3,a.getCountry());
        ps.setString(4,a.getIataCode());
    }
    @Override protected String getUpdateSql() {
        return "UPDATE airport SET name=?,city=?,country=?,iata_code=? WHERE id=?";
    }
    @Override protected void setUpdateParams(PreparedStatement ps, Airport a) throws SQLException {
        setInsertParams(ps,a);
        ps.setInt(5,a.getId());
    }
    @Override protected void setGeneratedId(Airport a, int id) {
        a.setId(id);
    }
}