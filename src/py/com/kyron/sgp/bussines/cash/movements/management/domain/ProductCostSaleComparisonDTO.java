package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class ProductCostSaleComparisonDTO extends GenericDTO {

	private String product;
	private BigDecimal order_item_product_unit_manufacture_cost;
	private BigDecimal order_item_unit_price_amount;
	private BigDecimal sale_invoice_item_unit_price_amount;
	private Long sale_invoice_item_quantity;
	private BigDecimal sale_price_acumulated;
	private BigDecimal production_cost_acumulated;
	private BigDecimal profit;
	private Date sale_date;
	private Long product_increase_over_cost_for_sale_price;
	
	private CurrencyDTO currencyDTO;
	
	private Date beginDate;
	private Date endDate;
	private Long id_currency;
	
	private String sale_invoice_item_quantity_formatted;
	private String sale_price_acumulated_formatted;
	private String production_cost_acumulated_formatted;
	private String profit_formatted;
	private String sale_date_formatted;
	private String increase_over_cost_for_sale_price_formatted;

	
	public ProductCostSaleComparisonDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductCostSaleComparisonDTO(Long id_currency, Date beginDate, Date endDate, CurrencyDTO currencyDTO) {
		// TODO Auto-generated constructor stub
		this.id_currency = id_currency;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.currencyDTO = currencyDTO;
	}
	
	public ProductCostSaleComparisonDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the order_item_product_unit_manufacture_cost
	 */
	public BigDecimal getOrder_item_product_unit_manufacture_cost() {
		return order_item_product_unit_manufacture_cost;
	}

	/**
	 * @param order_item_product_unit_manufacture_cost the order_item_product_unit_manufacture_cost to set
	 */
	public void setOrder_item_product_unit_manufacture_cost(
			BigDecimal order_item_product_unit_manufacture_cost) {
		this.order_item_product_unit_manufacture_cost = order_item_product_unit_manufacture_cost;
	}

	/**
	 * @return the order_item_unit_price_amount
	 */
	public BigDecimal getOrder_item_unit_price_amount() {
		return order_item_unit_price_amount;
	}

	/**
	 * @param order_item_unit_price_amount the order_item_unit_price_amount to set
	 */
	public void setOrder_item_unit_price_amount(
			BigDecimal order_item_unit_price_amount) {
		this.order_item_unit_price_amount = order_item_unit_price_amount;
	}

	/**
	 * @return the sale_invoice_item_unit_price_amount
	 */
	public BigDecimal getSale_invoice_item_unit_price_amount() {
		return sale_invoice_item_unit_price_amount;
	}

	/**
	 * @param sale_invoice_item_unit_price_amount the sale_invoice_item_unit_price_amount to set
	 */
	public void setSale_invoice_item_unit_price_amount(
			BigDecimal sale_invoice_item_unit_price_amount) {
		this.sale_invoice_item_unit_price_amount = sale_invoice_item_unit_price_amount;
	}

	/**
	 * @return the sale_invoice_item_quantity
	 */
	public Long getSale_invoice_item_quantity() {
		return sale_invoice_item_quantity;
	}

	/**
	 * @param sale_invoice_item_quantity the sale_invoice_item_quantity to set
	 */
	public void setSale_invoice_item_quantity(Long sale_invoice_item_quantity) {
		this.sale_invoice_item_quantity = sale_invoice_item_quantity;
	}

	/**
	 * @return the sale_price_acumulated
	 */
	public BigDecimal getSale_price_acumulated() {
		return sale_price_acumulated;
	}

	/**
	 * @param sale_price_acumulated the sale_price_acumulated to set
	 */
	public void setSale_price_acumulated(BigDecimal sale_price_acumulated) {
		this.sale_price_acumulated = sale_price_acumulated;
	}

	/**
	 * @return the production_cost_acumulated
	 */
	public BigDecimal getProduction_cost_acumulated() {
		return production_cost_acumulated;
	}

	/**
	 * @param production_cost_acumulated the production_cost_acumulated to set
	 */
	public void setProduction_cost_acumulated(BigDecimal production_cost_acumulated) {
		this.production_cost_acumulated = production_cost_acumulated;
	}

	/**
	 * @return the profit
	 */
	public BigDecimal getProfit() {
		return profit;
	}

	/**
	 * @param profit the profit to set
	 */
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	/**
	 * @return the sale_date
	 */
	public Date getSale_date() {
		return sale_date;
	}

	/**
	 * @param sale_date the sale_date to set
	 */
	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductCostSaleComparisonDTO [product=" + product
				+ ", order_item_product_unit_manufacture_cost="
				+ order_item_product_unit_manufacture_cost
				+ ", order_item_unit_price_amount="
				+ order_item_unit_price_amount
				+ ", sale_invoice_item_unit_price_amount="
				+ sale_invoice_item_unit_price_amount
				+ ", sale_invoice_item_quantity=" + sale_invoice_item_quantity
				+ ", sale_price_acumulated=" + sale_price_acumulated
				+ ", production_cost_acumulated=" + production_cost_acumulated
				+ ", profit=" + profit + ", sale_date=" + sale_date
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
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
		ProductCostSaleComparisonDTO other = (ProductCostSaleComparisonDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the sale_invoice_item_quantity_formatted
	 */
	public String getSale_invoice_item_quantity_formatted() {
		return sale_invoice_item_quantity_formatted;
	}

	/**
	 * @param sale_invoice_item_quantity_formatted the sale_invoice_item_quantity_formatted to set
	 */
	public void setSale_invoice_item_quantity_formatted(
			String sale_invoice_item_quantity_formatted) {
		this.sale_invoice_item_quantity_formatted = sale_invoice_item_quantity_formatted;
	}

	/**
	 * @return the sale_price_acumulated_formatted
	 */
	public String getSale_price_acumulated_formatted() {
		return sale_price_acumulated_formatted;
	}

	/**
	 * @param sale_price_acumulated_formatted the sale_price_acumulated_formatted to set
	 */
	public void setSale_price_acumulated_formatted(
			String sale_price_acumulated_formatted) {
		this.sale_price_acumulated_formatted = sale_price_acumulated_formatted;
	}

	/**
	 * @return the production_cost_acumulated_formatted
	 */
	public String getProduction_cost_acumulated_formatted() {
		return production_cost_acumulated_formatted;
	}

	/**
	 * @param production_cost_acumulated_formatted the production_cost_acumulated_formatted to set
	 */
	public void setProduction_cost_acumulated_formatted(
			String production_cost_acumulated_formatted) {
		this.production_cost_acumulated_formatted = production_cost_acumulated_formatted;
	}

	/**
	 * @return the profit_formatted
	 */
	public String getProfit_formatted() {
		return profit_formatted;
	}

	/**
	 * @param profit_formatted the profit_formatted to set
	 */
	public void setProfit_formatted(String profit_formatted) {
		this.profit_formatted = profit_formatted;
	}

	/**
	 * @return the sale_date_formatted
	 */
	public String getSale_date_formatted() {
		return sale_date_formatted;
	}

	/**
	 * @param sale_date_formatted the sale_date_formatted to set
	 */
	public void setSale_date_formatted(String sale_date_formatted) {
		this.sale_date_formatted = sale_date_formatted;
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
	 * @return the increase_over_cost_for_sale_price_formatted
	 */
	public String getIncrease_over_cost_for_sale_price_formatted() {
		return increase_over_cost_for_sale_price_formatted;
	}

	/**
	 * @param increase_over_cost_for_sale_price_formatted the increase_over_cost_for_sale_price_formatted to set
	 */
	public void setIncrease_over_cost_for_sale_price_formatted(
			String increase_over_cost_for_sale_price_formatted) {
		this.increase_over_cost_for_sale_price_formatted = increase_over_cost_for_sale_price_formatted;
	}

	/**
	 * @return the product_increase_over_cost_for_sale_price
	 */
	public Long getProduct_increase_over_cost_for_sale_price() {
		return product_increase_over_cost_for_sale_price;
	}

	/**
	 * @param product_increase_over_cost_for_sale_price the product_increase_over_cost_for_sale_price to set
	 */
	public void setProduct_increase_over_cost_for_sale_price(
			Long product_increase_over_cost_for_sale_price) {
		this.product_increase_over_cost_for_sale_price = product_increase_over_cost_for_sale_price;
	}
}
