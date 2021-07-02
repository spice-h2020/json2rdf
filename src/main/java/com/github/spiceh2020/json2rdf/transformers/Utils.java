package com.github.spiceh2020.json2rdf.transformers;

import com.google.common.escape.UnicodeEscaper;
import com.google.common.net.PercentEscaper;

public class Utils {
	static UnicodeEscaper basicEscaper = new PercentEscaper("%", false);

	public static String toSafeURIString(String s) {
		return basicEscaper.escape(s);
	}

}
