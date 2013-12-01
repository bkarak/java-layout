package com.jhlabs.awt;

import java.awt.Rectangle;

public class Alignment implements Direction {

	public final static int FILL_NONE = 0;
	public final static int FILL_HORIZONTAL = 1;
	public final static int FILL_VERTICAL = 2;
	public final static int FILL_BOTH = 3;

	public static void alignInCell(Rectangle r, Rectangle cell, int alignment, int fill) {
		r.x = cell.x;
		r.y = cell.y;

		/* Horizontal fill */
		switch (fill) {
		  case FILL_BOTH:
		  case FILL_HORIZONTAL:
			r.width = cell.width;
			break;
		}

		/* Vertical fill */
		switch (fill) {
		  case FILL_BOTH:
		  case FILL_VERTICAL:
			r.height = cell.height;
			break;
		}

		/* Horizontal alignment */
		switch (alignment) {
		  case CENTER:
		  case NORTH:
		  case SOUTH:
			r.x += (cell.width - r.width)/2;
			break;
		  case WEST:
		  case NORTHWEST:
		  case SOUTHWEST:
			break;
		  case EAST:
		  case NORTHEAST:
		  case SOUTHEAST:
			r.x += cell.width - r.width;
			break;
		}

		/* Vertical alignment */
		switch (alignment) {
		  case CENTER:
		  case WEST:
		  case EAST:
			r.y += (cell.height - r.height)/2;
			break;
		  case NORTH:
		  case NORTHWEST:
		  case NORTHEAST:
			break;
		  case SOUTH:
		  case SOUTHWEST:
		  case SOUTHEAST:
			r.y += cell.height - r.height;
			break;
		}

	}

}
