package py.com.kyron.sgp.persistence.stockmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;

public interface RawMaterialPurchaseRequestDAO {
	
	public Long pmsRawMaterialPurchaseRequestDTOIdBySequence();
	public void insertRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO RawMaterialPurchaseRequestDTO);
	public void updateRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO RawMaterialPurchaseRequestDTO);
	public List<RawMaterialPurchaseRequestDTO> listRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO RawMaterialPurchaseRequestDTO);

}
