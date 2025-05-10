# Use official Tomcat with JDK 21 base image
FROM tomcat:11.0.6-jdk21-temurin-noble

# Remove default webapps (optional)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into the Tomcat webapps folder
COPY target/myapp.war /usr/local/tomcat/webapps/myapp.war

# Expose default Tomcat port
EXPOSE 8080
