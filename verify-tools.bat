@echo off
echo Checking tools...
java -version
javac -version
git --version
call mvn -version
call ant -version
call gradle -version
pause
