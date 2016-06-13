
cd ..

for f in lib/*.jar
do
classpath=$f:$classpath
done


java -cp $classpath com.limn.frame.debug.DebugEditFrame