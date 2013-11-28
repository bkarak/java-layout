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


package htmllayout;



class TableParser
{
	Scanner in;

	int rows = -1, cols = -1;
	int hgap, vgap;
	int hpad, vpad;
	int horz = HtmlLayout.MAX, vert = HtmlLayout.MAX;
	Cell cells[][];

	int curRow, curCol;
	int gridcount;
	int cellCount;
	boolean taken[][];

	TableParser(Scanner in, boolean eatTable, TableParser parent) {
		this.in = in;

		if(eatTable) {
			if(in.scan() != Scanner.LT 
			  || in.scanU() != Scanner.STR
			  || !in.currentString.equals("TABLE"))
				error("description must start with TABLE tag");
		}

		if(parent != null) {
			hgap = parent.hgap;
			vgap = parent.vgap;
			hpad = parent.hpad;
			vpad = parent.vpad;
		}

		parseTable();
		finishTable();
	}

	final static String values[] = {
		"HORZ", "VERT", 
		"ROWS", "COLS",
		"HGAP", "VGAP",
		"HPAD", "VPAD",
		"COLSPAN", "ROWSPAN",
		"COMPONENT"};


	int lookup(String poss[], String value) {
		for(int i = 0; i < poss.length; i++) {
			if(poss[i].equals(value))
				return i;
		}

		error("Invalid value " + value);
		return -1;
	}

	// hack, the value of the key returned by parsePair
	int pairValue;

	int parsePair() {
		int key = lookup(values, in.currentString);

		scan(Scanner.EQ);
		scan(Scanner.STR);

		if(key == 10) {	// component
			return key;
		}

		if(key < 2) {	// horz and vert
			String sval = in.currentString.toUpperCase();

			int val = lookup(HtmlLayout.ALIGNNAMES, sval);

			if((key == 0 && val > HtmlLayout.MAX)			// for horz
			  || (key == 1 && val < HtmlLayout.CENTER))  { 	// for vert
				error(sval + " illegal value for " + values[key]);
			}

			pairValue = val;
			return key;
		}
							
		// everything else needs an int
		try {
			pairValue = Integer.parseInt(in.currentString);
		} catch(NumberFormatException nfe) {
			error(values[key] + " value must be an integer");
		}

		return key;
	}

	void parseTable() {
		int tok;

		while((tok = in.scanU()) == Scanner.STR) {
			switch(parsePair()) {
				case 0:
					horz = pairValue;
					break;
				case 1:
					vert = pairValue;
					break;
				case 2:
					rows = pairValue;
					break;
				case 3:
					cols = pairValue;
					break;
				case 4:
					hgap = pairValue;
					break;
				case 5:
					vgap = pairValue;
					break;
				case 6:
					hpad = pairValue;
					break;
				case 7:
					vpad = pairValue;
					break;
				default:
					error("Invalid attribute for TABLE");
			}
		}

		if(tok != Scanner.GT)
			error("wrong token");

		if(cols < 1 || rows < 1)
			error("must specify positive rows and columns for TABLE");

		cells = new Cell[rows][cols]; 
		taken = new boolean[rows][cols];

		while(in.scan() == Scanner.LT && in.scanU() == Scanner.STR) {
			if(in.currentString.equals("TR")) {
				if(parseTR())	// </Table>
					return;
			} else if(in.currentString.equals("/TABLE")) {
				scan(Scanner.GT);
				return;
			} else {
				error("Unexpected tag " + in.currentString);
			}
		}

		scan(Scanner.EOF);
	}

	// return true if the table should close
	// leaves input "clean" (no leftover tag parts)
	boolean parseTR() {
		int tok;
		int curRowVGap = vgap;

		if(!(curRow < rows))
			error("Excess rows in table");

		while((tok = in.scanU()) == Scanner.STR) {
			switch(parsePair()) {
				case 5:
					curRowVGap = pairValue;
					break;
				default:
					error("Invalid  attribute for TR");
			}
		}

		if(tok != Scanner.GT)
			error("wrong token");

		while(in.scan() == Scanner.LT && in.scanU() == Scanner.STR) {
			if(in.currentString.equals("TD")) {
				parseTD(curRowVGap);
				if(in.lastTok != Scanner.STR)
					continue;
			} 
			
			if(in.currentString.equals("TR")) {
				finishRow(curRowVGap);
				return parseTR();

			} else if(in.currentString.equals("/TR")) {
				scan(Scanner.GT);
				finishRow(curRowVGap);
				return false;

			} else if(in.currentString.equals("/TABLE")) {
				scan(Scanner.GT);
				finishRow(curRowVGap);
				return true;

			} else {
				error("Unexpected tag " + in.currentString);
			}
		}

		scan(Scanner.EOF);
		finishRow(curRowVGap);
		return true;
	}

	private void finishRow(int curRowVGap) {
		while(curCol < cols) {
			if(!taken[curRow][curCol]) {
				Cell cell = new Cell(hgap, curRowVGap, 0, 0);

				addCell(cell);
			} else {
				curCol++;
			}
		}
		curCol = 0;
		curRow++;
	}

	private void finishTable() {
		while(curRow < rows) 
			finishRow(vgap);
	}


	// leaves /table and /tr as the last token if it encounteers them
	// otherwise (in the case of a /td) leaves > as the last token
	void parseTD(int curRowVGap) {
		Cell c = new Cell(hgap, curRowVGap, hpad, vpad);

		int tok;

		while((tok = in.scanU()) == Scanner.STR) {
			switch(parsePair()) {
				case 0:
					c.hfill = pairValue;
					break;
				case 1:
					c.vfill = pairValue;
					break;
				case 4:
					c.hgap = pairValue;
					break;
				case 5:
					c.vgap = pairValue;
					break;
				case 6:
					c.hpad = pairValue;
					break;
				case 7:
					c.vpad = pairValue;
					break;
				case 8:
					if(pairValue < 1)
						error("colspan must be >= 1");

					c.colspan = pairValue;
					break;
				case 9:
					if(pairValue < 1)
						error("rowspan must be >= 1");

					c.rowspan = pairValue;
					break;
				case 10:
					c.name = in.currentString;
					break;
				default:
					error("Invalid attribute for TR");
			}
		}

		if(tok != Scanner.GT)
			error("wrong token");

		addCell(c);

		tok = in.scan();
		if(tok == Scanner.STR) {
			if(c.name != null)
				error("TDs can only have a component or text");

			c.labelText = in.currentString;
			tok = in.scan();
		}

		while(true) {
			if(tok == Scanner.EOF) 
				return;

			if(tok != Scanner.LT || in.scanU() != Scanner.STR) {
				error("Parse error");
			}

			if(in.currentString.equals("TD")) {
				parseTD(curRowVGap);
				return;
			} else if(in.currentString.equals("/TD")) {
				scan(Scanner.GT);
				return;
			} else if(in.currentString.equals("TABLE")) {
				if(c.name != null || c.labelText != null)
					error("TDs can't have a component or text with a TABLE");

				c.nested = new HtmlLayout(this);
				
				tok = in.scan();
			} else {
				return;
			}
		}
	}

	private void addCell(Cell c) {
		while(taken[curRow][curCol]) {
			curCol++;
			if(curCol == cols)
				error("excess elements");
		}

		c.row = curRow;
		c.col = curCol;

		if(c.row + c.rowspan > rows || c.col + c.colspan > cols) {
			error("element exceeds table bounds");
		}

		for(int j = c.row; j < c.row + c.rowspan; j++) {
			for(int i = c.col; i < c.col + c.colspan; i++) {
				if(taken[j][i])
					error("table elements overlap");

				taken[j][i] = true;
			}
		}

		curCol += c.colspan;

		cells[c.row][c.col] = c;
		cellCount++;
	}

	void error(String message) {
		String context = in.source.substring(
			Math.max(0, in.pos - 10), 
			Math.min(in.pos + 1, in.source.length()));

		throw new BadTableHtmlException(
			"Bad html at or before character " + in.pos + " : " + message 
			+ " : " + context + " <--");
	}

	void scan(int tok) {
		if(in.scan() != tok)
			error("wrong token");
	}
}

