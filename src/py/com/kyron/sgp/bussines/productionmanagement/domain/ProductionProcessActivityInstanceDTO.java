package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;

public class ProductionProcessActivityInstanceDTO extends GenericDTO {

	private Long id_production_activity;
	private Long id_person;
	private Long id_order;
	private Long id_order_item;
	private Long id_product;
	private Boolean require_parcial_product_recall;
	private Date parcial_product_recall_date;
	private Long recall_locker_number;
	private Boolean is_asignable;
	private Date assignment_date;
	private Boolean delivers_product_instance;
	private Boolean delivers_partial_result;
	private Date partial_result_delivery_date;
	private Long occupied_locker_number;
	private String status;
	private String or_status;
	private String previous_status;
	private String next_status;
	private Long activity_instance_unique_number;
	private Long product_instance_unique_number;
	private Date instantiation_date;
	private Date activity_start_work_date;
	private Date activity_finish_work_date;
	private Date activity_cancellation_date;
	private String activity_cancellation_reason_description;
	
	private PersonDTO personDTO;
	private ProductionProcessActivityDTO productionProcessActivityDTO;
	private List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO;
	/* comodin properties */
	private Boolean activity_start_work_date_empty;
	private Boolean activity_finish_work_date_empty;
	private Boolean assignment_date_empty;
	
	public ProductionProcessActivityInstanceDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductionProcessActivityInstanceDTO(Long id) {
		super();
		this.setId(id);
	}

	public ProductionProcessActivityInstanceDTO(Long id,Long id_order) {
		this.id_order = id_order;
	}
	
	public ProductionProcessActivityInstanceDTO(
			Long id_person,
			Boolean is_asignable,
			Boolean assignment_date_empty,
			Boolean require_parcial_product_recall,
			String status,
			String or_status,
			String next_status,
			Boolean delivers_partial_result,
			Boolean delivers_product_instance) {
		// TODO Auto-generated constructor stub
		this.id_person = id_person;
		this.is_asignable = is_asignable;
		this.assignment_date_empty = assignment_date_empty;
		this.require_parcial_product_recall = require_parcial_product_recall;
		this.status = status;
		this.or_status = or_status;
		this.next_status = next_status;
		this.delivers_partial_result = delivers_partial_result;
		this.delivers_product_instance = delivers_product_instance;
	}

	public ProductionProcessActivityInstanceDTO(Long id,Long id_order,Long activity_instance_unique_number) {
		// TODO Auto-generated constructor stub
		this.activity_instance_unique_number = activity_instance_unique_number;
	}
	
	public ProductionProcessActivityInstanceDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_production_activity
	 */
	public Long getId_production_activity() {
		return id_production_activity;
	}

	/**
	 * @param id_production_activity the id_production_activity to set
	 */
	public void setId_production_activity(Long id_production_activity) {
		this.id_production_activity = id_production_activity;
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
	 * @return the id_order_item
	 */
	public Long getId_order_item() {
		return id_order_item;
	}

	/**
	 * @param id_order_item the id_order_item to set
	 */
	public void setId_order_item(Long id_order_item) {
		this.id_order_item = id_order_item;
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
	 * @return the require_parcial_product_recall
	 */
	public Boolean getRequire_parcial_product_recall() {
		return require_parcial_product_recall;
	}

	/**
	 * @param require_parcial_product_recall the require_parcial_product_recall to set
	 */
	public void setRequire_parcial_product_recall(
			Boolean require_parcial_product_recall) {
		this.require_parcial_product_recall = require_parcial_product_recall;
	}

	/**
	 * @return the is_asignable
	 */
	public Boolean getIs_asignable() {
		return is_asignable;
	}

	/**
	 * @param is_asignable the is_asignable to set
	 */
	public void setIs_asignable(Boolean is_asignable) {
		this.is_asignable = is_asignable;
	}

	/**
	 * @return the assignment_date
	 */
	public Date getAssignment_date() {
		return assignment_date;
	}

	/**
	 * @param assignment_date the assignment_date to set
	 */
	public void setAssignment_date(Date assignment_date) {
		this.assignment_date = assignment_date;
	}

	/**
	 * @return the delivers_product_instance
	 */
	public Boolean getDelivers_product_instance() {
		return delivers_product_instance;
	}

	/**
	 * @param delivers_product_instance the delivers_product_instance to set
	 */
	public void setDelivers_product_instance(Boolean delivers_product_instance) {
		this.delivers_product_instance = delivers_product_instance;
	}

	/**
	 * @return the delivers_partial_result
	 */
	public Boolean getDelivers_partial_result() {
		return delivers_partial_result;
	}

	/**
	 * @param delivers_partial_result the delivers_partial_result to set
	 */
	public void setDelivers_partial_result(Boolean delivers_partial_result) {
		this.delivers_partial_result = delivers_partial_result;
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
	 * @return the previous_status
	 */
	public String getPrevious_status() {
		return previous_status;
	}

	/**
	 * @param previous_status the previous_status to set
	 */
	public void setPrevious_status(String previous_status) {
		this.previous_status = previous_status;
	}

	/**
	 * @return the next_status
	 */
	public String getNext_status() {
		return next_status;
	}

	/**
	 * @param next_status the next_status to set
	 */
	public void setNext_status(String next_status) {
		this.next_status = next_status;
	}

	/**
	 * @return the activity_instance_unique_number
	 */
	public Long getActivity_instance_unique_number() {
		return activity_instance_unique_number;
	}

	/**
	 * @param activity_instance_unique_number the activity_instance_unique_number to set
	 */
	public void setActivity_instance_unique_number(
			Long activity_instance_unique_number) {
		this.activity_instance_unique_number = activity_instance_unique_number;
	}

	/**
	 * @return the product_instance_unique_number
	 */
	public Long getProduct_instance_unique_number() {
		return product_instance_unique_number;
	}

	/**
	 * @param product_instance_unique_number the product_instance_unique_number to set
	 */
	public void setProduct_instance_unique_number(
			Long product_instance_unique_number) {
		this.product_instance_unique_number = product_instance_unique_number;
	}

	/**
	 * @return the instantiation_date
	 */
	public Date getInstantiation_date() {
		return instantiation_date;
	}

	/**
	 * @param instantiation_date the instantiation_date to set
	 */
	public void setInstantiation_date(Date instantiation_date) {
		this.instantiation_date = instantiation_date;
	}

	/**
	 * @return the activity_start_work_date
	 */
	public Date getActivity_start_work_date() {
		return activity_start_work_date;
	}

	/**
	 * @param activity_start_work_date the activity_start_work_date to set
	 */
	public void setActivity_start_work_date(Date activity_start_work_date) {
		this.activity_start_work_date = activity_start_work_date;
	}

	/**
	 * @return the activity_finish_work_date
	 */
	public Date getActivity_finish_work_date() {
		return activity_finish_work_date;
	}

	/**
	 * @param activity_finish_work_date the activity_finish_work_date to set
	 */
	public void setActivity_finish_work_date(Date activity_finish_work_date) {
		this.activity_finish_work_date = activity_finish_work_date;
	}

	/**
	 * @return the activity_cancellation_date
	 */
	public Date getActivity_cancellation_date() {
		return activity_cancellation_date;
	}

	/**
	 * @param activity_cancellation_date the activity_cancellation_date to set
	 */
	public void setActivity_cancellation_date(Date activity_cancellation_date) {
		this.activity_cancellation_date = activity_cancellation_date;
	}

	/**
	 * @return the activity_cancellation_reason_description
	 */
	public String getActivity_cancellation_reason_description() {
		return activity_cancellation_reason_description;
	}

	/**
	 * @param activity_cancellation_reason_description the activity_cancellation_reason_description to set
	 */
	public void setActivity_cancellation_reason_description(
			String activity_cancellation_reason_description) {
		this.activity_cancellation_reason_description = activity_cancellation_reason_description;
	}

	/**
	 * @return the personDTO
	 */
	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	/**
	 * @param personDTO the personDTO to set
	 */
	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductionProcessActivityInstanceDTO [id_production_activity="
				+ id_production_activity + ", id_person=" + id_person
				+ ", id_order=" + id_order + ", id_order_item=" + id_order_item
				+ ", id_product=" + id_product
				+ ", require_parcial_product_recall="
				+ require_parcial_product_recall
				+ ", parcial_product_recall_date="
				+ parcial_product_recall_date + ", recall_locker_number="
				+ recall_locker_number + ", is_asignable=" + is_asignable
				+ ", assignment_date=" + assignment_date
				+ ", delivers_product_instance=" + delivers_product_instance
				+ ", delivers_partial_result=" + delivers_partial_result
				+ ", partial_result_delivery_date="
				+ partial_result_delivery_date + ", occupied_locker_number="
				+ occupied_locker_number + ", status=" + status
				+ ", or_status=" + or_status + ", previous_status="
				+ previous_status + ", next_status=" + next_status
				+ ", activity_instance_unique_number="
				+ activity_instance_unique_number
				+ ", product_instance_unique_number="
				+ product_instance_unique_number + ", instantiation_date="
				+ instantiation_date + ", activity_start_work_date="
				+ activity_start_work_date + ", activity_finish_work_date="
				+ activity_finish_work_date + ", activity_cancellation_date="
				+ activity_cancellation_date
				+ ", activity_cancellation_reason_description="
				+ activity_cancellation_reason_description + ", personDTO="
				+ personDTO + ", productionProcessActivityDTO="
				+ productionProcessActivityDTO
				+ ", activity_start_work_date_empty="
				+ activity_start_work_date_empty
				+ ", activity_finish_work_date_empty="
				+ activity_finish_work_date_empty + ", assignment_date_empty="
				+ assignment_date_empty + ", getId()=" + getId()
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
		ProductionProcessActivityInstanceDTO other = (ProductionProcessActivityInstanceDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the activity_finish_work_date_empty
	 */
	public Boolean getActivity_finish_work_date_empty() {
		return activity_finish_work_date_empty;
	}

	/**
	 * @param activity_finish_work_date_empty the activity_finish_work_date_empty to set
	 */
	public void setActivity_finish_work_date_empty(
			Boolean activity_finish_work_date_empty) {
		this.activity_finish_work_date_empty = activity_finish_work_date_empty;
	}

	/**
	 * @return the productionProcessActivityDTO
	 */
	public ProductionProcessActivityDTO getProductionProcessActivityDTO() {
		return productionProcessActivityDTO;
	}

	/**
	 * @param productionProcessActivityDTO the productionProcessActivityDTO to set
	 */
	public void setProductionProcessActivityDTO(
			ProductionProcessActivityDTO productionProcessActivityDTO) {
		this.productionProcessActivityDTO = productionProcessActivityDTO;
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
	 * @return the assignment_date_empty
	 */
	public Boolean getAssignment_date_empty() {
		return assignment_date_empty;
	}

	/**
	 * @param assignment_date_empty the assignment_date_empty to set
	 */
	public void setAssignment_date_empty(Boolean assignment_date_empty) {
		this.assignment_date_empty = assignment_date_empty;
	}

	/**
	 * @return the activity_start_work_date_empty
	 */
	public Boolean getActivity_start_work_date_empty() {
		return activity_start_work_date_empty;
	}

	/**
	 * @param activity_start_work_date_empty the activity_start_work_date_empty to set
	 */
	public void setActivity_start_work_date_empty(
			Boolean activity_start_work_date_empty) {
		this.activity_start_work_date_empty = activity_start_work_date_empty;
	}

	/**
	 * @return the parcial_product_recall_date
	 */
	public Date getParcial_product_recall_date() {
		return parcial_product_recall_date;
	}

	/**
	 * @param parcial_product_recall_date the parcial_product_recall_date to set
	 */
	public void setParcial_product_recall_date(Date parcial_product_recall_date) {
		this.parcial_product_recall_date = parcial_product_recall_date;
	}

	/**
	 * @return the recall_locker_number
	 */
	public Long getRecall_locker_number() {
		return recall_locker_number;
	}

	/**
	 * @param recall_locker_number the recall_locker_number to set
	 */
	public void setRecall_locker_number(Long recall_locker_number) {
		this.recall_locker_number = recall_locker_number;
	}

	/**
	 * @return the partial_result_delivery_date
	 */
	public Date getPartial_result_delivery_date() {
		return partial_result_delivery_date;
	}

	/**
	 * @param partial_result_delivery_date the partial_result_delivery_date to set
	 */
	public void setPartial_result_delivery_date(Date partial_result_delivery_date) {
		this.partial_result_delivery_date = partial_result_delivery_date;
	}

	/**
	 * @return the occupied_locker_number
	 */
	public Long getOccupied_locker_number() {
		return occupied_locker_number;
	}

	/**
	 * @param occupied_locker_number the occupied_locker_number to set
	 */
	public void setOccupied_locker_number(Long occupied_locker_number) {
		this.occupied_locker_number = occupied_locker_number;
	}

	/**
	 * @return the or_status
	 */
	public String getOr_status() {
		return or_status;
	}

	/**
	 * @param or_status the or_status to set
	 */
	public void setOr_status(String or_status) {
		this.or_status = or_status;
	}

	/**
	 * @return the listPAIRawMaterialSupplyDTO
	 */
	public List<PAIRawMaterialSupplyDTO> getListPAIRawMaterialSupplyDTO() {
		return listPAIRawMaterialSupplyDTO;
	}

	/**
	 * @param listPAIRawMaterialSupplyDTO the listPAIRawMaterialSupplyDTO to set
	 */
	public void setListPAIRawMaterialSupplyDTO(
			List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO) {
		this.listPAIRawMaterialSupplyDTO = listPAIRawMaterialSupplyDTO;
	}

}
