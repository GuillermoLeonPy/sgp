/**
 * 
 */
package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;

/**
 * @author testuser
 *
 */
public class RawMaterialExistenceDTO extends GenericDTO {

	private Long id_raw_material;
	private Long id_measurment_unit;
	private BigDecimal calculated_quantity;
	@Range(min=1L,max=9999L)
	private BigDecimal limit_calculated_quantity;
	private BigDecimal efective_quantity;
	@Range(min=1L,max=9999L)
	private BigDecimal automatic_purchase_request_quantity;
	private Date registration_date;
	private MeasurmentUnitDTO measurmentUnitDTO;
	
	/**
	 * 
	 */
	public RawMaterialExistenceDTO() {
		// TODO Auto-generated constructor stub
	}

	public RawMaterialExistenceDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public RawMaterialExistenceDTO(Long id_raw_material,Long id_measurment_unit) {
		// TODO Auto-generated constructor stub
		this.id_raw_material = id_raw_material;
		this.id_measurment_unit = id_measurment_unit;
	}

	public RawMaterialExistenceDTO(BigDecimal calculated_quantity) {
		// TODO Auto-generated constructor stub
		this.calculated_quantity = calculated_quantity;
	}
	
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public RawMaterialExistenceDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public Long getId_raw_material() {
		return id_raw_material;
	}

	public void setId_raw_material(Long id_raw_material) {
		this.id_raw_material = id_raw_material;
	}

	public Long getId_measurment_unit() {
		return id_measurment_unit;
	}

	public void setId_measurment_unit(Long id_measurment_unit) {
		this.id_measurment_unit = id_measurment_unit;
	}

	public BigDecimal getCalculated_quantity() {
		return calculated_quantity;
	}

	public void setCalculated_quantity(BigDecimal calculated_quantity) {
		this.calculated_quantity = calculated_quantity;
	}

	public BigDecimal getLimit_calculated_quantity() {
		return limit_calculated_quantity;
	}

	public void setLimit_calculated_quantity(BigDecimal limit_calculated_quantity) {
		this.limit_calculated_quantity = limit_calculated_quantity;
	}

	public BigDecimal getEfective_quantity() {
		return efective_quantity;
	}

	public void setEfective_quantity(BigDecimal efective_quantity) {
		this.efective_quantity = efective_quantity;
	}

	public BigDecimal getAutomatic_purchase_request_quantity() {
		return automatic_purchase_request_quantity;
	}

	public void setAutomatic_purchase_request_quantity(
			BigDecimal automatic_purchase_request_quantity) {
		this.automatic_purchase_request_quantity = automatic_purchase_request_quantity;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	@Override
	public String toString() {
		return "RawMaterialExistenceDTO [id_raw_material=" + id_raw_material
				+ ", id_measurment_unit=" + id_measurment_unit
				+ ", calculated_quantity=" + calculated_quantity
				+ ", limit_calculated_quantity=" + limit_calculated_quantity
				+ ", efective_quantity=" + efective_quantity
				+ ", automatic_purchase_request_quantity="
				+ automatic_purchase_request_quantity + ", registration_date="
				+ registration_date + ", measurmentUnitDTO="
				+ measurmentUnitDTO + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	public MeasurmentUnitDTO getMeasurmentUnitDTO() {
		return measurmentUnitDTO;
	}

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
		RawMaterialExistenceDTO other = (RawMaterialExistenceDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
