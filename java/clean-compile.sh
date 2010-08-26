rm -rf classes
mkdir -p classes
CLASSPATH=".:classes"
for i in `ls ./lib/*.jar`
do
  CLASSPATH=${CLASSPATH}:${i}
done
echo $CLASSPATH
javac -classpath $CLASSPATH -d classes src/com/lulu/coverGeneration/examples/*.java
