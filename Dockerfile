FROM tomcat:11.0.6-jdk21-temurin-noble

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from current directory (where Dockerfile is) into Tomcat
COPY myapp.war /usr/local/tomcat/webapps/myapp.war

# Expose Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]
