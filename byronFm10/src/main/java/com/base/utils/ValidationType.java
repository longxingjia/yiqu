package com.base.utils;


/**
 * <p>
 * Type of validation.
 * </p>
 * 
 * @author <a href="mailto:sunyi4j@gmail.com">Roy</a> on Sep 2, 2011
 */
public enum ValidationType {
	EMPTY {
		
		@Override
		public String getRule() {
			return "^\\s*((\\S+.*)|(.*\\S+))\\s*$";
		}
	},
	EMAIL {

		@Override
		public String getRule() {
			return "[\\w\\d]+[\\w\\.\\-\\+]*@[\\w\\d\\-]+(\\.[\\w]+)+";
		}
	},	
	USERNAME {

		@Override
		public String getRule() {
			return "[\\d\\w_]{4,20}";
		}
	},
	PASSWORD {

		@Override
		public String getRule() {
			return "[A-Za-z0-9]{8,16}";
		}
	},
	PHONE_NUMBER {

		@Override
		public String getRule() {
			return "\\d{11}";
		}
	},
	SERVER_ADDR {

		@Override
		public String getRule() {
			return "^http://.+[^/]$";
		}
	},
	BIRTHDAY {

		@Override
		public String getRule() {
			return "[^\\s]+";
		}
	},
	YEAR_TWO_CHARS {

		@Override
		public String getRule() {
			return "\\d{2}";
		}
	},
	NUMBER {

		@Override
		public String getRule() {
			return "\\d+";
		}
	};

	/**
	 * Define validation rule with regular expression
	 * @return
	 */
	abstract public String getRule();

}