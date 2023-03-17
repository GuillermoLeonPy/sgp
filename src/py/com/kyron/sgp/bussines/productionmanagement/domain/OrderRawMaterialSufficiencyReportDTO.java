package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class OrderRawMaterialSufficiencyReportDTO extends GenericDTO {

	
	private Long id_order;
	private Date report_emission_date;
	private Long order_identifier_number;
	private Long id_sale_invoice;
	private String sale_invoice_identifier_number;
	private Date sale_invoice_emission_date;
	private String bussines_name;
	private String bussines_ci_ruc;
	private String sale_invoice_payment_condition;
	private String sale_invoice_status;
	private BigDecimal sale_invoice_total_amount;
	private String currency_id_code;
	public List<OrderItemRawMaterialSufficiencyReportDTO> listOrderItemRawMaterialSufficiencyReportDTO;
	public List<OrderItemRawMaterialSufficiencyReportDetailDTO> listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder;
	
	public OrderRawMaterialSufficiencyReportDTO() {
		// TODO Auto-generated constructor stub
	}

	public OrderRawMaterialSufficiencyReportDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_order
	 */
	public Long getId_order() {
		return id_order;
	}

	/**
	 * @param id_order the id_order to set
	 */
	public void setId_order(Long id_order) {
		this.id_order = id_order;
	}

	/**
	 * @return the report_emission_date
	 */
	public Date getReport_emission_date() {
		return report_emission_date;
	}

	/**
	 * @param report_emission_date the report_emission_date to set
	 */
	public void setReport_emission_date(Date report_emission_date) {
		this.report_emission_date = report_emission_date;
	}

	/**
	 * @return the order_identifier_number
	 */
	public Long getOrder_identifier_number() {
		return order_identifier_number;
	}

	/**
	 * @param order_identifier_number the order_identifier_number to set
	 */
	public void setOrder_identifier_number(Long order_identifier_number) {
		this.order_identifier_number = order_identifier_number;
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
	 * @return the sale_invoice_identifier_number
	 */
	public String getSale_invoice_identifier_number() {
		return sale_invoice_identifier_number;
	}

	/**
	 * @param sale_invoice_identifier_number the sale_invoice_identifier_number to set
	 */
	public void setSale_invoice_identifier_number(
			String sale_invoice_identifier_number) {
		this.sale_invoice_identifier_number = sale_invoice_identifier_number;
	}

	/**
	 * @return the sale_invoice_emission_date
	 */
	public Date getSale_invoice_emission_date() {
		return sale_invoice_emission_date;
	}

	/**
	 * @param sale_invoice_emission_date the sale_invoice_emission_date to set
	 */
	public void setSale_invoice_emission_date(Date sale_invoice_emission_date) {
		this.sale_invoice_emission_date = sale_invoice_emission_date;
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
	 * @return the sale_invoice_payment_condition
	 */
	public String getSale_invoice_payment_condition() {
		return sale_invoice_payment_condition;
	}

	/**
	 * @param sale_invoice_payment_condition the sale_invoice_payment_condition to set
	 */
	public void setSale_invoice_payment_condition(
			String sale_invoice_payment_condition) {
		this.sale_invoice_payment_condition = sale_invoice_payment_condition;
	}

	/**
	 * @return the sale_invoice_status
	 */
	public String getSale_invoice_status() {
		return sale_invoice_status;
	}

	/**
	 * @param sale_invoice_status the sale_invoice_status to set
	 */
	public void setSale_invoice_status(String sale_invoice_status) {
		this.sale_invoice_status = sale_invoice_status;
	}

	/**
	 * @return the sale_invoice_total_amount
	 */
	public BigDecimal getSale_invoice_total_amount() {
		return sale_invoice_total_amount;
	}

	/**
	 * @param sale_invoice_total_amount the sale_invoice_total_amount to set
	 */
	public void setSale_invoice_total_amount(BigDecimal sale_invoice_total_amount) {
		this.sale_invoice_total_amount = sale_invoice_total_amount;
	}

	/**
	 * @return the currency_id_code
	 */
	public String getCurrency_id_code() {
		return currency_id_code;
	}

	/**
	 * @param currency_id_code the currency_id_code to set
	 */
	public void setCurrency_id_code(String currency_id_code) {
		this.currency_id_code = currency_id_code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderRawMaterialSufficiencyReportDTO [id_order=" + id_order
				+ ", report_emission_date=" + report_emission_date
				+ ", order_identifier_number=" + order_identifier_number
				+ ", id_sale_invoice=" + id_sale_invoice
				+ ", sale_invoice_identifier_number="
				+ sale_invoice_identifier_number
				+ ", sale_invoice_emission_date=" + sale_invoice_emission_date
				+ ", bussines_name=" + bussines_name + ", bussines_ci_ruc="
				+ bussines_ci_ruc + ", sale_invoice_payment_condition="
				+ sale_invoice_payment_condition + ", sale_invoice_status="
				+ sale_invoice_status + ", sale_invoice_total_amount="
				+ sale_invoice_total_amount + ", currency_id_code="
				+ currency_id_code + ", getId()=" + getId()
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
		OrderRawMaterialSufficiencyReportDTO other = (OrderRawMaterialSufficiencyReportDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listOrderItemRawMaterialSufficiencyReportDTO
	 */
	public List<OrderItemRawMaterialSufficiencyReportDTO> getListOrderItemRawMaterialSufficiencyReportDTO() {
		return listOrderItemRawMaterialSufficiencyReportDTO;
	}

	/**
	 * @param listOrderItemRawMaterialSufficiencyReportDTO the listOrderItemRawMaterialSufficiencyReportDTO to set
	 */
	public void setListOrderItemRawMaterialSufficiencyReportDTO(
			List<OrderItemRawMaterialSufficiencyReportDTO> listOrderItemRawMaterialSufficiencyReportDTO) {
		this.listOrderItemRawMaterialSufficiencyReportDTO = listOrderItemRawMaterialSufficiencyReportDTO;
	}

	/**
	 * @return the listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder
	 */
	public List<OrderItemRawMaterialSufficiencyReportDetailDTO> getListOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder() {
		return listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder;
	}

	/**
	 * @param listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder the listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder to set
	 */
	public void setListOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder(
			List<OrderItemRawMaterialSufficiencyReportDetailDTO> listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder) {
		this.listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder = listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder;
	}
}
