package com.jhlabs.awt;

import java.awt.*;

public class GridLayoutPlus extends BasicGridLayout {

	protected int[] rowWeights, colWeights, colFlags;

	public GridLayoutPlus() {
		super(0, 1, 2, 2);
	}

	public GridLayoutPlus(int rows, int cols) {
		super(rows, cols, 2, 2);
	}

	public GridLayoutPlus(int rows, int cols, int hGap, int vGap) {
		super(rows, cols, hGap, vGap, 0, 0);
	}

	public GridLayoutPlus(int rows, int cols, int hGap, int vGap, int hMargin, int vMargin) {
		super(rows, cols, hGap, vGap, hMargin, vMargin);
	}

	private int[] setWeight(int[]w, int index, int weight) {
		if (w == null)
			w = new int[index+1];
		else if (index >= w.length) {
			int[] n = new int[index+1];
			System.arraycopy(w, 0, n, 0, w.length);
			w = n;
		}
		w[index] = weight;
		return w;
	}

	public void setRowWeight(int row, int weight) {
		rowWeights = setWeight(rowWeights, row, weight);
	}

	public void setColWeight(int col, int weight) {
		colWeights = setWeight(colWeights, col, weight);
	}

	public void setColAlignment(int col, int v) {
		colFlags = setWeight(colFlags, col, v);
	}

	protected int getRowWeight(int row) {
		if (rowWeights != null && row < rowWeights.length)
			return rowWeights[row];
		return 0;
	}

	protected int getColWeight(int col) {
		if (colWeights != null && col < colWeights.length)
			return colWeights[col];
		return 0;
	}
	
	protected int getColAlignment(int col) {
		if (colFlags != null && col < colFlags.length)
			return colFlags[col];
		return alignment;
	}

	/**
	 * Adds the specified named component to the layout.
	 * @param name the String name
	 * @param comp the component to be added
	 */
	public void addLayoutComponent (String name, Component comp) {
	}

	/**
	 * Removes the specified component from the layout.
	 * @param comp the component to be removed
	 */
	public void removeLayoutComponent (Component comp) {
	}

	protected int alignmentFor(Component c, int row, int col) {
		return getColAlignment(col);
	}

	protected int fillFor(Component c, int row, int col) {
		return fill;
	}

	protected int weightForColumn(int col) {
		return 1;
	}

	protected int weightForColumn(int row, int col) {
		return 1;
	}
}
