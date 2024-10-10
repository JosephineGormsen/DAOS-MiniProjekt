import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class opgB {
    static Connection minConnection;
    static Statement stmt;
    static BufferedReader inLine;

    public static void main(String[] args) {
        try {
            inLine = new BufferedReader(new InputStreamReader(System.in));

            // Database connection setup
            String server = "localhost"; // Database server
            String dbnavn = "karakterDB";    // Database name
            String login = "sa";          // Database login username
            String password = "SQLpassword1234"; // Database login password

            // Load the JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection to the database
            minConnection = DriverManager.getConnection("jdbc:sqlserver://" + server + ";databaseName=" + dbnavn + ";user=" + login + ";password=" + password + ";");

            if (minConnection != null) {
                System.out.println("Connection established successfully.");
                // Create the statement object after the connection is established
                stmt = minConnection.createStatement();
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (Exception e) {
            // Print out a more detailed error message
            System.out.println("Error during connection setup: " + e.getMessage());
            e.printStackTrace();
        }

        // Proceed only if stmt is initialized
        if (stmt != null) {
            insertmedstring();
        } else {
            System.out.println("Statement not initialized due to connection failure.");
        }

    }

    public static void insertmedstring() {
        try {
            // indlæsning
            System.out.println("Vi skal nu oprette en ny eksamensafvikling.\nI vores tilfælde er en eksamensafvikling det samme som et eksamensforsæg \n");
            System.out.println("Indtast navnet på den eksamen som du vil oprette en afvikling af:");
            String eksamen = inLine.readLine();

            System.out.println("I hvilken termin foregår denne eksamen? (Fx. S2024, V2024)");
            String termin = inLine.readLine();

            System.out.println("Hvad var startdatoen for denne eksamen? (Fx 2024-10-27, 2023-06-23");
            String startDato = inLine.readLine();

            System.out.println("Hvad var slutdatoen for denne eksamen? (Fx 2024-10-27, 2023-06-23");
            String slutDato = inLine.readLine();


            String sql = "insert into eksamensAfvikling values('" + termin + "', 0, '" +  startDato + "', '" + slutDato + "', null, null, 1, '" + eksamen + "')";
            System.out.println("SQL-streng er "+ sql);
            stmt.execute(sql);

            // pænt svar til brugeren
            System.out.println("Eksamenen er nu registreret");
            // pæn lukning
            if (!minConnection.isClosed()) minConnection.close();
        }
        catch (SQLException e) {
            switch (e.getErrorCode())
            // fejl-kode 547 svarer til en foreign key fejl
            { case 547 : {
                System.out.println("ukendt fremmednøglefejl");
                break;
            }
            // fejl-kode 2627 svarer til primary key fejl
                case 2627: { System.out.println("fejlSQL:  "+e.getMessage());
                }
            };
        }
        catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
        }
    };

}
