package py.com.kyron.sgp.bussines.session.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;

public class RawSessionData {

	private final String userName;
	private final Date startSessionTime;
	private final Locale userSessionLocale;
	private final PersonDTO personDTO;
	private Set<String> programKeysBySession;
	
	public RawSessionData(String userName, Date startSessionTime, Locale userSessionLocale, PersonDTO personDTO) {
		// TODO Auto-generated constructor stub
		this.userName = userName;
		this.startSessionTime = startSessionTime;
		this.userSessionLocale = userSessionLocale;
		this.personDTO = personDTO;
		this.programKeysBySession = null;
	}

	public String getUserName() {
		return userName;
	}

//	public void setUserName(String userName) {
//		this.userName = userName;
//	}

	public Date getStartSessionTime() {
		return startSessionTime;
	}

//	public void setStartSessionTime(Date startSessionTime) {
//		this.startSessionTime = startSessionTime;
//	}

	public Locale getUserSessionLocale() {
		return userSessionLocale;
	}

//	public void setUserSessionLocale(Locale userSessionLocale) {
//		this.userSessionLocale = userSessionLocale;
//	}

	@Override
	public String toString() {
		return "RawSessionData [userName=" + userName + ", startSessionTime="
				+ startSessionTime + ", userSessionLocale=" + userSessionLocale
				+ ", personDTO=" + personDTO + "]";
	}

	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	public Set<String> getProgramKeysBySession() {
		if(this.programKeysBySession == null) this.prepareProgramsKeysBySession();
		return this.programKeysBySession;
	}

	private void prepareProgramsKeysBySession(){
		this.programKeysBySession = new HashSet<String>();
		if(this.personDTO.getApplicationSecurityRolDTO()!=null && this.personDTO.getApplicationSecurityRolDTO().getRoleProgramList()!=null){
			for(ApplicationSecurityProgramDTO program : this.personDTO.getApplicationSecurityRolDTO().getRoleProgramList()){
				this.programKeysBySession.add(program.getProgram_key());
			}
		}
	}
}
