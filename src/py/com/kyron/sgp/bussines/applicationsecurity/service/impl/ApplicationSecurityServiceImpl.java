package py.com.kyron.sgp.bussines.applicationsecurity.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;
import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityPersonRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.persistence.applicationsecurity.dao.ApplicationSecurityDAO;

public class ApplicationSecurityServiceImpl implements ApplicationSecurityService {

	
	private final Logger logger = LoggerFactory.getLogger(ApplicationSecurityServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private ApplicationSecurityDAO applicationSecurityDAO;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	
	
	public ApplicationSecurityServiceImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\nApplicationSecurityServiceImpl()...");
	}

	@Override
	public ApplicationSecurityProgramDTO insertApplicationSecurityProgramDTO(
			ApplicationSecurityProgramDTO applicationSecurityProgramDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
				
				List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO = null;
				long id = 0;
				Object[] applicationMessagesKeys = this.bussinesSessionUtils.getApplicationUtils().getApplicationMessagesKeys();
				for(Object key: applicationMessagesKeys){
					
					if(
					(key.toString().contains("main.menu.module.") 
					&& key.toString().contains("management.")
					&& key.toString().substring(key.toString().indexOf("management.")).length() > "management.".length())
					||
					(key.toString().startsWith("secured.access.program."))
					){
						applicationSecurityProgramDTO = new ApplicationSecurityProgramDTO();
						applicationSecurityProgramDTO.setProgram_key(key.toString());
						listApplicationSecurityProgramDTO = this.applicationSecurityDAO.listApplicationSecurityProgramDTO(applicationSecurityProgramDTO);
						
						if(listApplicationSecurityProgramDTO == null || listApplicationSecurityProgramDTO.isEmpty()){
							//logger.info("\nthis.applicationSecurityDAO.applicationSecurityProgramDTOIdBySequence() : " + this.applicationSecurityDAO.applicationSecurityProgramDTOIdBySequence((new Date()).getTime()));
							
							
							applicationSecurityProgramDTO.setId(this.applicationSecurityDAO.applicationSecurityProgramDTOIdBySequence(id));
							id++;
							logger.info("\ninserting program key: " + key);
							this.applicationSecurityDAO.insertApplicationSecurityProgramDTO(applicationSecurityProgramDTO);
						}//if(listApplicationSecurityProgramDTO == null || listApplicationSecurityProgramDTO.isEmpty()){
						listApplicationSecurityProgramDTO = null;
					}
				}//for(Object key: applicationMessagesKeys)
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
		return null;
	}

	@Override
	public ApplicationSecurityRolDTO insertApplicationSecurityRolDTO(
			ApplicationSecurityRolDTO applicationSecurityRolDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			applicationSecurityRolDTO.setId(this.applicationSecurityDAO.applicationSecurityRolDTOIdBySequence(1L));
			this.applicationSecurityDAO.insertApplicationSecurityRolDTO(applicationSecurityRolDTO);
			int counter = 0;
			for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
				
				this.applicationSecurityDAO.insertApplicationSecurityRolProgramDTO(
								new ApplicationSecurityRolProgramDTO(
								this.applicationSecurityDAO.applicationSecurityProgramDTOIdBySequence(counter++),				
								applicationSecurityRolDTO.getId(),
								vApplicationSecurityProgramDTO.getId(), true)
								);
				
			}
			
			return applicationSecurityRolDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ApplicationSecurityRolProgramDTO insertApplicationSecurityRolProgramDTO(
			ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationSecurityPersonRolDTO insertApplicationSecurityPersonRolDTO(
			ApplicationSecurityPersonRolDTO applicationSecurityPersonRolDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BussinesSessionUtils getBussinesSessionUtils() {
		// TODO Auto-generated method stub
		return this.bussinesSessionUtils;
	}

	public ApplicationSecurityDAO getApplicationSecurityDAO() {
		return applicationSecurityDAO;
	}

	public void setApplicationSecurityDAO(
			ApplicationSecurityDAO applicationSecurityDAO) {
		this.applicationSecurityDAO = applicationSecurityDAO;
	}

	public void setBussinesSessionUtils(BussinesSessionUtils bussinesSessionUtils) {
		this.bussinesSessionUtils = bussinesSessionUtils;
	}

	@Override
	public List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO(
			ApplicationSecurityProgramDTO applicationSecurityProgramDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			if(applicationSecurityProgramDTO==null)applicationSecurityProgramDTO = new ApplicationSecurityProgramDTO();
			List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO =
					this.applicationSecurityDAO.listApplicationSecurityProgramDTO(applicationSecurityProgramDTO);
			this.setApplicationProgramKeyValues(listApplicationSecurityProgramDTO);
		return listApplicationSecurityProgramDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
						persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}	
	}

	private void setApplicationProgramKeyValues(List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO) throws Exception{
		if(listApplicationSecurityProgramDTO!=null)
		for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : listApplicationSecurityProgramDTO){
			vApplicationSecurityProgramDTO.setProgram_key_value(
					this.bussinesSessionUtils.getApplicationUtils().
						getMessageByKey(vApplicationSecurityProgramDTO.getProgram_key(),
										this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())
					);
			logger.info("\nvApplicationSecurityProgramDTO : " + vApplicationSecurityProgramDTO);
		}//for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : listApplicationSecurityProgramDTO){		
	}//private void setApplicationProgramKeyValues(List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO){

	private void setApplicationProgramKeyValues(List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO, Locale locale) throws Exception{
		if(listApplicationSecurityProgramDTO!=null)
		for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : listApplicationSecurityProgramDTO){
			vApplicationSecurityProgramDTO.setProgram_key_value(
					this.bussinesSessionUtils.getApplicationUtils().
						getMessageByKey(vApplicationSecurityProgramDTO.getProgram_key(),locale)
					);
			logger.info("\nvApplicationSecurityProgramDTO : " + vApplicationSecurityProgramDTO);
		}//for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : listApplicationSecurityProgramDTO){		
	}//private void setApplicationProgramKeyValues(List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO){
	
	@Override
	public List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO(
			ApplicationSecurityRolDTO applicationSecurityRolDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			if(applicationSecurityRolDTO==null)applicationSecurityRolDTO = new ApplicationSecurityRolDTO(); 
			List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO =this.applicationSecurityDAO.listApplicationSecurityRolDTO(applicationSecurityRolDTO);
			
			for(ApplicationSecurityRolDTO vApplicationSecurityRolDTO : listApplicationSecurityRolDTO){
				List<ApplicationSecurityRolProgramDTO> listApplicationSecurityRolProgramDTO = 
				this.applicationSecurityDAO.listApplicationSecurityRolProgramDTO(new ApplicationSecurityRolProgramDTO(vApplicationSecurityRolDTO.getId(),null,null));
				
				for(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO : listApplicationSecurityRolProgramDTO){
					if(vApplicationSecurityRolDTO.getRoleProgramList() == null ) vApplicationSecurityRolDTO.setRoleProgramList(new ArrayList<ApplicationSecurityProgramDTO>());
					List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO = this.applicationSecurityDAO.listApplicationSecurityProgramDTO(new ApplicationSecurityProgramDTO(applicationSecurityRolProgramDTO.getId_app_sec_program()));
					if(listApplicationSecurityProgramDTO!=null)
						vApplicationSecurityRolDTO.getRoleProgramList().addAll(listApplicationSecurityProgramDTO);
				}//for(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO : listApplicationSecurityRolProgramDTO){
				this.setApplicationProgramKeyValues(vApplicationSecurityRolDTO.getRoleProgramList());
			}//for(ApplicationSecurityRolDTO vApplicationSecurityRolDTO : listApplicationSecurityRolDTO)
			
			return listApplicationSecurityRolDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
						persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ApplicationSecurityRolProgramDTO> listApplicationSecurityRolProgramDTO(
			ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO)
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
	public List<ApplicationSecurityPersonRolDTO> listApplicationSecurityPersonRolDTO(
			ApplicationSecurityPersonRolDTO applicationSecurityPersonRolDTO)
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
	public List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO(
			ApplicationSecurityRolDTO applicationSecurityRolDTO, Locale locale)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			if(applicationSecurityRolDTO==null)applicationSecurityRolDTO = new ApplicationSecurityRolDTO(); 
			List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO =this.applicationSecurityDAO.listApplicationSecurityRolDTO(applicationSecurityRolDTO);
			
			for(ApplicationSecurityRolDTO vApplicationSecurityRolDTO : listApplicationSecurityRolDTO){
				List<ApplicationSecurityRolProgramDTO> listApplicationSecurityRolProgramDTO = 
				this.applicationSecurityDAO.listApplicationSecurityRolProgramDTO(new ApplicationSecurityRolProgramDTO(vApplicationSecurityRolDTO.getId(),null,null));
				
				for(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO : listApplicationSecurityRolProgramDTO){
					if(vApplicationSecurityRolDTO.getRoleProgramList() == null ) vApplicationSecurityRolDTO.setRoleProgramList(new ArrayList<ApplicationSecurityProgramDTO>());
					List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO = this.applicationSecurityDAO.listApplicationSecurityProgramDTO(new ApplicationSecurityProgramDTO(applicationSecurityRolProgramDTO.getId_app_sec_program()));
					if(listApplicationSecurityProgramDTO!=null)
						vApplicationSecurityRolDTO.getRoleProgramList().addAll(listApplicationSecurityProgramDTO);
				}//for(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO : listApplicationSecurityRolProgramDTO){
				this.setApplicationProgramKeyValues(vApplicationSecurityRolDTO.getRoleProgramList(), locale);
			}//for(ApplicationSecurityRolDTO vApplicationSecurityRolDTO : listApplicationSecurityRolDTO)
			
			return listApplicationSecurityRolDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
						persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public ApplicationSecurityRolDTO updateApplicationSecurityRolDTO(
			ApplicationSecurityRolDTO applicationSecurityRolDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nupdateApplicationSecurityRolDTO\nhere shoud call the update row for the app_sec_rol table...");
			this.printApplicationSecurityRolDTOToUpdate(applicationSecurityRolDTO);
						
			ApplicationSecurityRolDTO actualApplicationSecurityRolDTO = 
					this.listApplicationSecurityRolDTO(
							new ApplicationSecurityRolDTO(applicationSecurityRolDTO.getId())).get(0);		
			logger.info("\ndeleteActualProgramsNotInNewList\n----------------------------");
			this.deleteActualProgramsNotInNewList(applicationSecurityRolDTO, actualApplicationSecurityRolDTO);
			logger.info("\ninsertNewProgramsNotInActualListn----------------------------");
			this.insertNewProgramsNotInActualList(applicationSecurityRolDTO, actualApplicationSecurityRolDTO);
			
			return applicationSecurityRolDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private void printApplicationSecurityRolDTOToUpdate(ApplicationSecurityRolDTO applicationSecurityRolDTO){
		logger.info("\nprintApplicationSecurityRolDTOToUpdate"
				+	"\n======================================");
		logger.info("\napplicationSecurityRolDTO : \n"  + applicationSecurityRolDTO
				+"\nprogram list"
				+"\n============");
		for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
			logger.info("\nvApplicationSecurityProgramDTO\n" + vApplicationSecurityProgramDTO);
		}
	}

	private void deleteActualProgramsNotInNewList(ApplicationSecurityRolDTO applicationSecurityRolDTO, ApplicationSecurityRolDTO actualApplicationSecurityRolDTO) throws PmsServiceException{
		logger.info("\ndeleteActualProgramsNotInNewList"
				+	"\n================================");
		for(ApplicationSecurityProgramDTO actualApplicationSecurityProgramDTO : actualApplicationSecurityRolDTO.getRoleProgramList()){
			boolean isInNewProgramList = false;
			for(ApplicationSecurityProgramDTO toInsertApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
				if(actualApplicationSecurityProgramDTO.getId().equals(toInsertApplicationSecurityProgramDTO.getId())){
					isInNewProgramList = true;
					break;
				}
			}//for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
			if(!isInNewProgramList){
				//delete
				logger.info("\ndeleting...\nactualApplicationSecurityProgramDTO : " + actualApplicationSecurityProgramDTO);
				this.applicationSecurityDAO.deleteApplicationSecurityRolProgramDTO(
						new ApplicationSecurityRolProgramDTO(
								null,				
								applicationSecurityRolDTO.getId(),
								actualApplicationSecurityProgramDTO.getId(), null)
								);
				
			}//if(!isInNewProgramList)
		}//for(ApplicationSecurityProgramDTO actualApplicationSecurityProgramDTO : actualApplicationSecurityRolDTO.getRoleProgramList())
	}//private void deleteActualProgramsNotInNewList(ApplicationSecurityRolDTO applicationSecurityRolDTO) throws PmsServiceException{

	
	private void insertNewProgramsNotInActualList(ApplicationSecurityRolDTO applicationSecurityRolDTO, ApplicationSecurityRolDTO actualApplicationSecurityRolDTO) throws PmsServiceException{
		logger.info("\ninsertNewProgramsNotInActualList"
				+	"\n================================");
		int counter = 0;
		for(ApplicationSecurityProgramDTO toInsertApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
			boolean isInNewActualList = false;
			for(ApplicationSecurityProgramDTO actualApplicationSecurityProgramDTO : actualApplicationSecurityRolDTO.getRoleProgramList()){
				if(actualApplicationSecurityProgramDTO.getId().equals(toInsertApplicationSecurityProgramDTO.getId())){
					isInNewActualList = true;
					break;
				}				
			}//for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : applicationSecurityRolDTO.getRoleProgramList()){
			if(!isInNewActualList){
				//insert
				logger.info("\ninserting...\ntoInsertApplicationSecurityProgramDTO : " + toInsertApplicationSecurityProgramDTO);
				this.applicationSecurityDAO.insertApplicationSecurityRolProgramDTO(
						new ApplicationSecurityRolProgramDTO(
						this.applicationSecurityDAO.applicationSecurityProgramDTOIdBySequence(counter++),				
						applicationSecurityRolDTO.getId(),
						toInsertApplicationSecurityProgramDTO.getId(), true)
						);
			}//if(!isInNewActualList){
		}//for(ApplicationSecurityProgramDTO actualApplicationSecurityProgramDTO : actualApplicationSecurityRolDTO.getRoleProgramList())
	}
	
	public void init(){
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
	}
}
