/*
 * FPImageData.java
 *
 * Created on 2008年9月24日, 下午 3:30
 */

package com.startek_eng.fc320sdk.ui;

/**
 *
 * @author  casio6666
 */
public class FPImageData implements java.io.Serializable
{
	byte[] _imageData;	//raw image buffer.
	int _height;		//image height
	int _width;			//image width
	//int dataType;		//bmp, raw ...??
	public FPImageData()
	{
		_imageData = null;
		_height = 0;
		_width = 0;
	}
	public boolean setImageData(byte[] imageData,int width,int height)
	{
		if (imageData!=null && height>0 && width>0 && (imageData.length >= width*height))
		{
			//encryp image data here
			_height = height;
			_width = width;
			_imageData  = new byte[width*height];
			System.arraycopy(imageData,
							 0,
							 _imageData,
							 0,
							 _imageData.length);
			return true;
		}
		return false;
	}
	public byte[] getRawData()
	{
		return _imageData;
	}
	public int getWidth()
	{
		return _width;
	}
	public int getHeight()
	{
		return _height;
	}
	public boolean isValidImage()
	{
		return (_imageData!=null && _width>0 && _height>0);
	}
};