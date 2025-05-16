package com.airportapp.dao.jdbc;

import com.airportapp.dao.TicketDao;
import com.airportapp.model.Ticket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class JdbcTicketDao extends JdbcGenericDao<Ticket> implements TicketDao {
    @Override protected String tableName() { return "ticket"; }
    @Override protected Ticket mapRow(ResultSet rs) throws SQLException {
        var t = new Ticket();
        t.setId(rs.getInt("id"));
        t.setFlightId(rs.getInt("flight_id"));
        t.setPassengerId(rs.getInt("passenger_id"));
        t.setSeatNumber(rs.getString("seat_number"));
        t.setPrice(rs.getBigDecimal("price"));
        return t;
    }
    @Override protected String insertSql() {
        return "INSERT INTO ticket(flight_id,passenger_id,seat_number,price) VALUES(?,?,?,?)";
    }
    @Override protected void setInsert(PreparedStatement ps, Ticket t) throws SQLException {
        ps.setInt(1,t.getFlightId());
        ps.setInt(2,t.getPassengerId());
        ps.setString(3,t.getSeatNumber());
        ps.setBigDecimal(4,t.getPrice());
    }
    @Override protected String updateSql() {
        return "UPDATE ticket SET flight_id=?,passenger_id=?,seat_number=?,price=? WHERE id=?";
    }
    @Override protected void setUpdate(PreparedStatement ps, Ticket t) throws SQLException {
        setInsert(ps,t);
        ps.setInt(5,t.getId());
    }
    @Override protected void setId(Ticket t, int id) { t.setId(id); }
}