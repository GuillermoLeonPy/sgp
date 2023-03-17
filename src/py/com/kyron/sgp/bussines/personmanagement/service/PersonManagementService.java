package py.com.kyron.sgp.bussines.personmanagement.service;

import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;

public interface PersonManagementService {
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PersonDTO registerPerson(PersonDTO personDTO) throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PersonDTO updatePerson(PersonDTO personDTO) throws PmsServiceException;
	/*spring-framework-reference.pdf, pag:381
	 * In proxy mode (which is the default), only external method calls coming in through the proxy are
	intercepted. This means that self-invocation, in effect, a method within the target object calling
	another method of the target object, will not lead to an actual transaction at runtime even if the
	invoked method is marked with @Transactional
	 */
	public List<PersonDTO> listPersonDTO(PersonDTO personDTO) throws PmsServiceException;
	public PersonDTO personDTOByUsernameAndPassword(String username, String password, Locale locale) throws PmsServiceException;
	public BussinesSessionUtils getBussinesSessionUtils();
}
