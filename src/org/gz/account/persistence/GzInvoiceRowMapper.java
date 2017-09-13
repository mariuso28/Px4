package org.gz.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import org.gz.account.GzInvoice;
import org.gz.account.GzXaction;

public class GzInvoiceRowMapper implements RowMapper<GzInvoice> {

	@Override
	public GzInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		GzInvoice invoice = new GzInvoice();
		GzXactionRowMapper.setBaseValues(rs, invoice, GzXaction.XTYPEINVOICE);
		setValues(rs,invoice);
		return invoice;
	}

	public static void setValues(ResultSet rs,GzInvoice invoice) throws SQLException {
		invoice.setParentId(rs.getLong("parentId"));
		invoice.setPaymentId(rs.getLong("paymentid"));
		Timestamp ts = rs.getTimestamp("duedate");
		invoice.setDueDate(new Date(ts.getTime()));
		invoice.setFlight(rs.getDouble("flight"));
		invoice.setRetain(rs.getDouble("retain"));
		String status = rs.getString("status");
		invoice.setStatus(status.charAt(0));
	}
	
}
