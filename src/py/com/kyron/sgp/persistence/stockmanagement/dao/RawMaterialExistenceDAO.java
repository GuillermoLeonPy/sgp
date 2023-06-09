package py.com.kyron.sgp.persistence.stockmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.stockmanagement.domain.InsufficiencyRawMaterialReportDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;

public interface RawMaterialExistenceDAO {
	public Long pmsRawMaterialExistenceDTOIdBySequence();
	public void insertRawMaterialExistenceDTO(RawMaterialExistenceDTO RawMaterialExistenceDTO);
	//public void updateRawMaterialExistenceDTO(RawMaterialExistenceDTO RawMaterialExistenceDTO);
	public List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO(RawMaterialExistenceDTO RawMaterialExistenceDTO);
	public void effectuatePAIRawMaterialSupplyDTO(PAIRawMaterialSupplyDTO PAIRawMaterialSupplyDTO);
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO(PAIRawMaterialSupplyDTO PAIRawMaterialSupplyDTO);
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTOHistory(PAIRawMaterialSupplyDTO PAIRawMaterialSupplyDTO);
	/**/
	public List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO);
	public List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOHistory(PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO);	
	
	public List<InsufficiencyRawMaterialReportDTO> currentInsufficiencyRawMaterialReport(Long id_currency);
}
