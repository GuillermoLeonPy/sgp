package py.com.kyron.sgp.bussines.personmanagement.service.impl;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityPersonRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.persistence.applicationsecurity.dao.ApplicationSecurityDAO;
import py.com.kyron.sgp.persistence.personmanagement.dao.PersonDAO;

public class PersonManagementServiceImpl implements PersonManagementService {

	private final Logger logger = LoggerFactory.getLogger(PersonManagementServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private PersonDAO personDAO;
	private ApplicationSecurityDAO applicationSecurityDAO;
	private ApplicationSecurityService applicationSecurityService;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	
	public PersonManagementServiceImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\nPersonManagementServiceImpl()...");
	}
	
	public void init(){
		this.applicationSecurityService = (ApplicationSecurityService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.APPLICATION_SECURITY_SERVICE);
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
	}

	@Override
	public PersonDTO registerPerson(PersonDTO personDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nregisterPerson\npersonDTO.toString(): " + personDTO.toString()
					+"\npersonDTO.getApplicationSecurityRolDTO() : " + personDTO.getApplicationSecurityRolDTO());
			personDTO.setId(this.personDAO.pmsPersonIdBySequence());
			this.personDAO.insert(personDTO);
			if(/*personDTO.getApplication_user_name()!=null
					&& !personDTO.getApplication_user_name().isEmpty()
					&& personDTO.getApplication_password()!=null
					&& !personDTO.getApplication_password().isEmpty()
					&&*/	personDTO.getApplicationSecurityRolDTO()!=null && personDTO.getApplicationSecurityRolDTO().getId()!=null){
				logger.info("\n********\n********\n*******");
				this.applicationSecurityDAO.insertApplicationSecurityPersonRolDTO(
						new ApplicationSecurityPersonRolDTO(
								this.applicationSecurityDAO.applicationSecurityPersonRolDTOIdBySequence(1L),
								personDTO.getId(),
								personDTO.getApplicationSecurityRolDTO().getId(), true));
			}
			
						
			return personDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromException(e));
		}
	}

	public BussinesSessionUtils getBussinesSessionUtils() {
		return bussinesSessionUtils;
	}

	public void setBussinesSessionUtils(BussinesSessionUtils bussinesSessionUtils) {
		this.bussinesSessionUtils = bussinesSessionUtils;
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Override
	public List<PersonDTO> listPersonDTO(PersonDTO personDTO)throws PmsServiceException {
		// TODO Auto-generated method stub
		try{		
			List<PersonDTO> listPersonDTO = this.personDAO.listPersonDTO(personDTO);
			for(PersonDTO p: listPersonDTO){
				logger.info("\nPersonDTO : \n" + p.toString());
				ApplicationSecurityPersonRolDTO vApplicationSecurityPersonRolDTO = new ApplicationSecurityPersonRolDTO(null,p.getId(), null);
				logger.info("\nApplicationSecurityPersonRolDTO: \n" + vApplicationSecurityPersonRolDTO.toString());				
				List<ApplicationSecurityPersonRolDTO> listApplicationSecurityPersonRolDTO = 
				applicationSecurityDAO.listApplicationSecurityPersonRolDTO(vApplicationSecurityPersonRolDTO);
				if(listApplicationSecurityPersonRolDTO!=null && !listApplicationSecurityPersonRolDTO.isEmpty()){
					/*p.setApplicationSecurityRolDTO(this.applicationSecurityDAO.listApplicationSecurityRolDTO
							(new ApplicationSecurityRolDTO(listApplicationSecurityPersonRolDTO.get(0).getId_app_sec_rol())).get(0));*/
					p.setApplicationSecurityRolDTO(this.applicationSecurityService.listApplicationSecurityRolDTO(
							new ApplicationSecurityRolDTO(listApplicationSecurityPersonRolDTO.get(0).getId_app_sec_rol())).get(0)
							);
					
					
				}
				
			}
			return listPersonDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
						persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromException(e));
		}		
	}

	public ApplicationSecurityDAO getApplicationSecurityDAO() {
		return applicationSecurityDAO;
	}

	public void setApplicationSecurityDAO(
			ApplicationSecurityDAO applicationSecurityDAO) {
		this.applicationSecurityDAO = applicationSecurityDAO;
	}

	@Override
	public PersonDTO personDTOByUsernameAndPassword(String username,
			String password, Locale locale) throws PmsServiceException {
		// TODO Auto-generated method stub
		/*List<PersonDTO> listPersonDTO =  this.personDAO.listPersonDTO(new PersonDTO(username, password));
		if(listPersonDTO!=null && !listPersonDTO.isEmpty())
			return listPersonDTO.get(0);
		return null;*/
		try{		
			List<PersonDTO> listPersonDTO = this.personDAO.listPersonDTO(new PersonDTO(username, password));
			for(PersonDTO p: listPersonDTO){
				logger.info("\nPersonDTO : \n" + p.toString());
				ApplicationSecurityPersonRolDTO vApplicationSecurityPersonRolDTO = new ApplicationSecurityPersonRolDTO(null,p.getId(), null);
				logger.info("\nApplicationSecurityPersonRolDTO: \n" + vApplicationSecurityPersonRolDTO.toString());				
				List<ApplicationSecurityPersonRolDTO> listApplicationSecurityPersonRolDTO = 
				applicationSecurityDAO.listApplicationSecurityPersonRolDTO(vApplicationSecurityPersonRolDTO);
				if(listApplicationSecurityPersonRolDTO!=null && !listApplicationSecurityPersonRolDTO.isEmpty()){
					/*p.setApplicationSecurityRolDTO(this.applicationSecurityDAO.listApplicationSecurityRolDTO
							(new ApplicationSecurityRolDTO(listApplicationSecurityPersonRolDTO.get(0).getId_app_sec_rol())).get(0));*/
					p.setApplicationSecurityRolDTO(this.applicationSecurityService.listApplicationSecurityRolDTO(
							new ApplicationSecurityRolDTO(listApplicationSecurityPersonRolDTO.get(0).getId_app_sec_rol()),locale).get(0)
							);					
				}//if(listApplicationSecurityPersonRolDTO!=null && !listApplicationSecurityPersonRolDTO.isEmpty()){				
			}//for(PersonDTO p: listPersonDTO)
			if(listPersonDTO!=null && !listPersonDTO.isEmpty())
				return listPersonDTO.get(0);
			return null;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
						persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromException(e));
		}		
	}

	@Override
	public PersonDTO updatePerson(PersonDTO personDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nupdatePerson\nhere shoud call the update row for the pms_person table...");

			PersonDTO actualPersonDTO = this.listPersonDTO(new PersonDTO(personDTO.getId())).get(0);
			
			if(personDTO.getApplicationSecurityRolDTO()!=null && personDTO.getApplicationSecurityRolDTO().getId()!=null
			&& actualPersonDTO.getApplicationSecurityRolDTO()!=null
			&& !personDTO.getApplicationSecurityRolDTO().getId().equals(actualPersonDTO.getApplicationSecurityRolDTO().getId())){
				//delete actual
				this.applicationSecurityDAO.deleteApplicationSecurityPersonRolDTO(
						new ApplicationSecurityPersonRolDTO(
								null,
								personDTO.getId(),
								actualPersonDTO.getApplicationSecurityRolDTO().getId(), true));
				
				//insert new
				this.applicationSecurityDAO.insertApplicationSecurityPersonRolDTO(
						new ApplicationSecurityPersonRolDTO(
								this.applicationSecurityDAO.applicationSecurityPersonRolDTOIdBySequence(1L),
								personDTO.getId(),
								personDTO.getApplicationSecurityRolDTO().getId(), true));
			
			}else if(personDTO.getApplicationSecurityRolDTO()!=null && personDTO.getApplicationSecurityRolDTO().getId()!=null
			&& actualPersonDTO.getApplicationSecurityRolDTO()==null){
				//insert new
				this.applicationSecurityDAO.insertApplicationSecurityPersonRolDTO(
						new ApplicationSecurityPersonRolDTO(
								this.applicationSecurityDAO.applicationSecurityPersonRolDTOIdBySequence(1L),
								personDTO.getId(),
								personDTO.getApplicationSecurityRolDTO().getId(), true));				
			}
			logger.info("\n************************************\nWARNING: it is not expected to update a user and remove its role asignment without granting him a new role\n************************************\n");
						
			return personDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromException(e));
		}
	}


}
