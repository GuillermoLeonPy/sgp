package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;

public class PurchaseInvoiceItemDTO extends GenericDTO {

	private Long id_purchase_invoice;
	private Long id_raw_material;
	private Long id_measurment_unit;
	@DecimalMax(value="999.99")
	@DecimalMin(value="0.1")
	private BigDecimal quantity;
	@DecimalMax(value="999999.99")
	@DecimalMin(value="0.1")
	private BigDecimal unit_price_amount;
	private BigDecimal exempt_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	
	private Long temporal_id;
	private RawMaterialDTO rawMaterialDTO;
	private MeasurmentUnitDTO measurmentUnitDTO;
	
	private String raw_material_id;
	private String measurment_unit_id;
	
	public PurchaseInvoiceItemDTO() {
		// TODO Auto-generated constructor stub
	}

	public PurchaseInvoiceItemDTO(Long id) {
		super();
		this.setId(id);
	}
	

	public PurchaseInvoiceItemDTO(Long id,Long id_purchase_invoice,Long temporal_id) {
		// TODO Auto-generated constructor stub
		super();
		this.setId(id);
		this.id_purchase_invoice = id_purchase_invoice;
		this.temporal_id = temporal_id;
	}	
	
	public PurchaseInvoiceItemDTO(Long id, String creation_user,
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
	 * @return the id_raw_material
	 */
	public Long getId_raw_material() {
		return id_raw_material;
	}

	/**
	 * @param id_raw_material the id_raw_material to set
	 */
	public void setId_raw_material(Long id_raw_material) {
		this.id_raw_material = id_raw_material;
	}

	/**
	 * @return the id_measurment_unit
	 */
	public Long getId_measurment_unit() {
		return id_measurment_unit;
	}

	/**
	 * @param id_measurment_unit the id_measurment_unit to set
	 */
	public void setId_measurment_unit(Long id_measurment_unit) {
		this.id_measurment_unit = id_measurment_unit;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unit_price_amount
	 */
	public BigDecimal getUnit_price_amount() {
		return unit_price_amount;
	}

	/**
	 * @param unit_price_amount the unit_price_amount to set
	 */
	public void setUnit_price_amount(BigDecimal unit_price_amount) {
		this.unit_price_amount = unit_price_amount;
	}

	/**
	 * @return the exempt_amount
	 */
	public BigDecimal getExempt_amount() {
		return exempt_amount;
	}

	/**
	 * @param exempt_amount the exempt_amount to set
	 */
	public void setExempt_amount(BigDecimal exempt_amount) {
		this.exempt_amount = exempt_amount;
	}

	/**
	 * @return the value_added_tax_10_amount
	 */
	public BigDecimal getValue_added_tax_10_amount() {
		return value_added_tax_10_amount;
	}

	/**
	 * @param value_added_tax_10_amount the value_added_tax_10_amount to set
	 */
	public void setValue_added_tax_10_amount(BigDecimal value_added_tax_10_amount) {
		this.value_added_tax_10_amount = value_added_tax_10_amount;
	}

	/**
	 * @return the value_added_tax_5_amount
	 */
	public BigDecimal getValue_added_tax_5_amount() {
		return value_added_tax_5_amount;
	}

	/**
	 * @param value_added_tax_5_amount the value_added_tax_5_amount to set
	 */
	public void setValue_added_tax_5_amount(BigDecimal value_added_tax_5_amount) {
		this.value_added_tax_5_amount = value_added_tax_5_amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseInvoiceItemDTO [id_purchase_invoice="
				+ id_purchase_invoice + ", id_raw_material=" + id_raw_material
				+ ", id_measurment_unit=" + id_measurment_unit + ", quantity="
				+ quantity + ", unit_price_amount=" + unit_price_amount
				+ ", exempt_amount=" + exempt_amount
				+ ", value_added_tax_10_amount=" + value_added_tax_10_amount
				+ ", value_added_tax_5_amount=" + value_added_tax_5_amount
				+ ", temporal_id=" + temporal_id + ", raw_material_id="
				+ raw_material_id + ", measurment_unit_id="
				+ measurment_unit_id + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	/**
	 * @return the temporal_id
	 */
	public Long getTemporal_id() {
		return temporal_id;
	}

	/**
	 * @param temporal_id the temporal_id to set
	 */
	public void setTemporal_id(Long temporal_id) {
		this.temporal_id = temporal_id;
	}

	/**
	 * @return the rawMaterialDTO
	 */
	public RawMaterialDTO getRawMaterialDTO() {
		return rawMaterialDTO;
	}

	/**
	 * @param rawMaterialDTO the rawMaterialDTO to set
	 */
	public void setRawMaterialDTO(RawMaterialDTO rawMaterialDTO) {
		this.rawMaterialDTO = rawMaterialDTO;
	}

	/**
	 * @return the measurmentUnitDTO
	 */
	public MeasurmentUnitDTO getMeasurmentUnitDTO() {
		return measurmentUnitDTO;
	}

	/**
	 * @param measurmentUnitDTO the measurmentUnitDTO to set
	 */
	public void setMeasurmentUnitDTO(MeasurmentUnitDTO measurmentUnitDTO) {
		this.measurmentUnitDTO = measurmentUnitDTO;
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
		result = prime * result
				+ ((temporal_id == null) ? 0 : temporal_id.hashCode());
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
		PurchaseInvoiceItemDTO other = (PurchaseInvoiceItemDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (temporal_id == null) {
			if (other.temporal_id != null)
				return false;
		} else if (!temporal_id.equals(other.temporal_id))
			return false;
		return true;
	}

	/**
	 * @return the raw_material_id
	 */
	public String getRaw_material_id() {
		return raw_material_id;
	}

	/**
	 * @param raw_material_id the raw_material_id to set
	 */
	public void setRaw_material_id(String raw_material_id) {
		this.raw_material_id = raw_material_id;
	}

	/**
	 * @return the measurment_unit_id
	 */
	public String getMeasurment_unit_id() {
		return measurment_unit_id;
	}

	/**
	 * @param measurment_unit_id the measurment_unit_id to set
	 */
	public void setMeasurment_unit_id(String measurment_unit_id) {
		this.measurment_unit_id = measurment_unit_id;
	}
}
