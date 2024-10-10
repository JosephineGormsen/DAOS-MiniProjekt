import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class opgA {
    static Connection minConnection;
    static Statement stmt;
    static BufferedReader inLine;

    public static void main(String[] args) {
        try {
            inLine = new BufferedReader(new InputStreamReader(System.in));

            // Database connection setup
            String server = "localhost"; // Database server
            String dbnavn = "DAOSMiniProjekt";    // Database name
            String login = "sa";          // Database login username
            String password = "reallyStrongPwd123"; // Database login password

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

        insertmedstring();
    }

    public static void insertmedstring() {
        try {
            // indlæsning
            System.out.println("Vi skal nu oprette et nyt eksamensforsøg.");
            System.out.println("Indtast navn på den studerende som du vil oprette et eksamensforsøg for:");
            String studerendeNavn = inLine.readLine();
            String administrativBedømmelse = inLine.readLine();

            String sql = "select studenterID from studerende where navn = '" + studerendeNavn + "'";
            ResultSet res = stmt.executeQuery(sql);
            //gennemløber svaret
            int StudenterID = 0;
            while (res.next()) {
                StudenterID = res.getInt("studenterID");
            }

            System.out.println("Indtast hvilken eksamen har " + studerendeNavn + " været oppe i:");
            String eksamenNavn = inLine.readLine();

            System.out.println("Mødte den studerende op til eksamenen? (ja/nej)");
            String yesNo = inLine.readLine();
            if (yesNo.toLowerCase().equals("ja")) {
                System.out.println("Hvilken karakter fik den studerende? (-3, 00, 02, 4, 7, 10, 12");
                String karakter = inLine.readLine();
                sql = "insert into eksamensAfvikling values('S2024', 1, '2024.04.27','2024.04.29', null, " + karakter + ", " + StudenterID + ", " + eksamenNavn + "'";
                System.out.println("SQL-streng er " + sql);
                stmt.execute(sql);
            } else {
                System.out.println("Hvorfor mødte den studerende ikke op?");
                System.out.println("Indtast koden for grunden til at eksaminationen ikke blev gennemført");
                System.out.println("SY = Syg \nIM = Ikke mødt op \nIA = ikke afleveret");
                String ab = inLine.readLine();
                sql = "insert into eksamensAfvikling values('S2024', 1, '2024.04.27','2024.04.29', '" + ab + "' , " + null + ", " + StudenterID + ", " + eksamenNavn + "'";
                System.out.println("SQL-streng er " + sql);
                stmt.execute(sql);
            }

            // pænt svar til brugeren
            System.out.println("Ansættelsen er nu registreret");
            if (!minConnection.isClosed()) minConnection.close();
            // pæn lukning
            if (!minConnection.isClosed()) minConnection.close();
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                // fejl-kode 547 svarer til en foreign key fejl
                case 547:
                    if (e.getMessage().contains("cprforeign"))
                        System.out.println("cpr-nummer er ikke oprettet");
                    else if (e.getMessage().contains("firmaforeign"))
                        System.out.println("firmaet er ikke oprettet");
                    else
                        System.out.println("ukendt fremmednøglefejl");
                    break;
                // fejl-kode 2627 svarer til primary key fejl
                case 2627:
                    System.out.println("den pågældende ansættelse er allerede oprettet");
                    break;
                default:
                    System.out.println("fejlSQL:  " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
