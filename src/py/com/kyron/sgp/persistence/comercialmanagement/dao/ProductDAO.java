package py.com.kyron.sgp.persistence.comercialmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;


public interface ProductDAO {

	public Long pmsProductIdBySequence();
	public void insertProductDTO(ProductDTO ProductDTO);
	public List<ProductDTO> listProductDTO(ProductDTO ProductoDTO);
}
