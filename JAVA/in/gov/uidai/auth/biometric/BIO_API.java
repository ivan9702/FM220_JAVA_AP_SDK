//Begin of API
package in.gov.uidai.auth.biometric;
/**
* The below Interface should be implemented by the device vendors for their
* supporting fingerprint devices.
*
*/
public class BIO_API {
/**
* Return codes
*/
public static final int BIO_API_DEVICE_INIT_SUCCESSFUL = 0;
public static final int BIO_API_DEVICE_INIT_UNSUCCESSFUL = 1;
public static final int BIO_API_DEVICE_ERR_UNABLE_TO_CAPTURE = 2;
public static final int BIO_API_DEVICE_ERR_TOO_MANY_HANDLES = 3;
public static final int BIO_API_DEVICE_ERR_TIMEOUT_EXPIRED = 4;
public static final int BIO_API_DEVICE_ERR_PURPOSE_NOT_SUPPORTED = 5;
public static final int BIO_API_TEMPLATES_MATCH = 6;
public static final int BIO_API_TEMPLATES_NOTMATCH = 7;
public static final int BIO_API_DEVICE_CAPTURE_SUCCESSFUL = 10;
public static final int BIO_API_DEVICE_CAPTURE_UNSUCCESSFUL = 11;
public static final int BIO_API_DEVICE_PURPOSE_VERIFICATION = 12;
public static final int BIO_API_DEVICE_PURPOSE_ENROLLMENT = 13;
public static final int BIO_API_TEMPLATE_ISO_FORMAT = 16;
public static final int BIO_API_TEMPLATE_ANSI_FORMAT = 17;
public static final int BIO_API_DEVICE_SAMPLE_QUALITY_GOOD = 18;
public static final int BIO_API_DEVICE_SAMPLE_QUALITY_POOR = 19;
public static final int BIO_API_TEMPLATE_EXTRACT_UNSUCCESSFUL = 20;
public static final int BIO_API_TEMPLATE_EXTRACT_SUCCESSFUL = 20;
public static final int BIO_API_DEVICE_INIT_DLL_ALREADY_LOADED = 21;
public static final boolean BIO_API_DEVICE_CONNECTED = true;
public static final boolean BIO_API_DEVICE_DISCONNECTED = false;
/**
* Initializes the device
* This is to initialize the device and dynamically
* load the library files (dlls) into system path specified by
* java.library.path at the command prompt. It returns
* BIO_API_DEVICE_INIT_SUCCESSFUL if the DLL are successfully loaded first time into
* system, it returns BIO_API_DEVICE_INIT_UNSUCCESSFUL if any errors occured, it returns
* BIO_API_DEVICE_INIT_DLL_ALREADY_LOADED if already loaded
* @param path
* @return
*/
public native int init(String path);
/**
* Device connection check. It returns BIO_API_DEVICE_CONNECTED if connected
* else BIO_API_DEVICE_DISCONNECTED
* @return
*/
public native boolean isDeviceConnected();
/**
* Capture the image when the scan button is clicked. It returns
* an array of bytes of captured image in ISO 19794-4
* Parameters: purpose, is either BIO_API_DEVICE_PURPOSE_VERIFICATION or
* BIO_API_DEVICE_PURPOSE_ENROLLMENT and timeout, which is a configurable value
* Returns BIO_API_DEVICE_ERR_UNABLE_TO_CAPTURE
BIO_API_DEVICE_ERR_TIMEOUT_EXPIRED
BIO_API_DEVICE_ERR_TOO_MANY_HANDLES
BIO_API_DEVICE_ERR_DLL_ALREADY_LOADED
BIO_API_DEVICE_ERR_PURPOSE_NOT_SUPPORTED
BIO_API_DEVICE_CAPTURE_SUCESSFUL
BIO_API_DEVICE_CAPTURE_UNSUCESSFUL
* @param sample, captured sample in bytes
* @param purpose
* @param timeout
* @return
*/
public native int captureSample(byte[] sample, int purpose, int timeout);
/**
* Returns the sample size of the captured raw image
*/
public native int getSampleSize();
/**
* Returns the width of the sample
*/
public native int getSampleWidth();
/**
* Returns the height of the sample
*/
public native int getSampleHeight();
/**
* Extract the template from the captured sample.
* The extracted template will be in ISO 19794-4
* It returns BIO_API_TEMPLATE_EXTRACT_SUCCESSFUL or
* BIO_API_TEMPLATE_EXTRACT_UNSUCCESSFUL
* @param templateData
* @param sampleData
* @param width
* @param height
* @param format, should be BIO_API_TEMPLATE_ISO_FORMAT (ISO 19794-4)
* @return
*/
public native int extractTemplate(byte[] templateData, byte[] sampleData, int width, int height, int format);
/**
* Returns the size of the extracted template
*/
public native int getTemplateSize();
/**
* Compare the templates for duplicates It returns
* BIO_API_TEMPLATES_NOTMATCH, if 2 templates doesnt match else it
* returns BIO_API_TEMPLATES_MATCH
* @param tData1, extracted template1
* @param tData2, extracted template2
* @return
*/
public native int compareTemplates(byte[] tData1, byte[] tData2);
/**
* Converts the raw sample image to bmp for display in the UI.
* Returns the BMP Image object
* @param path to the raw image file
* @return
*/
public native Object createImage(byte[] sample);
/**
* Returns the device manufacturer name
* @return
*/
public native String getDeviceMake();
/**
* returns device model name
* @return
*/
public native String getDeviceModel();
/**
* Returns the serial number of the device used
* @return
*/
public native String getDeviceSerialNumber();
/**
* Returns the qulity of the sample captured
* Returns BIO_API_DEVICE_SAMPLE_QUALITY_GOOD if good else
* BIO_API_DEVICE_SAMPLE_QUALITY_POOR is poor
*/
public native int getSampleQuality();
/**
* returns template extracter's vendor name
* @return
*/
public native String getExtracterVendor();
/**
* returns template extracter's name
* @return
*/
public native String getExtracterName();
/**
* returns tenmplate extracter's version
* @return
*/
public native String getExtracterVersion();
}
//End of API