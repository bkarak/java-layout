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

import java.awt.*;
import java.io.*;


/** Reads in files containing table-html and creates a window for each
  * layed out according to the html.  Buttons are created in the place
  * of each component.  Used for testing table-html and layout behavior.
  *
  * @see htmllayout.HtmlLayout
  * @author Paul Buchheit
  */
public class HtmlLayoutTest extends Frame
{

	HtmlLayoutTest(String title, String html) {
		super(title);

		HtmlLayout hl = new HtmlLayout(html);
		setLayout(hl);
		addComps(hl.cells);
	}
	
	void addComps(Cell cells[]) {
		for(int i = 0; i < cells.length; i++) {
			if(cells[i].name != null) {
				Button b = new Button(cells[i].name);
				add(b, cells[i].name);
			} else if(cells[i].nested != null) {
				addComps(cells[i].nested.cells);
			}
		}
	}

	
	/** uses deprecated api for compatibility with web browsers */
	public boolean handleEvent(Event evt) {
		if(evt.id == Event.WINDOW_DESTROY) {
			hide();
			return true;
		} else {
			return false;
		}
	}

	public static void main(String args[]) throws IOException {
		if(args.length == 0) {
			System.err.println("HtmlLayoutTest filename [...]");
			System.err.println(
			  "\tReads in each file \"filename\" containing table html\n" +
			  "\tand creates a window layed out according to that html.\n" +
			  "\tButtons are created to fill in for the components.");
		}

		for(int i = 0; i < args.length; i++) {
			RandomAccessFile raf = new RandomAccessFile(args[i], "r");
			byte data[] = new byte[(int)raf.length()];
			raf.readFully(data);
			raf.close();

			Frame f = new HtmlLayoutTest(args[i], new String(data));
			f.pack();
			f.setVisible(true);
		}
	}
}

