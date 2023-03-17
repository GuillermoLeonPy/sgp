/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class ProductionProcessDTO extends GenericDTO {

	@Size(min=9, max=100)
	private String process_id;
	@Size(min=9, max=250)
	private String process_description;
	private Long id_product;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean is_active;
	private Boolean is_enable;
	private List<ProductionProcessActivityDTO> listProductionProcessActivityDTO;
	
	/**
	 * 
	 */
	public ProductionProcessDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductionProcessDTO(Long id) {
		super();
		this.setId(id);
	}

	public ProductionProcessDTO(Long id, Long id_product) {
		super();
		this.setId(id);
		this.id_product = id_product;
	}
	
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public ProductionProcessDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the process_id
	 */
	public String getProcess_id() {
		return process_id;
	}

	/**
	 * @param process_id the process_id to set
	 */
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

	/**
	 * @return the process_description
	 */
	public String getProcess_description() {
		return process_description;
	}

	/**
	 * @param process_description the process_description to set
	 */
	public void setProcess_description(String process_description) {
		this.process_description = process_description;
	}

	/**
	 * @return the id_product
	 */
	public Long getId_product() {
		return id_product;
	}

	/**
	 * @param id_product the id_product to set
	 */
	public void setId_product(Long id_product) {
		this.id_product = id_product;
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
	 * @return the validity_end_date
	 */
	public Date getValidity_end_date() {
		return validity_end_date;
	}

	/**
	 * @param validity_end_date the validity_end_date to set
	 */
	public void setValidity_end_date(Date validity_end_date) {
		this.validity_end_date = validity_end_date;
	}

	/**
	 * @return the is_active
	 */
	public Boolean getIs_active() {
		return is_active;
	}

	/**
	 * @param is_active the is_active to set
	 */
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	/**
	 * @return the is_enable
	 */
	public Boolean getIs_enable() {
		return is_enable;
	}

	/**
	 * @param is_enable the is_enable to set
	 */
	public void setIs_enable(Boolean is_enable) {
		this.is_enable = is_enable;
	}

	/**
	 * @return the listProductionProcessActivityDTO
	 */
	public List<ProductionProcessActivityDTO> getListProductionProcessActivityDTO() {
		return listProductionProcessActivityDTO;
	}

	/**
	 * @param listProductionProcessActivityDTO the listProductionProcessActivityDTO to set
	 */
	public void setListProductionProcessActivityDTO(
			List<ProductionProcessActivityDTO> listProductionProcessActivityDTO) {
		this.listProductionProcessActivityDTO = listProductionProcessActivityDTO;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductionProcessDTO [process_id=" + process_id
				+ ", process_description=" + process_description
				+ ", id_product=" + id_product + ", registration_date="
				+ registration_date + ", validity_end_date="
				+ validity_end_date + ", is_active=" + is_active
				+ ", is_enable=" + is_enable
				+ ", listProductionProcessActivityDTO="
				+ listProductionProcessActivityDTO + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

}
