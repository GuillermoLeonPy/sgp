package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;

public interface ProductionProcessActivityDAO {

	public Long pmsProductionProcessActivityDTOIdBySequence(long id);
	public void insertProductionProcessActivityDTO(ProductionProcessActivityDTO ProductionProcessActivityDTO);
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTO(ProductionProcessActivityDTO ProductionProcessActivityDTO);
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTOByIdProductionProcess(Long id_production_process);
	/* ProductionProcessActivityInstanceDTO operations*/
	public void instantiateProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public void assignProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public Long getMachineRequerimentByIdOrderIdProductIdProductionProcessActivity(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public void finalizeProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public Long allocateHalfWayProductProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public void effectuatePartialProductRecallProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTOHistory(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
	public void deliversFinalProductProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO ProductionProcessActivityInstanceDTO);
}
