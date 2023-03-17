/**
 * 
 */
package py.com.kyron.sgp.bussines.comercialmanagement.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class ProductDTO extends GenericDTO {
	@Size(min=9, max=100)
	private String product_id;
	@Size(min=9, max=250)
	private String product_description;
	@Range(min=0L,max=900)
	private Long increase_over_cost_for_sale_price;
	private Boolean has_production_process;
	@Range(min=1L,max=250L)
	private Long orderItemQuantity;
	/**
	 * 
	 */
	public ProductDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public ProductDTO(Long id,Long orderItemQuantity) {
		super();
		this.setId(id);
		this.orderItemQuantity = orderItemQuantity;
	}
	
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public ProductDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductDTO [product_id=" + product_id
				+ ", product_description=" + product_description
				+ ", has_production_process=" + has_production_process
				+ ", orderItemQuantity=" + orderItemQuantity + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the has_production_process
	 */
	public Boolean getHas_production_process() {
		return has_production_process;
	}

	/**
	 * @param has_production_process the has_production_process to set
	 */
	public void setHas_production_process(Boolean has_production_process) {
		this.has_production_process = has_production_process;
	}

	/**
	 * @return the orderItemQuantity
	 */
	public Long getOrderItemQuantity() {
		return orderItemQuantity;
	}

	/**
	 * @param orderItemQuantity the orderItemQuantity to set
	 */
	public void setOrderItemQuantity(Long orderItemQuantity) {
		this.orderItemQuantity = orderItemQuantity;
	}

	/**
	 * @return the increase_over_cost_for_sale_price
	 */
	public Long getIncrease_over_cost_for_sale_price() {
		return increase_over_cost_for_sale_price;
	}

	/**
	 * @param increase_over_cost_for_sale_price the increase_over_cost_for_sale_price to set
	 */
	public void setIncrease_over_cost_for_sale_price(
			Long increase_over_cost_for_sale_price) {
		this.increase_over_cost_for_sale_price = increase_over_cost_for_sale_price;
	}

}
