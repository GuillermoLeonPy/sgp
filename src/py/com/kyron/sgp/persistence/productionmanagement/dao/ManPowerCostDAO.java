package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;

public interface ManPowerCostDAO {
	public Long pmsManPowerCostDTOIdBySequence();
	public void insertManPowerCostDTO(ManPowerCostDTO ManPowerCostDTO);
	public void updateManPowerCostDTO(ManPowerCostDTO ManPowerCostDTO);
	public List<ManPowerCostDTO> listManPowerCostDTO(ManPowerCostDTO ManPowerCostDTO);
	public Long getManPowerCostValidRowId();
}
