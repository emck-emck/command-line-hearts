javac src/hearts/*.java
jar cfm dist/Hearts.jar Manifest.txt src/hearts/*.class
rm src/hearts/*.class