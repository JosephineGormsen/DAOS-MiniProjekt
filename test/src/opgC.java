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
        //Lav et java-program, der anvender JDBC. Via programmet skal du indtaste navnet på en
        //given eksamen og en given termin. Programmet skal som resultat vise en liste af de
        //studerende, der har deltaget i denne afvikling af denne eksamen i denne termin. Udover
        //den studerendes navn og id, skal karakteren også være med i resultatet.

        //select
        //s.id as studerende_id,
        //s.navn as studerende_navn,
        //e.navn as eksamen_navn,
        //ea.termin,
        //ea.karakter
        //
        //FROM
        //studerende s
        //join eksamensAfvikling ea on s.id = ea.studerende_id
        //join eksamen e on ea.eksamen_id = e_id
        //where e.navn = ? and ea.termin = ?;
       try {
            PreparedStatement prestmt = minConnection.prepareStatement("select s.id as studerende_id, s.navn as studerende_navn, e.navn as eksamen_navn, ea.termin, ea.karakter FROM studerende s join eksamensAfvikling ea on s.id = ea.studerende_id join eksamen e on ea.eksamen_id = e_id where e.navn = ? and ea.termin = ?;");
            System.out.println("Indtast eksamens navn: ");
            String eksamensNavn = inLine.readLine();
             System.out.println("Indtast termin: ");
            String termin = inLine.readLine();
            prestmt.setString(1, eksamensNavn);
            prestmt.setString(2, termin);
            ResultSet res = prestmt.executeQuery();
            while (res.next()) {
                System.out.println("Studerende id: " + res.getInt("studerende_id") + " Studerende navn: " + res.getString("studerende_navn") + " Eksamen navn: " + res.getString("eksamen_navn") + " Termin: " + res.getString("termin") + " Karakter: " + res.getString("karakter"));
            }
        } catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
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
            System.out.println("Indtast  ");
            System.out.println("skriv eksamen for select eksamen");
            String in = inLine.readLine();
            switch (in) {
                case "eksamen": {
                    selectEksamen();
                    break;
                }
                default:
                    System.out.println("ukendt indtastning");
            }
        }
        catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
        }
    }
}
