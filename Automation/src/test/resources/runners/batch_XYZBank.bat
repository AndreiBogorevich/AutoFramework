set ProjectPath=C:\Users\bogorevich_a\git\AutoFramework\Automation
echo %ProjectPath%
set classpath=%ProjectPath%\target\classes;%ProjectPath%\Lib\*
echo %classpath%
cd %ProjectPath%
java org.testng.TestNG src\test\resources\runners\testng_XYZBank.xml