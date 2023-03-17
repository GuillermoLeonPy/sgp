package py.com.kyron.sgp.bussines.applicationsecurity.service;

import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityPersonRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolProgramDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;

public interface ApplicationSecurityService {
	//insert
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ApplicationSecurityProgramDTO insertApplicationSecurityProgramDTO(ApplicationSecurityProgramDTO applicationSecurityProgramDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ApplicationSecurityRolDTO insertApplicationSecurityRolDTO(ApplicationSecurityRolDTO applicationSecurityRolDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ApplicationSecurityRolProgramDTO insertApplicationSecurityRolProgramDTO(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ApplicationSecurityPersonRolDTO insertApplicationSecurityPersonRolDTO(ApplicationSecurityPersonRolDTO applicationSecurityPersonRolDTO)throws PmsServiceException ;
	
	//update
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public ApplicationSecurityRolDTO updateApplicationSecurityRolDTO(ApplicationSecurityRolDTO applicationSecurityRolDTO)throws PmsServiceException ;
		
	/*list methods*/
	public List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO(ApplicationSecurityProgramDTO applicationSecurityProgramDTO)throws PmsServiceException ;
	public List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO(ApplicationSecurityRolDTO applicationSecurityRolDTO)throws PmsServiceException ;
	public List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO(ApplicationSecurityRolDTO applicationSecurityRolDTO, Locale locale)throws PmsServiceException ;
	public List<ApplicationSecurityRolProgramDTO> listApplicationSecurityRolProgramDTO(ApplicationSecurityRolProgramDTO applicationSecurityRolProgramDTO)throws PmsServiceException ;
	public List<ApplicationSecurityPersonRolDTO> listApplicationSecurityPersonRolDTO(ApplicationSecurityPersonRolDTO applicationSecurityPersonRolDTO)throws PmsServiceException ;
	
	public BussinesSessionUtils getBussinesSessionUtils();
}
