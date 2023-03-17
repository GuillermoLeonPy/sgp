package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;

public interface RawMaterialRequirementDAO {

	public Long pmsRawMaterialRequirementDTOIdBySequence(long id);
	public void insertRawMaterialRequirementDTO(RawMaterialRequirementDTO RawMaterialRequirementDTO);
	public void rawMaterialRequirementDTOSetEndValidityDate(RawMaterialRequirementDTO RawMaterialRequirementDTO);
	public List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO(RawMaterialRequirementDTO RawMaterialRequirementDTO);
}
