import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class opgC {

    /**
     * @param args
     */
    static Connection minConnection;
    static Statement stmt;
    static BufferedReader inLine;

    public static void selectEksamen(){
       try {
           String sql = "SELECT \n" +
                   "    s.studenterID,\n" +
                   "    s.navn AS studerende_navn,\n" +
                   "    e.navn AS eksamen_navn,\n" +
                   "    ea.termin,\n" +
                   "    ea.karakter\n" +
                   "FROM \n" +
                   "    studerende s\n" +
                   "JOIN \n" +
                   "    eksamensAfvikling ea ON s.studenterID = ea.fk_studerende \n" +
                   "JOIN \n" +
                   "    eksamen e ON ea.fk_eksamen = e.navn \n" +
                   "WHERE \n" +
                   "    e.navn = ? AND ea.termin = ?;";
           PreparedStatement prestmt = minConnection.prepareStatement(sql);

           System.out.println("Indtast eksamens navn: ");
           String eksamensNavn = inLine.readLine();
           System.out.println("Indtast termin: ");
           String termin = inLine.readLine();

              prestmt.setString(1, eksamensNavn);
              prestmt.setString(2, termin);

           ResultSet res = prestmt.executeQuery();
           boolean hasResults = false;
           while (res.next()) {
               hasResults = true;
               System.out.println("Studerende id: " + res.getInt("studenterID") +
                       " Studerende navn: " + res.getString("studerende_navn") +
                       " Eksamen navn: " + res.getString("eksamen_navn") +
                       " Termin: " + res.getString("termin") +
                       " Karakter: " + res.getString("karakter"));
           }
           if (!hasResults) {
               System.out.println("Ingen resultater fundet.");
           }
           res.close();
           prestmt.close();
       } catch (Exception e) {
           System.out.println("Fejl: " + e.getMessage());
           e.printStackTrace();
       }

    }

    public static void main(String [] args){
        try {
            inLine = new BufferedReader(new InputStreamReader(System.in));
            //generel opsætning
            //via native driver
            String server="localhost"; //virker måske hos dig
            //virker det ikke - prøv kun med localhost
            String dbnavn="DAOSMiniProjekt";            //virker måske hos dig
            String login="sa";                     //skal ikke ændres
            String password="reallyStrongPwd123";            //skal ændres
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            minConnection = DriverManager.getConnection("jdbc:sqlserver://"+server+";databaseName="+dbnavn+
                    ";user=" + login + ";password=" + password + ";");
            //minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=eksempeldb;user=sa;password=torben07;");
            stmt = minConnection.createStatement();
            selectEksamen();
            stmt.close();
            minConnection.close();
        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
            e.printStackTrace();
        }
        }
    }

