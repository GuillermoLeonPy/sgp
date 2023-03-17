package py.com.kyron.sgp.persistence.personmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;

public interface PersonDAO {
	
	public Long pmsPersonIdBySequence();
	public void insert(PersonDTO personDTO);
	public void update(PersonDTO personDTO);
	public List<PersonDTO> listPersonDTO(PersonDTO personDTO);
	

}
