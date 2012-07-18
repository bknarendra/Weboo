@echo off
FOR %%a in (*.jar) DO CALL :AddToPath %%a
echo %CLASSPATH%

pause
:AddToPath
SET CLASSPATH=%1 %CLASSPATH%
GOTO :EOF