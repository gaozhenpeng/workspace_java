@echo off
rem run jpos server
rem
rem 
rem

set cwd=%~dp0
set cwd=%cwd:~0,-1%

:: set JAVA_HOME=%cwd%\jre1.7.0
:: set PATH=%JAVA_HOME%\bin;%PATH%
set CLPATH=%cwd%\classes

set CP=
for %%i in ("%cwd%\lib\*.jar") do call set CP=%%i;%%CP%%
set CP=%CP:~0,-1%
set CP=%cwd%\deploy;%CLPATH%;%CP%

set JAVA_OPTS=
:::: for jvm before 1.6
::set JAVA_OPTS=%JAVA_OPTS% -Xms256m -Xmx512m
::set JAVA_OPTS=%JAVA_OPTS% -XX:PermSize=256m -XX:MaxPermSize=512m 
set JAVA_OPTS=%JAVA_OPTS% -XX:+HeapDumpOnOutOfMemoryError
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=9099
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl=false
set JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true
:::: for 32-bit jvm
::set JAVA_OPTS=%JAVA_OPTS% -server

set DEFEND_FILE=%cwd%\java_instance.txt


pushd %cwd%

if exist %DEFEND_FILE% call:KILL_PROCESS_AND_FILE
call:SLEEP_SECS 15

:RUN_JAVA
if exist %DEFEND_FILE% goto ENDMSG
echo "---- create defend file"
echo RKL DEFEND FILE > %DEFEND_FILE%
echo "---- start java.exe"
:: java.exe %JAVA_OPTS% -cp %CP% org.jpos.q2.Q2 >NUL 2>&1
java.exe %JAVA_OPTS% -cp %CP% org.jpos.q2.Q2 
echo "---- del %DEFEND_FILE%"
del /f /s /q %DEFEND_FILE%
call:SLEEP_SECS 10
goto RUN_JAVA


popd

goto END


:KILL_PROCESS_AND_FILE
echo "---- kill java.exe"
taskkill /t /im "java.exe"
call:SLEEP_SECS 30
taskkill /t /f /im "java.exe"
echo "---- del %DEFEND_FILE%"
del /f /s /q %DEFEND_FILE%
goto END

:SLEEP_SECS
echo "---- sleep %1s"
set SEC=%1
set SEC=%SEC%000
ping 1.1.1.1 -n 1 -w %SEC% >NUL 2>&1
goto END

:ENDMSG
echo "---- Cmd Exit"
goto END

:END
