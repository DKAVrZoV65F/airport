package com.airportapp.dao.jdbc;

import com.airportapp.dao.GenericDao;
import com.airportapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Базовый JDBC-DAO. Для каждого запроса вы должны реализовать:
 *   - getTableName()
 *   - mapRow(ResultSet)
 *   - setInsertParams
 *   - setUpdateParams
 */
public abstract class JdbcGenericDao<E> implements GenericDao<E, Integer> {

    protected abstract String getTableName();
    protected abstract E mapRow(ResultSet rs) throws SQLException;
    protected abstract void setInsertParams(PreparedStatement ps, E e) throws SQLException;
    protected abstract void setUpdateParams(PreparedStatement ps, E e) throws SQLException;

    public E findById(Integer id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id=?";
        try (var c=DbUtil.getConnection();
             var ps=c.prepareStatement(sql)) {
            ps.setInt(1,id);
            var rs=ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch(Exception ex){throw new RuntimeException(ex);}
        return null;
    }

    public List<E> findAll() {
        List<E> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (var c=DbUtil.getConnection();
             var ps=c.prepareStatement(sql);
             var rs=ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch(Exception ex){throw new RuntimeException(ex);}
        return list;
    }

    public void save(E e) {
        String sql = insertSql();
        try (var c=DbUtil.getConnection();
             var ps=c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParams(ps,e);
            ps.executeUpdate();
            var keys = ps.getGeneratedKeys();
            if (keys.next()) setGeneratedId(e, keys.getInt(1));
        } catch(Exception ex){throw new RuntimeException(ex);}
    }

    public void update(E e) {
        String sql = updateSql();
        try (var c=DbUtil.getConnection();
             var ps=c.prepareStatement(sql)) {
            setUpdateParams(ps,e);
            ps.executeUpdate();
        } catch(Exception ex){throw new RuntimeException(ex);}
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM "+getTableName()+" WHERE id=?";
        try (var c=DbUtil.getConnection();
             var ps=c.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch(Exception ex){throw new RuntimeException(ex);}
    }

    private String insertSql() {
        // INSERT INTO table (col1,col2,…) VALUES (?,?,…)
        return getInsertSql();
    }
    private String updateSql() {
        // UPDATE table SET col1=?,… WHERE id=?
        return getUpdateSql();
    }

    // подклассы реализуют эти методы:
    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract void setGeneratedId(E e, int id);
}