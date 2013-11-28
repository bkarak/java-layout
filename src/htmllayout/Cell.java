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

import java.util.Hashtable;
import java.awt.*;



class Cell
{
	int hfill = HtmlLayout.LEFT;
	int vfill = HtmlLayout.CENTER;

	String name;
	HtmlLayout nested;
	String labelText;

	Component comp;
	int row, col;
	int rowspan = 1, colspan = 1;
	int hgap, vgap;
	int hpad, vpad;

	Cell(int hg, int vg, int hp, int vp) {
		hgap = hg;
		vgap = vg;
		hpad = hp;
		vpad = vp;
	}

	void addToNameTable(Hashtable nameToCell) {
		if(name != null) {
			if(nameToCell.put(name, this) != null) {
				throw new BadTableHtmlException(
					"Duplicate component name: " + name);
			}
		} else if(nested != null) {
			nested.addCellsToTable(nameToCell);
		}
	}

	private int reqwidth, reqheight;

	void addLabels(Container parent) {
		if(labelText != null && comp == null) {
			comp = new Label(labelText);
			parent.add(comp, HtmlLayout.anonLabelName);
		} else if(nested != null) {
			nested.addLabels(parent);
		}
	}

	void finalLayout(int xpos[], int ypos[]) {
		if(comp == null && nested == null)
			return;

		int r = row + rowspan;
		int c = col + colspan;
		
		int ll = xpos[col];
		int lt = ypos[row];
		int mr = xpos[c];
		int mb = ypos[r];

		int left = ll;
		int right = mr;
		int top = lt;
		int bottom = mb;

		if(col != 0)
			left += hgap;
		if(row != 0)
			top += vgap;

		if(hfill != HtmlLayout.MAX && hfill != HtmlLayout.FIT) {
			left = HtmlLayout.calcTopOrLeft(left, right, reqwidth, hfill);
			right = left + reqwidth;
		}

		if(vfill != HtmlLayout.MAX && vfill != HtmlLayout.FIT) {
			top = HtmlLayout.calcTopOrLeft(top, bottom, reqheight, vfill);
			bottom = top + reqheight;
		}

		position(ll, lt, mr, mb, left, top, right, bottom);

	}

	private void position(int ll, int lt, int mr, int mb,
						  int l, int t, int r, int b) {
		if(l < ll)
			l = ll;
		if(t < lt)
			t = lt;
		if(r > mr)
			r = mr;
		if(b > mb)
			b = mb;

		if(comp != null) {
			comp.setBounds(l, t, r - l, b - t);
		} else {
			nested.layout(t, b, l, r);
		}
	}
		

	void firstXLayout(int xpos[], boolean wantX[]) {
		addToXTable(xpos);
		
		if(hfill == HtmlLayout.MAX) {
			for(int i = col; i < col + colspan; i++)
				wantX[i] = true;
		}
	}

	void firstYLayout(int ypos[], boolean wantY[]) {
		addToYTable(ypos);
		
		if(vfill == HtmlLayout.MAX) {
			for(int i = row; i < row + rowspan; i++)
				wantY[i] = true;
		}
	}


	void updateSize(int whichSize) {
		Dimension d = getSize(whichSize);
		reqwidth = d.width;
		reqheight = d.height;
	}

	private void squeeze(int pos[], int touch[][], int count[], int limit[],
		int start, int end, int size) {

		int availsize = pos[end] - pos[start];
		if(availsize > size) {
			int mylimit = availsize - size;

			if(mylimit < limit[end]) 
				limit[end] = mylimit;

		} else {
			touch[end][count[end]++] = start;
		}
	}

	void squeezeX(int xpos[], int touch[][], int count[], int limit[]) {
		squeeze(xpos, touch, count, limit, col, col + colspan, 
			reqwidth + (col == 0 ? 0 : hgap));
		
	}

	void squeezeY(int ypos[], int touch[][], int count[], int limit[]) {
		squeeze(ypos, touch, count, limit, row, row + rowspan, 
			reqheight + (row == 0 ? 0 : vgap));
	}

	void addToXTable(int xpos[]) {
		int c = col + colspan;
		int right = xpos[col] + reqwidth + (col == 0 ? 0 : hgap);

		if(xpos[c] < right)
			xpos[c] = right;
	}

	void addToYTable(int ypos[]) {
		int r = row + rowspan;
		int bottom = ypos[row] + reqheight + (row == 0 ? 0 : vgap);

		if(ypos[r] < bottom)
			ypos[r] = bottom;
	}

	static final Insets zeroInsets = new Insets(0, 0, 0, 0);

	Dimension getSize(int whichSize) {
		if(nested != null)
			return nested.layoutSize(zeroInsets, whichSize);

		if(comp == null || !comp.isVisible())
			return new Dimension(0, 0);

		Dimension d; 
		if(whichSize == HtmlLayout.MIN) {
			d = comp.getMinimumSize();
		} else if(whichSize == HtmlLayout.PREF) {
			d = comp.getPreferredSize();
		} else {
		    throw new IllegalArgumentException("Bad whichSize " + whichSize);
		}

		if(vpad != 0 || hpad != 0)
			d = new Dimension(d.width + hpad, d.height + vpad);

		return d;
	}

	private String descString() {
		return 
			" pos = " +
			row + ", " + col +
			" span = " +
			rowspan + ", " + colspan +
			" fill = " +
			hfill + ", " + vfill +
			" gap = " +
			hgap + ", " + vgap +
			" pad = " +
			hpad + ", " + vpad +
			(name != null ? (" name = " + name) :
			  (nested != null ? (" nested = ") :
				(labelText != null ? (" label = " + labelText) :
				  " empty "))); 
	}

	void dump(int space) {
		for(int i = 0; i < space; i++) System.err.print(' ');

		System.err.println("Cell" + descString());

		if(nested != null)
			nested.dump(space + 3);
	}

	public String toString() {
		return "[Cell pos = " +
			descString() +
			(nested != null ? nested.toString() : "") +
			"]";
	}
}


