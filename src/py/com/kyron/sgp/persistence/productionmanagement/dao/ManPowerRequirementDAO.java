package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;

public interface ManPowerRequirementDAO {

	public Long pmsManPowerRequirementDTOIdBySequence();
	public void insertManPowerRequirementDTO(ManPowerRequirementDTO ManPowerRequirementDTO);
	public void manPowerRequirementDTOSetEndValidityDate(ManPowerRequirementDTO ManPowerRequirementDTO);
	public List<ManPowerRequirementDTO> listManPowerRequirementDTO(ManPowerRequirementDTO ManPowerRequirementDTO);
}
