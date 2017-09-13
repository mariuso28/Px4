package org.gz.account;

public class GzPayment extends GzInvoice {
	
	private static final long serialVersionUID = -807739017265637123L;
	private long invoiceId;
	
	public GzPayment()
	{
	}
	/*
	public GzPayment(GzInvoice invoice,Date timestamp,double amount)
	{
		this(invoice.getPayer(),invoice.getPayee(),amount,timestamp,invoice.getId());
	}
		public GzPayment(String payer,String payee,double amount,Date timestamp,long invoiceId)
	{
		super(payer,payee,amount,0,0,timestamp,null,null);
		setType(GzXaction.XTYPEPAYMENT);
		setInvoiceId(invoiceId);
	}
*/
	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

}
