package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;

public class RawMaterialPurchaseRequestDTO extends GenericDTO {

	private Long id_raw_material;
	private Long id_measurment_unit;
	private Long id_person_supplier;
	@DecimalMax(value="9999.99")
	@DecimalMin(value="0.1")
	private BigDecimal quantity;
	private Date registration_date;
	private String status;
	private RawMaterialDTO rawMaterialDTO;
	private MeasurmentUnitDTO measurmentUnitDTO;
	private PersonDTO personDTO;
	
	public RawMaterialPurchaseRequestDTO() {
		// TODO Auto-generated constructor stub
	}

	public RawMaterialPurchaseRequestDTO(Long id) {
		super();
		this.setId(id);
	}

	public RawMaterialPurchaseRequestDTO(Long id_raw_material,Long id_measurment_unit,Long id_person_supplier) {
		// TODO Auto-generated constructor stub
		this.id_raw_material = id_raw_material;
		this.id_measurment_unit = id_measurment_unit;
		this.id_person_supplier = id_person_supplier;
	}
	
	public RawMaterialPurchaseRequestDTO(Long id, String creation_user,
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

	public Long getId_person_supplier() {
		return id_person_supplier;
	}

	public void setId_person_supplier(Long id_person_supplier) {
		this.id_person_supplier = id_person_supplier;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	@Override
	public String toString() {
		return "RawMaterialPurchaseRequestDTO [id_raw_material="
				+ id_raw_material + ", id_measurment_unit="
				+ id_measurment_unit + ", id_person_supplier="
				+ id_person_supplier + ", quantity=" + quantity
				+ ", registration_date=" + registration_date + ", status="
				+ status + ", rawMaterialDTO=" + rawMaterialDTO
				+ ", measurmentUnitDTO=" + measurmentUnitDTO + ", personDTO="
				+ personDTO + ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	public RawMaterialDTO getRawMaterialDTO() {
		return rawMaterialDTO;
	}

	public void setRawMaterialDTO(RawMaterialDTO rawMaterialDTO) {
		this.rawMaterialDTO = rawMaterialDTO;
	}

	public MeasurmentUnitDTO getMeasurmentUnitDTO() {
		return measurmentUnitDTO;
	}

	public void setMeasurmentUnitDTO(MeasurmentUnitDTO measurmentUnitDTO) {
		this.measurmentUnitDTO = measurmentUnitDTO;
	}

	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		RawMaterialPurchaseRequestDTO other = (RawMaterialPurchaseRequestDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
