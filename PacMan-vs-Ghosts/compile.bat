rmdir \s \q bin
mkdir bin
dir \s \B *.java > sources.txt
javac -d bin -cp "bin" @sources.txt
del \Q sources.txt
mkdir bin\game\core\resources
mkdir bin\game\core\resources\data
mkdir bin\game\core\resources\images

copy src\game\core\resources\data\* bin\game\core\resources\data
copy src\game\core\resources\images\* bin\game\core\resources\images
