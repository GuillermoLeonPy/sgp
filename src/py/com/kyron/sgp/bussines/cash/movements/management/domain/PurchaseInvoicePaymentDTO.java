package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class PurchaseInvoicePaymentDTO extends GenericDTO {
	
	private Long id_purchase_invoice;
	private Long id_currency;
	private String status;
	private BigDecimal amount;
	private BigDecimal purchase_invoice_balance;
	private Long payment_number;
	private Date registration_date;
	private Date payment_due_date;
	private Date cancellation_date;
	private Date annulment_date;
	private BigDecimal overdue_amount;
	private CurrencyDTO currencyDTO;	
	private CashReceiptDocumentDTO cashReceiptDocumentDTO;
	
	
	public PurchaseInvoicePaymentDTO() {
		// TODO Auto-generated constructor stub
	}

	public PurchaseInvoicePaymentDTO(Long id) {
		// TODO Auto-generated constructor stub
		this.setId(id);
	}
	
	public PurchaseInvoicePaymentDTO(Long id,Long id_purchase_invoice) {
		// TODO Auto-generated constructor stub
		this.id_purchase_invoice = id_purchase_invoice;
	}
	
	public PurchaseInvoicePaymentDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_purchase_invoice
	 */
	public Long getId_purchase_invoice() {
		return id_purchase_invoice;
	}

	/**
	 * @param id_purchase_invoice the id_purchase_invoice to set
	 */
	public void setId_purchase_invoice(Long id_purchase_invoice) {
		this.id_purchase_invoice = id_purchase_invoice;
	}

	/**
	 * @return the id_currency
	 */
	public Long getId_currency() {
		return id_currency;
	}

	/**
	 * @param id_currency the id_currency to set
	 */
	public void setId_currency(Long id_currency) {
		this.id_currency = id_currency;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the purchase_invoice_balance
	 */
	public BigDecimal getPurchase_invoice_balance() {
		return purchase_invoice_balance;
	}

	/**
	 * @param purchase_invoice_balance the purchase_invoice_balance to set
	 */
	public void setPurchase_invoice_balance(BigDecimal purchase_invoice_balance) {
		this.purchase_invoice_balance = purchase_invoice_balance;
	}

	/**
	 * @return the payment_number
	 */
	public Long getPayment_number() {
		return payment_number;
	}

	/**
	 * @param payment_number the payment_number to set
	 */
	public void setPayment_number(Long payment_number) {
		this.payment_number = payment_number;
	}

	/**
	 * @return the registration_date
	 */
	public Date getRegistration_date() {
		return registration_date;
	}

	/**
	 * @param registration_date the registration_date to set
	 */
	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	/**
	 * @return the payment_due_date
	 */
	public Date getPayment_due_date() {
		return payment_due_date;
	}

	/**
	 * @param payment_due_date the payment_due_date to set
	 */
	public void setPayment_due_date(Date payment_due_date) {
		this.payment_due_date = payment_due_date;
	}

	/**
	 * @return the cancellation_date
	 */
	public Date getCancellation_date() {
		return cancellation_date;
	}

	/**
	 * @param cancellation_date the cancellation_date to set
	 */
	public void setCancellation_date(Date cancellation_date) {
		this.cancellation_date = cancellation_date;
	}

	/**
	 * @return the annulment_date
	 */
	public Date getAnnulment_date() {
		return annulment_date;
	}

	/**
	 * @param annulment_date the annulment_date to set
	 */
	public void setAnnulment_date(Date annulment_date) {
		this.annulment_date = annulment_date;
	}

	/**
	 * @return the overdue_amount
	 */
	public BigDecimal getOverdue_amount() {
		return overdue_amount;
	}

	/**
	 * @param overdue_amount the overdue_amount to set
	 */
	public void setOverdue_amount(BigDecimal overdue_amount) {
		this.overdue_amount = overdue_amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseInvoicePaymentDTO [id_purchase_invoice="
				+ id_purchase_invoice + ", id_currency=" + id_currency
				+ ", status=" + status + ", amount=" + amount
				+ ", purchase_invoice_balance=" + purchase_invoice_balance
				+ ", payment_number=" + payment_number + ", registration_date="
				+ registration_date + ", payment_due_date=" + payment_due_date
				+ ", cancellation_date=" + cancellation_date
				+ ", annulment_date=" + annulment_date + ", overdue_amount="
				+ overdue_amount + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseInvoicePaymentDTO other = (PurchaseInvoicePaymentDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the currencyDTO
	 */
	public CurrencyDTO getCurrencyDTO() {
		return currencyDTO;
	}

	/**
	 * @param currencyDTO the currencyDTO to set
	 */
	public void setCurrencyDTO(CurrencyDTO currencyDTO) {
		this.currencyDTO = currencyDTO;
	}

	/**
	 * @return the cashReceiptDocumentDTO
	 */
	public CashReceiptDocumentDTO getCashReceiptDocumentDTO() {
		return cashReceiptDocumentDTO;
	}

	/**
	 * @param cashReceiptDocumentDTO the cashReceiptDocumentDTO to set
	 */
	public void setCashReceiptDocumentDTO(
			CashReceiptDocumentDTO cashReceiptDocumentDTO) {
		this.cashReceiptDocumentDTO = cashReceiptDocumentDTO;
	}
}
