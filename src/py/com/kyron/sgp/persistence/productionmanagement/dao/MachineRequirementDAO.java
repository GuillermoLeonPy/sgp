package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;

public interface MachineRequirementDAO {

	public Long pmsMachineRequirementDTOIdBySequence();
	public void insertMachineRequirementDTO(MachineRequirementDTO MachineRequirementDTO);
	public void machineRequirementDTOSetEndValidityDate(MachineRequirementDTO MachineRequirementDTO);
	public List<MachineRequirementDTO> listMachineRequirementDTO(MachineRequirementDTO MachineRequirementDTO);
}
