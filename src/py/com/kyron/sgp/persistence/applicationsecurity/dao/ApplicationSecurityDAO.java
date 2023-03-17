package py.com.kyron.sgp.persistence.applicationsecurity.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityPersonRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolProgramDTO;

public interface ApplicationSecurityDAO {
	/*sequences*/
	public Long applicationSecurityProgramDTOIdBySequence(long id);
	public Long applicationSecurityRolDTOIdBySequence(long id);
	public Long applicationSecurityRolProgramDTOIdBySequence(long id);
	public Long applicationSecurityPersonRolDTOIdBySequence(long id);

	/*inserts*/
	public void insertApplicationSecurityProgramDTO(ApplicationSecurityProgramDTO ApplicationSecurityProgramDTO);
	public void insertApplicationSecurityRolDTO(ApplicationSecurityRolDTO ApplicationSecurityRolDTO);
	public void insertApplicationSecurityRolProgramDTO(ApplicationSecurityRolProgramDTO ApplicationSecurityRolProgramDTO);
	public void insertApplicationSecurityPersonRolDTO(ApplicationSecurityPersonRolDTO ApplicationSecurityPersonRolDTO);
	
	/*lists*/
	public List<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO(ApplicationSecurityProgramDTO ApplicationSecurityProgramDTO);
	public List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO(ApplicationSecurityRolDTO ApplicationSecurityRolDTO);
	public List<ApplicationSecurityRolProgramDTO> listApplicationSecurityRolProgramDTO(ApplicationSecurityRolProgramDTO ApplicationSecurityRolProgramDTO);
	public List<ApplicationSecurityPersonRolDTO> listApplicationSecurityPersonRolDTO(ApplicationSecurityPersonRolDTO ApplicationSecurityPersonRolDTO);
	
	/*delete*/
	public void deleteApplicationSecurityRolProgramDTO(ApplicationSecurityRolProgramDTO ApplicationSecurityRolProgramDTO);
	public void deleteApplicationSecurityPersonRolDTO(ApplicationSecurityPersonRolDTO ApplicationSecurityPersonRolDTO);
}
