package org.theiet.rsuite.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Collection of string-related utility methods.
 */
public class StringUtils
{
	public static final String NEW_LINE_SEPARATOR = System.getProperty("line.separator");
	
    /**
     * Check if a string is blank.
     * <p>A string is considered blank if it is <tt>null</tt>, is empty,
     * or contains only whitespace characters.
     * </p>
     * @param s           String to check.
     * @return            <tt>true</tt> if string is blank.
     */
    public static boolean isBlank(
            String s
    ) {
        if (s == null) return true;
        int len = s.length();
        for (int i=0; i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) return false;
        }
        return true;
    }
    
    /**
     * Join a list of strings.
     * @param   separator       Separator to include between each string.
     * @param   list            List of strings to join.
     * @return  Joined string.
     */
    public static String join(
            String separator,
            Collection<String> list
    ) {
        if (list == null) return "";
        if (separator == null) separator = "";
        StringBuilder buf = new StringBuilder();
        return join(separator, buf, list).toString();
    }
    
    /**
     * Join a list of strings.
     * @param   separator       Separator to include between each string.
     * @param   list            List of strings to join.
     * @return  Joined string.
     */
    public static String join(
            String separator,
            String... list
    ) {
        if (list == null) return "";
        if (separator == null) separator = "";
        StringBuilder buf = new StringBuilder();
        return join(separator, buf, Arrays.asList(list)).toString();
    }
    
    /**
     * Join a list of strings and append to buffer.
     * @param   separator       Separator to include between each string.
     * @param   buf             Buffer to append joined string to.
     * @param   list            List of strings to join.
     * @return  <var>buf</var>.
     */
    public static <T extends Appendable> T join(
            CharSequence separator,
            T buf,
            Collection<? extends CharSequence> list
    ) {
        if (list == null) {
            return buf;
        }
        if (separator == null) separator = "";
        try {
            boolean first = true;
            for (CharSequence s : list) {
                if (first) {
                    first = false;
                } else {
                    buf.append(separator);
                }
                buf.append(s);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getLocalizedMessage(), ioe);
        }
        return buf;
    }

    /**
     * Load contents of input stream into a string.
     * @param is        Input stream to read.
     * @return  String representing contents of input stream.
     * @throws IOException if an I/O error.
     */
    public static String convertInputStreamToString(
            InputStream is
    ) throws IOException
    {
        if(is!=null)
        {
            StringBuilder sb = new StringBuilder();
            String line;
            try
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while((line=reader.readLine())!=null)
                {
                    sb.append(line).append("\n");
                }
            }
            finally
            {
                if(is!=null)
                    is.close();
            }
            return sb.toString();
        }
        return "";
    }
    
    /**
     * Escape XML special characters.
     * @param line  Text to escape characters.
     * @return  String with XML specials converted to entity references.
     */
    public static String escapeXmlMarkup(String line) {
        if (line == null)
            return null;
        int len = line.length();
        StringBuilder buf = new StringBuilder((int)(len*1.20f));
        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);
            switch (c) {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            case '"':
                buf.append("&quot;");
                break;
            case '\'':
                buf.append("&apos;");
                break;
            default:
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Retrieve boolean value from string map.
     * @param   map     String map.
     * @param   name    Name of key to get value of.
     * @param   defVal  Default value if key is not defined.
     */
    public static boolean getBooleanVariable(
            Map<String,? extends Object> map,
            String name,
            boolean defVal
    )
    {
        Object o = map.get(name);
        if (o == null) return defVal;
        String s = o.toString().trim().toLowerCase();
        if (s.equals("true") || s.equals("yes")) return true;
        if (s.equals("false") || s.equals("no")) return false;
        return defVal;
    }
    
    /**
     * Retrieve string value from string map.
     * @param   map     String map.
     * @param   name    Name of key to get value of.
     * @param   defVal  Default value if key is not defined.
     */
    public static String getStringVariable(
            Map<String,? extends Object> map,
            String name,
            String defVal
    )
    {
        Object o = map.get(name);
        if (o == null) return defVal;
        return o.toString();
    }

    /**
     * Expand variables in a string.
     * <p>This method will expand occurrences of
     * "<tt>${<var>name</var>}</tt>" in a string.  The replacement values
     * are defined within the <var>varValues</var> map, or in the
     * {@link System#getProperties system properties}, where
     * <tt><var>name</var></tt> is the lookup key.  Values defined in
     * the system properties supercede values in <var>varValues</var>.
     * If no value is defined for <tt><var>name</var></tt>, then
     * the variable will be replaced with the empty string.
     * </p>
     * <p>If it is required to have the literal string sequence of
     * "<tt>${</tt>" in the string, then you must escape it as follows:
     * "<tt>$${</tt>".  This is the only escape sequence checked for.
     * Therefore, there is no need to escape '<tt>$</tt>' if you require
     * to have a literal '<tt>$</tt>' in the string.
     * </p>
     * <p><b>Notes:</b></p>
     * <ul>
     * <li>Any <var>varValues</var> values used are cast to
     *     {@link String} objects.  If a cast fails for a value,
     *     the return of the value's <tt>toString()</tt> method will be
     *     used.
     *     </li>
     * <li>If security does not allow access to the system properties,
     *     then the system properties are not used in variable value
     *     expansion.
     *     </li>
     * </ul>
     * <p></p>
     * @param s       String to expand variables within.
     * @param varValues   Map of variable names to associated variable values;
     *            if <tt>null</tt>, then only the system properties
     *            will be used in variable expansion.
     * @return        Expanded string.
     * @see       #expandString(String,Map,boolean)
     * @see       #expandString(String,Map,Properties)
     */
    public static String expandString(
            String s,
            Map<String,?> varValues
    ) {
        return expandString(s, varValues, true);
    }

    /**
     * Expand variables in a string.
     * <p>This method will expand occurrences of
     * "<tt>${<var>name</var>}</tt>" in a string.  The replacement values
     * are defined within the <var>varValues</var> map, or in the
     * {@link System#getProperties system properties}, where
     * <tt><var>name</var></tt> is the lookup key.  Values defined in
     * the system properties supercede values in <var>varValues</var>.
     * If no value is defined for <tt><var>name</var></tt>, then
     * the variable will be replaced with the empty string.
     * </p>
     * <p>If it is required to have the literal string sequence of
     * "<tt>${</tt>" in the string, then you must escape it as follows:
     * "<tt>$${</tt>".  This is the only escape sequence checked for.
     * Therefore, there is no need to escape '<tt>$</tt>' if you require
     * to have a literal '<tt>$</tt>' in the string.
     * </p>
     * <p><b>Notes:</b></p>
     * <ul>
     * <li>Any <var>varValues</var> values used are cast to
     *     {@link String} objects.  If a cast fails for a value,
     *     the return of the value's <tt>toString()</tt> method will be
     *     used.
     *     </li>
     * <li>If security does not allow access to the system properties,
     *     then the system properties are not used in variable value
     *     expansion, regardless of the value of the
     *     <var>checkSystemProps</var> parameter.
     *     </li>
     * </ul>
     * <p></p>
     * @param s       String to expand variables within.
     * @param varValues   Map of variable names to associated variable values;
     *            if <tt>null</tt>, then only the system properties
     *            will be used in variable expansion unless
     *            <var>checkSystemProps</var> is <tt>false</tt>.
     * @param checkSystemProps
     *            If <tt>true</tt>, system properties are checked
     *            for variables before <var>varValues</var>; if
     *            <tt>false</tt>, no system property lookups are done.
     * @return        Expanded string.
     * @see       #expandString(String,Map)
     * @see       #expandString(String,Map,Properties)
     */
    public static String expandString(
            String s,
            Map<String,?> varValues,
            boolean checkSystemProps
    ) {
        Properties props = null;
        if (checkSystemProps) {
            try {
                props = System.getProperties();
            } catch (SecurityException se) {
                // will not be using system properties...
            }
        }
        return expandString(s, varValues, props);
    }

    // Constants for variable expansion ins strings
    private static final char   STR_VAR_START_CHAR = '$';
    private static final String STR_VAR_START      = "${";
    private static final int    STR_VAR_START_LEN  = STR_VAR_START.length();
    private static final String STR_VAR_END        = "}";
    private static final int    STR_VAR_END_LEN    = STR_VAR_END.length();

    /**
     * Expand variables in a string.
     * <p>This method will expand occurrences of
     * "<tt>${<var>name</var>}</tt>" in a string.  The replacement values
     * are defined within the <var>varValues</var> map, or in the
     * {@link Properties properties} denoted by
     * the <var>props</var> parameter, where
     * <tt><var>name</var></tt> is the lookup key.  Values defined in
     * <var>props</var> supercede values in <var>varValues</var>.
     * If no value is defined for <tt><var>name</var></tt>, then
     * the variable will be replaced with the empty string.
     * </p>
     * <p>If it is required to have the literal string sequence of
     * "<tt>${</tt>" in the string, then you must escape it as follows:
     * "<tt>$${</tt>".  This is the only escape sequence checked for.
     * Therefore, there is no need to escape '<tt>$</tt>' if you require
     * to have a literal '<tt>$</tt>' in the string.
     * </p>
     * <p><b>Notes:</b></p>
     * <ul>
     * <li>Any <var>varValues</var> values used are cast to
     *     {@link String} objects.  If a cast fails for a value,
     *     the return of the value's <tt>toString()</tt> method will be
     *     used.
     *     </li>
     * <li>The <var>props</var> parameter can be any {@link Properties}
     *     instance.  However, if you desire to have variable values
     *     defined via system properties, the return value
     *     of {@link System#getProperties()} should be used for
     *     the <var>props</var> parameter.
     *     </li>
     * </ul>
     * <p></p>
     * @param s       String to expand variables within.
     * @param varValues   Map of variable names to associated variable values;
     *            if <tt>null</tt>, then only the system properties
     *            will be used in variable expansion.
     * @param props   {@link Properties Properties} that will supercede
     *            values defined in <var>varValues</var>; this
     *            parameter can be <tt>null</tt>.
     * @return        Expanded string.
     * @see       #expandString(String,Map)
     * @see       #expandString(String,Map,boolean)
     */
    public static String expandString(
            String s,
            Map<String,?> varValues,
            Properties props
    ) {
        // Do quick scan to see if there are any possible variables to expand
        int pos = s.indexOf(STR_VAR_START, 0);
        if (pos < 0) {
            return s;
        }

        int len = s.length();
        StringBuilder buf = new StringBuilder(len);
        String name, val;
        Object o;

        int off = 0;
        while (pos >= 0) {
            if ((pos > 0) && (s.charAt(pos-1) == STR_VAR_START_CHAR)) {
                // ${ escaped, so we ignore
                buf.append(s, off, pos-1);
                buf.append(STR_VAR_START);
                off = pos + STR_VAR_START_LEN;

            } else {
                if (pos != off) {
                    buf.append(s, off, pos);
                }
                pos += STR_VAR_START_LEN;
                if ((off = s.indexOf(STR_VAR_END, pos)) < 0) {
                    // No proper end to variable; leave as-is
                    pos -= STR_VAR_START_LEN;
                    buf.append(s, pos, len);
                    break;
                }
                name = s.substring(pos, off);
                val = null;
                if (props != null) {
                    val = props.getProperty(name);
                }
                if (val == null && varValues != null) {
                    if ((o = varValues.get(name)) != null) {
                        try {
                            val = (String)o;
                        } catch (ClassCastException cce) {
                            val = o.toString();
                        }
                    }
                }
                if (val != null) {
                    buf.append(val);
                }
                off += STR_VAR_END_LEN;
            }
            if ((pos = s.indexOf(STR_VAR_START, off)) < 0) {
                buf.append(s.substring(off));
                break;
            }
        }

        return buf.toString();
    }

	public static String printStackTrace(Throwable e) {
		if (e == null)
			return ("No StackTrace available on NULL Pointer Exception.");
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	
	/**
	 * Normalize white space in string.
	 * <p>Leading and trailing white space is removed.  Multiple white-space
	 * sequences within the string are replaced with a single space.
	 * </p>
	 * @param s    String to normalize.
	 * @return String with white space normalized.
	 */
	public static String normalizeSpace(
	        String s
	) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            if (st.hasMoreTokens()) {
                sb.append(' ');
            }
        }
        return sb.toString();
	}

	public static boolean arrayContains(String[] values, String s) {
		if (isBlank(s)) {
			return false;
		}
		for (String val : values) {
			if (!isBlank(val) && val.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets formatted UK date from Joda DateTime object.
	 *
	 * @param dt DateTime
	 * @return formatted date string
	 */
	public static String getUKDateFromDateTime(DateTime dt) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return (dt == null) ? "" : dt.toString(fmt);
	}
}

