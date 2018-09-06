/*
 * FPPanelLite.java
 *
 * Created on 2007年10月25日, 下午 3:35
 */

package com.startek_eng.fc320sdk.ui;

import com.startek_eng.sdk.client.WSABrowserAPIWrapper;
import com.startek_eng.sdk.SensorConnectFailedException;
import com.startek_eng.sdk.LibraryLoadFailedException;

import com.startek_eng.fpobject.FPImageData;
/**
 *
 * @author  liaocl
 */
public class FPPanelLite extends javax.swing.JPanel {
    
    /** Creates new form FPPanelLite */
    static FPPanelLite instance = null;
    
	public static FPPanelLite getInstance()
	{
		if (instance == null)
		{
			instance = new FPPanelLite();
		}
		return instance;
	}
    public FPPanelLite() {
        //DBGMSG("==>FPPanelLite");
        initComponents();
        m_FPImageCanvas = new FPImageCanvas();
		m_FPImageCanvas.clear();
        m_FPImageCanvas.setLayout(new java.awt.BorderLayout());
        //this.removeAll();
        if (m_WSABrowserAPI==null)
		{
			try
			{
				m_WSABrowserAPI = WSABrowserAPIWrapper.getInstance();
                m_hConnect = 0;
                /*if (m_WSABrowserAPI!=null)
                {
                    try
                    {
                        m_hConnect = m_WSABrowserAPI.FP_ConnectCaptureDriver(0);
                    }catch(java.lang.Exception e)
                    {}
                }*/
			}catch (SensorConnectFailedException e)
			{
				m_WSABrowserAPI = null;
                m_hConnect = 0;
				e.printStackTrace();
			}
			catch (LibraryLoadFailedException e)
			{
				m_WSABrowserAPI = null;
                m_hConnect = 0;
				e.printStackTrace();
			}
		}
        m_FPImageCanvas.setVisible(false);
        m_lblStatus.setVisible(true);
        this.add(m_FPImageCanvas, java.awt.BorderLayout.CENTER);
        this.add(m_lblStatus, java.awt.BorderLayout.SOUTH);
        //DBGMSG("<==FPPanelLite");
    }
    protected void reInitial()
    {
        //DBGMSG("==>reInitial");
        try
		{   if (m_WSABrowserAPI!=null)
                m_WSABrowserAPI.reInitial();
        }catch(java.lang.Exception e)
        {
        }
        m_hConnect = 0;
        //DBGMSG("<==reInitial");
    }
    protected void finalize() throws java.lang.Throwable
    {
        if (m_WSABrowserAPI!=null && m_hConnect>0)
        {
            try
            {
                if (m_WSABrowserAPI!=null)
                    m_WSABrowserAPI.FP_DisconnectCaptureDriver(m_hConnect);
                m_hConnect = -1;
            }catch(java.lang.Exception e)
            {
            }
        }
        super.finalize();
    }
    synchronized void setSensorReady(boolean ready,String status)
    {
        if (m_SensorReady != ready)
        {
            //this.removeAll();
            if (ready)
            {
                m_lblStatus.setVisible(false);
                m_FPImageCanvas.setVisible(true);
                //this.add(m_FPImageCanvas, java.awt.BorderLayout.CENTER);
            }else
            {
                m_FPImageCanvas.setVisible(false);
                m_lblStatus.setVisible(true);
                FP_Clear();
                //this.add(m_lblStatus, java.awt.BorderLayout.CENTER);
            }
            m_SensorReady = ready;
            this.updateUI();
            this.repaint();
            if (status!=null)
                m_lblStatus.setText(status);
            else
                m_lblStatus.setText("Sensor Not Ready");
        }
    }
    int showImage(byte[] rawData,int width, int height, int snapRtn)
	{
		int rtn=WSABrowserAPIWrapper.OK;
		m_FPImageCanvas.drawFPImage(rawData,width,height);
		switch (snapRtn & WSABrowserAPIWrapper.U_POSITION_CHECK_MASK )
		{
			case WSABrowserAPIWrapper.U_POSITION_NO_FP:
			case WSABrowserAPIWrapper.U_POSITION_TOO_LOW:
			case WSABrowserAPIWrapper.U_POSITION_TOO_TOP:
			case WSABrowserAPIWrapper.U_POSITION_TOO_RIGHT:
			case WSABrowserAPIWrapper.U_POSITION_TOO_LEFT:
			case WSABrowserAPIWrapper.U_POSITION_TOO_LOW_RIGHT:
			case WSABrowserAPIWrapper.U_POSITION_TOO_LOW_LEFT:
			case WSABrowserAPIWrapper.U_POSITION_TOO_TOP_RIGHT:
			case WSABrowserAPIWrapper.U_POSITION_TOO_TOP_LEFT:
					//lblPos.setEnabled(true);
					rtn=WSABrowserAPIWrapper.FAIL;
					break;
			case WSABrowserAPIWrapper.U_POSITION_OK:
			default:
					//lblPos.setEnabled(false);
					break;
		}
		switch (snapRtn & WSABrowserAPIWrapper.U_DENSITY_CHECK_MASK)
		{
			case WSABrowserAPIWrapper.U_DENSITY_TOO_DARK:
					//lblWet.setEnabled(true);
					//lblDry.setEnabled(false);
					rtn=WSABrowserAPIWrapper.FAIL;
                    setStatus(getResourceString("Too Wet, Dry your finger with handkerchief"));
					break;
			case WSABrowserAPIWrapper.U_DENSITY_LITTLE_LIGHT:
			case WSABrowserAPIWrapper.U_DENSITY_TOO_LIGHT:
					//lblWet.setEnabled(false);
					//lblDry.setEnabled(true);
                    setStatus(getResourceString("Too Dry, Please \"slight\" moisturize your finger"));
					rtn=WSABrowserAPIWrapper.FAIL;
					break;
			case WSABrowserAPIWrapper.U_DENSITY_AMBIGUOUS:
					//lblWet.setEnabled(true);
					//lblDry.setEnabled(true);
					setStatus(getResourceString("Please place your finger on sensor"));
					rtn=WSABrowserAPIWrapper.FAIL;
					break;
			default :
					//lblWet.setEnabled(false);
					//lblDry.setEnabled(false);
					break;
		}
		//setStatus(getResourceString("snapRtn: "+snapRtn + " rtn :"+rtn);
		return rtn;
	}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        m_lblStatus = new javax.swing.JLabel();

        m_lblStatus.setFont(new java.awt.Font("Verdana", 1, 12));
        m_lblStatus.setForeground(new java.awt.Color(255, 0, 0));
        m_lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_lblStatus.setText("Sensor Not Ready");
        m_lblStatus.setMaximumSize(new java.awt.Dimension(118, 100));
        m_lblStatus.setPreferredSize(new java.awt.Dimension(118, 100));

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(150, 150));
    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel m_lblStatus;
    // End of variables declaration//GEN-END:variables
    private String m_statusMsg = null;
    private FPImageCanvas m_FPImageCanvas = null;
    private boolean m_SensorReady = false;
    private static WSABrowserAPIWrapper m_WSABrowserAPI;
    
    static long m_hConnect = 0;
    static String m_hardwareCode = null;
	static int m_sensorType=-1;
    
    static SDKClientResourceBundle  myResourceBundle = null;
	static boolean m_action = true;
    static
    {
         try
        {
            myResourceBundle = SDKClientResourceBundle.getDefaultInstance();
        }catch(java.util.MissingResourceException ignore)
        {}
    }
    private void chkHandleAndAlertMsg(long deviceHandle) 
    {
        if (m_hConnect<=0)
            setStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
        if (deviceHandle==WSABrowserAPIWrapper.SENSOR_ALREADY_ON_USE)
        {
            
            int rtn = javax.swing.JOptionPane.showConfirmDialog(this,  getResourceString("Can't connect to STARTEK-ENG supported reader")+"!\r\n"+getResourceString("Fingerprint Reader has been occupied by other applications") +".\r\n"+ getResourceString("Please check and restart the application again")+"!\r\n"+getResourceString("Would you like to close the application now?") 
                                                    ,getResourceString("Would you like to close the application now?") 
                                                    ,javax.swing.JOptionPane.YES_NO_OPTION
                                                    ,javax.swing.JOptionPane.ERROR_MESSAGE);
            if (javax.swing.JOptionPane.YES_OPTION == rtn)
            {
                System.exit(0);
            }
        }
    }
    void setStatus(String msg)
    {
        m_statusMsg = msg;
        //System.out.println(msg);
    }
    void setStatus(String msg,String title)
    {
        m_statusMsg = msg;
        //System.out.println(msg);
    }
    void setStatus(java.lang.Exception e)
    {
        m_statusMsg = e.toString();
        e.printStackTrace();
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
    public String FP_GetStatusMsg()
    {
        return m_statusMsg;
    }
    public void FP_Sound(String path)
    {
        //DBGMSG("==>FP_Sound");
        if (m_WSABrowserAPI!=null)
        {
            try
            {
                m_WSABrowserAPI.WSA_PlaySound(path);
            }catch(java.lang.Exception e)
            {}
        }
        //DBGMSG("<==FP_Sound");
    }
    public void FP_Stop()
	{
        //DBGMSG("==>FP_Stop:"+m_hConnect);
        m_statusMsg = "";
        if (!m_action && m_hConnect>0)
        {
            try
            {
                if (m_WSABrowserAPI!=null)
                    m_WSABrowserAPI.FP_DisconnectCaptureDriver(m_hConnect);
                m_hConnect = -1;
            }catch(java.lang.Exception e)
            {
            }
        }
        m_action = false;
        //DBGMSG("<==FP_Stop:"+m_hConnect);
    }
    public synchronized String FP_GetHardwareCode()throws java.lang.Exception
    {
        //DBGMSG("==>FP_GetHardwareCode");
        FP_ConnectDevice();
        //DBGMSG("<==FP_GetHardwareCode");
        return m_hardwareCode;
    }
    public synchronized boolean FP_ConnectDevice()
    {
        //DBGMSG("==>FP_ConnectDevice:"+m_hConnect);
        if (m_hConnect<=0)
        {
            //setSensorReady(false,null);
            try
            {
                m_hConnect = m_WSABrowserAPI.FP_ConnectCaptureDriver(0);
                if (m_hConnect<=0)
                {
                    try
                    {
                        m_WSABrowserAPI.FP_DisconnectCaptureDriver(m_hConnect);
                        m_hConnect = m_WSABrowserAPI.FP_ConnectCaptureDriver(0);
                    }catch(java.lang.Exception e)
                    {
                    }
                }
                if (m_hConnect>0)
                {
                    m_sensorType = m_WSABrowserAPI.FP_GetSensorType();
                    m_hardwareCode = m_WSABrowserAPI.WSA_GetHardwareCode(m_hConnect);
                }
            }catch(java.lang.Exception e)
            {
            }
        }
        chkHandleAndAlertMsg(m_hConnect);
        //DBGMSG("<==FP_ConnectDevice:"+m_hConnect);
        return (m_hConnect>0);
    }
    public void FP_Clear()
    {
        //DBGMSG("==>FP_Clear");
        if (m_FPImageCanvas!=null)
            m_FPImageCanvas.clear();
        //DBGMSG("<==FP_Clear");
    }
    public byte[] FP_EncryptData(byte[] srcData, String publicKey) throws java.lang.Exception
    {
        //DBGMSG("==>FP_EncryptData");
        byte[] result = null;
        if (m_WSABrowserAPI!=null && srcData!=null && publicKey!=null)
        {
            result = new byte[srcData.length];
            m_WSABrowserAPI.FP_Encrypt(srcData, result, srcData.length, publicKey,publicKey.length());
        }
        //DBGMSG("<==FP_EncryptData");
        return result;
    }
    
    private void DBGMSG(Object msgObj)
    {
System.out.println("["+System.currentTimeMillis()+"]"+msgObj);        
    }
    public synchronized byte[] FP_GetISOMinutia(FPImageData fpImgData)
    {
        //DBGMSG("==>FP_GetISOMinutia:"+m_hConnect);
        m_action = true;
        byte[] result = null;
        m_statusMsg = "";
		long hFPImage = -1;
		long hCaptureHandle = -1;
		int rtn = WSABrowserAPIWrapper.FAIL;
        int counter = 0;
        if (m_WSABrowserAPI !=null)
		{
			this.setVisible(true);
			this.repaint();
			try
			{
				if (m_hConnect<=0)
				{
                    setSensorReady(false,null);
                    FP_ConnectDevice();
				}
				if (m_hConnect>0)
				{
                    hCaptureHandle = m_WSABrowserAPI.FP_CreateCaptureHandle(m_hConnect);
					hFPImage = m_WSABrowserAPI.FP_CreateImageHandle(m_hConnect, (byte)WSABrowserAPIWrapper.GRAY_IMAGE, WSABrowserAPIWrapper.SMALL);
					
					if (hFPImage>0 && hCaptureHandle>0)
					{
                        setSensorReady(true,null);
						setStatus(getResourceString("Please place your finger on sensor"));
						byte[] bRawData = new byte[WSABrowserAPIWrapper.FP_IMAGE_WIDTH*WSABrowserAPIWrapper.FP_IMAGE_HEIGHT];
						int[] nImgWidthHeight = new int[2];
						if (m_sensorType == WSABrowserAPIWrapper.SENSORTYPE_AREA)
						{
							/*while (m_action && (rtn=m_WSABrowserAPI.FP_CheckBlank(m_hConnect))!= WSABrowserAPIWrapper.OK)
							{
								//rtn = m_WSABrowserAPI.FP_Snap(m_hConnect);
								m_WSABrowserAPI.FP_GetImageData(m_hConnect,bRawData,nImgWidthHeight);
								showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
								//showImage(m_hConnect,hFPImage,rtn);
								if ((counter++)%10==0)
								{
									setStatus(getResourceString("Please remove your finger from sensor"));
								}
							}*/
						}
						byte[] minutiae = new byte[WSABrowserAPIWrapper.MAX_ISOMINUTIAE_LENGTH];
						counter = 0;
						while (m_action && m_hConnect>0 && hCaptureHandle>0 && hFPImage>0)
						{
							rtn = m_WSABrowserAPI.FP_Capture(m_hConnect,hCaptureHandle);//m_WSABrowserAPI.FP_Snap(m_hConnect);
                            if (m_action && m_sensorType == WSABrowserAPIWrapper.SENSORTYPE_AREA)
                            {
                                if (((rtn & WSABrowserAPIWrapper.U_POSITION_CHECK_MASK)==WSABrowserAPIWrapper.U_POSITION_NO_FP) 
                                    || (m_WSABrowserAPI.FP_CheckBlank(m_hConnect)== WSABrowserAPIWrapper.OK))
                                {
                                    try
                                    {
                                        Thread.sleep(400);
                                    }catch(java.lang.InterruptedException e)
                                    {
                                    }
                                }
                            }
							m_WSABrowserAPI.FP_GetImageData(m_hConnect,bRawData,nImgWidthHeight);
							showImage(bRawData,nImgWidthHeight[0],nImgWidthHeight[1],rtn);
							if (m_action && rtn == WSABrowserAPIWrapper.OK)
							{
								rtn = m_WSABrowserAPI.WSA_ImageToISOWithExtension(m_hConnect,bRawData,(short)nImgWidthHeight[0],(short)nImgWidthHeight[1],minutiae);
								if (rtn >= WSABrowserAPIWrapper.OK && 
                                    rtn <= WSABrowserAPIWrapper.MAX_ISOMINUTIAE_LENGTH &&
									rtn >= WSABrowserAPIWrapper.MIN_ISOMINUTIAE_LENGTH)
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
                                        if (fpImgData!=null)
                                            fpImgData.setImageData(bRawData,nImgWidthHeight[0],nImgWidthHeight[1]);
                                        setStatus(getResourceString("Get Image OK"));
                                        break;
                                    }
								}else
									setStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								
							}else
							{
								/*if (((counter++)%20==0) && m_sensorType == WSABrowserAPIWrapper.SENSORTYPE_CHIPLINE)
								{
									tmpHandle = m_WSABrowserAPI.FP_ConnectCaptureDriver(0);
									m_sensorType = m_WSABrowserAPI.FP_GetSensorType();
									if (tmpHandle<=0)
									{
										m_WSABrowserAPI.FP_DestroyImageHandle (m_hConnect, hFPImage);
										hFPImage = -1;
										m_WSABrowserAPI.FP_DestroyCaptureHandle (m_hConnect, hCaptureHandle);
										hCaptureHandle = -1;
									}
								}*/
								if (m_hConnect>0)
								{
									setStatus(getResourceString("Please place your finger on sensor"));//+ rtn
								}
							}
						}
					}else
					{
						setStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_CreateImageHandle");
					}
				}else
				{
					setStatus(getResourceString("Can't connect to STARTEK-ENG supported reader"),"FP_ConnectCaptureDriver");
				}
			}catch(Exception e)
			{
				setStatus(e);
			}finally
			{
				if ( m_WSABrowserAPI!=null && m_hConnect>0)
				{
					if (hFPImage>0)
					{
						try
						{	m_WSABrowserAPI.FP_DestroyImageHandle (m_hConnect, hFPImage);
                            hFPImage =  0;
						}catch (Exception e){setStatus(e);}
					}
					if (hCaptureHandle>0)
					{
						try
						{	m_WSABrowserAPI.FP_DestroyCaptureHandle (m_hConnect, hCaptureHandle);
                            hCaptureHandle =  0;
						}catch (Exception e){setStatus(e);}
					}
                    if (!m_action && m_hConnect>0)
                    {
                        try
                        {
                            if (m_WSABrowserAPI!=null)
                                m_WSABrowserAPI.FP_DisconnectCaptureDriver(m_hConnect);
                            m_hConnect = -1;
                        }catch(java.lang.Exception e)
                        {
                        }
                    }
                    setSensorReady(false,"");
				}
				if (m_action && result == null)
				{
					reInitial();
				}
                m_action = false;
			}
        }
        //DBGMSG("<==FP_GetISOMinutia:"+m_hConnect);
        return result;
    }
    public static void main(String[] args)
	{
		FPPanelLite panel = new FPPanelLite();

		javax.swing.JFrame frame = new javax.swing.JFrame();
		//EXIT_ON_CLOSE == 3
		frame.setDefaultCloseOperation(3);
		frame.setTitle("FPPanel Test");
        frame.getContentPane().setLayout(new java.awt.BorderLayout());
		frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
		frame.setSize(400,280);
        frame.pack();
        //frame.getContentPane().setSize(400,315);
		java.awt.Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);

        panel.setSensorReady(false,"");
        try
        {
            FPImageData tmpImg = new FPImageData();
            byte[] result = panel.FP_GetISOMinutia(tmpImg);
        }catch(java.lang.Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Finished");
        try
        {
            Thread.sleep(3000);
        }catch(java.lang.InterruptedException e)
        {
        }
        //System.out.println(rtn);
	}
}
