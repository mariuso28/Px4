package org.gz.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import org.gz.account.GzBaseTransaction;
import org.gz.account.GzDeposit;
import org.gz.account.GzInvoice;
import org.gz.account.GzPayment;
import org.gz.account.GzWithdrawl;
import org.gz.account.GzXaction;

public class GzXactionRowMapper implements RowMapper<GzBaseTransaction>
{
		@Override
		public GzBaseTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			char type = rs.getString("type").charAt(0);
			
			switch (type)
			{
				case GzXaction.XTYPEWITHDRAWL : return createWithdrawl(rs);
				case GzXaction.XTYPEDEPOSIT : return createDeposit(rs);
				case GzXaction.XTYPEPAYMENT : return createPayment(rs);
				case GzXaction.XTYPEINVOICE : return createInvoice(rs);
			}
			
			return null;
		}	
		
		private GzBaseTransaction createInvoice(ResultSet rs) throws SQLException {
			GzInvoice invoice = new GzInvoice();
			setBaseValues(rs,invoice,GzXaction.XTYPEINVOICE);
			GzInvoiceRowMapper.setValues(rs, invoice);
			return invoice;
		}

		private GzBaseTransaction createPayment(ResultSet rs) throws SQLException {
			GzPayment p = new GzPayment();
			setBaseValues(rs,p,GzXaction.XTYPEPAYMENT);
			p.setInvoiceId(rs.getLong("invoiceid"));
			return p;
		}

		private GzWithdrawl createWithdrawl(ResultSet rs) throws SQLException {
			
			GzWithdrawl w = new GzWithdrawl();
			setBaseValues(rs,w,GzXaction.XTYPEWITHDRAWL);
			return w;
		}
		
		private GzDeposit createDeposit(ResultSet rs) throws SQLException {
			
			GzDeposit d = new GzDeposit();
			setBaseValues(rs,d,GzXaction.XTYPEDEPOSIT);
			return d;
		}

		public static void setBaseValues(ResultSet rs,GzBaseTransaction tx,char type) throws SQLException
		{
			tx.setId(rs.getLong("id"));
			tx.setAmount(rs.getDouble("amount"));
			tx.setPayee(rs.getString("payee"));
			tx.setPayer(rs.getString("payer"));
			Timestamp ts = rs.getTimestamp("timestamp");
			tx.setTimestamp(new Date(ts.getTime()));
			tx.setType(type);
		}
		
}
