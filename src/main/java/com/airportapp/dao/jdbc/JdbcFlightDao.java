package com.airportapp.dao.jdbc;

import com.airportapp.dao.FlightDao;
import com.airportapp.model.Flight;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JdbcFlightDao extends JdbcGenericDao<Flight> implements FlightDao {
    @Override protected String tableName() { return "flight"; }
    @Override protected Flight mapRow(ResultSet rs) throws SQLException {
        var f = new Flight();
        f.setId(rs.getInt("id"));
        f.setFlightNumber(rs.getString("flight_number"));
        f.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
        f.setArrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime());
        f.setAircraftId(rs.getInt("aircraft_id"));
        f.setOriginAirportId(rs.getInt("origin_airport_id"));
        f.setDestAirportId(rs.getInt("dest_airport_id"));
        return f;
    }
    @Override protected String insertSql() {
        return "INSERT INTO flight(flight_number,aircraft_id,origin_airport_id,dest_airport_id,departure_time,arrival_time) "
                + "VALUES(?,?,?,?,?,?)";
    }
    @Override protected void setInsert(PreparedStatement ps, Flight f) throws SQLException {
        ps.setString(1,f.getFlightNumber());
        ps.setInt(2,f.getAircraftId());
        ps.setInt(3,f.getOriginAirportId());
        ps.setInt(4,f.getDestAirportId());
        ps.setTimestamp(5,Timestamp.valueOf(f.getDepartureTime()));
        ps.setTimestamp(6,Timestamp.valueOf(f.getArrivalTime()));
    }
    @Override protected String updateSql() {
        return "UPDATE flight SET flight_number=?,aircraft_id=?,origin_airport_id=?,dest_airport_id=?,"
                + "departure_time=?,arrival_time=? WHERE id=?";
    }
    @Override protected void setUpdate(PreparedStatement ps, Flight f) throws SQLException {
        setInsert(ps,f);
        ps.setInt(7,f.getId());
    }
    @Override protected void setId(Flight f, int id) { f.setId(id); }
}