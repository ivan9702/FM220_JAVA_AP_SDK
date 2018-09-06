/*
 * SDKClientResourceBundle.java
 *
 * Created on June 21, 2007, 12:07 PM
 */

package com.startek_eng.fc320sdk.ui;

/**
 *
 * @author  liaocl
 */
public class SDKClientResourceBundle 
{
    private java.util.ResourceBundle resource = null;
    private static SDKClientResourceBundle instance = null;
    /** Creates a new instance of SDKClientResourceBundle */
 
    public SDKClientResourceBundle(java.util.Locale locale) throws java.util.MissingResourceException
    {
        if (locale.equals(java.util.Locale.TRADITIONAL_CHINESE))
        {
            //java.util.ResourceBundle.getBundle("com/startek_eng/webta/resource/WebTAResource").getString("PanelSetupStatus.StepDes[2]")
            resource = java.util.ResourceBundle.getBundle("com/startek_eng/sdk/client/ui/SDKClientResource",locale);   
        }else
        {
            resource = java.util.ResourceBundle.getBundle("com/startek_eng/sdk/client/ui/SDKClientResource");
        }
    }
    public static SDKClientResourceBundle getDefaultInstance() throws java.util.MissingResourceException
    {
        if (instance == null)
        {
            instance = new SDKClientResourceBundle(java.util.Locale.getDefault());
        }
        return instance;
	}
    /*public String getResourceString(String defaultStr)
    {
        String key = defaultStr.replace(' ', '_'); 
        return getResourceString(key,defaultStr);
    }*/
     public String getResourceString(String key, String defaultStr)
    {
        String result = null;
        try
        {
            result = resource.getString(key);
        }catch(Exception ignore)
        {
            result = defaultStr;
        }
        return result;
    }
    public void localizeUI(java.awt.Container cont)
    {
        java.awt.Component[] comps = cont.getComponents();
        for (int i=0;i<comps.length;i++)
        {
            localizeUI(comps[i]);
            if (java.awt.Container.class.isInstance(comps[i]))
            {
                localizeUI((java.awt.Container)comps[i]);
            }
        }
    }
    public void localizeUI(java.awt.Component comp)
    {
        try
        {
            java.awt.Font font = comp.getFont();
            if (font !=null)
                 comp.setFont(new java.awt.Font(getResourceString("Font","MS Sans Serif"),font.getStyle(),font.getSize()));
        }catch(Exception ignore)
        {
        }
    }
}
