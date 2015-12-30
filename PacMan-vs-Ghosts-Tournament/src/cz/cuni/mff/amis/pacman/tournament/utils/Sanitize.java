package cz.cuni.mff.amis.pacman.tournament.utils;

public class Sanitize {

	public static String allowed = "abcdefghijklmnopqrstuvwxyz0123456789_-";
	
	public static String allowedUpper = allowed.toUpperCase();
	
	public static String idify(String id) {
		StringBuffer result = new StringBuffer(id.length());
		for (int i = 0; i < id.length(); ++i) {
			if (allowed.contains(id.substring(i, i+1)) || allowedUpper.contains(id.substring(i, i+1))) {
				result.append(id.charAt(i));
			} else {
				result.append("_");
			}
		}
		return result.toString();
	}
	
}
