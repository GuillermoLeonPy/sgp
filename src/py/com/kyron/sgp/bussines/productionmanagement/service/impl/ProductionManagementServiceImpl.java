package py.com.kyron.sgp.bussines.productionmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
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
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.persistence.productionmanagement.dao.MachineDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.MachineRequirementDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.ManPowerCostDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.ManPowerRequirementDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.MeasurmentUnitDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.ProductionProcessActivityDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.ProductionProcessDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.RawMaterialDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.RawMaterialRequirementDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.TariffDAO;

public class ProductionManagementServiceImpl implements
		ProductionManagementService {

	private final Logger logger = LoggerFactory.getLogger(ProductionManagementServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private MachineDAO machineDAO;
	private RawMaterialDAO rawMaterialDAO;
	private MeasurmentUnitDAO measurmentUnitDAO;
	private TariffDAO tariffDAO;
	private ManPowerCostDAO manPowerCostDAO;
	private ProductionProcessDAO productionProcessDAO;
	private ProductionProcessActivityDAO productionProcessActivityDAO;
	private RawMaterialRequirementDAO rawMaterialRequirementDAO;
	private ManPowerRequirementDAO manPowerRequirementDAO;
	private MachineRequirementDAO machineRequirementDAO;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	private PersonManagementService personManagementService;
	private StockManagementService stockManagementService;
	
	public ProductionManagementServiceImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\nProductionManagementServiceImpl()...");
	}

	@Override
	public List<MachineDTO> listMachineDTO(MachineDTO machineDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.machineDAO.listMachineDTO(machineDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public BussinesSessionUtils getBussinesSessionUtils() {
		return bussinesSessionUtils;
	}

	public void setBussinesSessionUtils(BussinesSessionUtils bussinesSessionUtils) {
		this.bussinesSessionUtils = bussinesSessionUtils;
	}

	public MachineDAO getMachineDAO() {
		return machineDAO;
	}

	public void setMachineDAO(MachineDAO machineDAO) {
		this.machineDAO = machineDAO;
	}

	@Override
	public MachineDTO insertMachineDTO(MachineDTO machineDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			machineDTO.setId(this.machineDAO.pmsMachineIdBySequence());
			this.machineDAO.insertMachineDTO(machineDTO);
			return machineDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public RawMaterialDAO getRawMaterialDAO() {
		return rawMaterialDAO;
	}

	public void setRawMaterialDAO(RawMaterialDAO rawMaterialDAO) {
		this.rawMaterialDAO = rawMaterialDAO;
	}

	@Override
	public RawMaterialDTO insertRawMaterialDTO(RawMaterialDTO rawMaterialDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			rawMaterialDTO.setId(this.rawMaterialDAO.pmsRawMaterialIdBySequence());
			this.rawMaterialDAO.insertRawMaterialDTO(rawMaterialDTO);
			return rawMaterialDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<RawMaterialDTO> listRawMaterialDTO(RawMaterialDTO rawMaterialDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.rawMaterialDAO.listRawMaterialDTO(rawMaterialDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public MeasurmentUnitDAO getMeasurmentUnitDAO() {
		return measurmentUnitDAO;
	}

	public void setMeasurmentUnitDAO(MeasurmentUnitDAO measurmentUnitDAO) {
		this.measurmentUnitDAO = measurmentUnitDAO;
	}

	@Override
	public MeasurmentUnitDTO insertMeasurmentUnitDTO(
			MeasurmentUnitDTO measurmentUnitDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			measurmentUnitDTO.setId(this.measurmentUnitDAO.pmsMeasurmentUnitIdBySequence());
			this.measurmentUnitDAO.insertMeasurmentUnitDTO(measurmentUnitDTO);
			return measurmentUnitDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<MeasurmentUnitDTO> listMeasurmentUnitDTO(
			MeasurmentUnitDTO measurmentUnitDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.measurmentUnitDAO.listMeasurmentUnitDTO(measurmentUnitDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public TariffDAO getTariffDAO() {
		return tariffDAO;
	}

	public void setTariffDAO(TariffDAO tariffDAO) {
		this.tariffDAO = tariffDAO;
	}

	@Override
	public TariffDTO insertTariffDTO(TariffDTO tariffDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			tariffDTO.setId(this.tariffDAO.pmsTariffDTOIdBySequence());
			this.tariffDAO.insertTariffDTO(tariffDTO);
			return tariffDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<TariffDTO> listTariffDTO(TariffDTO tariffDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.tariffDAO.listTariffDTO(tariffDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ManPowerCostDTO insertManPowerCostDTO(ManPowerCostDTO manPowerCostDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			
			manPowerCostDTO.setId(this.manPowerCostDAO.pmsManPowerCostDTOIdBySequence());
			logger.info("\n check if the values set to the variables in the db function are propagated to the application"
						+"\n============================================================================================="
						+"\nbefore data base function excecution; manPowerCostDTO : \n" + manPowerCostDTO);
			this.manPowerCostDAO.insertManPowerCostDTO(manPowerCostDTO);
			logger.info("\n============================================================================================="
						+"\nafter data base function excecution; manPowerCostDTO : \n" + manPowerCostDTO);
			return manPowerCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ManPowerCostDTO updateManPowerCostDTO(ManPowerCostDTO manPowerCostDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\n check if the values set to the variables in the db function are propagated to the application"
					+"\n============================================================================================="
					+"\nbefore data base function excecution; manPowerCostDTO : \n" + manPowerCostDTO);
			this.manPowerCostDAO.updateManPowerCostDTO(manPowerCostDTO);
			logger.info("\n============================================================================================="
					+"\nafter data base function excecution; manPowerCostDTO : \n" + manPowerCostDTO);
			return manPowerCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ManPowerCostDTO> listManPowerCostDTO(
			ManPowerCostDTO manPowerCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<ManPowerCostDTO> listManPowerCostDTO = this.manPowerCostDAO.listManPowerCostDTO(manPowerCostDTO);
			for(ManPowerCostDTO vManPowerCostDTO : listManPowerCostDTO){
				vManPowerCostDTO.setTariffDTO(
				this.tariffDAO.listTariffDTO(new TariffDTO(vManPowerCostDTO.getId_tariff())).get(0));
			}//for(ManPowerCostDTO vManPowerCostDTO : listManPowerCostDTO){
			return listManPowerCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public ManPowerCostDAO getManPowerCostDAO() {
		return manPowerCostDAO;
	}

	public void setManPowerCostDAO(ManPowerCostDAO manPowerCostDAO) {
		this.manPowerCostDAO = manPowerCostDAO;
	}

	@Override
	public Long getManPowerCostValidRowId() throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			
			return this.manPowerCostDAO.getManPowerCostValidRowId();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public RawMaterialCostDTO insertRawMaterialCostDTO(
			RawMaterialCostDTO rawMaterialCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			rawMaterialCostDTO.setId(this.rawMaterialDAO.pmsRawMaterialCostDTOIdBySequence());
			logger.info("\n check if the values set to the variables in the db function are propagated to the application"
					+"\n============================================================================================="
					+"\nbefore data base function excecution; rawMaterialCostDTO : \n" + rawMaterialCostDTO);
			
			this.rawMaterialDAO.insertRawMaterialCostDTO(rawMaterialCostDTO);
			logger.info("\n============================================================================================="
					+"\nafter data base function excecution; rawMaterialCostDTO : \n" + rawMaterialCostDTO);
			return rawMaterialCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public RawMaterialCostDTO updateRawMaterialCostDTO(
			RawMaterialCostDTO rawMaterialCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\n check if the values set to the variables in the db function are propagated to the application"
					+"\n============================================================================================="
					+"\nbefore data base function excecution; rawMaterialCostDTO : \n" + rawMaterialCostDTO);
			this.rawMaterialDAO.updateRawMaterialCostDTO(rawMaterialCostDTO);
			logger.info("\n============================================================================================="
					+"\nafter data base function excecution; rawMaterialCostDTO : \n" + rawMaterialCostDTO);
			return rawMaterialCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<RawMaterialCostDTO> listRawMaterialCostDTO(
			RawMaterialCostDTO rawMaterialCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<RawMaterialCostDTO> listRawMaterialCostDTO = this.rawMaterialDAO.listRawMaterialCostDTO(rawMaterialCostDTO);
			for(RawMaterialCostDTO vRawMaterialCostDTO: listRawMaterialCostDTO){
				vRawMaterialCostDTO.setTariffDTO(this.tariffDAO.listTariffDTO(new TariffDTO(vRawMaterialCostDTO.getId_tariff())).get(0));
			}
			return listRawMaterialCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long getRawMaterialCostValidRowId(Long id_raw_material)throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.rawMaterialDAO.getRawMaterialCostValidRowId(id_raw_material);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public MachineUseCostDTO insertMachineUseCostDTO(
			MachineUseCostDTO machineUseCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			machineUseCostDTO.setId(this.machineDAO.pmsMachineUseCostDTOIdBySequence());
			this.machineDAO.insertMachineUseCostDTO(machineUseCostDTO);
			return machineUseCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public MachineUseCostDTO updateMachineUseCostDTO(
			MachineUseCostDTO machineUseCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.machineDAO.updateMachineUseCostDTO(machineUseCostDTO);
			return machineUseCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<MachineUseCostDTO> listMachineUseCostDTO(
			MachineUseCostDTO machineUseCostDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<MachineUseCostDTO> listMachineUseCostDTO = this.machineDAO.listMachineUseCostDTO(machineUseCostDTO);
			for(MachineUseCostDTO vMachineUseCostDTO : listMachineUseCostDTO){
				vMachineUseCostDTO.setTariffDTO(this.tariffDAO.listTariffDTO(new TariffDTO(vMachineUseCostDTO.getId_tariff())).get(0));
			}
			return listMachineUseCostDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long getMachineUseCostValidRowId(Long id_machine)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.machineDAO.getMachineUseCostValidRowId(id_machine);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the productionProcessDAO
	 */
	public ProductionProcessDAO getProductionProcessDAO() {
		return productionProcessDAO;
	}

	/**
	 * @param productionProcessDAO the productionProcessDAO to set
	 */
	public void setProductionProcessDAO(ProductionProcessDAO productionProcessDAO) {
		this.productionProcessDAO = productionProcessDAO;
	}

	/**
	 * @return the productionProcessActivityDAO
	 */
	public ProductionProcessActivityDAO getProductionProcessActivityDAO() {
		return productionProcessActivityDAO;
	}

	/**
	 * @param productionProcessActivityDAO the productionProcessActivityDAO to set
	 */
	public void setProductionProcessActivityDAO(
			ProductionProcessActivityDAO productionProcessActivityDAO) {
		this.productionProcessActivityDAO = productionProcessActivityDAO;
	}

	/**
	 * @return the rawMaterialRequirementDAO
	 */
	public RawMaterialRequirementDAO getRawMaterialRequirementDAO() {
		return rawMaterialRequirementDAO;
	}

	/**
	 * @param rawMaterialRequirementDAO the rawMaterialRequirementDAO to set
	 */
	public void setRawMaterialRequirementDAO(
			RawMaterialRequirementDAO rawMaterialRequirementDAO) {
		this.rawMaterialRequirementDAO = rawMaterialRequirementDAO;
	}

	/**
	 * @return the manPowerRequirementDAO
	 */
	public ManPowerRequirementDAO getManPowerRequirementDAO() {
		return manPowerRequirementDAO;
	}

	/**
	 * @param manPowerRequirementDAO the manPowerRequirementDAO to set
	 */
	public void setManPowerRequirementDAO(
			ManPowerRequirementDAO manPowerRequirementDAO) {
		this.manPowerRequirementDAO = manPowerRequirementDAO;
	}

	/**
	 * @return the machineRequirementDAO
	 */
	public MachineRequirementDAO getMachineRequirementDAO() {
		return machineRequirementDAO;
	}

	/**
	 * @param machineRequirementDAO the machineRequirementDAO to set
	 */
	public void setMachineRequirementDAO(MachineRequirementDAO machineRequirementDAO) {
		this.machineRequirementDAO = machineRequirementDAO;
	}

	@Override
	public ProductionProcessDTO insertProductionProcessDTO(
			ProductionProcessDTO productionProcessDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			productionProcessDTO.setId(this.productionProcessDAO.pmsProductionProcessDTOIdBySequence());
			this.productionProcessDAO.insertProductionProcessDTO(productionProcessDTO);
			
			long productionProcessActivityIdForSequence = 0;
			for(ProductionProcessActivityDTO vProductionProcessActivityDTO : productionProcessDTO.getListProductionProcessActivityDTO()){
				vProductionProcessActivityDTO.setId_production_process(productionProcessDTO.getId());
				this.insertProductionProcessActivityDTO(vProductionProcessActivityDTO,productionProcessActivityIdForSequence++);
			}
								
			return productionProcessDTO;
		}catch(Exception e){
			productionProcessDTO.setId(null);
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductionProcessDTO> listProductionProcessDTO(
			ProductionProcessDTO productionProcessDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<ProductionProcessDTO> listProductionProcessDTO = this.productionProcessDAO.listProductionProcessDTO(productionProcessDTO);
			for(ProductionProcessDTO vProductionProcessDTO :  listProductionProcessDTO){
				vProductionProcessDTO.setListProductionProcessActivityDTO(this.listProductionProcessActivityDTOByIdProductionProcess(vProductionProcessDTO.getId()));
			}
			return listProductionProcessDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ProductionProcessActivityDTO insertProductionProcessActivityDTO(
			ProductionProcessActivityDTO productionProcessActivityDTO, long productionProcessActivityIdForSequence)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			
			productionProcessActivityDTO.setId(this.productionProcessActivityDAO.pmsProductionProcessActivityDTOIdBySequence(productionProcessActivityIdForSequence));
			this.productionProcessActivityDAO.insertProductionProcessActivityDTO(productionProcessActivityDTO);
			long rawMaterialRequerimentIdForSequence = 0;
			for(RawMaterialRequirementDTO vRawMaterialRequirementDTO :  productionProcessActivityDTO.getListRawMaterialRequirementDTO()){				
				vRawMaterialRequirementDTO.setId_production_process_activity(productionProcessActivityDTO.getId());
				this.insertRawMaterialRequirementDTO(vRawMaterialRequirementDTO,rawMaterialRequerimentIdForSequence++);
			}
			
			for(MachineRequirementDTO vMachineRequirementDTO : productionProcessActivityDTO.getListMachineRequirementDTO()){
				vMachineRequirementDTO.setId_production_process_activity(productionProcessActivityDTO.getId());
				this.insertMachineRequirementDTO(vMachineRequirementDTO);
				break;//just one element
			}
			
			for(ManPowerRequirementDTO vManPowerRequirementDTO: productionProcessActivityDTO.getListManPowerRequirementDTO()){
				vManPowerRequirementDTO.setId_production_process_activity(productionProcessActivityDTO.getId());
				this.insertManPowerRequirementDTO(vManPowerRequirementDTO);
				break;//just one element
			}
			
			return productionProcessActivityDTO;
		}catch(Exception e){
			productionProcessActivityDTO.setId(null);
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTO(
			ProductionProcessActivityDTO productionProcessActivityDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return null;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductionProcessActivityDTO> listProductionProcessActivityDTOByIdProductionProcess(
			Long id_production_process) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<ProductionProcessActivityDTO> listProductionProcessActivityDTO = this.productionProcessActivityDAO.listProductionProcessActivityDTOByIdProductionProcess(id_production_process);
			for(ProductionProcessActivityDTO vProductionProcessActivityDTO : listProductionProcessActivityDTO){
				vProductionProcessActivityDTO.setListMachineRequirementDTO(this.listMachineRequirementDTO(new MachineRequirementDTO(null, vProductionProcessActivityDTO.getId())));
				vProductionProcessActivityDTO.setListManPowerRequirementDTO(this.listManPowerRequirementDTO(new ManPowerRequirementDTO(null, vProductionProcessActivityDTO.getId())));
				vProductionProcessActivityDTO.setListRawMaterialRequirementDTO(this.listRawMaterialRequirementDTO(new RawMaterialRequirementDTO(null, vProductionProcessActivityDTO.getId())));
			}
			return listProductionProcessActivityDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public RawMaterialRequirementDTO insertRawMaterialRequirementDTO(
			RawMaterialRequirementDTO rawMaterialRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			long id = 0;
			rawMaterialRequirementDTO.setId(this.rawMaterialRequirementDAO.pmsRawMaterialRequirementDTOIdBySequence(id++));
			this.rawMaterialRequirementDAO.insertRawMaterialRequirementDTO(rawMaterialRequirementDTO);
			return rawMaterialRequirementDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private RawMaterialRequirementDTO insertRawMaterialRequirementDTO(
			RawMaterialRequirementDTO rawMaterialRequirementDTO,long id)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			rawMaterialRequirementDTO.setId(this.rawMaterialRequirementDAO.pmsRawMaterialRequirementDTOIdBySequence(id));
			this.rawMaterialRequirementDAO.insertRawMaterialRequirementDTO(rawMaterialRequirementDTO);
			return rawMaterialRequirementDTO;
		}catch(Exception e){
			rawMaterialRequirementDTO.setId(null);
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO(
			RawMaterialRequirementDTO rawMaterialRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO = this.rawMaterialRequirementDAO.listRawMaterialRequirementDTO(rawMaterialRequirementDTO);
			for(RawMaterialRequirementDTO vRawMaterialRequirementDTO : listRawMaterialRequirementDTO){
				vRawMaterialRequirementDTO.setRawMaterialDTO(this.rawMaterialDAO.listRawMaterialDTO(new RawMaterialDTO(vRawMaterialRequirementDTO.getId_raw_material())).get(0));
				vRawMaterialRequirementDTO.setMeasurmentUnitDTO(this.measurmentUnitDAO.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vRawMaterialRequirementDTO.getId_measurment_unit())).get(0));
			}
			return listRawMaterialRequirementDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public MachineRequirementDTO insertMachineRequirementDTO(
			MachineRequirementDTO machineRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			machineRequirementDTO.setId(this.machineRequirementDAO.pmsMachineRequirementDTOIdBySequence());
			this.machineRequirementDAO.insertMachineRequirementDTO(machineRequirementDTO);
			return machineRequirementDTO;
		}catch(Exception e){
			machineRequirementDTO.setId(null);
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<MachineRequirementDTO> listMachineRequirementDTO(
			MachineRequirementDTO machineRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<MachineRequirementDTO> listMachineRequirementDTO = this.machineRequirementDAO.listMachineRequirementDTO(machineRequirementDTO);
			for(MachineRequirementDTO vMachineRequirementDTO : listMachineRequirementDTO){
				vMachineRequirementDTO.setMachineDTO(this.machineDAO.listMachineDTO(new MachineDTO(vMachineRequirementDTO.getId_machine())).get(0));
			}
			return listMachineRequirementDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ManPowerRequirementDTO insertManPowerRequirementDTO(
			ManPowerRequirementDTO manPowerRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			manPowerRequirementDTO.setId(this.manPowerRequirementDAO.pmsManPowerRequirementDTOIdBySequence());
			this.manPowerRequirementDAO.insertManPowerRequirementDTO(manPowerRequirementDTO);
			return manPowerRequirementDTO;
		}catch(Exception e){
			manPowerRequirementDTO.setId(null);
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ManPowerRequirementDTO> listManPowerRequirementDTO(
			ManPowerRequirementDTO manPowerRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.manPowerRequirementDAO.listManPowerRequirementDTO(manPowerRequirementDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ProductionProcessActivityDTO insertRawMaterialRequirementDTOValidate(
			ProductionProcessActivityDTO vProductionProcessActivityDTO,
			RawMaterialRequirementDTO vRawMaterialRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			if(vProductionProcessActivityDTO.getListRawMaterialRequirementDTO() == null)
				vProductionProcessActivityDTO.setListRawMaterialRequirementDTO(new ArrayList<RawMaterialRequirementDTO>());
			else if(vProductionProcessActivityDTO.getListRawMaterialRequirementDTO().contains(vRawMaterialRequirementDTO))
				vProductionProcessActivityDTO.getListRawMaterialRequirementDTO().remove(vRawMaterialRequirementDTO);			
				
				
			for(RawMaterialRequirementDTO regRawMaterialRequirementDTO: vProductionProcessActivityDTO.getListRawMaterialRequirementDTO()){
				if(vRawMaterialRequirementDTO.getRawMaterialDTO().equals(regRawMaterialRequirementDTO.getRawMaterialDTO())
						&&
				vRawMaterialRequirementDTO.getMeasurmentUnitDTO().equals(regRawMaterialRequirementDTO.getMeasurmentUnitDTO()))
				{
					throw new PmsServiceException
					("py.com.kyron.sgp.bussines.productionmanagement.service.impl.production.management.service.impl.validate.raw.material.requeriment.to.add.rawmaterial.measurmentunit.already.exists.error", 
					null, 
					new Object[]
							{vRawMaterialRequirementDTO.getRawMaterialDTO().getRaw_material_id(),
							vRawMaterialRequirementDTO.getMeasurmentUnitDTO().getMeasurment_unit_id(),
							vProductionProcessActivityDTO.getActivity_id()});
				}
			}
			vProductionProcessActivityDTO.getListRawMaterialRequirementDTO().add(vRawMaterialRequirementDTO);
			return vProductionProcessActivityDTO;
		}catch(PmsServiceException e){
			logger.error("\ne.toString() : " + e.toString(), e);
			throw e;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public RawMaterialRequirementDTO rawMaterialRequirementDTOSetEndValidityDate(
			RawMaterialRequirementDTO rawMaterialRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.rawMaterialRequirementDAO.rawMaterialRequirementDTOSetEndValidityDate(rawMaterialRequirementDTO);
			return this.rawMaterialRequirementDAO.listRawMaterialRequirementDTO(rawMaterialRequirementDTO).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public MachineRequirementDTO machineRequirementDTOSetEndValidityDate(
			MachineRequirementDTO machineRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.machineRequirementDAO.machineRequirementDTOSetEndValidityDate(machineRequirementDTO);
			return this.machineRequirementDAO.listMachineRequirementDTO(machineRequirementDTO).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ManPowerRequirementDTO manPowerRequirementDTOSetEndValidityDate(
			ManPowerRequirementDTO manPowerRequirementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.manPowerRequirementDAO.manPowerRequirementDTOSetEndValidityDate(manPowerRequirementDTO);
			return this.manPowerRequirementDAO.listManPowerRequirementDTO(manPowerRequirementDTO).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	public void init(){
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
	}

	@Override
	public List<ProductionProcessActivityInstanceDTO> instantiateProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.productionProcessActivityDAO.instantiateProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO = 
			this.productionProcessActivityDAO.listProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			for(ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO : listProductionProcessActivityInstanceDTO){
				vProductionProcessActivityInstanceDTO.setProductionProcessActivityDTO(
						this.productionProcessActivityDAO.listProductionProcessActivityDTO
						(new ProductionProcessActivityDTO(vProductionProcessActivityInstanceDTO.getId_production_activity())).get(0));
				if(vProductionProcessActivityInstanceDTO.getId_person()!=null)
					vProductionProcessActivityInstanceDTO.setPersonDTO(this.personManagementService.listPersonDTO(new PersonDTO(vProductionProcessActivityInstanceDTO.getId_person())).get(0));				
			}
			return listProductionProcessActivityInstanceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the personManagementService
	 */
	public PersonManagementService getPersonManagementService() {
		return personManagementService;
	}

	/**
	 * @param personManagementService the personManagementService to set
	 */
	public void setPersonManagementService(
			PersonManagementService personManagementService) {
		this.personManagementService = personManagementService;
	}

	@Override
	public ProductionProcessActivityInstanceDTO assignProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.productionProcessActivityDAO.assignProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTO(new ProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long getMachineRequerimentByIdOrderIdProductIdProductionProcessActivity(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.productionProcessActivityDAO.getMachineRequerimentByIdOrderIdProductIdProductionProcessActivity(productionProcessActivityInstanceDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ProductionProcessActivityInstanceDTO finalizeProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.productionProcessActivityDAO.finalizeProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTO(new ProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ProductionProcessActivityInstanceDTO allocateHalfWayProductProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.productionProcessActivityDAO.allocateHalfWayProductProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTOHistory(new ProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ProductionProcessActivityInstanceDTO effectuatePartialProductRecallProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info(	"\n ============================================= "
						+	"\n" + productionProcessActivityInstanceDTO.toString()
						+	"\n ============================================= ");
			this.productionProcessActivityDAO.effectuatePartialProductRecallProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTO(new ProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTOHistory(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTOHistory = 
					this.productionProcessActivityDAO.listProductionProcessActivityInstanceDTOHistory(productionProcessActivityInstanceDTO);
			for(ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO : listProductionProcessActivityInstanceDTOHistory){
				vProductionProcessActivityInstanceDTO.setListPAIRawMaterialSupplyDTO(
				this.stockManagementService.listPAIRawMaterialSupplyDTOHistory(new PAIRawMaterialSupplyDTO(null, vProductionProcessActivityInstanceDTO.getId()))
				);
				vProductionProcessActivityInstanceDTO.setProductionProcessActivityDTO(
						this.productionProcessActivityDAO.listProductionProcessActivityDTO
						(new ProductionProcessActivityDTO(vProductionProcessActivityInstanceDTO.getId_production_activity())).get(0));
				if(vProductionProcessActivityInstanceDTO.getId_person()!=null)
					vProductionProcessActivityInstanceDTO.setPersonDTO(this.personManagementService.listPersonDTO(new PersonDTO(vProductionProcessActivityInstanceDTO.getId_person())).get(0));
			}
			return listProductionProcessActivityInstanceDTOHistory;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the stockManagementService
	 */
	public StockManagementService getStockManagementService() {
		return stockManagementService;
	}

	/**
	 * @param stockManagementService the stockManagementService to set
	 */
	public void setStockManagementService(
			StockManagementService stockManagementService) {
		this.stockManagementService = stockManagementService;
	}

	@Override
	public ProductionProcessActivityInstanceDTO deliversFinalProductProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.productionProcessActivityDAO.deliversFinalProductProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
			return this.listProductionProcessActivityInstanceDTOHistory(new ProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
}
