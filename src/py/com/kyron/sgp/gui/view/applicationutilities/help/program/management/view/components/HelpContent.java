package py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components;

import java.util.List;



public class HelpContent {

	private String mainMenuItemKey;
	private String mainMenuItemKeyValue;
	private String moduleKey;
	private String moduleKeyValue;
	
	private List<HelpContentItem> listHelpContentItem;
	
	public HelpContent(
			final String mainMenuItemKey,
			final String mainMenuItemKeyValue,
			final String moduleKey,
			final String moduleKeyValue
			){
		this.mainMenuItemKey = mainMenuItemKey;
		this.mainMenuItemKeyValue = mainMenuItemKeyValue;
		this.moduleKey = moduleKey;
		this.moduleKeyValue = moduleKeyValue;
	}
	
	
	/**
	 * @return the mainMenuItemKey
	 */
	public String getMainMenuItemKey() {
		return mainMenuItemKey;
	}
	/**
	 * @param mainMenuItemKey the mainMenuItemKey to set
	 */
	public void setMainMenuItemKey(String mainMenuItemKey) {
		this.mainMenuItemKey = mainMenuItemKey;
	}
	/**
	 * @return the mainMenuItemKeyValue
	 */
	public String getMainMenuItemKeyValue() {
		return mainMenuItemKeyValue;
	}
	/**
	 * @param mainMenuItemKeyValue the mainMenuItemKeyValue to set
	 */
	public void setMainMenuItemKeyValue(String mainMenuItemKeyValue) {
		this.mainMenuItemKeyValue = mainMenuItemKeyValue;
	}
	/**
	 * @return the moduleKey
	 */
	public String getModuleKey() {
		return moduleKey;
	}
	/**
	 * @param moduleKey the moduleKey to set
	 */
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}
	/**
	 * @return the moduleKeyValue
	 */
	public String getModuleKeyValue() {
		return moduleKeyValue;
	}
	/**
	 * @param moduleKeyValue the moduleKeyValue to set
	 */
	public void setModuleKeyValue(String moduleKeyValue) {
		this.moduleKeyValue = moduleKeyValue;
	}

	/**
	 * @return the listHelpContentItem
	 */
	public List<HelpContentItem> getListHelpContentItem() {
		return listHelpContentItem;
	}
	/**
	 * @param listHelpContentItem the listHelpContentItem to set
	 */
	public void setListHelpContentItem(List<HelpContentItem> listHelpContentItem) {
		this.listHelpContentItem = listHelpContentItem;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mainMenuItemKey == null) ? 0 : mainMenuItemKey.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HelpContent other = (HelpContent) obj;
		if (mainMenuItemKey == null) {
			if (other.mainMenuItemKey != null)
				return false;
		} else if (!mainMenuItemKey.equals(other.mainMenuItemKey))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HelpContent [mainMenuItemKey=" + mainMenuItemKey
				+ ", mainMenuItemKeyValue=" + mainMenuItemKeyValue
				+ ", moduleKey=" + moduleKey + ", moduleKeyValue="
				+ moduleKeyValue + "]";
	}
}
