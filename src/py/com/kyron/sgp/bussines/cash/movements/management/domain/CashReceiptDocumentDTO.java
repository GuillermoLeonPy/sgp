package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class CashReceiptDocumentDTO extends GenericDTO {

	
	private Long id_sale_invoice_payment;
	private Long id_currency;
	private Long id_person;
	@Pattern(regexp = "^[0-9]+(-?[0-9]+)+$")
	@Size(min=6, max=20)
	private String identifier_number;
	private String bussines_name;
	private String bussines_ci_ruc;
	private Date registration_date;
	private BigDecimal amount;
	private Long id_purchase_invoice_payment;
	private Date emission_date;
	private String currency_id;
	private Boolean overduedPayment;
	@DecimalMax(value="999999999.99")
	@DecimalMin(value="0.1")
	private BigDecimal overduePaymentamount;
	public Long id_purchase_invoice;
	public Long id_purchase_invoice_credit_note;
	private String pms_implementing_enterprise_ruc;


	private CurrencyDTO currencyDTO;
	
	public CashReceiptDocumentDTO() {
		// TODO Auto-generated constructor stub
	}

	public CashReceiptDocumentDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public CashReceiptDocumentDTO(Long id,Long id_sale_invoice_payment) {
		// TODO Auto-generated constructor stub
		this.id_sale_invoice_payment = id_sale_invoice_payment;
	}

	public CashReceiptDocumentDTO(BigDecimal amount,CurrencyDTO currencyDTO) {
		// TODO Auto-generated constructor stub
		this.amount = amount;
		this.currencyDTO = currencyDTO;
	}
	
	public CashReceiptDocumentDTO(Long id,Long id_sale_invoice_payment,Long id_purchase_invoice_payment) {
		// TODO Auto-generated constructor stub
		this.id_purchase_invoice_payment = id_purchase_invoice_payment;
	}
	
	public CashReceiptDocumentDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_sale_invoice_payment
	 */
	public Long getId_sale_invoice_payment() {
		return id_sale_invoice_payment;
	}

	/**
	 * @param id_sale_invoice_payment the id_sale_invoice_payment to set
	 */
	public void setId_sale_invoice_payment(Long id_sale_invoice_payment) {
		this.id_sale_invoice_payment = id_sale_invoice_payment;
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
	 * @return the id_person
	 */
	public Long getId_person() {
		return id_person;
	}

	/**
	 * @param id_person the id_person to set
	 */
	public void setId_person(Long id_person) {
		this.id_person = id_person;
	}

	/**
	 * @return the identifier_number
	 */
	public String getIdentifier_number() {
		return identifier_number;
	}

	/**
	 * @param identifier_number the identifier_number to set
	 */
	public void setIdentifier_number(String identifier_number) {
		this.identifier_number = identifier_number;
	}

	/**
	 * @return the bussines_name
	 */
	public String getBussines_name() {
		return bussines_name;
	}

	/**
	 * @param bussines_name the bussines_name to set
	 */
	public void setBussines_name(String bussines_name) {
		this.bussines_name = bussines_name;
	}

	/**
	 * @return the bussines_ci_ruc
	 */
	public String getBussines_ci_ruc() {
		return bussines_ci_ruc;
	}

	/**
	 * @param bussines_ci_ruc the bussines_ci_ruc to set
	 */
	public void setBussines_ci_ruc(String bussines_ci_ruc) {
		this.bussines_ci_ruc = bussines_ci_ruc;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CashReceiptDocumentDTO [id_sale_invoice_payment="
				+ id_sale_invoice_payment + ", id_currency=" + id_currency
				+ ", id_person=" + id_person + ", identifier_number="
				+ identifier_number + ", bussines_name=" + bussines_name
				+ ", bussines_ci_ruc=" + bussines_ci_ruc
				+ ", registration_date=" + registration_date + ", amount="
				+ amount + ", id_purchase_invoice_payment="
				+ id_purchase_invoice_payment + ", emission_date="
				+ emission_date + ", currency_id=" + currency_id
				+ ", overduedPayment=" + overduedPayment
				+ ", overduePaymentamount=" + overduePaymentamount
				+ ", id_purchase_invoice=" + id_purchase_invoice
				+ ", id_purchase_invoice_credit_note="
				+ id_purchase_invoice_credit_note
				+ ", pms_implementing_enterprise_ruc="
				+ pms_implementing_enterprise_ruc + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
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
		CashReceiptDocumentDTO other = (CashReceiptDocumentDTO) obj;
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
	 * @return the id_purchase_invoice_payment
	 */
	public Long getId_purchase_invoice_payment() {
		return id_purchase_invoice_payment;
	}

	/**
	 * @param id_purchase_invoice_payment the id_purchase_invoice_payment to set
	 */
	public void setId_purchase_invoice_payment(Long id_purchase_invoice_payment) {
		this.id_purchase_invoice_payment = id_purchase_invoice_payment;
	}

	/**
	 * @return the emission_date
	 */
	public Date getEmission_date() {
		return emission_date;
	}

	/**
	 * @param emission_date the emission_date to set
	 */
	public void setEmission_date(Date emission_date) {
		this.emission_date = emission_date;
	}

	/**
	 * @return the currency_id
	 */
	public String getCurrency_id() {
		return currency_id;
	}

	/**
	 * @param currency_id the currency_id to set
	 */
	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	/**
	 * @return the overduedPayment
	 */
	public Boolean getOverduedPayment() {
		return overduedPayment;
	}

	/**
	 * @param overduedPayment the overduedPayment to set
	 */
	public void setOverduedPayment(Boolean overduedPayment) {
		this.overduedPayment = overduedPayment;
	}

	/**
	 * @return the overduePaymentamount
	 */
	public BigDecimal getOverduePaymentamount() {
		return overduePaymentamount;
	}

	/**
	 * @param overduePaymentamount the overduePaymentamount to set
	 */
	public void setOverduePaymentamount(BigDecimal overduePaymentamount) {
		this.overduePaymentamount = overduePaymentamount;
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
	 * @return the id_purchase_invoice_credit_note
	 */
	public Long getId_purchase_invoice_credit_note() {
		return id_purchase_invoice_credit_note;
	}

	/**
	 * @param id_purchase_invoice_credit_note the id_purchase_invoice_credit_note to set
	 */
	public void setId_purchase_invoice_credit_note(
			Long id_purchase_invoice_credit_note) {
		this.id_purchase_invoice_credit_note = id_purchase_invoice_credit_note;
	}

	/**
	 * @return the pms_implementing_enterprise_ruc
	 */
	public String getPms_implementing_enterprise_ruc() {
		return pms_implementing_enterprise_ruc;
	}

	/**
	 * @param pms_implementing_enterprise_ruc the pms_implementing_enterprise_ruc to set
	 */
	public void setPms_implementing_enterprise_ruc(
			String pms_implementing_enterprise_ruc) {
		this.pms_implementing_enterprise_ruc = pms_implementing_enterprise_ruc;
	}

}
