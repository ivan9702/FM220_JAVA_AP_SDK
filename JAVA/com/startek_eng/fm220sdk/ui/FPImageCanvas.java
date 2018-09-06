/*
 * FPImageCanvas.java
 *
 * Created on 2007年10月25日, 下午 3:38
 */

package com.startek_eng.fc320sdk.ui;

import java.awt.image.ColorModel;
import javax.swing.JPanel;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
/**
 *
 * @author  liaocl
 */

class FPImageCanvas extends JPanel//Canvas implements Runnable
{
	protected static short FP_IMGSIZE_Height = 64;
	protected static short FP_IMGSIZE_Width = 64;

	static ColorModel colorModel = null;

	private Image fpImage=null;
	static
	{
		byte gray[] = new byte[256];
		for (int i = 0; i < gray.length; i++)
		{
			gray[i] = (byte) i; 
		}
		colorModel = new java.awt.image.IndexColorModel(8, 256, gray, gray, gray); 
		//byte[] map = new byte[] {(byte)(255),(byte)(0)};
		//colorModel = new java.awt.image.IndexColorModel(1, 2, map, map, map);
	}
	protected FPImageCanvas()
	{
		super();
//		setIgnoreRepaint(false);
	}
	protected void drawFPImage(byte[] rawData,int imgWidth, int imgHeight)
	{
		this.setVisible(true);
		java.awt.image.MemoryImageSource mis = new java.awt.image.MemoryImageSource(imgWidth, imgHeight, colorModel, 
													 rawData, 0, imgWidth); 
		fpImage = createImage(mis); 
		this.repaint();
	}
	public synchronized void paint(Graphics g)
	{
		if (fpImage == null)
		{
			g.setColor(new Color(255, 255, 255) );
            //g.setColor(new Color(0, 0, 0) );
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			//g.clearRect(0,0,this.getWidth(),this.getHeight());
		}else
		{
			g.drawImage(fpImage, 0,0,this.getWidth(),this.getHeight(),this);
		}
	}
	public void update(Graphics g)
	{
		paint(g);
	}
	protected synchronized void clear()
	{
		fpImage = null;
		this.repaint();
	}
	protected void run()
	{
		this.repaint();
	}
	public Dimension getPreferredSize() { 
		return new Dimension(FP_IMGSIZE_Width, FP_IMGSIZE_Height); 
	}
}
