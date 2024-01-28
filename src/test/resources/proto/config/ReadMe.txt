#INFO################
1. The protoc-compile.bat needs to be run manually to compile the .proto files and generate the .java files
2. Running this batch file will only be done when you want to generate java files for new proto files or regenerate for existing ones.

#SETUP###############
1. Download the windows protobuf executable from:
    https://github.com/google/protobuf/releases/tag/v3.3.0
   Protobuf site for reference:
    https://developers.google.com/protocol-buffers/docs/downloads.html
2. Unpack the zipped folder on your local machine.
3. Add the path of your protoc.exe to the PATH evironment variable.
    Example:
    protoc.exe location:
        C:\Apps\Google\Protobuf\protoc-3.3.0-win32\bin
    Environment Variables:
        PROTOC_HOME = C:\Apps\Google\Protobuf\protoc-3.3.0-win32
        PATH = [your entries]; %PROTOC_HOME%\bin
4. After doing the above steps, open the command prompt and enter:
    protoc --version

#COMPILE#############
0. IMPORTANT: DO NOT CHANGE THE 'protoc-compile.bat' FILE!!!
1. Simply run the protoc-compile.bat
2. Proto Java files will get generated. By default, the java file name will be a camel case of your proto file name.

