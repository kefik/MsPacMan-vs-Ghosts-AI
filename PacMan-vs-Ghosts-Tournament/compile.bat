cd bin
rmdir /s /q ch
cd ..
dir /s /B *.java > sources.txt
javac -d bin -cp bin;../MarioAI4J/bin;lib/* @sources.txt
del /Q sources.txt