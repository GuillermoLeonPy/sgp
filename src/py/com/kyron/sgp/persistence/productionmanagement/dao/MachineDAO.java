package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineUseCostDTO;

public interface MachineDAO {

	public Long pmsMachineIdBySequence();
	public void insertMachineDTO(MachineDTO MachineDTO);
	public List<MachineDTO> listMachineDTO(MachineDTO MachineDTO);
	
	public Long pmsMachineUseCostDTOIdBySequence();
	public void insertMachineUseCostDTO(MachineUseCostDTO MachineUseCostDTO);
	public void updateMachineUseCostDTO(MachineUseCostDTO MachineUseCostDTO);
	public List<MachineUseCostDTO> listMachineUseCostDTO(MachineUseCostDTO MachineUseCostDTO);
	public Long getMachineUseCostValidRowId(Long id_machine);
}
