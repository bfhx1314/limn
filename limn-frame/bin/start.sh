


for f in ../lib/*.jar
do
classpath=$f:$classpath
done
cd ..
java -cp $classpath com.limn.frame.control.ConsoleFrame
