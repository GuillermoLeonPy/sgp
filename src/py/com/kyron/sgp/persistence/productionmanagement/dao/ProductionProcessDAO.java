package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;

public interface ProductionProcessDAO {

	public Long pmsProductionProcessDTOIdBySequence();
	public void insertProductionProcessDTO(ProductionProcessDTO ProductionProcessDTO);
	public List<ProductionProcessDTO> listProductionProcessDTO(ProductionProcessDTO ProductionProcessDTO);
}
