package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;

public interface RawMaterialDAO {

	public Long pmsRawMaterialIdBySequence();
	public void insertRawMaterialDTO(RawMaterialDTO RawMaterialDTO);
	List<RawMaterialDTO> listRawMaterialDTO(RawMaterialDTO RawMaterialDTO);
	
	public Long pmsRawMaterialCostDTOIdBySequence();
	public void insertRawMaterialCostDTO(RawMaterialCostDTO RawMaterialCostDTO);
	public void updateRawMaterialCostDTO(RawMaterialCostDTO RawMaterialCostDTO);
	public List<RawMaterialCostDTO> listRawMaterialCostDTO(RawMaterialCostDTO RawMaterialCostDTO);
	public Long getRawMaterialCostValidRowId(Long id_raw_material);
}
