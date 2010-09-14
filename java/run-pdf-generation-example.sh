CLASSPATH=".:classes"
for i in `ls ./lib/*.jar`
do
  CLASSPATH=${CLASSPATH}:${i}
done
echo $CLASSPATH
java -classpath $CLASSPATH com.lulu.coverGeneration.examples.PdfGeneration $1
