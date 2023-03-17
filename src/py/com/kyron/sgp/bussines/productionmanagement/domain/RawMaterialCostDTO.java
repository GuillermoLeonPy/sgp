/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class RawMaterialCostDTO extends GenericDTO {
	
	private Long id_raw_material;
	private Long id_tariff;
	@Range(min=0L,max=999999999L)
	private BigDecimal tariff_amount;
	private BigDecimal purchase_invoice_average_cost;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean active;
	private TariffDTO tariffDTO;
	/**
	 * 
	 */

	public RawMaterialCostDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public RawMaterialCostDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public RawMaterialCostDTO(Long id_raw_material, Long id_tariff) {
		// TODO Auto-generated constructor stub
		this.id_raw_material = id_raw_material;
		this.id_tariff = id_tariff;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public RawMaterialCostDTO(Long id, String creation_user,
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

	public Long getId_tariff() {
		return id_tariff;
	}

	public void setId_tariff(Long id_tariff) {
		this.id_tariff = id_tariff;
	}

	public BigDecimal getTariff_amount() {
		return tariff_amount;
	}

	public void setTariff_amount(BigDecimal tariff_amount) {
		this.tariff_amount = tariff_amount;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public Date getValidity_end_date() {
		return validity_end_date;
	}

	public void setValidity_end_date(Date validity_end_date) {
		this.validity_end_date = validity_end_date;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public TariffDTO getTariffDTO() {
		return tariffDTO;
	}

	public void setTariffDTO(TariffDTO tariffDTO) {
		this.tariffDTO = tariffDTO;
	}

	@Override
	public String toString() {
		return "RawMaterialCostDTO [id_raw_material=" + id_raw_material
				+ ", id_tariff=" + id_tariff + ", tariff_amount="
				+ tariff_amount + ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date + ", active="
				+ active + ", tariffDTO=" + tariffDTO + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the purchase_invoice_average_cost
	 */
	public BigDecimal getPurchase_invoice_average_cost() {
		return purchase_invoice_average_cost;
	}

	/**
	 * @param purchase_invoice_average_cost the purchase_invoice_average_cost to set
	 */
	public void setPurchase_invoice_average_cost(
			BigDecimal purchase_invoice_average_cost) {
		this.purchase_invoice_average_cost = purchase_invoice_average_cost;
	}

}
