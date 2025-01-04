import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve the homepage
        server.createContext("/", new HomePageHandler());

        // Serve the CSS file
        server.createContext("/styles.css", exchange -> {
            byte[] css = Files.readAllBytes(Paths.get("src/styles.css"));
            exchange.getResponseHeaders().set("Content-Type", "text/css");
            exchange.sendResponseHeaders(200, css.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(css);
            }
        });

        // Serve the image file
        server.createContext("/giyu.jpg", exchange -> {
            byte[] image = Files.readAllBytes(Paths.get("src/giyu.jpg"));
            exchange.getResponseHeaders().set("Content-Type", "image/jpg");
            exchange.sendResponseHeaders(200, image.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(image);
            }
        });
        server.createContext("/next", exchange -> {
            String nextPageResponse = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Shalom's Portfolio Projects</title>
            <link rel="stylesheet" type="text/css" href="/styles.css">
        </head>
        <body>
            <h1>Welcome to my Portfolio!</h1>
            <p>You were redirected here by clicking the button!</p>
            <p>This page contains all the projects that I have created.<p>
            <a href="/">Go Back to Home</a>
            <button onclick="goToHomePage()">Go Back to Home</button>
        </body>
            <h1>Calculator Application</h1>
        </html>
    """;
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, nextPageResponse.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(nextPageResponse.getBytes());
            }
        });

        System.out.println("Server is running on http://localhost:8080/");
        server.start();
    }

    // Handler for the homepage
    static class HomePageHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <img src="giyu.jpg" alt="giyu" width="500" height="400">
                    <title>Shalom's Portfolio</title>
                    <link rel="stylesheet" type="text/css" href="/styles.css">
                    <script>
                        function goToNextPage() {
                            window.location.href = '/next';
                        }
                    </script>
                </head>
                <body>
                    <h1>Welcome to my Portfolio!</h1>
                    <p>My name is Shalom Oyewusi and this is my Portfolio!</p>
                    <button onclick="goToNextPage()">Go to Next Page</button>
                </body>
                </html>
            """;
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}