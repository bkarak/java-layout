/*
    HtmlLayout - A superior Java LayoutManager
    Copyright (C) 1998  Paul Buchheit

    HtmlLayout is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    HtmlLayout is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    HtmlLayout was created by Paul Buchheit, paul@braindamage.org
    HtmlLayout lives at http://braindamage.org/HtmlLayout
    There you can find the latest information and software.
*/


package org.htmllayout.parser;


public class Scanner {
	static final int LT=0, GT=1, EQ=3, STR=4, EOF=-1, ERROR=-2;

	String currentString;
	int lastTok;

	boolean inTag;

	String source;
	int pos, end;

	public Scanner(String source) {
		this.source = source;
		end = source.length();
	}

	int scanU() {
		int s = scan();
		if(s == STR)
			currentString = currentString.toUpperCase();

		return s;
	}

	int scan() {
		lastTok = sscan();
		return lastTok;
	}

	private int sscan() {
		while(true) {

		if(pos == end)
			return EOF;


		switch(source.charAt(pos)) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				pos++;
				continue;

			case '<':
				if(inTag) {
					return ERROR;
				} else {
					pos++;
					inTag = true;
					return LT;
				}

			case '>':
				if(!inTag) {
					return ERROR;
				} else {
					pos++;
					inTag = false;
					return GT;
				}

			case '=':
				if(inTag) {
					pos++;
					return EQ;
				}
				// else fall through

			default:
				return doString();
		}
		}
	}

	private int doString() {
		boolean usingQuote = inTag && source.charAt(pos) == '"';

		if(usingQuote)
			pos++;

		int start = pos;

		char c;
		while(pos < end) {
			c = source.charAt(pos);
			if(c == '>' || c == '<')
				break;

			if(inTag && c == '=')
				break;

			if(c == '"' && usingQuote) {
				currentString = source.substring(start, pos);
				pos++;
				return STR;
			}

			if(inTag && !usingQuote && isWhitespace(c)) 
				break;

			pos++;
		}

		currentString = source.substring(start, pos);
		if(!inTag)
			currentString = currentString.trim();

		if(currentString.length() == 0)
			return scan();

		return STR;
	}

	/* I'm avoiding Character.isWhitespace because it's JDK1.1 */
	private boolean isWhitespace(char c) {
		return c <= ' ' 
			&& (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f');
	}
}

