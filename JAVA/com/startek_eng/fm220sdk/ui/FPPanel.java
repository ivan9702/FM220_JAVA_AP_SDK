/*
 * FPPanel.java
 *
 * Created on November 30, 2006, 10:22 AM
 */

package com.startek_eng.fc320sdk.ui;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.Icon;

import com.startek_eng.fc320sdk.FC320SDKWrapper;
import com.startek_eng.fc320sdk.SensorConnectFailedException;
import com.startek_eng.fc320sdk.LibraryLoadFailedException;
/**
 *
 * @author  liaocl
 */
public class FPPanel extends JPanel
{
	
	/** Creates new form FPPanel */
	public FPPanel()
	{
		super();
		initComponents();
        if (myResourceBundle!=null)
            myResourceBundle.localizeUI(this);
		if (wsaBrowserAPI==null)
		{
			try
			{
				wsaBrowserAPI = FC320SDKWrapper.getInstance();
			}
			catch (SensorConnectFailedException e)
			{
				wsaBrowserAPI = null;
				e.printStackTrace();
			}
			catch (LibraryLoadFailedException e)
			{
				wsaBrowserAPI = null;
				e.printStackTrace();
			}
		}
        clear();
	}
    String getResourceString(String defaultStr)
    {
         if (myResourceBundle!=null)
         {
             String key = defaultStr.replace(' ', '_'); 
             return myResourceBundle.getResourceString(key, defaultStr);   
         }else
             return defaultStr;
    }
    protected void finalize() throws Throwable
    {
        super.finalize();
        stop();
    }
	public void reInitial()
	{
		try
		{
            action = false;
			if (wsaBrowserAPI==null)
			{
				wsaBrowserAPI = FC320SDKWrapper.getInstance();
			}
			if (wsaBrowserAPI!=null)
			{
                if (theHConnect>0)
                {
                    try
                    {
                        wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
                    }catch(Exception ignore)
                    {}
                    theHConnect = -1;
                }
				//wsaBrowserAPI.reInitial();
			}
		}
		catch (SensorConnectFailedException e)
		{
			e.printStackTrace();
		}
		catch (LibraryLoadFailedException e)
		{
			e.printStackTrace();
		}
	}
	static FPPanel instance = null;
	public static FPPanel getInstance()
	{
		if (instance == null)
		{
			instance = new FPPanel();
		}
		return instance;
	}
	private void initComponents()
	{
		initMainPanel();
		initFPImagePanel();
		initStatusPanel();
		initMsgPanel();
	}
	private void initFPImagePanel()
	{
		fpImgCanvas = new FPImageCanvas();
		fpImgCanvas.clear();
		JPanel tmpPanel = new JPanel(new GridBagLayout());
        tmpPanel.setBorder(new javax.swing.border.EmptyBorder(20,25,15,20));
		tmpPanel.setOpaque(false);
		//fpImgCanvas.setOpaque(false);
		//tmpPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(100,100,100)));
		GridBagConstraints cbag = new GridBagConstraints();
		
		cbag.fill = GridBagConstraints.BOTH; //GridBagConstraints.HORIZONTAL;
		//cbag.anchor = GridBagConstraints.PAGE_START ; 
		//cbag.insets = new Insets(20,25,30,15);	//margion
		cbag.weightx =1;		//x(width)的比例
		cbag.weighty =1;		//y(height)的比例
		cbag.gridx = 1;			//位置
		cbag.gridy = 1;			//0,0位置 Position
        cbag.ipadx = 10;			//位置
        cbag.ipady = 10;			//位置
		tmpPanel.add(fpImgCanvas,cbag);
		tmpPanel.setPreferredSize(new Dimension(70, 70));
		fpImgPanel.add(tmpPanel,BorderLayout.CENTER);
	}
	private void initStatusPanel()
	{
		JPanel tmpPanel = new JPanel(new GridBagLayout());
		tmpPanel.setOpaque(false);
		//tmpPanel.setBorder(BorderFactory.createTitledBorder("status"));
		tmpPanel.setVisible(true);
        tmpPanel.setBorder(new javax.swing.border.EmptyBorder(15,80,20,60));
		lblPos = new JLabel();
		lblWet = new JLabel();
		lblDry = new JLabel();
		try
		{
			if (ICON_OK==null)
				ICON_OK = new ImageIcon(new java.net.URL(this.getClass().getResource("img/OK.JPG"),"OK.JPG"));
			if (ICON_FAIL==null)
				ICON_FAIL = new ImageIcon(new java.net.URL(this.getClass().getResource("img/NG.JPG"),"NG.JPG"));
			lblPos.setIcon(ICON_FAIL);
			lblWet.setIcon(ICON_FAIL);
			lblDry.setIcon(ICON_FAIL);
			lblPos.setDisabledIcon(ICON_OK);
			lblWet.setDisabledIcon(ICON_OK);
			lblDry.setDisabledIcon(ICON_OK);
			lblWet.setPreferredSize(new Dimension(50,50));
			lblDry.setPreferredSize(new Dimension(50,50));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		GridBagConstraints cbag = new GridBagConstraints();
		
		cbag.fill = GridBagConstraints.HORIZONTAL; //GridBagConstraints.HORIZONTAL;
		//cbag.anchor = GridBagConstraints.PAGE_START ; 
		cbag.insets = new Insets(10,10,10,10);	//margion
		cbag.weightx = 0.0;		//x(width)的比例
		cbag.weighty = 0.3;		//y(height)的比例
		cbag.gridx = 0;			//位置
		cbag.gridy = 0;			//0,0位置 Position
		JLabel tmp = new JLabel(getResourceString("Position"), JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(80,50));
		tmpPanel.add(tmp,cbag);
		cbag.gridx = 0;	
		cbag.gridy = 1;			//0,1位置 Web
		tmp = new JLabel(getResourceString("Too Wet"), JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(80,50));
		tmpPanel.add(tmp,cbag);
		cbag.gridx = 0;
		cbag.gridy = 2;			//0,2位置 Dry
		tmp = new JLabel(getResourceString("Too Dry"), JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(80,50));
		tmpPanel.add(tmp,cbag);
		
		cbag.insets = new Insets(0,10,0,60);	//margion
		cbag.gridx = 1;
		cbag.gridy = 0;			//1,0位置
		tmpPanel.add(lblPos,cbag);
		cbag.gridx = 1;
		cbag.gridy = 1;			//1,1位置
		tmpPanel.add(lblWet,cbag);
		cbag.gridx = 1;
		cbag.gridy = 2;			//1,2位置
		tmpPanel.add(lblDry,cbag);
	
		fpStatusPanel.add(tmpPanel,BorderLayout.CENTER);
	}
	private void initMsgPanel()
	{
		JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmpPanel.setOpaque(false);
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			try
			{
				lblEnrollStep[i] = new JLabel();
				//System.out.println("img/0"+(i+1)+"-1.GIF" +" ==== " +"0"+(i+1)+"-1.GIF" );
				lblEnrollStep[i].setIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/0"+(i+1)+"-1.GIF"),"0"+(i+1)+"-1.GIF")));
				lblEnrollStep[i].setDisabledIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/0"+(i+1)+"-2.GIF"),"0"+(i+1)+"-2.GIF")));
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally
			{
				tmpPanel.add(lblEnrollStep[i]);
			}
		}
		msgPanel.add(tmpPanel,BorderLayout.SOUTH);
		tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblStatus = new JTextPane();
        lblStatus.setEditable(false);
		lblStatus.setOpaque(false);
		tmpPanel.setOpaque(false);
		tmpPanel.add(lblStatus);
		msgPanel.add(tmpPanel,BorderLayout.NORTH);
	}
	private void loadBackground()
	{
		try
		{
			if (bgImage==null)
			{
				bgImage = java.awt.Toolkit.getDefaultToolkit().createImage(new java.net.URL(this.getClass().getResource("img/bg.gif"),"bg.gif"));
				//bgImage = createImage((int)getPreferredSize().getWidth() ,(int) getPreferredSize().getHeight() );
				//Graphics offscreenGraphics = bgImage.getGraphics();
				//offscreenGraphics.drawImage(tmpImage,0,0,(int)getPreferredSize().getWidth(),(int)getPreferredSize().getHeight(),this);
				//bgImage = offscreenGraphics.g;
				msgPanel.setOpaque(false);
				fpImgPanel.setOpaque(false);
				fpStatusPanel.setOpaque(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 
		if(bgImage != null) g.drawImage(bgImage, 0,0,this.getWidth(),this.getHeight(),this);
	}
	public void update(Graphics g)
	{
		paint(g);
	}

	private void initMainPanel()
	{
		msgPanel = new JPanel(new BorderLayout());
		fpImgPanel = new JPanel(new BorderLayout());
		fpStatusPanel = new JPanel(new BorderLayout());

		//Set up Mim size of each panel
		msgPanel.setPreferredSize(new Dimension(300,60));
        msgPanel.setMinimumSize(new Dimension(300,60));
		//fpImgPanel.setPreferredSize(new Dimension((int)(perferredWidth*0.55),(int)(perferredHeight*0.6-50)));
		fpImgPanel.setPreferredSize(new Dimension(150,150));
		fpStatusPanel.setPreferredSize(new Dimension(150,150));
		loadBackground();
		this.setLayout(new GridBagLayout());
		//this.setBackground(new java.awt.Color(0,0,0));
        
        
		GridBagConstraints cbag = new GridBagConstraints();
		
        
		cbag.fill = GridBagConstraints.BOTH; //GridBagConstraints.HORIZONTAL;
		//cbag.anchor = GridBagConstraints.PAGE_START ; 
//		cbag.anchor = GridBagConstraints.PAGE_END; 
		cbag.insets = new Insets(0,0,0,0);  //top padding
		cbag.weightx = 1;		//x(width)的比例
		cbag.weighty = 0.1;     //y(height)的比例
		cbag.gridx = 0;			//位置
		cbag.gridy = 11;			//位置
		cbag.gridwidth = 6;		//單位
        cbag.gridheight = 1;		//單位
        cbag.ipadx = 11;			//位置
        cbag.ipady = 11;			//位置
		//msgPanel.setBackground(new java.awt.Color(255,0,0));
		this.add(msgPanel,cbag);
/*		cbag.fill = GridBagConstraints.BOTH;
		cbag.insets = new Insets(25,30,25,12);  //top padding
//		cbag.anchor = GridBagConstraints.PAGE_START ; 
		//cbag.ipady = 60;      //make this component tall
		cbag.weighty = 0.6;		
		cbag.weightx = 0.55;
		cbag.gridwidth = 5;
		cbag.gridx = 0;
		cbag.gridy = 0;
		//fpImgPanel.setBackground(new java.awt.Color(0,255,0));
		this.add(fpImgPanel,cbag);
		//cbag.anchor = GridBagConstraints.PAGE_END; //bottom of space
		cbag.weighty = 0.6;
		cbag.weightx = 0.45;
		cbag.gridwidth = 1;
		cbag.insets = new Insets(25,18,28,30);  //top padding
		cbag.gridx = 5;
		cbag.gridy = 0;
		//fpStatusPanel.setBackground(new java.awt.Color(0,0,255));
		this.add(fpStatusPanel,cbag);*/
        
        JPanel tmpPanel = new JPanel();
        tmpPanel.setPreferredSize(new Dimension(300,150));
        tmpPanel.setMinimumSize(new Dimension(300,150));
        tmpPanel.setOpaque(false);
        tmpPanel.setLayout(new javax.swing.BoxLayout(tmpPanel,javax.swing.BoxLayout.X_AXIS));
        tmpPanel.add(fpImgPanel);
        tmpPanel.add(fpStatusPanel);
        cbag.fill = GridBagConstraints.BOTH;
        cbag.weighty = 0.9;
		cbag.weightx = 1;
		cbag.gridwidth = 6;		//單位
        cbag.gridheight = 9;		//單位
		cbag.insets = new Insets(0,0,0,0);  //top padding
		cbag.gridx = 0;
		cbag.gridy = 0;
        this.add(tmpPanel,cbag);
        
	}


	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	public Dimension getPreferredSize()
	{
		return new Dimension(perferredWidth, perferredHeight); 
	}
	private static void showStatus(String msg, String functionName)
	{
		if (action)
		{
			//System.out.println(functionName + " : " +msg);
		}else
		{
			//System.out.println("Cancel the working job");
		}
	}
	private static void showStatus(Exception e)
	{
		e.printStackTrace();
	}
	public synchronized void showStatus(String msg)
	{
		if (action)
		{
			if (msg!=null && lblStatus!=null)
				lblStatus.setText(msg);
		}else
		{
			//System.out.println("Cancel the working job");
		}
	}
	private int showImage(long theHConnect,long hFPImage, int snapRtn)
	{
		int rtn=FC320SDKWrapper.FAIL;
		/*if (theHConnect>0 && hFPImage>0 && fpImgCanvas != null)
		{
			try
			{
				rtn = wsaBrowserAPI.FP_GetImage(theHConnect, hFPImage);
				if (rtn==FC320SDKWrapper.OK)
				{
					rtn = wsaBrowserAPI.FP_DisplayImage(theHConnect, fpImgCanvas, hFPImage, 0, 0,(short) fpImgCanvas.getWidth(),(short)fpImgCanvas.getHeight());
				}
			}
			catch (Exception e)
			{
				showStatus(e);
				//showStatus(e.getMessage());
			}finally
			{
				switch (snapRtn & FC320SDKWrapper.U_POSITION_CHECK_MASK )
				{
					case FC320SDKWrapper.U_POSITION_NO_FP:
					case FC320SDKWrapper.U_POSITION_TOO_LOW:
					case FC320SDKWrapper.U_POSITION_TOO_TOP:
					case FC320SDKWrapper.U_POSITION_TOO_RIGHT:
					case FC320SDKWrapper.U_POSITION_TOO_LEFT:
					case FC320SDKWrapper.U_POSITION_TOO_LOW_RIGHT:
					case FC320SDKWrapper.U_POSITION_TOO_LOW_LEFT:
					case FC320SDKWrapper.U_POSITION_TOO_TOP_RIGHT:
					case FC320SDKWrapper.U_POSITION_TOO_TOP_LEFT:
							lblPos.setEnabled(true);
							rtn=FC320SDKWrapper.FAIL;
							break;
					case FC320SDKWrapper.U_POSITION_OK:
					default:
							lblPos.setEnabled(false);
							break;
				}
				switch (snapRtn & FC320SDKWrapper.U_DENSITY_CHECK_MASK)
				{
					case FC320SDKWrapper.U_DENSITY_TOO_DARK:
							lblWet.setEnabled(true);
							lblDry.setEnabled(false);
							rtn=FC320SDKWrapper.FAIL;
							break;
					case FC320SDKWrapper.U_DENSITY_LITTLE_LIGHT:
					case FC320SDKWrapper.U_DENSITY_TOO_LIGHT:
							lblWet.setEnabled(false);
							lblDry.setEnabled(true);
							rtn=FC320SDKWrapper.FAIL;
							break;
					case FC320SDKWrapper.U_DENSITY_AMBIGUOUS:
							lblWet.setEnabled(true);
							lblDry.setEnabled(true);
							rtn=FC320SDKWrapper.FAIL;
							break;
					default :
							lblWet.setEnabled(false);
							lblDry.setEnabled(false);
							break;
				}
			}
		}*/
		return rtn;
	}
	private int showImage(byte[] rawData,int width, int height, int snapRtn)
	{
		int rtn=FC320SDKWrapper.OK;
		fpImgCanvas.drawFPImage(rawData,width,height);
		switch (snapRtn & FC320SDKWrapper.U_POSITION_CHECK_MASK )
		{
			case FC320SDKWrapper.U_POSITION_NO_FP:
			case FC320SDKWrapper.U_POSITION_TOO_LOW:
			case FC320SDKWrapper.U_POSITION_TOO_TOP:
			case FC320SDKWrapper.U_POSITION_TOO_RIGHT:
			case FC320SDKWrapper.U_POSITION_TOO_LEFT:
			case FC320SDKWrapper.U_POSITION_TOO_LOW_RIGHT:
			case FC320SDKWrapper.U_POSITION_TOO_LOW_LEFT:
			case FC320SDKWrapper.U_POSITION_TOO_TOP_RIGHT:
			case FC320SDKWrapper.U_POSITION_TOO_TOP_LEFT:
					lblPos.setEnabled(true);
					rtn=FC320SDKWrapper.FAIL;
					break;
			case FC320SDKWrapper.U_POSITION_OK:
			default:
					lblPos.setEnabled(false);
					break;
		}
		switch (snapRtn & FC320SDKWrapper.U_DENSITY_CHECK_MASK)
		{
			case FC320SDKWrapper.U_DENSITY_TOO_DARK:
					lblWet.setEnabled(true);
					lblDry.setEnabled(false);
					rtn=FC320SDKWrapper.FAIL;
                    showStatus(getResourceString("Too Wet, Dry your finger with handkerchief"));
					break;
			case FC320SDKWrapper.U_DENSITY_LITTLE_LIGHT:
			case FC320SDKWrapper.U_DENSITY_TOO_LIGHT:
					lblWet.setEnabled(false);
					lblDry.setEnabled(true);
                    showStatus(getResourceString("Too Dry, Please \"slight\" moisturize your finger"));
					rtn=FC320SDKWrapper.FAIL;
					break;
			case FC320SDKWrapper.U_DENSITY_AMBIGUOUS:
					lblWet.setEnabled(true);
					lblDry.setEnabled(true);
					showStatus(getResourceString("Please place your finger on sensor"));
					rtn=FC320SDKWrapper.FAIL;
					break;
			default :
					lblWet.setEnabled(false);
					lblDry.setEnabled(false);
					break;
		}
		//showStatus(getResourceString("snapRtn: "+snapRtn + " rtn :"+rtn);
		return rtn;
	}
	public synchronized int getRawImageData(FPImageData fpImgData)
	{
		int rtn = FC320SDKWrapper.FAIL;

		long tmpHandle = -1;
		long hFPImage = -1;
		long hCaptureHandle = -1;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
		if (fpImgCanvas!=null)
			fpImgCanvas.clear();
        if (wsaBrowserAPI== null)
        {
            reInitial();
        }
		if (wsaBrowserAPI !=null && fpImgData != null)
		{
			try
			{
				if (theHConnect<=0)
				{
					FP_ConnectDevice();
				}
				if (theHConnect>0)
				{
					showStatus(getResourceString("Please place your finger on sensor"));
					hFPImage = wsaBrowserAPI.FP_CreateImageHandle(theHConnect, (byte)FC320SDKWrapper.GRAY_IMAGE, FC320SDKWrapper.SMALL);
					hCaptureHandle = wsaBrowserAPI.FP_CreateCaptureHandle(theHConnect);
					if (hFPImage>0)
					{
						
						byte[] bRawData = new byte[FC320SDKWrapper.FP_IMAGE_WIDTH*FC320SDKWrapper.FP_IMAGE_HEIGHT];
						short[] nImgWidthHeight = new short[2];
						if (theSensorType == FC320SDKWrapper.SENSORTYPE_AREA)
						{
							while (action && (rtn=wsaBrowserAPI.FP_CheckBlank(theHConnect))!= FC320SDKWrapper.OK)
							{
								//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
								wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
                                if ((counter++)%10==0)
								{
									showStatus(getResourceString("Please remove your finger from sensor"));
								}
							}
						}
						int okCount = 2;
						while (action && theHConnect>0 && hCaptureHandle>0 && hFPImage>0)
						{
							rtn = wsaBrowserAPI.FP_Capture(theHConnect,hCaptureHandle);//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
                            if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA &&
                                ((rtn & FC320SDKWrapper.U_POSITION_CHECK_MASK)==FC320SDKWrapper.U_POSITION_NO_FP) )
                            {
                                try
                                {
                                    Thread.sleep(400);
                                }catch(java.lang.InterruptedException e)
                                {
                                }
                            }
							wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if (rtn == FC320SDKWrapper.OK)
							{
								counter = 1;
								if (fpImgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]))
								{
									okCount--;
									//if (okCount<0)
									{
                                        showStatus(getResourceString("Get Image OK"));
										break;
									}
								}
							}else
							{
								okCount = 2;
								if (((counter++)%20==0) && theSensorType == FC320SDKWrapper.SENSORTYPE_CHIPLINE)
								{
									tmpHandle = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
									if (tmpHandle<=0)
									{
										wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
										hFPImage = 0;
										wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
										hCaptureHandle = 0;
										wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
										theHConnect = -1;
									}
								}
							}
							showStatus(getResourceString("Please place your finger on sensor"));
						}
					}else
					{
						showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
					}
				}else
				{
					showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( wsaBrowserAPI!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
						}catch (Exception e){;}
					}
					if (hCaptureHandle>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
						}catch (Exception e){showStatus(e);}
					}
					//wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
				}
				if (rtn != FC320SDKWrapper.OK)
				{
					reInitial();
				}
			}
		}
		return rtn;
	}
	public synchronized byte[] getFPEnrollISOMinutia()
	{
		byte result[]=null;
		int rtn = 0;

		long hFPImage = -1;
		long hCaptureHandle = -1;
		long hFPEnroll = -1;
		long tmpHandle = -1;
		int counter =0;
		
		if (wsaBrowserAPI !=null)
		{
            boolean retryAgain = true;
            while (retryAgain)
            {
                result=null;
                action = true;
                rtn = 0;
                hFPImage = -1;
                hCaptureHandle = -1;
                hFPEnroll = -1;
                tmpHandle = -1;
                counter =0;
                retryAgain = false;
                for (int i=0;i<lblEnrollStep.length;i++)
                {
                    lblEnrollStep[i].setVisible(true);
                    lblEnrollStep[i].setEnabled(true);
                }
                this.setVisible(true);
                this.repaint();
                if (fpImgCanvas!=null)
                    fpImgCanvas.clear();
                fpImgData = null;
                if (wsaBrowserAPI== null)
                {
                    reInitial();
                }
                try
                {
                    if (theHConnect<=0)
                    {
                        FP_ConnectDevice();
                    }
                    if (theHConnect>0)
                    {
                        hFPImage = wsaBrowserAPI.FP_CreateImageHandle(theHConnect, (byte)FC320SDKWrapper.GRAY_IMAGE, FC320SDKWrapper.SMALL);
                        hFPEnroll = wsaBrowserAPI.FP_CreateEnrollHandle(theHConnect, (byte)FC320SDKWrapper.DEFAULT_MODE);
                        hCaptureHandle = wsaBrowserAPI.FP_CreateCaptureHandle(theHConnect);
                        //remove finger
                        if (hFPImage>0 && hFPEnroll>0 && hCaptureHandle > 0)
                        {
                            byte[] bRawData = new byte[FC320SDKWrapper.FP_IMAGE_WIDTH*FC320SDKWrapper.FP_IMAGE_HEIGHT];
                            int[] nImgWidthHeight = new int[2];
                            int[] coreXY = new int[2];
                            byte[] p_isominu = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
                            byte[] fp_isominu = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
                            java.util.Arrays.fill(fp_isominu,(byte)0);
                            int enrollCounter = 0;
                            while (action && theHConnect>0 && hCaptureHandle>0 && hFPImage>0)
                            {
                                //Remove Finger
                                if (theSensorType == FC320SDKWrapper.SENSORTYPE_AREA)
                                {
                                    while (action && (rtn=wsaBrowserAPI.FP_CheckBlank(theHConnect))!= FC320SDKWrapper.OK)
                                    {
                                        //rtn = wsaBrowserAPI.FP_Snap(theHConnect);
                                        wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
                                        showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
                                        showImage(theHConnect,hFPImage,rtn);
                                        lblPos.setEnabled(true);
                                        if ((counter++)%10==0)
                                        {
                                            showStatus(getResourceString("Please remove your finger from sensor"));
                                        }
                                    }
                                }
                                //Place Finger
                                while (action)
                                {
                                    showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
                                    rtn = wsaBrowserAPI.FP_Capture(theHConnect,hCaptureHandle);//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
                                    if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA &&
                                        ((rtn & FC320SDKWrapper.U_POSITION_CHECK_MASK)==FC320SDKWrapper.U_POSITION_NO_FP) )
                                    {
                                        try
                                        {
                                            Thread.sleep(400);
                                        }catch(java.lang.InterruptedException e)
                                        {
                                        }
                                    }
                                    wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
                                    showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
                                    showImage(theHConnect,hFPImage,rtn);
                                    if (rtn == FC320SDKWrapper.OK)
                                    {
                                        counter = 1;
                                        rtn = wsaBrowserAPI.FP_GetISOminutiaEx(theHConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],p_isominu,coreXY);
                                        if (rtn == wsaBrowserAPI.OK)
                                        {
                                            showStatus(getResourceString("Get Image OK"));//+" : "+rtn + " POFM:" + (p_isominu[27]&0xFF));
                                            if ((p_isominu[27]&0xFF) > 15)
                                            {
                                                soundSnapOK();
                                                //showStatus(getResourceString("FP_GetISOminutiaEx OK : ")+rtn +" POFM:" + (p_isominu[27]&0xFF));
                                                break;//exit place finger
                                            }
                                        }
                                    }else
                                    {
                                        if (((counter++)%20==0) && theSensorType == FC320SDKWrapper.SENSORTYPE_CHIPLINE)
                                        {
                                            tmpHandle = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                                            if (theHConnect>0)
                                            {
                                                m_hardwareCode = wsaBrowserAPI.WSA_GetHardwareCode(theHConnect);
                                                theSensorType = wsaBrowserAPI.FP_GetSensorType();
                                            }
                                            if (tmpHandle<=0)
                                            {
                                                wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
                                                hFPImage = -1;
                                                wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
                                                hCaptureHandle = -1;
                                                wsaBrowserAPI.FP_DestroyEnrollHandle(theHConnect,hFPEnroll);
                                                hFPEnroll = -1;
                                                wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
                                                theHConnect = -1;
                                            }
                                        }
                                    }
                                    showStatus(getResourceString("Please place your finger on sensor"));
                                }
                                //do Enroll
                                if (rtn == wsaBrowserAPI.OK && hFPEnroll>0)
                                {
                                    rtn  = wsaBrowserAPI.FP_ISOminutiaEnroll(theHConnect,hFPEnroll, p_isominu, fp_isominu);
//System.out.println("FP_ISOminutiaEnroll:"+rtn);
                                    if (rtn == FC320SDKWrapper.U_NOT_YET || rtn == FC320SDKWrapper.FAIL)
                                    {
                                        lblEnrollStep[enrollCounter++].setEnabled(false);
                                        if (enrollCounter>=lblEnrollStep.length)
                                        {
                                            showStatus(getResourceString("Enroll Failed, Please retry"));
                                            
                                            if (javax.swing.JOptionPane.showConfirmDialog( this,  
                                                                            getResourceString("Enrollment Failed !! Please retry")+"\r\n"+getResourceString("Would you like to re-enroll finger again")+"?",
                                                                            getResourceString("Would you like to re-enroll finger again")+"?",
                                                                            javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) 
                                            {
                                                retryAgain = true;
                                            }else
                                            {
                                                action = false;
                                            }
                                            break;
                                        }
                                    }else if ((rtn >= FC320SDKWrapper.U_CLASS_A) && (rtn <= FC320SDKWrapper.U_CLASS_E))
                                    {
                                        lblEnrollStep[enrollCounter++].setEnabled(false);
                                        boolean finished = true;
                                        if (rtn != FC320SDKWrapper.U_CLASS_A)
                                        {
                                            showStatus(getResourceString("Enroll Failed, Please retry"));
                                             if (enrollCounter<lblEnrollStep.length)
                                             {
                                                if (javax.swing.JOptionPane.showConfirmDialog( this,  
                                                                                getResourceString("Obtain fingerprint template successfully. Class Quality is")+" "+FP_GetClassLevelString(rtn)+" (MAX:A)\r\n"+getResourceString("Enhance Class")+"?"+"\r\n",
                                                                                getResourceString("Enhance Class")+"?",
                                                                                javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) 
                                                {
                                                    finished = false;
                                                }
                                            }else
                                            {
                                                if (javax.swing.JOptionPane.showConfirmDialog( this,
                                                                                getResourceString("Obtain fingerprint template successfully. Class Quality is")+" "+FP_GetClassLevelString(rtn)+" (MAX:A)\r\n"
                                                                                +getResourceString("Press <Yes> to accept current fingerprint data")+"\r\n"
                                                                                +getResourceString("Press <No> to re-enroll fingerprint data")+"\r\n",
                                                                                getResourceString("Do you accept current fingerprint data")+"?",
                                                                                javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) 
                                                {
                                                    retryAgain = true;
                                                    break;//exit FP Enroll 
                                                }
                                            }
                                        }
                                        if (finished)
                                        {
                                            int len_minutiae = (fp_isominu[10]&0xFF)*256+(fp_isominu[11]&0xFF);
                                            result = new byte[len_minutiae];//new byte[FC320SDKWrapper.FP_CODE_LENGTH];
                                            if (result.length<= FC320SDKWrapper.FP_CODE_LENGTH &&
                                                result.length>= FC320SDKWrapper.MIN_ISOMINUTIAE_LENGTH )
                                            {
                                                System.arraycopy(fp_isominu,
                                                                 0,
                                                                 result,
                                                                 0,
                                                                 len_minutiae);
                                                if (fpImgData==null)
                                                {
                                                    fpImgData = new FPImageData();
                                                }
                                                fpImgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]);
                                                showStatus(getResourceString("Enroll Success"));
                                                break;
                                            }else
                                            {
                                                showStatus(getResourceString("Enroll Failed [Size Error]"));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }//end while
                        }else
                        {
                            showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
                        }
                    }else
                    {
                        showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
                    }
                }catch(Exception e)
                {
                    showStatus(e);
                }finally
                {
                    if ( wsaBrowserAPI!=null && theHConnect>0)
                    {
                        if (hFPImage>0)
                        {
                            try
                            {	wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
                                hFPImage = -1;
                            }catch (Exception e){;}
                        }
                        if (hFPEnroll>0)
                        {
                            try
                            {	wsaBrowserAPI.FP_DestroyEnrollHandle(theHConnect,hFPEnroll);
                                hFPEnroll = -1;
                            }catch (Exception e){;}
                        }
                        if (hCaptureHandle>0)
                        {
                            try
                            {	wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
                                hCaptureHandle = -1;
                            }catch (Exception e){showStatus(e);}
                        }
                        //wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
                    }
                    if (result == null)
                    {
                        reInitial();
                    }
                }//end finally
            }//end while retry 
		}//if (wsaBrowserAPI !=null)
		return result;
	}
    public synchronized byte[] getFPEnrollCode()
	{
		byte result[]=null;
		int rtn = 0;

		long hFPImage = -1;
		long hCaptureHandle = -1;
		long hFPEnroll = -1;
		long tmpHandle = -1;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(true);
			lblEnrollStep[i].setEnabled(true);
		}
		if (fpImgCanvas!=null)
			fpImgCanvas.clear();
		fpImgData = null;
        if (wsaBrowserAPI== null)
        {
            reInitial();
        }
		if (wsaBrowserAPI !=null)
		{
			this.setVisible(true);
			this.repaint();
			try
			{
				if (theHConnect<=0)
				{
					FP_ConnectDevice();
				}
				if (theHConnect>0)
				{
					showStatus(getResourceString("Please place your finger on sensor"));
					hFPImage = wsaBrowserAPI.FP_CreateImageHandle(theHConnect, (byte)FC320SDKWrapper.GRAY_IMAGE, FC320SDKWrapper.SMALL);
					hFPEnroll = wsaBrowserAPI.FP_CreateEnrollHandle(theHConnect, (byte)FC320SDKWrapper.DEFAULT_MODE);
					hCaptureHandle = wsaBrowserAPI.FP_CreateCaptureHandle(theHConnect);
					//remove finger
					if (hFPImage>0 && hFPEnroll>0 && hCaptureHandle > 0)
					{
						byte[] bRawData = new byte[FC320SDKWrapper.FP_IMAGE_WIDTH*FC320SDKWrapper.FP_IMAGE_HEIGHT];
						int[] nImgWidthHeight = new int[2];
						int[] coreXY = new int[2];
						byte[] p_code = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
						byte[] fp_code = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
						java.util.Arrays.fill(p_code,(byte)0);
						java.util.Arrays.fill(fp_code,(byte)0);
						int enrollCounter = 0;
						while (action && theHConnect>0 && hCaptureHandle>0 && hFPImage>0)
						{
							//Remove Finger
							if (theSensorType == FC320SDKWrapper.SENSORTYPE_AREA)
							{
								while (action && (rtn=wsaBrowserAPI.FP_CheckBlank(theHConnect))!= FC320SDKWrapper.OK)
								{
									//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
									wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
									showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
									showImage(theHConnect,hFPImage,rtn);
                                    lblPos.setEnabled(true);
									if ((counter++)%10==0)
									{
										showStatus(getResourceString("Please remove your finger from sensor"));
									}
								}
							}
							//Place Finger
							while (action)
							{
								rtn = wsaBrowserAPI.FP_Capture(theHConnect,hCaptureHandle);//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
                                if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA &&
                                ((rtn & FC320SDKWrapper.U_POSITION_CHECK_MASK)==FC320SDKWrapper.U_POSITION_NO_FP) )
                                {
                                    try
                                    {
                                        Thread.sleep(400);
                                    }catch(java.lang.InterruptedException e)
                                    {
                                    }
                                }
								wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
								if (rtn == FC320SDKWrapper.OK)
								{
									counter = 1;
									rtn = wsaBrowserAPI.FP_GetPrimaryCode(theHConnect,p_code);//wsaBrowserAPI.FP_GetISOminutiaEx(theHConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],p_isominu,coreXY);
									if (rtn == wsaBrowserAPI.OK)
									{
										showStatus(getResourceString("Get Image OK"));
                                        soundSnapOK();
									}
								}else
								{
									if (((counter++)%20==0) && theSensorType == FC320SDKWrapper.SENSORTYPE_CHIPLINE)
									{
										tmpHandle = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                                        if (theHConnect>0)
                                        {
                                            m_hardwareCode = wsaBrowserAPI.WSA_GetHardwareCode(theHConnect);
                                            theSensorType = wsaBrowserAPI.FP_GetSensorType();
                                        }
										if (tmpHandle<=0)
										{
											wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
											hFPImage = -1;
											wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
											hCaptureHandle = -1;
											wsaBrowserAPI.FP_DestroyEnrollHandle(theHConnect,hFPEnroll);
											hFPEnroll = -1;
											wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
											theHConnect = -1;
										}
									}
								}
								showStatus(getResourceString("Please place your finger on sensor"));
							}
							//do Enroll
							if (rtn == wsaBrowserAPI.OK && hFPEnroll>0)
							{
								rtn  = wsaBrowserAPI.FP_Enroll(theHConnect,hFPEnroll, p_code, fp_code);
								if (rtn == FC320SDKWrapper.U_NOT_YET)
								{
									lblEnrollStep[enrollCounter++].setEnabled(false);
									if (enrollCounter>=lblEnrollStep.length)
									{
										showStatus(getResourceString("Enroll Failed, Please retry"));
										break;
									}
								}
								if ((rtn >= FC320SDKWrapper.U_CLASS_A) && (rtn <= FC320SDKWrapper.U_CLASS_E))
								{
									lblEnrollStep[enrollCounter++].setEnabled(false);
									result = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
                                    System.arraycopy(fp_code,
														 0,
														 result,
														 0,
														 FC320SDKWrapper.FP_CODE_LENGTH);
                                    fpImgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]);
                                    showStatus(getResourceString("Enroll Success"));
								}else if (rtn == FC320SDKWrapper.FAIL)
								{
									showStatus(getResourceString("Enroll Failed"));
									break;
								}
							}
						}
					}else
					{
						showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
					}
				}else
				{
					showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( wsaBrowserAPI!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
						}catch (Exception e){;}
					}
					if (hFPEnroll>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyEnrollHandle(theHConnect,hFPEnroll);
						}catch (Exception e){;}
					}
					if (hCaptureHandle>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
						}catch (Exception e){showStatus(e);}
					}
					//wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
				}
				if (result == null)
				{
					reInitial();
				}
			}
		}
		return result;
	}
    public synchronized byte[] getFPCode(FPImageData imgData)
	{
		int rtn = FC320SDKWrapper.FAIL;
		byte result[] = null;
        
		long hFPImage = -1;
		long hCaptureHandle = -1;
		long tmpHandle = -1;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
		if (fpImgCanvas!=null)
			fpImgCanvas.clear();
        if (wsaBrowserAPI== null)
        {
            reInitial();
        }
		if (wsaBrowserAPI !=null)
		{
			this.setVisible(true);
			this.repaint();
			try
			{
				if (theHConnect<=0)
				{
					FP_ConnectDevice();
				}
				if (theHConnect>0)
				{
					hFPImage = wsaBrowserAPI.FP_CreateImageHandle(theHConnect, (byte)FC320SDKWrapper.GRAY_IMAGE, FC320SDKWrapper.SMALL);
					hCaptureHandle = wsaBrowserAPI.FP_CreateCaptureHandle(theHConnect);
					if (hFPImage>0 && hCaptureHandle>0)
					{
						showStatus(getResourceString("Please place your finger on sensor"));
						byte[] bRawData = new byte[FC320SDKWrapper.FP_IMAGE_WIDTH*FC320SDKWrapper.FP_IMAGE_HEIGHT];
						int[] nImgWidthHeight = new int[2];
						int[] coreXY = new int[2];
						if (theSensorType == FC320SDKWrapper.SENSORTYPE_AREA)
						{
							while (action && (rtn=wsaBrowserAPI.FP_CheckBlank(theHConnect))!= FC320SDKWrapper.OK)
							{
								//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
								rtn = wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
								if ((counter++)%10==0)
								{
									showStatus(getResourceString("Please remove your finger from sensor"));
								}
							}
						}
						byte[] p_code = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
						counter = 0;
						while (action && theHConnect>0 && hCaptureHandle>0 && hFPImage>0)
						{
                            showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
							rtn = wsaBrowserAPI.FP_Capture(theHConnect,hCaptureHandle);//wsaBrowserAPI.FP_Snap(theHConnect);
                            if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA &&
                                ((rtn & FC320SDKWrapper.U_POSITION_CHECK_MASK)==FC320SDKWrapper.U_POSITION_NO_FP) )
                            {
                                try
                                {
                                    Thread.sleep(400);
                                }catch(java.lang.InterruptedException e)
                                {
                                }
                            }
							wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if (action && rtn == FC320SDKWrapper.OK)
							{
								rtn = wsaBrowserAPI.FP_GetPrimaryCode(theHConnect,p_code);//wsaBrowserAPI.FP_GetISOminutiaEx(theHConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],minutiae,coreXY);
								if (rtn == FC320SDKWrapper.OK)
								{
									counter = 1;
                                    result = p_code;
                                    showStatus(getResourceString("Get Image OK"));
                                    soundSnapOK();
                                    break;
								}else
									showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								
							}else
							{
								if (((counter++)%20==0) && theSensorType == FC320SDKWrapper.SENSORTYPE_CHIPLINE)
								{
									tmpHandle = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                                    if (theHConnect>0)
                                    {
                                        m_hardwareCode = wsaBrowserAPI.WSA_GetHardwareCode(theHConnect);
                                        theSensorType = wsaBrowserAPI.FP_GetSensorType();
                                    }
									if (tmpHandle<=0)
									{
										wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
										hFPImage = -1;
										wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
										hCaptureHandle = -1;
										wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
										theHConnect = -1;
									}
								}
								if (theHConnect>0)
								{
									showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								}
							}
						}
					}else
					{
						showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
					}
				}else
				{
					showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( wsaBrowserAPI!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
						}catch (Exception e){showStatus(e);}
					}
					if (hCaptureHandle>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
						}catch (Exception e){showStatus(e);}
					}
					//wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
				}
				if (result == null)
				{
					reInitial();
				}
			}
		}else 
        {
            showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
        }
		return result;
	}
	public synchronized byte[] getFPISOMinutia(FPImageData imgData)
	{
		int rtn = FC320SDKWrapper.FAIL;
		byte result[] = null;
        
		long hFPImage = -1;
		long hCaptureHandle = -1;
		long tmpHandle = -1;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
		if (fpImgCanvas!=null)
			fpImgCanvas.clear();
        if (wsaBrowserAPI== null)
        {
            reInitial();
        }
		if (wsaBrowserAPI !=null)
		{
			this.setVisible(true);
			this.repaint();
			try
			{
				if (theHConnect<=0)
				{
					FP_ConnectDevice();
				}
				if (theHConnect>0)
				{
					hFPImage = wsaBrowserAPI.FP_CreateImageHandle(theHConnect, (byte)FC320SDKWrapper.GRAY_IMAGE, FC320SDKWrapper.SMALL);
					hCaptureHandle = wsaBrowserAPI.FP_CreateCaptureHandle(theHConnect);
					if (hFPImage>0 && hCaptureHandle>0)
					{
						showStatus(getResourceString("Please place your finger on sensor"));
						byte[] bRawData = new byte[FC320SDKWrapper.FP_IMAGE_WIDTH*FC320SDKWrapper.FP_IMAGE_HEIGHT];
						int[] nImgWidthHeight = new int[2];
						int[] coreXY = new int[2];
						if (theSensorType == FC320SDKWrapper.SENSORTYPE_AREA)
						{
							while (action && (rtn=wsaBrowserAPI.FP_CheckBlank(theHConnect))!= FC320SDKWrapper.OK)
							{
								//rtn = wsaBrowserAPI.FP_Snap(theHConnect);
								wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
								if ((counter++)%10==0)
								{
									showStatus(getResourceString("Please remove your finger from sensor"));
								}
							}
						}
						byte[] minutiae = new byte[FC320SDKWrapper.FP_CODE_LENGTH];
						counter = 0;
						while (action && theHConnect>0 && hCaptureHandle>0 && hFPImage>0)
						{
							rtn = wsaBrowserAPI.FP_Capture(theHConnect,hCaptureHandle);//wsaBrowserAPI.FP_Snap(theHConnect);
                            if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA &&
                                ((rtn & FC320SDKWrapper.U_POSITION_CHECK_MASK)==FC320SDKWrapper.U_POSITION_NO_FP) )
                            {
                                try
                                {
                                    Thread.sleep(400);
                                }catch(java.lang.InterruptedException e)
                                {
                                }
                            }
							wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							/*if (action && theSensorType == FC320SDKWrapper.SENSORTYPE_AREA && rtn == FC320SDKWrapper.OK)
							{
								int delay = 3;
								do
								{
									rtn = wsaBrowserAPI.FP_Snap(theHConnect);
									wsaBrowserAPI.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
									showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
									showImage(theHConnect,hFPImage,rtn);
									delay--;
								}
								while (delay>0);
							}*/
							if (action && rtn == FC320SDKWrapper.OK)
							{
								rtn = wsaBrowserAPI.WSA_ImageToISOWithExtension(theHConnect,bRawData,(short)nImgWidthHeight[0],(short)nImgWidthHeight[1],minutiae);
								if (rtn >= FC320SDKWrapper.OK && 
                                    rtn <= FC320SDKWrapper.FP_CODE_LENGTH &&
									rtn >= FC320SDKWrapper.MIN_ISOMINUTIAE_LENGTH)
								{
									counter = 1;
                                    if ((minutiae[27]&0xFF) > 15) // # of featutre point > 8
                                    {
                                        result = new byte[rtn];
                                        java.util.Arrays.fill(result,(byte)0);
                                        System.arraycopy(minutiae,
															 0,
															 result,
															 0,
															 rtn);
                                        imgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]);
                                        showStatus(getResourceString("Get Image OK"));
                                        soundSnapOK();
                                        break;
                                    }
								}else
									showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								
							}else
							{
								if (((counter++)%20==0) && theSensorType == FC320SDKWrapper.SENSORTYPE_CHIPLINE)
								{
									tmpHandle = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                                    if (theHConnect>0)
                                    {
                                        m_hardwareCode = wsaBrowserAPI.WSA_GetHardwareCode(theHConnect);
                                        theSensorType = wsaBrowserAPI.FP_GetSensorType();
                                    }
									if (tmpHandle<=0)
									{
										wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
										hFPImage = -1;
										wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
										hCaptureHandle = -1;
										wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
										theHConnect = -1;
									}
								}
								if (theHConnect>0)
								{
									showStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								}
							}
						}
					}else
					{
						showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
					}
				}else
				{
					showStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( wsaBrowserAPI!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyImageHandle (theHConnect, hFPImage);
						}catch (Exception e){showStatus(e);}
					}
					if (hCaptureHandle>0)
					{
						try
						{	wsaBrowserAPI.FP_DestroyCaptureHandle (theHConnect, hCaptureHandle);
						}catch (Exception e){showStatus(e);}
					}
					//wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
				}
				if (result == null)
				{
					reInitial();
				}
			}
		}
		return result;
	}
	public void stop()
	{
		action = false;
		showStatus(getResourceString("The process have been canceled")+"!");
        clear();
		if (wsaBrowserAPI!=null)
		{
			try
			{
				wsaBrowserAPI.FP_DisconnectCaptureDriver(0);
			}
			catch (Exception e)
			{
			}
		}
		//fpImgCanvas.clear();
		theHConnect=0;
	}
	public FPImageData getLastImageData()
	{
		return fpImgData;
	}
	public byte[] setCurrentFPImageData(FPImageData imgData)
	{
		byte[] result = null;
		if (imgData!=null)
		{
			try
			{
				if (theHConnect<=0)
				{
					FP_ConnectDevice();
				}
				if (theHConnect>0)
				{
					fpImgCanvas.drawFPImage(imgData.getRawData(),imgData.getWidth(),imgData.getHeight());
					byte [] p_isominu = new byte [512];
					int [] coreXY = new int[2];
					int rtn = wsaBrowserAPI.FP_GetISOminutiaEx(theHConnect,imgData.getRawData(),imgData.getWidth(),imgData.getHeight(),p_isominu,coreXY);
	//System.out.println("rtn:"+rtn);
					if (rtn == wsaBrowserAPI.OK)
					{
						if ((p_isominu[27]&0xFF) > 15)
						{
							int len_minutiae = (p_isominu[10]&0xFF)*256+(p_isominu[11]&0xFF);
							result = new byte[len_minutiae+8];//new byte[FC320SDKWrapper.FP_CODE_LENGTH];
							System.arraycopy(p_isominu,
											 0,
											 result,
											 0,
											 len_minutiae);
System.out.println("X:"+coreXY[0]+" Y:"+coreXY[1]);
							result[len_minutiae] = (byte)((coreXY[0] & 0xFF000000)>>24);
							result[len_minutiae+1] = (byte)((coreXY[0] & 0x00FF0000)>>16);
							result[len_minutiae+2] = (byte)((coreXY[0] & 0x0000FF00)>>8);
							result[len_minutiae+3] = (byte)((coreXY[0] & 0x000000FF));
							result[len_minutiae+4] = (byte)((coreXY[1] & 0xFF000000)>>24);
							result[len_minutiae+5] = (byte)((coreXY[1] & 0x00FF0000)>>16);
							result[len_minutiae+6] = (byte)((coreXY[1] & 0x0000FF00)>>8);
							result[len_minutiae+7] = (byte)((coreXY[1] & 0x000000FF));
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
//System.out.println("result:"+result);
		return result;
	}
    
    public long getDeviceHandle()
    {
        return theHConnect;
    }
    public String getErrorMsg()
    {
        return lblStatus.getText();
    }
    
    public synchronized void clear()
    {
        lblStatus.setText("");
        for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
        lblPos.setEnabled(true);
        lblWet.setEnabled(true);
        lblDry.setEnabled(true);
        if (fpImgCanvas!=null)
            fpImgCanvas.clear();
    }
    public byte[] FP_EncryptData(byte[] srcData, String publicKey) throws java.lang.Exception
    {
        byte[] result = null;
        if (wsaBrowserAPI!=null && srcData!=null && publicKey!=null)
        {
            result = new byte[srcData.length];
            wsaBrowserAPI.FP_Encrypt(srcData, result, srcData.length, publicKey,publicKey.length());
        }
        return result;
    }
    public void FP_Sound(String path)
    {
        if (wsaBrowserAPI!=null)
        {
            try
            {
                if (m_EnableSound)
                    wsaBrowserAPI.WSA_PlaySound(path);
            }catch(java.lang.Exception e)
            {}
        }
    }
    public static String FP_GetClassLevelString(int classLevel)
    {
        String result  = "Undefined";
        switch(classLevel)
        {
            case FC320SDKWrapper.U_CLASS_A:
                result = "CLASS A";
                break;
            case FC320SDKWrapper.U_CLASS_B:
                result = "CLASS B";
                break;
            case FC320SDKWrapper.U_CLASS_C:
                result = "CLASS C";
                break;
            case FC320SDKWrapper.U_CLASS_D:
                result = "CLASS D";
                break;
            case FC320SDKWrapper.U_CLASS_E:
                result = "CLASS E";
                break;
        }
        return result;
    }
    
    public String FP_GetStatusMsg()
    {
        return lblStatus.getText();
    }
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JPanel msgPanel = null;;
	private JPanel fpImgPanel = null;;
	private JPanel fpStatusPanel = null;;
	
	private JLabel lblPos = null;;
	private JLabel lblWet = null;;
	private JLabel lblDry = null;;

	private FPImageCanvas fpImgCanvas;
	private FPImageData fpImgData;
	
	private JLabel lblEnrollStep[]=new JLabel[6];
	private JTextPane lblStatus = null;
	public static Icon ICON_OK = null;
	public static Icon ICON_FAIL = null;
	private static final int perferredWidth = 350;
	private static final int perferredHeight = 230;

	private static boolean action = true;
	private static FC320SDKWrapper wsaBrowserAPI;
	private static long theHConnect=-1;
	private static int theSensorType=0;
	// End of variables declaration//GEN-END:variables
	private Image bgImage = null;
    private static String m_hardwareCode = null;
    
    public static boolean m_EnableSound = true;
    static SDKClientResourceBundle  myResourceBundle = null;
    static
    {
         try
        {
            myResourceBundle = SDKClientResourceBundle.getDefaultInstance();
        }catch(java.util.MissingResourceException ignore)
        {}
    }
    public String FP_GetHardwareCode()throws java.lang.Exception
    {
        FP_ConnectDevice();
        return m_hardwareCode;
    }
    private void chkHandleAndAlertMsg(long deviceHandle) 
    {
        if (deviceHandle==FC320SDKWrapper.SENSOR_ALREADY_ON_USE)
        {
            int rtn = javax.swing.JOptionPane.showConfirmDialog(this,  getResourceString("Can't connected to STARTEK-ENG supported Sensor")+"!\r\n"+getResourceString("Fingerprint Reader has been occupied by other applications") +".\r\n"+ getResourceString("Please check and restart the application again")+"!\r\n"+getResourceString("Would you like to close the application now?") 
                                                    ,getResourceString("Would you like to close the application now?") 
                                                    ,javax.swing.JOptionPane.YES_NO_OPTION
                                                    ,javax.swing.JOptionPane.ERROR_MESSAGE);
            if (javax.swing.JOptionPane.YES_OPTION == rtn)
            {
                System.exit(0);
            }
        }
    }
    public synchronized void FP_ConnectDevice()
    {
        //DBGMSG("==>FP_ConnectDevice:"+theHConnect);
        if (theHConnect<=0)
        {
            //setSensorReady(false,null);
            try
            {
                theHConnect = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                if (theHConnect<=0)
                {
                    try
                    {
                        wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
                        theHConnect = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
                    }catch(java.lang.Exception e)
                    {
                    }
                }
                if (theHConnect>0)
                {
                    theSensorType = wsaBrowserAPI.FP_GetSensorType();
                    m_hardwareCode = wsaBrowserAPI.WSA_GetHardwareCode(theHConnect);
                }
            }catch(java.lang.Exception e)
            {
            }
        }
        chkHandleAndAlertMsg(theHConnect);
        //DBGMSG("<==FP_ConnectDevice:"+theHConnect);
    }
    public static void main(String[] args)
	{
		FPPanel panel = new FPPanel();

		javax.swing.JFrame frame = new javax.swing.JFrame();
		//EXIT_ON_CLOSE == 3
		frame.setDefaultCloseOperation(3);
		frame.setTitle("FPPanel Test");
		frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
		frame.setSize(350,230);
        frame.pack();
        //frame.getContentPane().setSize(400,315);
		java.awt.Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
		FPImageData tmpImg = new FPImageData();
		long rtn = panel.getRawImageData(tmpImg);
        panel.getFPEnrollISOMinutia();
        
        System.out.println(rtn);
		/*
		byte[] fpcode = panel.getFPEnrollCode();

		byte[] pcode = panel.getFPPrimaryCode();
		if (fpcode!=null && pcode!=null)
		{
			for (int i=0;i<wsaBrowserAPI.FP_CODE_LENGTH;i++)
			{
				if ((i%16)==0)
				{
					System.out.print("\r\nfp_code:");
				}
				System.out.print(fpcode[i]+" ");
			}
			
			for (int i=0;i<wsaBrowserAPI.FP_CODE_LENGTH;i++)
			{
				if ((i%16)==0)
				{
					System.out.print("\r\np_code:");
				}
				System.out.print(pcode[i]+" ");
			}
			long theHConnect=0;
			try
			{
				theHConnect = wsaBrowserAPI.FP_ConnectCaptureDriver(0);
				System.out.println ("\r\nFP_CodeMatchEx score:"+wsaBrowserAPI.FP_CodeMatchEx(theHConnect,pcode,fpcode,0));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}finally
			{
				if (theHConnect>0)
				{
					wsaBrowserAPI.FP_DisconnectCaptureDriver(theHConnect);
				}
			}
		}*/
	}
}