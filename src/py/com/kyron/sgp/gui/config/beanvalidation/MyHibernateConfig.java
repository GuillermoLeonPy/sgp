package py.com.kyron.sgp.gui.config.beanvalidation;

import javax.validation.ValidatorFactory;

public class MyHibernateConfig {

	private static ValidatorFactory validatorFactory;
	public MyHibernateConfig(ValidatorFactory validatorFactory) {
		// TODO Auto-generated constructor stub
		this.validatorFactory = validatorFactory;
	}
	public static ValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}
	public static void setValidatorFactory(ValidatorFactory validatorFactory) {
		MyHibernateConfig.validatorFactory = validatorFactory;
	}

}
