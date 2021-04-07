public class ReturnValue {
	private String str;
	private Double dbl;
	private Boolean bool;
	
	ReturnValue() {
		str = null;
		dbl = null;
		bool = null;
	}
	
	ReturnValue(String str) {
		this.str = str;
		try {
			dbl = Double.parseDouble(str);
		} catch (Exception e) {
			dbl = null;
		}
		bool = null;
	}
	
	ReturnValue(Double dbl) {
		str = null;
		this.dbl = dbl;
		bool = false;
	}
	
	ReturnValue(Boolean bool) {
		str = null;
		dbl = null;
		this.bool = bool;
	}

	public String getString() {
		return str;
	}

	public Double getDouble() {
		return dbl;
	}
	
	public Boolean getBoolean() {
		return bool;
	}
	
	public String toString() {
		if (str != null) {
			return str;
		} else if (dbl != null) {
			return dbl.toString();
		} else if (bool != null){
			return bool.toString();
		} else {
			return " ";
		}
	}
}