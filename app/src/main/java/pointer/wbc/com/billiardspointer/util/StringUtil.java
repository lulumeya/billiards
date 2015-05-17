package pointer.wbc.com.billiardspointer.util;

public class StringUtil {

	public static boolean isEmpty( String s ) {
		return s == null || "".equals( s.trim() );
	}

	public static boolean notEmpty( String s ) {
		return s != null && !"".equals( s.trim() );
	}

	public static boolean notEmpty( String... s ) {
		for ( int i = 0 ; i < s.length ; i++ ) {
			if ( s[i] == null || "".equals( s[i].trim() ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean notEmpty( Object s ) {
		return s != null && !"".equals( s.toString().trim() );
	}

	public static boolean isInt( String str ) {
		if ( notEmpty( str ) ) {
			try {
				Integer.parseInt( str );
				return true;
			} catch ( Exception e ) {
				return false;
			}
		}
		return false;
	}

	public static boolean isNumber( String str ) {
		if ( notEmpty( str ) ) {
			try {
				Double.parseDouble( str );
				return true;
			} catch ( Exception e ) {
				return false;
			}
		}
		return false;
	}
}