package py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components;

import java.util.Map;

public class HelpContentItem {

	private String helpContentItemKey;
	private String helpContentItemKeyValue;
	private Map<String,String> subjects;
	
	/**
	 * @param helpContentItemKey
	 * @param helpContentItemKeyValue
	 */
	public HelpContentItem(String helpContentItemKey,
			String helpContentItemKeyValue) {
		super();
		this.helpContentItemKey = helpContentItemKey;
		this.helpContentItemKeyValue = helpContentItemKeyValue;
	}
	/**
	 * @return the helpContentItemKey
	 */
	public String getHelpContentItemKey() {
		return helpContentItemKey;
	}
	/**
	 * @param helpContentItemKey the helpContentItemKey to set
	 */
	public void setHelpContentItemKey(String helpContentItemKey) {
		this.helpContentItemKey = helpContentItemKey;
	}
	/**
	 * @return the helpContentItemKeyValue
	 */
	public String getHelpContentItemKeyValue() {
		return helpContentItemKeyValue;
	}
	/**
	 * @param helpContentItemKeyValue the helpContentItemKeyValue to set
	 */
	public void setHelpContentItemKeyValue(String helpContentItemKeyValue) {
		this.helpContentItemKeyValue = helpContentItemKeyValue;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((helpContentItemKey == null) ? 0 : helpContentItemKey
						.hashCode());
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
		HelpContentItem other = (HelpContentItem) obj;
		if (helpContentItemKey == null) {
			if (other.helpContentItemKey != null)
				return false;
		} else if (!helpContentItemKey.equals(other.helpContentItemKey))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HelpContentItem [helpContentItemKey=" + helpContentItemKey
				+ ", helpContentItemKeyValue=" + helpContentItemKeyValue + "]";
	}
	/**
	 * @return the subjects
	 */
	public Map<String, String> getSubjects() {
		return subjects;
	}
	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(Map<String, String> subjects) {
		this.subjects = subjects;
	}

}
