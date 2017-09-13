package org.dx4.external.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ImageMapper implements RowMapper<byte[]> {

    public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
          return rs.getBytes(1);
    }
}
