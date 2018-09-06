del FM220SDK.jar
jar cvf SDK.jar ./com/startek_eng/fm220sdk/lib/*.dll ./com/startek_eng/fm220sdk/*.class
jarsigner -keystore ./startek -storepass startek123 -keypass startek -signedjar FM220SDK.jar SDK.jar  WSA 
del SDK.jar