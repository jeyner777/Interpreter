public class ReturnValue {
	private String str;
	private Double dbl;
	private Boolean bool;
	
	ReturnValue(String str) {
		this.str = str;
		dbl = null;
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

	public String getStr() {
		return str;
	}

	public Double getDbl() {
		return dbl;
	}
	
	public Boolean getBool() {
		return bool;
	}
	
	public String toString() {
		if (str != null) {
			return str;
		} else if (dbl != null) {
			return dbl.toString();
		} else {
			return bool.toString();
		}
	}
}