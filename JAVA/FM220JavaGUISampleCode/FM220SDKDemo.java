import com.startek_eng.fm220sdk.FM220SDKWrapper;
//import com.startek_eng.fm220sdk.FPImageCanvas;
//import com.startek_eng.fm220sdk.LibraryLoadFailedException;
import java.io.BufferedReader;

class FM220SDKDemo
{
    private static int Snap(FM220SDKWrapper s,long hConnect)
	{
        if (s==null)
        {
            System.out.println("ERROR!! Fm220SDKWrapper is not initialed");
            return FM220SDKWrapper.FAIL;
        }
        if (hConnect<0)
        {
            System.out.println("ERROR!! Device not connected");
            return FM220SDKWrapper.FAIL;
        }
		long hFPImage = 0;
        long hFPCapture=0;
		int rtn,result =FM220SDKWrapper.FAIL;

        System.out.println("\nPlease put your finger on the reader...");
		try
		{
            if( (hFPCapture=s.FP_CreateCaptureHandle(hConnect))<0)
			{
				System.out.println("ERROR!! FP_CreateSnapHandle() failed!!");
				return FM220SDKWrapper.FAIL;
			}
            if( (hFPImage=s.FP_CreateImageHandle(hConnect,(byte)FM220SDKWrapper.GRAY_IMAGE,FM220SDKWrapper.LARGE))<0)
			{
				System.out.println("ERROR!! FP_CreateImageHandle() failed!!");
				s.FP_DestroyCaptureHandle(hConnect,hFPCapture);
				return FM220SDKWrapper.FAIL;
			}
			//Please Remove Finger
			while(true)
			{   //check fingerprint is removed
				rtn=s.FP_CheckBlank(hConnect);
				if(rtn!=FM220SDKWrapper.FAIL)
					break;
				System.out.println("Please Remove your finger!!!");
			}//end while(true)
            
			while((result=s.FP_Capture(hConnect,hFPCapture))!=FM220SDKWrapper.OK)//Capture fingerprint 
            {   //Show Image Status
                switch(result & FM220SDKWrapper.U_POSITION_CHECK_MASK)
                {
                    case FM220SDKWrapper.U_POSITION_TOO_LOW:
                        System.out.println("Put your finger higher");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_TOP:
                        System.out.println("Put your finger lower");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_RIGHT:
                        System.out.println("Put your finger further to the left");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_LEFT:
                        System.out.println("Put your finger further to the right");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_LOW_RIGHT:
                        System.out.println("Put your finger further to the upper left");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_LOW_LEFT:
                        System.out.println("Put your finger further to the upper right");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_TOP_RIGHT:
                        System.out.println("Put your finger further to the lower left");
                        break;
                    case FM220SDKWrapper.U_POSITION_TOO_TOP_LEFT:
                        System.out.println("Put your finger further to the lower right");
                        break;
                    case FM220SDKWrapper.U_POSITION_OK:
                        System.out.println("Position is OK");
                        break;
                    case FM220SDKWrapper.U_POSITION_NO_FP:
                    default:
                        System.out.println("Make a closer contact with the reader");
                        break;
                } //end switch
                
                switch(result & FM220SDKWrapper.U_DENSITY_CHECK_MASK)
                {
                    case FM220SDKWrapper.U_DENSITY_TOO_DARK:
                        System.out.println("Wipe off excess moisture or put lighter");
                        break;
                    case FM220SDKWrapper.U_DENSITY_TOO_LIGHT:
                        System.out.println("Moisten your finger or put heavier");
                        break;
                    case FM220SDKWrapper.U_DENSITY_AMBIGUOUS:
                    default:
                        System.out.println("Please examine your finger");
                        break;
                }
				rtn=s.FP_GetImageQuality(hConnect);
                System.out.println("nfiq=" + rtn);

                if( (rtn=s.FP_GetImage(hConnect,hFPImage) )!=FM220SDKWrapper.OK)
                {
                    System.out.println("ERROR!! FP_GetImage() failed!!");
                    break;		
                }                  
            } //end wile
            if(result==FM220SDKWrapper.OK)
            {
                s.FP_SaveImage(hConnect,hFPImage,FM220SDKWrapper.BMP,"Snap.bmp");                
                s.FP_SaveISOImage(hConnect,hFPImage,FM220SDKWrapper.ISO,"Snap.isi",(byte)0,(byte)0);
                System.out.println("FP_Capture() OK [Snap.bmp:"+result+"]");
            }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
            if (hFPCapture>FM220SDKWrapper.OK)
				s.FP_DestroyCaptureHandle(hConnect,hFPCapture);
            if (hFPImage>FM220SDKWrapper.OK)
                s.FP_DestroyImageHandle (hConnect, hFPImage);
		}
        return result;
	}
    
    private static byte[] readBytesFromFile(String filePath)
    {
        byte[] result = null;
        try
        {
            java.io.FileInputStream in = new java.io.FileInputStream(filePath);
            byte[] tmpCode = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
            if (in.read(tmpCode)==FM220SDKWrapper.FP_CODE_LENGTH)
                result = tmpCode;
            in.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
     
    
    private static int saveBytes2File(String filePath,byte[] data)
    {
        int result =FM220SDKWrapper.FAIL ;
        try
        {
            java.io.FileOutputStream out = new java.io.FileOutputStream(filePath);
            out.write(data);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    private static int Enroll_SampleCode(FM220SDKWrapper s,long hConnect)
	{
        if (s==null)
        {
            System.out.println("ERROR!! FM220SDKWrapper is not initialed");
            return FM220SDKWrapper.FAIL;
        }
        if (hConnect<FM220SDKWrapper.OK)
        {
            System.out.println("ERROR!! Device not connected");
            return FM220SDKWrapper.FAIL;
        }
		long hFPEnroll = FM220SDKWrapper.FAIL;
        int rtn=FM220SDKWrapper.FAIL;
		int result = FM220SDKWrapper.FAIL;
        byte[] p_code = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
		byte[] fp_code = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
        
		try
		{
            if ( (hFPEnroll = s.FP_CreateEnrollHandle( hConnect,(byte)FM220SDKWrapper.DEFAULT_MODE) )<FM220SDKWrapper.OK) 
            {
                System.out.println("ERROR!! FP_CreateEnrollHandle() failed!");
                return  FM220SDKWrapper.FAIL;					         
            }
            for(int i=0;i<6;i++)
            {
                rtn = Snap(s,hConnect);
                if(rtn==FM220SDKWrapper.OK)
				{
					System.out.println("FP_Capture() OK");
					//rtn = s.FP_GetPrimaryCode(hConnect,p_code);
					rtn = s.FP_GetTemplate(hConnect,p_code,1,0);	//1: ISO 19794-2 format
					if(rtn==FM220SDKWrapper.OK)
					{
						System.out.println("FP_GetTemplate() OK");
						//rtn  = s.FP_Enroll(hConnect,hFPEnroll, p_code, fp_code);
						rtn  = s.FP_EnrollEx(hConnect,hFPEnroll, p_code, fp_code,1);
						if(rtn==FM220SDKWrapper.U_CLASS_A || rtn==FM220SDKWrapper.U_CLASS_B)
						{   //save enrolled fingerprint template
							//saveBytes2File("Enroll.dat",fp_code);
							s.FP_SaveISOminutia(hConnect,"Enroll.dat",fp_code);
							System.out.println("Enroll OK [Enroll.dat]");
							break;
						}
					}
				}
			}  //end for
            if(rtn!=FM220SDKWrapper.U_CLASS_A && rtn!=FM220SDKWrapper.U_CLASS_B)
				System.out.println("Enroll fail!!");
		}  //end try
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (hFPEnroll>=FM220SDKWrapper.OK)
				s.FP_DestroyEnrollHandle(hConnect,hFPEnroll);
		}
        return result;
	}
    
    private static long Match_SmapleCode(FM220SDKWrapper s,long hConnect)
    {
        if (s==null)
        {
            System.out.println("ERROR!! FC320SDKWrapper is not initialed");
            return FM220SDKWrapper.FAIL;
        }
        if (hConnect<FM220SDKWrapper.OK)
        {
            System.out.println("ERROR!! Device not connected");
            return FM220SDKWrapper.FAIL;
        }
        byte[] fp_code = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
        byte[] p_code = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
        long result= FM220SDKWrapper.FAIL;
		int rtn=FM220SDKWrapper.FAIL;
        long score = 0;
        
        //if enrolled file existed
        //fp_code=readBytesFromFile("Enroll.dat");
        
        try
        {
			rtn=s.FP_LoadISOminutia(hConnect,"Enroll.dat",fp_code);
			if (fp_code==null)
			{
				System.out.println("ERROR!! Fingerprint not enrolled [Read Enroll.dat failed]");
				return FM220SDKWrapper.FAIL;
			}
			rtn = Snap(s,hConnect);
            if(rtn==FM220SDKWrapper.OK)
            {
                System.out.println("FP_Capture() OK");
                //rtn = s.FP_GetPrimaryCode(hConnect,p_code);
                rtn = s.FP_GetTemplate(hConnect,p_code,1,0);
                if (rtn==FM220SDKWrapper.OK)
                {
                    score = s.FP_CodeMatchEx(hConnect,p_code,fp_code,FM220SDKWrapper.SECURITY_C);
                    System.out.println("Match score="+score);
					result = score;
                }else
                {
                    System.out.println("ERROR!! FP_GetTemplate() failed!!");
                }
            }
        }catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}
        return result;       
    }
    
 

    private static long GetImageData_SampleCode(FM220SDKWrapper s,long hConnect)
    {
        if (s==null)
        {
            System.out.println("ERROR!! FC320SDKWrapper is not initialed");
            return FM220SDKWrapper.FAIL;
        }
        if (hConnect<FM220SDKWrapper.OK)
        {
            System.out.println("ERROR!! Device not connected");
            return FM220SDKWrapper.FAIL;
        }
        byte[] minu_code = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
        short[] nWidth_Height = new short[2];
        
        long score=0;
        long rtn=FM220SDKWrapper.FAIL;
        try
        {
        //    rtn=s.DLG_FPEnroll(hConnect,null,fpcode,FM220SDKWrapper.FP_CODE_LENGTH);
        	rtn = Snap(s,hConnect);
            if (rtn<FM220SDKWrapper.OK)
            {    
                System.out.println("ERROR!! Snap Failed:"+rtn);
                return rtn;
            }else
	    {
	    	//get iso 19794-2 template
	    	rtn = s.FP_GetTemplate(hConnect,minu_code,1,0);
	    	if(rtn==FM220SDKWrapper.OK)
		{   //save enrolled fingerprint template
			//saveBytes2File("fp_tem.ist",minu_code);
			s.FP_SaveISOminutia(hConnect,"fp_tem.ist",minu_code);
			System.out.println("Save iso 19794-2 template OK [fp_tem.ist]");						
		}
	    	
                rtn=s.FP_GetImageDimension(hConnect,nWidth_Height);
                System.out.println("FP_GetImageDimension:"+rtn);
                if(rtn>=FM220SDKWrapper.OK)
                {
                    //save  fp image
                    byte[] bRawData = new byte[nWidth_Height[0]*nWidth_Height[1]];
                    rtn=s.FP_GetImageData(hConnect,bRawData,nWidth_Height[0],nWidth_Height[1]);
                    System.out.println("FP_GetImageData:"+rtn+" with Width: "+s.Width+" ,Height: "+s.Height+" . Bits per pixel:"+s.Bits_per_Pixel);
                    
                    //save raw to bmp
                    byte[] bBMPData=new byte[nWidth_Height[0]*nWidth_Height[1]+1078];
                      rtn=s.FP_RawDataToBMP(bRawData, nWidth_Height[0],nWidth_Height[1], bBMPData);
                      //rtn=sdk.RawDataToBMP(bRawData, nWidth_Height[0],nWidth_Height[1]);//bBMPData
                      if (rtn < FM220SDKWrapper.OK)
                      {
                          System.out.println("ERROR!! FP_RawDataToBMP Failed:"+rtn);
                          s.FP_DisconnectCaptureDriver(hConnect);
                          return rtn;
                      }
                      saveBytes2File("fp_img.bmp",bBMPData);
					       
					  //save raw to ISO 19794-4
					  byte[] bISOData=new byte[nWidth_Height[0]*nWidth_Height[1]+FM220SDKWrapper.ISO_HEADER_LEN]; 
					  byte fpPos,compRatio;
					  fpPos=0;
					  compRatio=0;
						 s.FP_RawtoISOimage(hConnect, bRawData, nWidth_Height[0], nWidth_Height[1], compRatio, fpPos,bISOData);
						 //s.FP_RawtoISOimage(bRawData, nWidth_Height[0],nWidth_Height[1], bISOData);
						 saveBytes2File("fp_img.isi",bISOData);
                }
	    }
            
        
            
        }catch(java.lang.Exception e)
        {
            e.printStackTrace();
        }
        return rtn; 
        
    }
  

    private static void DisplayMenu()
	{
		System.out.print(" 1 : Connect\t\t");
        System.out.print(" 2 : Snap and save iso 19794-4 format\n");
        System.out.print(" 3 : Enroll\t\t");
        System.out.print(" 4 : Match\n");
        System.out.print(" 5 : Snap and GetImageData and get iso 19794-2 template 19794-4 image\n");
        System.out.print(" 6 : Disconnect\t\t");
        System.out.print(" x : exit\n");      
	}
 
/*
    public static void main(String[] args)
    {
        try
        {
            java.awt.Component parent = null;//current window component
            FM220SDKWrapper sdk = null;
            long hConnect = FM220SDKWrapper.FAIL;
            long rtn=-1;
            long score =0;
            byte[] fpcode = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
            byte[] pcode = new byte[FM220SDKWrapper.FP_CODE_LENGTH];
            sdk = FM220SDKWrapper.getInstance();
            hConnect = sdk.FP_ConnectCaptureDriver(0);
            short[] nWidth_Height = new short[2];
            if (hConnect>=FM220SDKWrapper.OK)
            {
              //Demo Enroll
              rtn=sdk.DLG_FPEnroll(hConnect,parent,fpcode,FM220SDKWrapper.FP_CODE_LENGTH);
              if (rtn < FC320SDKWrapper.OK)
              {
                   System.out.println("ERROR!! DLG_FPEnroll Failed:"+rtn);
                   return;
              }else
              {
                  sdk.FP_GetImageDimension(hConnect,nWidth_Height);
                  if(rtn>=FM220SDKWrapper.OK)
                  {
                      byte[] bRawData = new byte[nWidth_Height[0]*nWidth_Height[1]];
                      rtn=sdk.FP_GetImageData(hConnect,bRawData,nWidth_Height[0],nWidth_Height[1]);
                      if (rtn < FM220SDKWrapper.OK)
                      {
                          System.out.println("ERROR!! FP_GetImageData Failed:"+rtn);
                          return;
                      }
                  }
              }
              //Demo Snap
              rtn=sdk.DLG_FPSnap(hConnect,parent,pcode,FC320SDKWrapper.FP_CODE_LENGTH);
              if(rtn < FC320SDKWrapper.OK)
              {
                  System.out.println("ERROR!! DLG_FPSnap Failed:"+rtn);
                  return;
              }
              //Demo Match
              score = sdk.FP_CodeMatchEx(hConnect,pcode,fpcode,FC320SDKWrapper.SECURITY_C);
              System.out.println("Matching score="+score);
              rtn = score;
            }else
            {
               System.out.println("ERROR!! Device Connect Failed");
               return;
            }
            sdk.FP_DisconnectCaptureDriver(hConnect);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
 */
	public static void main(String[] args)
	{
		FM220SDKWrapper s = null;
		long hConnect = FM220SDKWrapper.FAIL;
		int rtn;
		try
		{
			s = FM220SDKWrapper.getInstance();
			if (s==null)
			{
				System.out.println("ERROR!! Failed on getting Instance of FC320SDKWrapper");
			}
            BufferedReader in = new BufferedReader(new java.io.InputStreamReader(System.in));    
            while(true)
            {
                DisplayMenu();
                System.out.print("\nEnter choice : ");
                String str = in.readLine(); 
                switch(str.toCharArray()[0])
                {
                    case '1':
                        hConnect = s.FP_ConnectCaptureDriver(0);
						if (hConnect>=FM220SDKWrapper.OK)
						{
							System.out.println("Device Connect OK");
						}else
						{
							System.out.println("ERROR!! Device Connect Failed");
						}
                        break;
                    case '2':
                        Snap(s,hConnect);
                        break;
                    case '3':
                        Enroll_SampleCode(s,hConnect);
                        break;
                    case '4':
                        Match_SmapleCode(s,hConnect);
                        break;
                    case '5':
                        GetImageData_SampleCode(s,hConnect);                        
                        break;
                    case '6':
						if (hConnect>=FM220SDKWrapper.OK)
						{
							s.FP_DisconnectCaptureDriver(hConnect);
							hConnect = FM220SDKWrapper.FAIL;
						}
                        break;
                    
                    case 'x':
						if (hConnect>=FM220SDKWrapper.OK)
						{
							s.FP_DisconnectCaptureDriver(hConnect);
							hConnect = FM220SDKWrapper.FAIL;
						}
                        return ;
                    default:
                        break;
                }
            }//end while
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if ( s!=null && hConnect>=FM220SDKWrapper.OK)
			{
				s.FP_DisconnectCaptureDriver(hConnect);
				hConnect = FM220SDKWrapper.FAIL;
			}
		}
	}
}