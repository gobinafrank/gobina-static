package com.example.myapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<title>DobreTech - DevOps Pipeline Demo</title>");
        out.println("<link rel=\"stylesheet\" href=\"css/styles.css\">");
        out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css\">");
        out.println("<style>");
        out.println("body { padding-top: 0; }"); // Ensure no extra padding since we're not in the main app layout
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<header>");
        out.println("<div class=\"container\">");
        out.println("<div class=\"logo\">");
        out.println("<span class=\"logo-text\">DobreTech</span>");
        out.println("<span class=\"tagline\">DevOps Solutions</span>");
        out.println("</div>");
        out.println("<nav>");
        out.println("<ul>");
        out.println("<li><a href=\"index.html\">Home</a></li>");
        out.println("<li><a href=\"#\" class=\"active\">Demo</a></li>");
        out.println("</ul>");
        out.println("</nav>");
        out.println("</div>");
        out.println("</header>");
        
        out.println("<section class=\"demo-page\">");
        out.println("<div class=\"container\">");
        out.println("<div class=\"demo-card\">");
        out.println("<h1>DevOps Pipeline Demo</h1>");
        out.println("<div class=\"server-info\">");
        out.println("<p><i class=\"fas fa-clock\"></i> Server Time: " + currentTime + "</p>");
        out.println("<p><i class=\"fas fa-server\"></i> Server: Apache Tomcat</p>");
        out.println("<p><i class=\"fas fa-code\"></i> Application: Java Servlet</p>");
        out.println("</div>");
        
        out.println("<div class=\"pipeline-components\">");
        out.println("<h2>CI/CD Pipeline Components</h2>");
        out.println("<div class=\"component-grid\">");
        
        // Component cards
        String[][] components = {
            {"GitHub", "fa-github", "Version control system for source code management"},
            {"Jenkins", "fa-jenkins", "Automation server for continuous integration and delivery"},
            {"SonarQube", "fa-bug", "Code quality and security scanning platform"},
            {"Maven", "fa-cogs", "Build automation and dependency management tool"},
            {"Nexus", "fa-box", "Repository manager for storing build artifacts"},
            {"Ansible", "fa-rocket", "Automation tool for application deployment"},
            {"Apache", "fa-server", "Web server for hosting applications"},
            {"Prometheus/Grafana", "fa-chart-line", "Monitoring and visualization platform"}
        };
        
        for (String[] component : components) {
            out.println("<div class=\"component-card\">");
            out.println("<div class=\"component-icon\"><i class=\"fab " + component[1] + "\"></i></div>");
            out.println("<h3>" + component[0] + "</h3>");
            out.println("<p>" + component[2] + "</p>");
            out.println("</div>");
        }
        
        out.println("</div>"); // End component-grid
        out.println("</div>"); // End pipeline-components
        
        out.println("<div class=\"deployment-info\">");
        out.println("<h2>Deployment Information</h2>");
        out.println("<ul>");
        out.println("<li><strong>Version:</strong> 1.0</li>");
        out.println("<li><strong>Build Date:</strong> May 7, 2024</li>");
        out.println("<li><strong>Environment:</strong> Production</li>");
        out.println("<li><strong>Deployment Method:</strong> Automated via Jenkins and Ansible</li>");
        out.println("</ul>");
        out.println("</div>");
        
        out.println("<div class=\"action-buttons\">");
        out.println("<a href=\"index.html\" class=\"btn btn-primary\"><i class=\"fas fa-home\"></i> Back to Home</a>");
        out.println("<a href=\"#\" class=\"btn btn-secondary\"><i class=\"fas fa-sync\"></i> Refresh</a>");
        out.println("</div>");
        
        out.println("</div>"); // End demo-card
        out.println("</div>"); // End container
        out.println("</section>");
        
        out.println("<footer>");
        out.println("<div class=\"container\">");
        out.println("<div class=\"footer-bottom\">");
        out.println("<p>&copy; 2024 DobreTech. All rights reserved.</p>");
        out.println("</div>");
        out.println("</div>");
        out.println("</footer>");
        
        out.println("</body>");
        out.println("</html>");
    }
}
