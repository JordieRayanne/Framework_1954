javac -d Framework/web/WEB-INF/classes Framework/etu1954/framework/*.java Framework/etu1954/framework/annotation/*.java Framework/etu1954/framework/servlet/*.java
@REM create jar file
jar cvf framework.jar -C .\Framework\web\WEB-INF\classes\ .

copy framework.jar Test_Framework\web\WEB-INF\lib\
@REM compile test framework
javac -classpath ./Test_framework/web/WEB-INF/lib/framework.jar  -d Test_framework/web/WEB-INF/classes Test_framework/etu1954/framework/models/*.java 
@REM create war file
jar cvf test_framework.war -C .\Test_framework\web\ .

copy test_framework.war "D:\TomCat9\webapps"
