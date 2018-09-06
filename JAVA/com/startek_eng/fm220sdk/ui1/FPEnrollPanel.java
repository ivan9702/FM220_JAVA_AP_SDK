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
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.image.ColorModel;
import javax.swing.ImageIcon;

import com.startek_eng.fc320sdk.FC320SDKWrapper;
import com.startek_eng.fc320sdk.SensorConnectFailedException;
import com.startek_eng.fc320sdk.LibraryLoadFailedException;
import com.startek_eng.fpobject.FPImageData;  //????
/**
 *
 * @author  liaocl
 */
public class FPEnrollPanel extends JPanel
{
	
	/** Creates new form FPPanel */
	public FPEnrollPanel()
	{
		super();
		initComponents();
		if (fm200SDK==null)
		{
			try
			{
				fm200SDK = FM200SDKWrapper.getInstance();
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
	}
	static FPEnrollPanel instance = null;
	public static FPEnrollPanel getIntance()
	{
		if (instance == null)
		{
			instance = new FPEnrollPanel();
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
		tmpPanel.setOpaque(false);
		//fpImgCanvas.setOpaque(false);
		//tmpPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(100,100,100)));
		GridBagConstraints cbag = new GridBagConstraints();
		
		cbag.fill = GridBagConstraints.BOTH; //GridBagConstraints.HORIZONTAL;
		//cbag.anchor = GridBagConstraints.PAGE_START ; 
		//cbag.insets = new Insets(25,25,25,25);	//margion
		cbag.weightx =1;		//x(width)的比例
		cbag.weighty =1;		//y(height)的比例
		cbag.gridx = 0;			//位置
		cbag.gridy = 0;			//0,0位置 Position
		tmpPanel.add(fpImgCanvas,cbag);
		tmpPanel.setPreferredSize(new Dimension(130, 130));
		fpImgPanel.add(tmpPanel,BorderLayout.CENTER);
	}
	private void initStatusPanel()
	{
		JPanel tmpPanel = new JPanel(new GridBagLayout());
		tmpPanel.setOpaque(false);
		//tmpPanel.setBorder(BorderFactory.createTitledBorder("status"));
		tmpPanel.setVisible(true);

		lblPos = new JLabel();
		lblWet = new JLabel();
		lblDry = new JLabel();
		try
		{
			lblPos.setIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/NG.JPG"),"NG.JPG")));
			lblWet.setIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/NG.JPG"),"NG.JPG")));
			lblDry.setIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/NG.JPG"),"NG.JPG")));
			lblPos.setDisabledIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/OK.JPG"),"OK.JPG")));
			lblWet.setDisabledIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/OK.JPG"),"OK.JPG")));
			lblDry.setDisabledIcon(new ImageIcon(new java.net.URL(this.getClass().getResource("img/OK.JPG"),"OK.JPG")));
			lblWet.setPreferredSize(new Dimension(50,50));
			lblDry.setPreferredSize(new Dimension(50,50));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		GridBagConstraints cbag = new GridBagConstraints();
		
		cbag.fill = GridBagConstraints.BOTH; //GridBagConstraints.HORIZONTAL;
		//cbag.anchor = GridBagConstraints.PAGE_START ; 
		cbag.insets = new Insets(10,10,10,10);	//margion
		cbag.weightx = 0.0;		//x(width)的比例
		cbag.weighty = 0.3;		//y(height)的比例
		cbag.gridx = 0;			//位置
		cbag.gridy = 0;			//0,0位置 Position
		JLabel tmp = new JLabel("Position", JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(100,100));
		tmpPanel.add(tmp,cbag);
		cbag.gridx = 0;	
		cbag.gridy = 1;			//0,1位置 Web
		tmp = new JLabel("Wet", JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(100,100));
		tmpPanel.add(tmp,cbag);
		cbag.gridx = 0;
		cbag.gridy = 2;			//0,2位置 Dry
		tmp = new JLabel("Dry", JLabel.LEFT);
		tmp.setPreferredSize(new Dimension(100,100));
		tmpPanel.add(tmp,cbag);
		
		cbag.insets = new Insets(10,10,10,60);	//margion
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
		msgPanel.setPreferredSize(new Dimension(400,70));
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
		cbag.weighty = 0.4;	//y(height)的比例
		cbag.gridx = 0;			//位置
		cbag.gridy = 1;			//位置
		cbag.gridwidth = 6;		//單位
		//msgPanel.setBackground(new java.awt.Color(255,0,0));
		this.add(msgPanel,cbag);
		cbag.fill = GridBagConstraints.BOTH;
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
		this.add(fpStatusPanel,cbag);
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
	public void showStatus(String msg)
	{
		if (action)
		{
			//System.out.println(msg);
			lblStatus.setText(msg);
		}else
		{
			//System.out.println("Cancel the working job");
		}
	}
	private int showImage(long theHConnect,long hFPImage, int snapRtn)
	{
		int rtn=FM200SDKWrapper.FAIL;
		/*if (theHConnect>0 && hFPImage>0 && fpImgCanvas != null)
		{
			try
			{
				rtn = fm200SDK.FP_GetImage(theHConnect, hFPImage);
				if (rtn==FM200SDKWrapper.OK)
				{
					rtn = fm200SDK.FP_DisplayImage(theHConnect, fpImgCanvas, hFPImage, 0, 0,(short) fpImgCanvas.getWidth(),(short)fpImgCanvas.getHeight());
				}
			}
			catch (Exception e)
			{
				showStatus(e);
				//showStatus(e.getMessage());
			}finally
			{
				switch (snapRtn & FM200SDKWrapper.U_POSITION_CHECK_MASK )
				{
					case FM200SDKWrapper.U_POSITION_NO_FP:
					case FM200SDKWrapper.U_POSITION_TOO_LOW:
					case FM200SDKWrapper.U_POSITION_TOO_TOP:
					case FM200SDKWrapper.U_POSITION_TOO_RIGHT:
					case FM200SDKWrapper.U_POSITION_TOO_LEFT:
					case FM200SDKWrapper.U_POSITION_TOO_LOW_RIGHT:
					case FM200SDKWrapper.U_POSITION_TOO_LOW_LEFT:
					case FM200SDKWrapper.U_POSITION_TOO_TOP_RIGHT:
					case FM200SDKWrapper.U_POSITION_TOO_TOP_LEFT:
							lblPos.setEnabled(true);
							rtn=FM200SDKWrapper.FAIL;
							break;
					case FM200SDKWrapper.U_POSITION_OK:
					default:
							lblPos.setEnabled(false);
							break;
				}
				switch (snapRtn & FM200SDKWrapper.U_DENSITY_CHECK_MASK)
				{
					case FM200SDKWrapper.U_DENSITY_TOO_DARK:
							lblWet.setEnabled(true);
							lblDry.setEnabled(false);
							rtn=FM200SDKWrapper.FAIL;
							break;
					case FM200SDKWrapper.U_DENSITY_LITTLE_LIGHT:
					case FM200SDKWrapper.U_DENSITY_TOO_LIGHT:
							lblWet.setEnabled(false);
							lblDry.setEnabled(true);
							rtn=FM200SDKWrapper.FAIL;
							break;
					case FM200SDKWrapper.U_DENSITY_AMBIGUOUS:
							lblWet.setEnabled(true);
							lblDry.setEnabled(true);
							rtn=FM200SDKWrapper.FAIL;
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
		int rtn=FM200SDKWrapper.OK;
		fpImgCanvas.drawFPImage(rawData,width,height);
		switch (snapRtn & FM200SDKWrapper.U_POSITION_CHECK_MASK )
		{
			case FM200SDKWrapper.U_POSITION_NO_FP:
			case FM200SDKWrapper.U_POSITION_TOO_LOW:
			case FM200SDKWrapper.U_POSITION_TOO_TOP:
			case FM200SDKWrapper.U_POSITION_TOO_RIGHT:
			case FM200SDKWrapper.U_POSITION_TOO_LEFT:
			case FM200SDKWrapper.U_POSITION_TOO_LOW_RIGHT:
			case FM200SDKWrapper.U_POSITION_TOO_LOW_LEFT:
			case FM200SDKWrapper.U_POSITION_TOO_TOP_RIGHT:
			case FM200SDKWrapper.U_POSITION_TOO_TOP_LEFT:
					lblPos.setEnabled(true);
					rtn=FM200SDKWrapper.FAIL;
					break;
			case FM200SDKWrapper.U_POSITION_OK:
			default:
					lblPos.setEnabled(false);
					break;
		}
		switch (snapRtn & FM200SDKWrapper.U_DENSITY_CHECK_MASK)
		{
			case FM200SDKWrapper.U_DENSITY_TOO_DARK:
					lblWet.setEnabled(true);
					lblDry.setEnabled(false);
					rtn=FM200SDKWrapper.FAIL;
					break;
			case FM200SDKWrapper.U_DENSITY_LITTLE_LIGHT:
			case FM200SDKWrapper.U_DENSITY_TOO_LIGHT:
					lblWet.setEnabled(false);
					lblDry.setEnabled(true);
					rtn=FM200SDKWrapper.FAIL;
					break;
			case FM200SDKWrapper.U_DENSITY_AMBIGUOUS:
					lblWet.setEnabled(true);
					lblDry.setEnabled(true);
					rtn=FM200SDKWrapper.FAIL;
					break;
			default :
					lblWet.setEnabled(false);
					lblDry.setEnabled(false);
					break;
		}
		//showStatus("snapRtn: "+snapRtn + " rtn :"+rtn);
		return rtn;
	}
	public synchronized int getRawImageData(FPImageData fpImgData)
	{
		int rtn = FM200SDKWrapper.FAIL;

		//long theHConnect = 0;
		long hFPImage = 0;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
		if (fm200SDK !=null && fpImgData != null)
		{
			showStatus("Please place your finger on sensor");
			try
			{
				if (theHConnect<=0)
					theHConnect = fm200SDK.FP_ConnectCaptureDriver(0);
				if (theHConnect>0)
				{
					hFPImage = fm200SDK.FP_CreateImageHandle(theHConnect, (byte)FM200SDKWrapper.GRAY_IMAGE, FM200SDKWrapper.SMALL);
					if (hFPImage>0)
					{
						
						byte[] bRawData = new byte[FM200SDKWrapper.FP_IMAGE_WIDTH*FM200SDKWrapper.FP_IMAGE_HEIGHT];
						short[] nImgWidthHeight = new short[2];
						while (action && (rtn=fm200SDK.FP_CheckBlank(theHConnect))!= FM200SDKWrapper.OK)
						{
							//rtn = fm200SDK.FP_Snap(theHConnect);
							fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if ((counter++)%10==0)
							{
								showStatus("Please remove your finger from sensor");
							}
						}
						int okCount = 2;
						while (action)
						{
							rtn = fm200SDK.FP_Snap(theHConnect);
							fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if (rtn == FM200SDKWrapper.OK)
							{
								if (fpImgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]))
								{
									okCount--;
									if (okCount<0)
									{
										break;
									}
								}
							}else okCount = 2;
							showStatus("Please place your finger on sensor");
						}
					}else
					{
						showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_CreateImageHandle");
					}
				}else
				{
					showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( fm200SDK!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						fm200SDK.FP_DestroyImageHandle (theHConnect, hFPImage);
					}
					//fm200SDK.FP_DisconnectCaptureDriver(theHConnect);
				}
			}
		}
		return rtn;
	}
	public synchronized byte[] getFPEnrollCode()
	{
		byte result[]=null;
		int rtn = 0;

		//long theHConnect = 0;
		long hFPImage = 0;
		long hFPEnroll = 0;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(true);
			lblEnrollStep[i].setEnabled(true);
		}
		if (fm200SDK !=null)
		{
			this.setVisible(true);
			this.repaint();
			showStatus("Please place your finger on sensor");
			try
			{
				if (theHConnect<=0)
					theHConnect = fm200SDK.FP_ConnectCaptureDriver(0);
				if (theHConnect>0)
				{
					hFPImage = fm200SDK.FP_CreateImageHandle(theHConnect, (byte)FM200SDKWrapper.GRAY_IMAGE, FM200SDKWrapper.SMALL);
					hFPEnroll = fm200SDK.FP_CreateEnrollHandle(theHConnect, (byte)FM200SDKWrapper.DEFAULT_MODE);
					//remove finger
					if (hFPImage>0 && hFPEnroll>0)
					{
						byte[] bRawData = new byte[FM200SDKWrapper.FP_IMAGE_WIDTH*FM200SDKWrapper.FP_IMAGE_HEIGHT];
						short[] nImgWidthHeight = new short[2];
						byte[] p_code = new byte[FM200SDKWrapper.FP_CODE_LENGTH];
						byte[] fp_code = new byte[FM200SDKWrapper.FP_CODE_LENGTH];
						java.util.Arrays.fill(fp_code,(byte)0);
						int enrollCounter = 0;
						while (action)
						{
							//Remove Finger
							while (action && (rtn=fm200SDK.FP_CheckBlank(theHConnect))!= FM200SDKWrapper.OK)
							{
								//rtn = fm200SDK.FP_Snap(theHConnect);
								fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
								if ((counter++)%10==0)
								{
									showStatus("Please remove your finger from sensor");
								}
							}
							//Place Finger
							while (action)
							{
								rtn = fm200SDK.FP_Snap(theHConnect);
								fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								showImage(theHConnect,hFPImage,rtn);
								if (rtn == FM200SDKWrapper.OK)
								{
									rtn = fm200SDK.FP_GetPrimaryCodeEx(theHConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],p_code);
									if (rtn == fm200SDK.OK)
									{
										showStatus("FP_GetPrimaryCodeEx:"+rtn);
										break;
									}
								}
								showStatus("Please place your finger on sensor");
							}
							//do Enroll
							if (rtn == fm200SDK.OK)
							{
								rtn  = fm200SDK.FP_Enroll(theHConnect,hFPEnroll, p_code, fp_code);
								if (rtn == FM200SDKWrapper.U_NOT_YET)
								{
									lblEnrollStep[enrollCounter++].setEnabled(false);
									if (enrollCounter>=lblEnrollStep.length)
									{
										showStatus("Enroll Failed, Please retry");
										break;
									}
								}
								if ((rtn >= FM200SDKWrapper.U_CLASS_A) && (rtn <= FM200SDKWrapper.U_CLASS_E))
								{
									lblEnrollStep[enrollCounter++].setEnabled(false);
									result = new byte[FM200SDKWrapper.FP_CODE_LENGTH];
									System.arraycopy(fp_code,
													 0,
													 result,
													 0,
													 fp_code.length);
									showStatus("Enroll Success");
									break;
								}else if (rtn == FM200SDKWrapper.FAIL)
								{
									showStatus("Enroll Failed");
									break;
								}
							}
						}
					}else
					{
						showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_CreateImageHandle");
					}
				}else
				{
					showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( fm200SDK!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						fm200SDK.FP_DestroyImageHandle (theHConnect, hFPImage);
					}
					if (hFPEnroll>0)
						fm200SDK.FP_DestroyEnrollHandle(theHConnect,hFPEnroll);
					//fm200SDK.FP_DisconnectCaptureDriver(theHConnect);
				}
			}
		}
		return result;
	}
	public byte[] getFPPrimaryCode()
	{
		int rtn = FM200SDKWrapper.FAIL;
		byte result[] = null;
		//long theHConnect = 0;
		long hFPImage = 0;
		int counter =0;
		action = true;
		for (int i=0;i<lblEnrollStep.length;i++)
		{
			lblEnrollStep[i].setVisible(false);
		}
		if (fm200SDK !=null)
		{
			showStatus("Please place your finger on sensor");
			this.setVisible(true);
			this.repaint();
			try
			{
				if (theHConnect<=0)
					theHConnect = fm200SDK.FP_ConnectCaptureDriver(0);
				if (theHConnect>0)
				{
					hFPImage = fm200SDK.FP_CreateImageHandle(theHConnect, (byte)FM200SDKWrapper.GRAY_IMAGE, FM200SDKWrapper.SMALL);
					if (hFPImage>0)
					{
						
						byte[] bRawData = new byte[FM200SDKWrapper.FP_IMAGE_WIDTH*FM200SDKWrapper.FP_IMAGE_HEIGHT];
						short[] nImgWidthHeight = new short[2];
						while (action && (rtn=fm200SDK.FP_CheckBlank(theHConnect))!= FM200SDKWrapper.OK)
						{
							//rtn = fm200SDK.FP_Snap(theHConnect);
							fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if ((counter++)%10==0)
							{
								showStatus("Please remove your finger from sensor");
							}
						}
						while (action)
						{
							rtn = fm200SDK.FP_Snap(theHConnect);
							fm200SDK.FP_GetImageData(theHConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							showImage(theHConnect,hFPImage,rtn);
							if (rtn == FM200SDKWrapper.OK)
							{
								result = new byte[256];
								java.util.Arrays.fill(result,(byte)0);
								rtn = fm200SDK.FP_GetPrimaryCodeEx(theHConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],result);
								if (rtn == FM200SDKWrapper.OK)
								{
									break;
								}
							}
							showStatus("Please place your finger on sensor");
						}
					}else
					{
						showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_CreateImageHandle");
					}
				}else
				{
					showStatus("Can't connected to STARTEK-ENG suppoerted Sensor","FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				showStatus(e);
			}finally
			{
				if ( fm200SDK!=null && theHConnect>0)
				{
					if (hFPImage>0)
					{
						fm200SDK.FP_DestroyImageHandle (theHConnect, hFPImage);
					}
					//fm200SDK.FP_DisconnectCaptureDriver(theHConnect);
				}
			}
		}
		return result;
	}
	public void stop()
	{
		action = false;
		showStatus("The process have been canceled!");
		if (fm200SDK!=null)
		{
			fm200SDK.FP_DisconnectCaptureDriver(0);
		}
		fpImgCanvas.clear();
		theHConnect=0;
	}
	private Image bgImage = null;;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JPanel msgPanel = null;;
	private JPanel fpImgPanel = null;;
	private JPanel fpStatusPanel = null;;
	
	private JLabel lblPos = null;;
	private JLabel lblWet = null;;
	private JLabel lblDry = null;;

	private FPImageCanvas fpImgCanvas;
	
	private JLabel lblEnrollStep[]=new JLabel[6];
	private JTextPane lblStatus = null;

	private static final int perferredWidth = 500;
	private static final int perferredHeight = 350;

	private static boolean action = true;
	private static FM200SDKWrapper fm200SDK;
	private static long theHConnect=0;
	// End of variables declaration//GEN-END:variables
	public static void main(String[] args)
	{
		FPEnrollPanel panel = new FPEnrollPanel();

		javax.swing.JFrame frame = new javax.swing.JFrame();
		//EXIT_ON_CLOSE == 3
		frame.setDefaultCloseOperation(3);
		frame.setTitle("FPEnrollPanel Test");
		frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
		frame.setSize(530,380);
		java.awt.Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
		/*FPImageData tmpImg = new FPImageData();
		//long rtn = panel.getRawImageData(tmpImg);
		
		byte[] fpcode = panel.getFPEnrollCode();

		byte[] pcode = panel.getFPPrimaryCode();
		if (fpcode!=null && pcode!=null)
		{
			for (int i=0;i<fm200SDK.FP_CODE_LENGTH;i++)
			{
				if ((i%16)==0)
				{
					System.out.print("\r\nfp_code:");
				}
				System.out.print(fpcode[i]+" ");
			}
			
			for (int i=0;i<fm200SDK.FP_CODE_LENGTH;i++)
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
				theHConnect = fm200SDK.FP_ConnectCaptureDriver(0);
				System.out.println ("\r\nFP_CodeMatchEx score:"+fm200SDK.FP_CodeMatchEx(theHConnect,pcode,fpcode,0));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}finally
			{
				if (theHConnect>0)
				{
					fm200SDK.FP_DisconnectCaptureDriver(theHConnect);
				}
			}
		}*/
		
	}
}

class FPImageCanvas extends JPanel//Canvas implements Runnable
{
	public static short FP_IMGSIZE_Height = 128;
	public static short FP_IMGSIZE_Width = 128;

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
	public FPImageCanvas()
	{
		super();
//		setIgnoreRepaint(false);
	}
	public void drawFPImage(byte[] rawData,int imgWidth, int imgHeight)
	{
		this.setVisible(true);
		java.awt.image.MemoryImageSource mis = new java.awt.image.MemoryImageSource(imgWidth, imgHeight, colorModel, 
													 rawData, 0, imgWidth); 
		fpImage = createImage(mis); 
		this.repaint();
	}
	public void paint(Graphics g)
	{
		if (fpImage == null)
		{
			g.setColor(new Color(255, 255, 255) );
			g.clearRect(0,0,this.getWidth(),this.getHeight());
		}else
		{
			g.drawImage(fpImage, 0,0,this.getWidth(),this.getHeight(),this);
		}
	}
	public void update(Graphics g)
	{
		paint(g);
	}
	public void clear()
	{
		fpImage = null;
	}
	public void run()
	{
		this.repaint();
	}
	/*
	//Image offscreenImage = null;
	//Graphics offscr = null;

	public void paint(Graphics g)
	{ 
		Color bg = new Color(100,100,100);
		g.setColor(bg);
		g.fillRect(15,10,getWidth()-30,getHeight()-20);
		g.drawString("This is a test",0,0);
		try
		{
			Color bg = new Color(0,0,0);
			g.setColor(bg);
			g.fillRect(0,0,FP_IMGSIZE_Height,FP_IMGSIZE_Width);
			if (_imageFile!=null && _imageFile.length()>0)
			{
				//check off-screen drawable image is null or not
				if (offscreenImage == null)
				{
					//create off-screen drawable image
					offscreenImage = createImage(FP_IMGSIZE_Height,FP_IMGSIZE_Width);
					offscr = offscreenImage.getGraphics();
				}
				java.io.File tmpFile = new java.io.File(_imageFile);
				Image tmpImg = null;
				if (tmpFile!=null && tmpFile.exists())
				{
					tmpImg = javax.imageio.ImageIO.read(tmpFile);
				}
				if  (offscr!=null && tmpImg!=null)
				{
					offscr.drawImage(tmpImg,0,0,this);
					g.drawImage(offscreenImage,0,0,this);
				}else
				{
					System.out.print("---------------------------------------------------------offscr == NULL");
					g.drawImage(tmpImg,0,0,this);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//lbl_Image.setIcon(new javax.swing.ImageIcon(javax.imageio.ImageIO.read(new java.io.File(fileName))));
		//lbl_Image.updateUI();
	}
	public void update(Graphics g)
	{
		paint(g);
	}*/
	public Dimension getPreferredSize() { 
		return new Dimension(FP_IMGSIZE_Width, FP_IMGSIZE_Height); 
	}
}