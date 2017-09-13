package org.gz.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.gz.account.GzPayment;
import org.gz.account.GzXaction;

public class GzPaymentRowMapper implements RowMapper<GzPayment> {

	@Override
	public GzPayment mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		GzPayment payment = new GzPayment();
		GzXactionRowMapper.setBaseValues(rs, payment, GzXaction.XTYPEPAYMENT);
		setValues(rs,payment);
		return payment;
	}

	public static void setValues(ResultSet rs,GzPayment payment) throws SQLException {
		payment.setInvoiceId(rs.getLong("invoiceid"));
	}
}
