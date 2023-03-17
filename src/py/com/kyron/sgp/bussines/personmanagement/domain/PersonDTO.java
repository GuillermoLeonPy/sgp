package py.com.kyron.sgp.bussines.personmanagement.domain;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class PersonDTO extends GenericDTO {

	@Pattern(regexp = "^[0-9]+(-?[0-9]+)+$")
	@Size(min=6, max=20)
	private String ruc;
	
	@Pattern(regexp = "^[0-9]+(-?[0-9]+)+$")
	@Size(min=6, max=20)
	private String personal_telephone_number;
	
	//@Pattern(regexp = "^[A-Za-z]")
	@Size(min=6, max=50)
	private String personal_name;
	
	//@Pattern(regexp = "^[A-Za-z]")
	@Size(min=6, max=50)
	private String personal_last_name;
	
	@Email
	@Size(min=6, max=50)
	private String personal_email_address;
	
	@Range(min=800000L,max=999999999L)
	private Long personal_civil_id_document;
	
	//@Pattern(regexp = "^[A-Za-z]")
	@Size(min=6, max=250)
	private String personal_address;
	
	private Boolean is_supplier;
	private Boolean is_customer;
	private Boolean is_functionary;
	
	//@Pattern(regexp = "^[-a-zA-Z_:,.' ']")
	@Size(min=6, max=250)	
	private String commercial_name;
	
	//@Pattern(regexp = "^[A-Za-z]")
	@Size(min=5, max=16)
	private String application_user_name;
	//@Pattern(regexp = "^[A-Za-z]")
	@Size(min=6, max=8)
	private String application_password;	
	private String application_credentials_editable;
	
	private ApplicationSecurityRolDTO applicationSecurityRolDTO;
	
	public PersonDTO() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param application_user_name
	 * @param application_password
	 */
	public PersonDTO(String application_user_name, String application_password) {
		super();
		this.application_user_name = application_user_name;
		this.application_password = application_password;
	}

	public PersonDTO(boolean is_supplier, boolean is_customer, boolean is_functionary) {
		// TODO Auto-generated constructor stub
		this.is_supplier = is_supplier;
		this.is_customer = is_customer;
		this.is_functionary = is_functionary;
	}
	
	public PersonDTO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		super.setId(id);
	}
	
	public PersonDTO(Long id, String creation_user, Date creation_date,	String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,last_modif_date);
		// TODO Auto-generated constructor stub
	}


	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getPersonal_telephone_number() {
		return personal_telephone_number;
	}

	public void setPersonal_telephone_number(String personal_telephone_number) {
		this.personal_telephone_number = personal_telephone_number;
	}

	public String getPersonal_name() {
		return personal_name;
	}

	public void setPersonal_name(String personal_name) {
		this.personal_name = personal_name;
	}

	public String getPersonal_last_name() {
		return personal_last_name;
	}

	public void setPersonal_last_name(String personal_last_name) {
		this.personal_last_name = personal_last_name;
	}

	public String getPersonal_email_address() {
		return personal_email_address;
	}

	public void setPersonal_email_address(String personal_email_address) {
		this.personal_email_address = personal_email_address;
	}

	public Long getPersonal_civil_id_document() {
		return personal_civil_id_document;
	}

	public void setPersonal_civil_id_document(Long personal_civil_id_document) {
		this.personal_civil_id_document = personal_civil_id_document;
	}

	public String getPersonal_address() {
		return personal_address;
	}

	public void setPersonal_address(String personal_address) {
		this.personal_address = personal_address;
	}

	public Boolean getIs_supplier() {
		return is_supplier;
	}

	public void setIs_supplier(Boolean is_supplier) {
		this.is_supplier = is_supplier;
	}

	public Boolean getIs_customer() {
		return is_customer;
	}

	public void setIs_customer(Boolean is_customer) {
		this.is_customer = is_customer;
	}



	public String getCommercial_name() {
		return commercial_name;
	}

	public void setCommercial_name(String commercial_name) {
		this.commercial_name = commercial_name;
	}

	public String getApplication_user_name() {
		return application_user_name;
	}

	public void setApplication_user_name(String application_user_name) {
		this.application_user_name = application_user_name;
	}

	public String getApplication_password() {
		return application_password;
	}

	public void setApplication_password(String application_password) {
		this.application_password = application_password;
	}

	public String getApplication_credentials_editable() {
		return application_credentials_editable;
	}

	public void setApplication_credentials_editable(
			String application_credentials_editable) {
		this.application_credentials_editable = application_credentials_editable;
	}

	public Boolean getIs_functionary() {
		return is_functionary;
	}

	public void setIs_functionary(Boolean is_functionary) {
		this.is_functionary = is_functionary;
	}

	@Override
	public String toString() {
		return "PersonDTO [ruc=" + ruc + ", personal_telephone_number="
				+ personal_telephone_number + ", personal_name="
				+ personal_name + ", personal_last_name=" + personal_last_name
				+ ", personal_email_address=" + personal_email_address
				+ ", personal_civil_id_document=" + personal_civil_id_document
				+ ", personal_address=" + personal_address + ", is_supplier="
				+ is_supplier + ", is_customer=" + is_customer
				+ ", is_functionary=" + is_functionary + ", commercial_name="
				+ commercial_name + ", application_user_name="
				+ application_user_name + ", application_password="
				+ application_password + ", application_credentials_editable="
				+ application_credentials_editable + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public ApplicationSecurityRolDTO getApplicationSecurityRolDTO() {
		return applicationSecurityRolDTO;
	}

	public void setApplicationSecurityRolDTO(
			ApplicationSecurityRolDTO applicationSecurityRolDTO) {
		this.applicationSecurityRolDTO = applicationSecurityRolDTO;
	}



}
