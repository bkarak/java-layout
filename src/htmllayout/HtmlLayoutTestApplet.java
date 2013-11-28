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
import java.net.*;
import java.io.*;
import java.applet.*;


/** Reads the file specified by the "htmlURL" param and, when the
  * "Test filname" button is pressed, opens a window layed out by that
  * table-html, just as with HtmlLayoutTest.
  * @see htmllayout.HtmlLayout
  * @see htmllayout.HtmlLayoutTest
  * @author Paul Buchheit
  */
public class HtmlLayoutTestApplet extends Applet
{

	String html;
	String htmlURL;

	public void init() {
		htmlURL = getParameter("htmlURL");
		if(htmlURL == null) {
			System.err.println(
			  "HtmlLayoutTestApplet: htmlURL param must be specified");
		} else {
			try {
				URL url = new URL(getCodeBase(), htmlURL);

/****** replaced for JDK 1.0 browsers
				BufferedReader br = new BufferedReader(
					new InputStreamReader());

				StringBuffer sb = new StringBuffer();
				String s;
				while((s = br.readLine()) != null) {
					sb.append(s);
					sb.append("\n");
				}
				html = sb.toString();
******/
				// instead
				InputStream is = url.openStream();
				StringBuffer sb = new StringBuffer();
				String s;
				while((s = readString(is)) != null) {
					sb.append(s);
				}
				html = sb.toString();

			} catch(Exception e) {
				System.err.println(
				  "HtmlLayoutTestApplet: " + e);
			}
		}

		if(html != null) {
			setLayout(new FlowLayout());
			add(new Button("Test " + htmlURL));
		}
	}

	private String readString(InputStream is) throws IOException {
		byte data[] = new byte[10000];
		int c, pos = 0;

		while(pos < data.length && 
			(c = is.read(data, pos, data.length - pos)) != -1) {
			pos += c;
		}

		if(pos == 0)
			return null;

		return new String(data, 0, 0, pos);	// for 1.0
	}

	/** uses deprecated api for compatibility with web browsers */
	public boolean action(Event evt, Object what) {
		Frame f = new HtmlLayoutTest(htmlURL, html);
		f.pack();
		f.show();
		return true;
	}

	public void paint(Graphics g) {
		if(html == null) {
			g.setColor(Color.red);
			g.drawString("Error", 10, 40);
		}
	}
}

