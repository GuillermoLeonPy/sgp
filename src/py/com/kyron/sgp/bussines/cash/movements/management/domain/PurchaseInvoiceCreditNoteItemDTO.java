package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;

public class PurchaseInvoiceCreditNoteItemDTO extends GenericDTO {
	
	private Long id_purchase_invoice_credit_note;
	private Long id_purchase_invoice_item;
	private Long id_raw_material;
	private Long id_measurment_unit;
	private BigDecimal quantity;
	private BigDecimal unit_price_amount;
	private BigDecimal exempt_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	private Long temporal_id;

	private RawMaterialDTO rawMaterialDTO;
	private MeasurmentUnitDTO measurmentUnitDTO;
	private PurchaseInvoiceItemDTO purchaseInvoiceItemDTO;
	
	public PurchaseInvoiceCreditNoteItemDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public PurchaseInvoiceCreditNoteItemDTO(Long id_purchase_invoice_credit_note) {
		// TODO Auto-generated constructor stub
		this.id_purchase_invoice_credit_note = id_purchase_invoice_credit_note;
	}
	
	public PurchaseInvoiceCreditNoteItemDTO(PurchaseInvoiceItemDTO purchaseInvoiceItemDTO,RawMaterialDTO rawMaterialDTO,MeasurmentUnitDTO measurmentUnitDTO, 
			Long id_purchase_invoice_item, Long temporal_id,BigDecimal unit_price_amount ) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceItemDTO = purchaseInvoiceItemDTO;
		this.rawMaterialDTO = rawMaterialDTO;
		this.id_raw_material = this.rawMaterialDTO.getId();
		this.measurmentUnitDTO = measurmentUnitDTO;
		this.id_measurment_unit = this.measurmentUnitDTO.getId();
		this.id_purchase_invoice_item = id_purchase_invoice_item;
		this.temporal_id = temporal_id;
		this.unit_price_amount = unit_price_amount;
	}

	public PurchaseInvoiceCreditNoteItemDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
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
	 * @return the id_purchase_invoice_item
	 */
	public Long getId_purchase_invoice_item() {
		return id_purchase_invoice_item;
	}

	/**
	 * @param id_purchase_invoice_item the id_purchase_invoice_item to set
	 */
	public void setId_purchase_invoice_item(Long id_purchase_invoice_item) {
		this.id_purchase_invoice_item = id_purchase_invoice_item;
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
		PurchaseInvoiceCreditNoteItemDTO other = (PurchaseInvoiceCreditNoteItemDTO) obj;
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

	/**
	 * @return the purchaseInvoiceItemDTO
	 */
	public PurchaseInvoiceItemDTO getPurchaseInvoiceItemDTO() {
		return purchaseInvoiceItemDTO;
	}

	/**
	 * @param purchaseInvoiceItemDTO the purchaseInvoiceItemDTO to set
	 */
	public void setPurchaseInvoiceItemDTO(
			PurchaseInvoiceItemDTO purchaseInvoiceItemDTO) {
		this.purchaseInvoiceItemDTO = purchaseInvoiceItemDTO;
	}
}
