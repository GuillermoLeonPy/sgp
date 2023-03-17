/**
 * 
 */
package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class SaleInvoicePaymentDTO extends GenericDTO {

	private Long id_sale_invoice;
	private Long id_currency;
	private String status;
	private BigDecimal amount;
	private BigDecimal sale_invoice_balance;
	private Long payment_number;
	private Date registration_date;
	private Date payment_due_date;
	private Date cancellation_date;
	private Date annulment_date;
	private CurrencyDTO currencyDTO;
	
	private CashReceiptDocumentDTO cashReceiptDocumentDTO;
	
	/* commodin property */
	private Long id_credit_note;
	
	/**
	 * 
	 */
	public SaleInvoicePaymentDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SaleInvoicePaymentDTO(Long id) {
		super();
		this.setId(id);
	}

	public SaleInvoicePaymentDTO(Long id,Long id_sale_invoice) {
		super();
		this.id_sale_invoice = id_sale_invoice;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public SaleInvoicePaymentDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_sale_invoice
	 */
	public Long getId_sale_invoice() {
		return id_sale_invoice;
	}

	/**
	 * @param id_sale_invoice the id_sale_invoice to set
	 */
	public void setId_sale_invoice(Long id_sale_invoice) {
		this.id_sale_invoice = id_sale_invoice;
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
	 * @return the sale_invoice_balance
	 */
	public BigDecimal getSale_invoice_balance() {
		return sale_invoice_balance;
	}

	/**
	 * @param sale_invoice_balance the sale_invoice_balance to set
	 */
	public void setSale_invoice_balance(BigDecimal sale_invoice_balance) {
		this.sale_invoice_balance = sale_invoice_balance;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoicePaymentDTO [id_sale_invoice=" + id_sale_invoice
				+ ", id_currency=" + id_currency + ", status=" + status
				+ ", amount=" + amount + ", sale_invoice_balance="
				+ sale_invoice_balance + ", payment_number=" + payment_number
				+ ", registration_date=" + registration_date
				+ ", payment_due_date=" + payment_due_date
				+ ", cancellation_date=" + cancellation_date
				+ ", annulment_date=" + annulment_date + ", currencyDTO="
				+ currencyDTO + ", cashReceiptDocumentDTO="
				+ cashReceiptDocumentDTO + ", id_credit_note=" + id_credit_note
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", getClass()=" + getClass()
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
		SaleInvoicePaymentDTO other = (SaleInvoicePaymentDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
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

	/**
	 * @return the id_credit_note
	 */
	public Long getId_credit_note() {
		return id_credit_note;
	}

	/**
	 * @param id_credit_note the id_credit_note to set
	 */
	public void setId_credit_note(Long id_credit_note) {
		this.id_credit_note = id_credit_note;
	}

}
