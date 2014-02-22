package m3rlin.ut1ls;

public class Params {

	public static String getString(String string, String defaultString) {
		if (string != null && string.length() > 0) {
			return string;
		}
		return defaultString;
	}

	public static int getInt(String string, int defaultInt) {
		try {
			if (string != null && string.length() > 0) {
				return Integer.parseInt(string);
			}
		} catch (NumberFormatException e) {		
			System.out.print("\n>> Invalid number, using default:" + defaultInt + "\n\n");
		}
		return defaultInt;
	}
}
