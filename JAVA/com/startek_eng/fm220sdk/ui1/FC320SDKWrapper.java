package com.startek_eng.fc320sdk;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader ;
//import java.util.Scanner;
/**
 * FC320SDKWrapper is the class to communicat with WSA matching servers. With the server plug-in, web services or applications can be easily integrated with STARTEK's WSA matching servers.
 * <pre><code>
 *    boolean sslEnable = false;
 *    int port = 8080;
 *    String secureCode = "STARTEK_DEMO";
 *    String serverIP = "61.218.109.72";//"wsa.startek-eng.com"
 *    //Initial WSARemotePlugin Object
 *    WSARemotePlugin plugin = WSARemotePlugin.getInstance(sslEnable,port,secureCode);
 *    //Initial Transaction Code
 *    String transactionCode =  plugin.getTransactionCode();
 *    //Add Matching Server
 *    try
 *    {
 *       int rtn = plugin.addMatchingServer(serverIP);
 *       if (rtn >=WSARemotePlugin.OK)
 *       {
 *           System.out.println("ServerAmount:"+plugin.getServerAmount());
 *           System.out.println("ServerAmount:"+plugin.getServerList());
 *           //plugin.WSAEnrollment(userID,groupdID,fpData,transactionCode);
 *           //plugin.WSAAuthentication(userID,fpData,transactionCode);
 *           //plugin.WSAIdentification(groupID,fpData,transactionCode);
 *       }
 *     }catch(WSAException e)
 *     {
 *        e.printStackTrace();
 *     }
 * </code></pre>
 * @author STARTEK Engineering Inc.
 */
public class FC320SDKWrapper
{
	private FC320SDKWrapper() throws SensorConnectFailedException, LibraryLoadFailedException
	{
		super();
		loadRelatedLibrary();
		try
		{
			FP_DisconnectCaptureDriver(0);
		}
		catch (UnsatisfiedLinkError e)
		{
			throw (new LibraryLoadFailedException(e));
		}
		catch (Exception e)
		{
			throw (new LibraryLoadFailedException(e));
		}
	}
	/** 
     * getInstance get a singlton instance of FC320SDKWrapper
     */
	public static FC320SDKWrapper getInstance() throws SensorConnectFailedException,LibraryLoadFailedException
	{
		if (_instance == null)
		{
			_instance = new FC320SDKWrapper();
		}
		return _instance;
	}
	protected void finalize() throws Throwable
	{
		FP_DisconnectCaptureDriver(0);
		super.finalize();
	}
	private static FC320SDKWrapper _instance = null;
	private static long _h_sensorconnect=-1;
	private static final String defaultLibarys[] = {"fpi42.dll","fpk42.dll","FC320DRV.dll","Fc320Api.dll","FC320SDKWrapper.dll"};
    private static File currentDirectory = null;
    /**
     *
     */    
	public static SensorConnectFailedException SCFException = new SensorConnectFailedException("\r\nFailed to connect to STARTEK-Eng supported sensors\r\nPlease plug-in your sensor\r\nIf you have any problem about the sensor! Please contact sales@mail.startek-eng.com");
	
	public static final int    OK                          = 0;
	public static final int    FAIL                        =-1;
	public static final int    CALCELLED                   =-1;

	public static final int        SNAP_TIME_LIMIT         =100;
	public static final int        SNAP_COUNT              =100;

	public static final int    LPT1                        = 0;
	public static final int    LPT2                        = 1;

	public static final int    PRN_300DPI                  = 0;
	public static final int    PRN_150DPI                  = 1;
	public static final int    PRN_100DPI                  = 2;
	public static final int    PRN_75DPI                   = 3;
	public static final int    PRN_600DPI                  = 4;

	/*-------------------------------------------*\
	|   Return code of system integrator level    |
	\*-------------------------------------------*/
	public static final int    S_DOMAIN_ERR                =-1;
	public static final int    S_RANGE_ERR                 =-2;
	public static final int    S_MEM_ERR                   =-3;
	public static final int    S_FILE_ERR                  =-4;
	public static final int    S_COMM_ERR                  =-5;
	public static final int    S_CHKSUM_ERR                =-6;
	public static final int    S_TIME_OUT                  =-7;
	public static final int    S_PRINT_ERR                 =-8;

	public static final int    S_KEYCARD_CHECK_FAIL       =-10;
	public static final int    S_EMPTY_IMAGE_SET          =-11;

	public static final int    S_FPSET_INVALID            =-21;
	public static final int    S_FPCODE_INVALID           =-22;
	public static final int    S_FP_INVALID               =-23;
	public static final int    S_SECURITY_ERR             =-24;

	public static final int    S_NOT_SUPPORTED             =-1;
	public static final int    S_VALID                     = 1;
	public static final int    S_INVALID                  =255;

	public static final int     IDD_SNAP_MSG1              =412;
	public static final int     IDD_SNAP_MSG2              =413;

	public static final int     IDD_SNAP_GROUP             =421;

	public static final int     IDD_ENRL_RLT               =431;

	public static final int     IDS_POSITION_NO_FP         =501;
	public static final int     IDS_POSITION_TOO_LOW       =502;
	public static final int     IDS_POSITION_TOO_TOP       =503;
	public static final int     IDS_POSITION_TOO_RIGHT     =504;
	public static final int     IDS_POSITION_TOO_LEFT      =505;
	public static final int     IDS_POSITION_TOO_LOW_RIGHT  =506;
	public static final int     IDS_POSITION_TOO_LOW_LEFT   =507;
	public static final int     IDS_POSITION_TOO_TOP_RIGHT  =508;
	public static final int     IDS_POSITION_TOO_TOP_LEFT   =509;
	public static final int     IDS_POSITION_OK             =510;

	public static final int     IDS_DENSITY_TOO_DARK        =511;
	public static final int     IDS_DENSITY_TOO_LIGHT       =512;
	public static final int     IDS_DENSITY_AMBIGUOUS       =513;
	public static final int     IDS_DENSITY_OK              =514;
	/*------------------------------*\
	|   Return code of user level    |
	\*------------------------------*/
	public static final int    U_LEFT                     =-41;
	public static final int    U_RIGHT                    =-42;
	public static final int    U_UP                       =-43;
	public static final int    U_DOWN                     =-44;

	public static final int    U_POSITION_CHECK_MASK      =0x00002F00;
	public static final int    U_POSITION_NO_FP           =0x00002000;
	public static final int    U_POSITION_TOO_LOW         =0x00000100;
	public static final int    U_POSITION_TOO_TOP         =0x00000200;
	public static final int    U_POSITION_TOO_RIGHT       =0x00000400;
	public static final int    U_POSITION_TOO_LEFT        =0x00000800;
	public static final int    U_POSITION_TOO_LOW_RIGHT   =(U_POSITION_TOO_LOW|U_POSITION_TOO_RIGHT);
	public static final int    U_POSITION_TOO_LOW_LEFT    =(U_POSITION_TOO_LOW|U_POSITION_TOO_LEFT);
	public static final int    U_POSITION_TOO_TOP_RIGHT   =(U_POSITION_TOO_TOP|U_POSITION_TOO_RIGHT);
	public static final int    U_POSITION_TOO_TOP_LEFT    =(U_POSITION_TOO_TOP|U_POSITION_TOO_LEFT);
	public static final int     U_POSITION_OK             =0x00000000;

	public static final int    U_DENSITY_CHECK_MASK       =0x000000E0;
	public static final int    U_DENSITY_TOO_DARK         =0x00000020;
	public static final int    U_DENSITY_TOO_LIGHT        =0x00000040;
	public static final int    U_DENSITY_LITTLE_LIGHT     =0x00000060;
	public static final int    U_DENSITY_AMBIGUOUS        =0x00000080;

	public static final int    U_INSUFFICIENT_FP          =-31;
	public static final int    U_NOT_YET                  =-32;

				
	public static final int    U_CLASS_A                 =65;
	public static final int    U_CLASS_B                 =66;
	public static final int    U_CLASS_C                 =67;
	public static final int    U_CLASS_D                 =68;
	public static final int    U_CLASS_E                 =69;
	public static final int    U_CLASS_R				 =82;

	/*---------------------------------*\
	|   Definition of security level    |
	\*---------------------------------*/
	public static final int    AUTO_SECURITY		  =0;
	public static final int    SECURITY_A                   =1;
	public static final int    SECURITY_B                   =2;
	public static final int    SECURITY_C                   =3;
	public static final int    SECURITY_D                   =4;
	public static final int    SECURITY_E                   =5;
	public static final int    SECURITY_M                  =30;
	public static final int    SECURITY_R                   =40;

	public static final int    FP_IMAGE_WIDTH             =256;
	public static final int    FP_IMAGE_HEIGHT            =256;
	public static final int    GRAY_IMAGE_SIZE        =(((int)FP_IMAGE_WIDTH)*(FP_IMAGE_HEIGHT));
	public static final int    BIN_IMAGE_SIZE          =(GRAY_IMAGE_SIZE/8);

	public static final int		LARGE                      =10;
	public static final int		SMALL                      =11;
	public static final int    RAW                         =12;
	public static final int    BMP                         =13;

	public static final int    GRAY_IMAGE                  =8;
	public static final int    BIN_IMAGE                   =1;

	public static final int    GRAY_LEVEL                  =256;
	public static final int    GRAY_STEP                   =(256/GRAY_LEVEL);

	/*---------------------------------*\
	|   Definition of enrollment mode   |
	\*---------------------------------*/
	public static final int    DEFAULT_MODE              =0x00;
	public static final int    FP_CODE_LENGTH            =256;

	/*---------------------------------*\
	|   Definition of Sensor Type       |
	\*---------------------------------*/
	public static final int    SENSORTYPE_AREA             = 0;
	public static final int    SENSORTYPE_CHIPLINE         = 1;
     /**
      * The method FP_ConnectCaptureDriver
      * @return On success, returns OK(0). Otherwise, a native error code is returned.
      * @param reserved test
      * @throws Exception 
      */ 
	public synchronized native long FP_ConnectCaptureDriver(int reserved) throws Exception;	//return Device handle
	public synchronized native void FP_DisconnectCaptureDriver (long hConnect);

	public synchronized native int FP_Snap(long hConnect)throws Exception;
	
	public synchronized native long FP_CreateCaptureHandle(long hConnect)throws Exception;	//return Capture handle
	public synchronized native int FP_Capture(long hConnect,long hFPCapture)throws Exception;
	public synchronized native int FP_DestroyCaptureHandle(long hConnect,long hFPCapture );

	public synchronized native int FP_GetPrimaryCode(long hConnect, byte[] p_code)throws Exception;

	public synchronized native long FP_CreateImageHandle(long hConnect,byte mode,int wSize)throws Exception;//return image handle
	public synchronized native int FP_GetImage( long hConnect,long hFPImage)throws Exception;
	public synchronized native int FP_DestroyImageHandle(long hConnect,long hFPImage);

	public synchronized native long FP_CreateEnrollHandle(long hConnect,byte mode)throws Exception;//return enroll handle
	public synchronized native int FP_Enroll(long hConnect,long hFPEnroll,byte[] p_code,byte[] fp_code)throws Exception;
	public synchronized native int FP_DestroyEnrollHandle(long hConnect,long hFPEnroll);

	public synchronized native int FP_ImageMatch(long hConnect,byte[] fp_code,int nSecurity)throws Exception;
	public synchronized native int FP_CodeMatch(long hConnect,byte[] p_code,byte[] fp_code,int nSecurity)throws Exception;
//	public synchronized native int FP_ImageMatchEx(long hConnect,byte[] fp_code ,int security,long nScore)throws Exception;
//	public synchronized native int FP_CodeMatchEx(long hConnect,byte[] p_code,byte[] fp_code,int nSecurity,long nScore)throws Exception;//nScore will be a return value
	public synchronized native long FP_ImageMatchEx(long hConnect,byte[] fp_code ,int security)throws Exception;//return score
	public synchronized native long FP_CodeMatchEx(long hConnect,byte[] p_code,byte[] fp_code,int nSecurity)throws Exception;//return score

	public synchronized native int FP_CheckBlank(long hConnect)throws Exception;
	public synchronized native int FP_Diagnose(long hConnect)throws Exception;

	public synchronized native int FP_SaveImage(long hConnect,long hFPImage,int FileType ,String szFilename)throws Exception;//char[]
	//public synchronized native int FP_DisplayImage(long hConnect,long hDC,long hFPImage ,int nStartX ,int nStartY,int nDestWidth,int nDestHeight)throws Exception;
	public synchronized native int FP_DisplayImage(long hConnect,java.awt.Canvas canvas,long hFPImage ,int nStartX ,int nStartY,short nDestWidth,short nDestHeight)throws Exception;

	public synchronized native int FP_GetImageData(long hConnect, byte[] bRawData,short[] nImgWidthHeight)throws Exception;

	public synchronized native int FP_GetPrimaryCodeEx(long hConnect, byte[] bRawData,short nWidth, short nHeight, byte[] p_code)throws Exception;
	public synchronized native int FP_GetEnrollCode(long hConnect, byte[] p_code,byte[] fp_code,short nImgWidth, short nImgHeight)throws Exception;
    
    public synchronized native int DLG_FPEnroll(long hConnect,long parentHwnd,byte[] isocodeBuffer,int bfLength)throws Exception;
    //,PSetImageProc proc1,PSetImageProc proc2, PSetImageProc proc3
    public synchronized native int DLG_FPSnap(long hConnect,long parentHwnd,byte[] isocodeBuffer,int bfLength)throws Exception;
/*
	private static final void showDbgMsg(Object obj)
	{
		System.out.println(obj);
	}
	private static void loadRelatedLibrary() throws LibraryLoadFailedException
	{
		int loadOK = OK;
		String tmpStr = null;
		ArrayList tmpLibArray = new ArrayList();
		Exception catchedException = null;
		
		for (int i=0;i<defaultLibarys.length;i++)
		{
			tmpLibArray.add(defaultLibarys[i]);
		}
		{
			loadOK = OK;
			InputStream in = null;
			
			//copy file
			FileOutputStream out=null;
			File tmpFile=null;
			File dllFile=null;
			String createPath = null;
			String placePath = null;
			byte[] buf = new byte[10000];
			for (Iterator i=tmpLibArray.iterator();i.hasNext()&&loadOK==OK;)
			{
				tmpStr = (String)i.next();
				in = FC320SDKWrapper.class.getResourceAsStream("lib/"+tmpStr);
				showDbgMsg( FC320SDKWrapper.class.getResource("lib/"+tmpStr));
				if (in!=null)
				{
					try
					{
						dllFile = File.createTempFile(tmpStr,null);//,tmplibDir);
						dllFile.deleteOnExit();
						//First time, check and initial placePath 
						if (placePath==null)
						{
							placePath = dllFile.getAbsolutePath();
							createPath = placePath;
							placePath = placePath.substring(0,placePath.lastIndexOf(tmpStr))+"/STARTEK_FC320/";
							tmpFile = new File(placePath);
							tmpFile.deleteOnExit();
							currentDirectory = tmpFile;
							if (!(tmpFile.exists()&&tmpFile.isDirectory()))
							{
								//Create Directory
								tmpFile.mkdirs();
							}else
							{
								//Remove all files from the directory
								if (currentDirectory!=null && currentDirectory.isDirectory())
								{
									try
									{
										File[] tmpFiles = currentDirectory.listFiles();
										for (int x=0;x<tmpFiles.length;x++)
										{
											tmpFiles[x].delete();
										}
									}
									catch (Exception e)
									{
									}
								}
							}
							//System.setProperty("java.library.path",placePath);
						}
						out = new FileOutputStream(dllFile);
						for (int n=in.read(buf);n!=-1;n=in.read(buf))
						{
							out.write(buf,0,n);
						}
						out.close();
						in.close();
						//Load WSABrowserAPIWrapper Only 
						if (tmpStr.compareTo(defaultLibarys[defaultLibarys.length-1])!=0)
						{
							File tmp = new File(placePath+tmpStr);
							tmp.deleteOnExit();
							try
							{
								if (tmp.exists())
									tmp.delete();
								if (!dllFile.renameTo(tmp))
								{
									dllFile.delete();
								}
							}
							catch (Exception e)
							{
							}
							System.load(tmp.getAbsolutePath());
						}else
						{
							File tmp = new File(placePath+dllFile.getName());
							
							dllFile.renameTo(tmp);
							//Try System load
							System.load(tmp.getAbsolutePath());
							//showDbgMsg(tmp.getAbsolutePath());
						}
						
					}
					catch (UnsatisfiedLinkError e)
					{
						File tmp = new File(placePath+tmpStr);
						if (!tmp.exists())
						{
							loadOK = FAIL;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						catchedException = e;
						loadOK = FAIL;
					}finally
					{
						try
						{
							if (out!=null)
								out.close();
							if (in!=null)
								in.close();
						}
						catch (java.io.IOException e)
						{
							//ignore it
						}
						try
						{
							File tmp = new File(createPath);
							if (tmp.exists())
							{
								tmp.delete();
							}
						}
						catch (Exception e)
						{
							//ignore it
						}
					}
				}
			}
		}
		if (loadOK == FAIL)
		{
			tmpStr = "Can't load those librarys "+tmpLibArray.toString();
			if (catchedException!=null)
				throw new LibraryLoadFailedException(tmpStr,catchedException);
			else
				throw new LibraryLoadFailedException(tmpStr);
		}
	}
//=======================================================TESTing
	private static void testRawData(FC320SDKWrapper s,long hConnect)
	{
		int rtn;
		long hFPEnroll=0;
		byte[] bRawData = new byte[FP_IMAGE_WIDTH*FP_IMAGE_HEIGHT];
		short[] nImgWidthHeight = new short[2];
		byte[] p_code = new byte[FP_CODE_LENGTH];
		byte[] fp_code = new byte[FP_CODE_LENGTH];
		long score = 0;

		//showDbgMsg("---------Diagnose+CheckBlank+Snap Testing---------");
		if (s!=null && hConnect>0)
		{
			try
			{
				int counter = 6550;
				while ((rtn=s.FP_CheckBlank(hConnect))!=OK)
				{
					if ((counter++)%10==0)
					{
						showDbgMsg("FP_CheckBlank:"+rtn+"<=Please Remove Your Finger");
					}
				}
				do
				{
					for (int i=0;i<10;i++)
					{
						rtn = s.FP_Snap(hConnect);
					}
					if (rtn!=OK && counter%5==0)
					{
						showDbgMsg("FP_Snap:"+rtn+"=>Please place your finger on sensor");
					}else
					{
						rtn = s.FP_GetImageData(hConnect,bRawData,nImgWidthHeight);
						showDbgMsg("FP_GetImageData:"+rtn+" H:"+nImgWidthHeight[0]+" W:"+nImgWidthHeight[1]);
						if (rtn == OK)
						{
							testImageHandle(s,hConnect,"GetImageData.BMP");
							hFPEnroll = s.FP_CreateEnrollHandle(hConnect, (byte)DEFAULT_MODE);
							int testCount = 0;
							do
							{
								rtn = s.FP_GetPrimaryCodeEx(hConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],p_code);
								showDbgMsg("FP_GetPrimaryCodeEx:"+rtn);
								if (rtn == OK)
								{
									if (p_code!=null)
									{
										rtn  = s.FP_Enroll(hConnect,hFPEnroll, p_code, fp_code);
										showDbgMsg("Enroll Times:"+ (counter++));
										showDbgMsg("FP_Enroll return:"+ rtn);
										if ((rtn >= U_CLASS_A) && (rtn <= U_CLASS_E))
										{
											break;
										}
									}
									rtn = s.FP_GetEnrollCode(hConnect,p_code,fp_code,nImgWidthHeight[0],nImgWidthHeight[1]);
									showDbgMsg("FP_GetEnrollCode:"+rtn);
								}
								rtn = s.FP_GetPrimaryCodeEx(hConnect,bRawData,nImgWidthHeight[0],nImgWidthHeight[1],p_code);
								score = s.FP_CodeMatchEx(hConnect,p_code,fp_code,SECURITY_C);
								showDbgMsg("-----------------------------------------------FP_CodeMatchEx:score="+score);
								if (score<10000)
								{
									rtn = FAIL;
								}
							}
							while ((--testCount)>0);
							if (hFPEnroll>0)
								s.FP_DestroyEnrollHandle(hConnect,hFPEnroll);
						}
					}
					if ((counter--)<0)
					{
						break;
					}
				}while (rtn!=OK);
				for (int i=0;i<FP_CODE_LENGTH;i++)
				{
					if ((i%16)==0)
					{
						showDbgMsg("");
					}
					System.out.print(fp_code[i]+" ");
				}
				showDbgMsg("");
				for (int i=0;i<6;i++)
				{
					showDbgMsg("~~~~~~~~~~~~Test Matching(6 times)~~~~~~~~~~~~"+i);
					p_code = testDiagnoseSnap(s,hConnect);
					score = 0;
					score = s.FP_CodeMatchEx(hConnect,p_code,fp_code,i);
					showDbgMsg("FP_CodeMatchEx:score="+score);
					rtn = s.FP_CodeMatch(hConnect,p_code,fp_code,i);
					showDbgMsg("FP_CodeMatch:"+rtn);

					p_code = testDiagnoseSnap(s,hConnect);
					score = 0;
					score = s.FP_ImageMatchEx(hConnect,fp_code,i);
					showDbgMsg("FP_ImageMatchEx:score="+score);
					rtn = s.FP_ImageMatch(hConnect,fp_code,i);
					showDbgMsg("FP_ImageMatch:"+rtn);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private static void testEnrollHandle(FC320SDKWrapper s,long hConnect)
	{
		long hFPEnroll = 0;
		int rtn;
		byte[] p_code;
		byte[] fp_code;
		try
		{
			hFPEnroll = s.FP_CreateEnrollHandle(hConnect, (byte)DEFAULT_MODE);
			if (hFPEnroll>0)
			{
				fp_code = new byte[FP_CODE_LENGTH];
				int counter=0;
				do
				{
					p_code = testCaptureHandle(s,hConnect);
					showDbgMsg("~~~~~~~~~~~~Continue Enrollment~~~~~~~~~~~~");
					if (p_code!=null)
					{
						rtn  = s.FP_Enroll(hConnect,hFPEnroll, p_code, fp_code);
						showDbgMsg("Enroll Times:"+ (counter++));
						showDbgMsg("FP_Enroll return:"+ rtn);
						if ((rtn >= U_CLASS_A) && (rtn <= U_CLASS_E))
						{
							break;
						}
					}
				}
				while (true);
				for (int i=0;i<FP_CODE_LENGTH;i++)
				{
					if ((i%16)==0)
					{
						showDbgMsg("");
					}
					System.out.print(fp_code[i]+" ");
				}
				showDbgMsg("");
				long score = 0;
				for (int i=0;i<6;i++)
				{
					showDbgMsg("~~~~~~~~~~~~Test Matching(6 times)~~~~~~~~~~~~"+i);
					p_code = testDiagnoseSnap(s,hConnect);
					score = 0;
					score = s.FP_CodeMatchEx(hConnect,p_code,fp_code,i);
					showDbgMsg("FP_CodeMatchEx:score="+score);
					rtn = s.FP_CodeMatch(hConnect,p_code,fp_code,i);
					showDbgMsg("FP_CodeMatch:"+rtn);

					p_code = testDiagnoseSnap(s,hConnect);
					score = 0;
					score = s.FP_ImageMatchEx(hConnect,fp_code,i);
					showDbgMsg("FP_ImageMatchEx:score="+score);
					rtn = s.FP_ImageMatch(hConnect,fp_code,i);
					showDbgMsg("FP_ImageMatch:"+rtn);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (hFPEnroll>0)
				s.FP_DestroyEnrollHandle(hConnect,hFPEnroll);
		}
		
	}

	private static void testImageHandle(FC320SDKWrapper s,long hConnect,String fileName)
	{
		long hFPImage = 0;
		int rtn;

		try
		{
			hFPImage = s.FP_CreateImageHandle(hConnect, (byte)GRAY_IMAGE, SMALL);
			showDbgMsg("FP_CreateImageHandle:"+hFPImage);
			rtn = s.FP_GetImage (hConnect, hFPImage);
			showDbgMsg("FP_GetImage:"+rtn);
			rtn = s.FP_SaveImage(hConnect, hFPImage, BMP, fileName);
			showDbgMsg("FP_SaveImage("+fileName+"):"+rtn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			s.FP_DestroyImageHandle (hConnect, hFPImage);
		}
	}
	private static byte[] testCaptureHandle(FC320SDKWrapper s,long hConnect)
	{
		int rtn;
		long hFPCapture=0;
		byte[] p_code = new byte[FP_CODE_LENGTH];
		//showDbgMsg("---------CaptureHandle Testing---------");
		if (s!=null && hConnect>=0)
		{
			try
			{
				showDbgMsg("FP_Diagnose:"+s.FP_Diagnose(hConnect));
				showDbgMsg("FP_CheckBlank:"+s.FP_CheckBlank(hConnect));
				int counter = 1000;
				while ((rtn=s.FP_CheckBlank(hConnect))!=OK)
				{
					if ((counter++)%10==0)
					{
						showDbgMsg("FP_CheckBlank:"+rtn+"<=Please Remove Your Finger");
					}
				}
				hFPCapture = s.FP_CreateCaptureHandle(hConnect);
				if (hFPCapture>0)
				{
					do
					{
						rtn = s.FP_Capture(hConnect,hFPCapture);
						if (rtn!=OK && counter%5==0)
						{
							showDbgMsg("FP_Capture:"+rtn+"=>Please place your finger on sensor");
						}
						if (counter--<0)
						{
							break;
						}
					}
					while (rtn !=OK);
					testImageHandle(s,hConnect,"CaptureHandle.BMP");
				}
				rtn = s.FP_GetPrimaryCode(hConnect,p_code);
				showDbgMsg("FP_GetPrimaryCode:"+rtn);
				for (int i=0;i<FP_CODE_LENGTH;i++)
				{
					if ((i%16)==0)
					{
						showDbgMsg("");
					}
					System.out.print(p_code[i]+" ");
				}
				showDbgMsg("");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (hFPCapture>0)
				{
					s.FP_DestroyCaptureHandle(hConnect,hFPCapture);
				}
			}
		}
		return p_code;
	}
	private static  byte[] testDiagnoseSnap(FC320SDKWrapper s,long hConnect)
	{
		int rtn;
		byte[] p_code = new byte[FP_CODE_LENGTH];
		//showDbgMsg("---------Diagnose+CheckBlank+Snap Testing---------");
		if (s!=null && hConnect>=0)
		{
			try
			{
				showDbgMsg("FP_Diagnose:"+s.FP_Diagnose(hConnect));
				showDbgMsg("FP_CheckBlank:"+s.FP_CheckBlank(hConnect));
				int counter = 6550;
				while ((rtn=s.FP_CheckBlank(hConnect))!=OK)
				{
					if ((counter++)%10==0)
					{
						showDbgMsg("FP_CheckBlank:"+rtn+"<=Please Remove Your Finger");
					}
				}
				do
				{
					rtn = s.FP_Snap(hConnect);
					if (rtn!=OK && counter%5==0)
					{
						showDbgMsg("FP_Snap:"+rtn+"=>Please place your finger on sensor");
					}
					if ((counter--)<0)
					{
						break;
					}
				}
				while (rtn!=OK);
				
				testImageHandle(s,hConnect,"SNAP.BMP");
				rtn = s.FP_GetPrimaryCode(hConnect,p_code);
				showDbgMsg("FP_GetPrimaryCode:"+rtn);
				for (int i=0;i<FP_CODE_LENGTH;i++)
				{
					if ((i%16)==0)
					{
						showDbgMsg("");
					}
					System.out.print(p_code[i]+" ");//Integer.toHexString(p_code[i])
				}
				showDbgMsg("");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return p_code;
	}*/
    
   
}
