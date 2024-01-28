cd ../../..
set proto-input=resources/proto/
set proto-output=java

protoc -I=%proto-input% --java_out=%proto-output% %proto-input%*.proto

pause