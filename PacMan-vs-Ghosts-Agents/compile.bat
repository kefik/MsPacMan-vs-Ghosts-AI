rmdir /s /q bin
mkdir bin
dir /s /B *.java > sources.txt
javac -d bin -cp "bin;../PacMan-vs-Ghosts/bin" @sources.txt
del /Q sources.txt