package py.com.kyron.sgp.bussines.productionmanagement.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineUseCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;

public interface ProductionManagementService {

	/*machine methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MachineDTO insertMachineDTO(MachineDTO machineDTO)throws PmsServiceException;	
	public List<MachineDTO> listMachineDTO(MachineDTO machineDTO)throws PmsServiceException ;
	
	/*raw material methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialDTO insertRawMaterialDTO(RawMaterialDTO rawMaterialDTO)throws PmsServiceException ;
	public List<RawMaterialDTO> listRawMaterialDTO(RawMaterialDTO rawMaterialDTO)throws PmsServiceException ;
	
	/*measurment unit methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MeasurmentUnitDTO insertMeasurmentUnitDTO(MeasurmentUnitDTO measurmentUnitDTO)throws PmsServiceException ;
	public List<MeasurmentUnitDTO> listMeasurmentUnitDTO(MeasurmentUnitDTO measurmentUnitDTO)throws PmsServiceException ;
	
	/*tariff methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public TariffDTO insertTariffDTO(TariffDTO tariffDTO)throws PmsServiceException ;
	public List<TariffDTO> listTariffDTO(TariffDTO tariffDTO)throws PmsServiceException ;
	
	/*man power cost methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ManPowerCostDTO insertManPowerCostDTO(ManPowerCostDTO manPowerCostDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ManPowerCostDTO updateManPowerCostDTO(ManPowerCostDTO manPowerCostDTO)throws PmsServiceException ;
	public List<ManPowerCostDTO> listManPowerCostDTO(ManPowerCostDTO manPowerCostDTO)throws PmsServiceException ;
	public Long getManPowerCostValidRowId()throws PmsServiceException ;

	/*raw material cost methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialCostDTO insertRawMaterialCostDTO(RawMaterialCostDTO rawMaterialCostDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialCostDTO updateRawMaterialCostDTO(RawMaterialCostDTO rawMaterialCostDTO)throws PmsServiceException ;
	public List<RawMaterialCostDTO> listRawMaterialCostDTO(RawMaterialCostDTO rawMaterialCostDTO)throws PmsServiceException ;
	public Long getRawMaterialCostValidRowId(Long id_raw_material)throws PmsServiceException;
	
	
	/*machine use cost methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MachineUseCostDTO insertMachineUseCostDTO(MachineUseCostDTO machineUseCostDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MachineUseCostDTO updateMachineUseCostDTO(MachineUseCostDTO machineUseCostDTO)throws PmsServiceException ;
	public List<MachineUseCostDTO> listMachineUseCostDTO(MachineUseCostDTO machineUseCostDTO)throws PmsServiceException ;
	public Long getMachineUseCostValidRowId(Long id_machine)throws PmsServiceException ;
	
	/* production process methods */
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessDTO insertProductionProcessDTO(ProductionProcessDTO productionProcessDTO)throws PmsServiceException ;
	public List<ProductionProcessDTO> listProductionProcessDTO(ProductionProcessDTO productionProcessDTO)throws PmsServiceException ;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityDTO insertProductionProcessActivityDTO(ProductionProcessActivityDTO productionProcessActivityDTO, long productionProcessActivityIdForSequence)throws PmsServiceException ;
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTO(ProductionProcessActivityDTO productionProcessActivityDTO) throws PmsServiceException ;
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTOByIdProductionProcess(Long id_production_process)throws PmsServiceException ;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialRequirementDTO insertRawMaterialRequirementDTO(RawMaterialRequirementDTO rawMaterialRequirementDTO)throws PmsServiceException ;
	public ProductionProcessActivityDTO insertRawMaterialRequirementDTOValidate(ProductionProcessActivityDTO vProductionProcessActivityDTO,RawMaterialRequirementDTO vRawMaterialRequirementDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialRequirementDTO rawMaterialRequirementDTOSetEndValidityDate(RawMaterialRequirementDTO rawMaterialRequirementDTO)throws PmsServiceException ;
	
	public List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO(RawMaterialRequirementDTO rawMaterialRequirementDTO)throws PmsServiceException ;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MachineRequirementDTO insertMachineRequirementDTO(MachineRequirementDTO machineRequirementDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public MachineRequirementDTO machineRequirementDTOSetEndValidityDate(MachineRequirementDTO machineRequirementDTO)throws PmsServiceException ;
	public List<MachineRequirementDTO> listMachineRequirementDTO(MachineRequirementDTO machineRequirementDTO)throws PmsServiceException ;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ManPowerRequirementDTO insertManPowerRequirementDTO(ManPowerRequirementDTO manPowerRequirementDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ManPowerRequirementDTO manPowerRequirementDTOSetEndValidityDate(ManPowerRequirementDTO manPowerRequirementDTO)throws PmsServiceException ;
	public List<ManPowerRequirementDTO> listManPowerRequirementDTO(ManPowerRequirementDTO manPowerRequirementDTO)throws PmsServiceException ;
	
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public List<ProductionProcessActivityInstanceDTO> instantiateProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityInstanceDTO assignProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;	
	public Long getMachineRequerimentByIdOrderIdProductIdProductionProcessActivity(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityInstanceDTO finalizeProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityInstanceDTO allocateHalfWayProductProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityInstanceDTO effectuatePartialProductRecallProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTOHistory(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ProductionProcessActivityInstanceDTO deliversFinalProductProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)throws PmsServiceException ;
}
