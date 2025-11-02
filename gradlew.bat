@ECHO OFF

SET DIR=%~dp0
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIR%

SET CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

IF NOT EXIST "%CLASSPATH%" (
  ECHO Gradle wrapper JAR missing. Run "gradle wrapper" from a JDK-enabled shell or open the project in Android Studio to regenerate it.
  EXIT /B 1
)

"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% %GRADLE_OPTS% -Dorg.gradle.appname=%APP_BASE_NAME% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
