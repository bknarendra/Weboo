@echo off

FOR %%a in (*.jar) DO CALL :AddToPath %%a
echo %CLASSPATH%
javac -cp "%CLASSPATH%;"; a/*.java

pause
:AddToPath
SET CLASSPATH=%1;%CLASSPATH%
GOTO :EOF