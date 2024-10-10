import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class JAVAexamples {

	/**
	 * @param args
	 */
	static Connection minConnection;
	static Statement stmt;
	static BufferedReader inLine;
	
	public static void selectudenparm() {
	try {
		// Laver sql-sætning og får den udført
		String sql = "select navn,stilling from person";
		System.out.println("SQL-streng er "+sql);
		ResultSet res=stmt.executeQuery(sql);
		// gennemløber svaret
		while (res.next()) {
			String s;
			s = res.getString("navn");
			System.out.println(s + "    " + res.getString(2));
		}
		// pæn lukning
 		if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}
	
	public static void selectmedparm() {
	try {
		// Indlæser søgestreng
		System.out.println("Indtast søgestreng");
		String inString = inLine.readLine();
		// Laver sql-sætning og får den udført
		String sql = "select navn,stilling from person where navn like '" + inString + "%'";
		System.out.println("SQL-streng er "+ sql);
		ResultSet res=stmt.executeQuery(sql);
		//gennemløber svaret
		while (res.next()) {
			System.out.println(res.getString(1) + "    " + res.getString(2));
		}
		// pæn lukning
 		if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}
	
	public static void insertmedstring() {
		try {
			// indlæsning
			System.out.println("Vi vil nu oprette et nyt ansættelsesfforhold");
			System.out.println("Indtast cpr (personen skal være oprettet på forhånd");
			String cprstr=inLine.readLine();
			System.out.println("Indtast firmanr (firma skal være oprettet på forhånd");
			String firmastr=inLine.readLine();
		
			// sender insert'en til db-serveren
			String sql = "insert into ansati values ('" + cprstr + "'," + firmastr + ")";
			System.out.println("SQL-streng er "+ sql);
			stmt.execute(sql);
			// pænt svar til brugeren
			System.out.println("Ansættelsen er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			        switch (e.getErrorCode())
			        // fejl-kode 547 svarer til en foreign key fejl
			        { case 547 : {if (e.getMessage().contains("cprforeign"))
										System.out.println("cpr-nummer er ikke oprettet");
			        			  else
			        			  if (e.getMessage().contains("firmaforeign"))
										System.out.println("firmaet er ikke oprettet");
			        			  else
			        				    System.out.println("ukendt fremmednøglefejl");
								  break;
			        			}
			        // fejl-kode 2627 svarer til primary key fejl
			          case 2627: {System.out.println("den pågældende ansættelse er allerede oprettet");
			          	          break;
			         			 }
			          default: System.out.println("fejlSQL:  "+e.getMessage());
				};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};
	
	public static void insertprepared() {
		try {
			// indl�sning
			System.out.println("Vi vil nu oprette et nyt ansættelsesforhold");
			System.out.println("Indtast cpr (personen skal være oprettet på forhånd");
			String cprstr=inLine.readLine();
			System.out.println("Indtast firmanr (firma skal være oprettet på forhånd");
			String firmastr=inLine.readLine();
			// Anvendelse af prepared statement
			String sql = "insert into ansati values (?,?)";
			PreparedStatement prestmt = minConnection.prepareStatement(sql);
			prestmt.clearParameters();
			prestmt.setString(1,cprstr);
			prestmt.setInt(2,Integer.parseInt(firmastr));
			// Udf�rer s�tningen
			prestmt.execute();
			// p�nt svar til brugeren
			System.out.println("Ansættelsen er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			        switch (e.getErrorCode())
			        // fejl-kode 547 svarer til en foreign key fejl
			        { case 547 : {if (e.getMessage().contains("cprforeign"))
									  System.out.println("cpr-nummer er ikke oprettet");
			        			  else	
			        			  if (e.getMessage().contains("firmaforeign"))
						              System.out.println("firmaet er ikke oprettet");
			        			  else
			        				  System.out.println("ukendt fremmednøglefejl");
								  break;
			        			}
			        // fejl-kode 2627 svarer til primary key fejl
			          case 2627: {System.out.println("den pågældende ansættelse er allerede oprettet");
			          	          break;
			         			 }
			          default: System.out.println("fejlSQL:  "+e.getMessage());
				};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};	
	
	public static void main(String[] args) {
		try {
			inLine = new BufferedReader(new InputStreamReader(System.in));
			//generel opsætning
			//via native driver
			String server="localhost"; //virker måske hos dig
			                                       //virker det ikke - prøv kun med localhost
			String dbnavn="cykelDB";            //virker måske hos dig
			String login="sa";                     //skal ikke ændres
			String password="SQLpassword1234";            //skal ændres
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			minConnection = DriverManager.getConnection("jdbc:sqlserver://"+server+";databaseName="+dbnavn+
					";user=" + login + ";password=" + password + ";");
			//minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=eksempeldb;user=sa;password=torben07;");
			stmt = minConnection.createStatement();
			//Indlæsning og kald af den rigtige metode
//			System.out.println("Indtast  ");
//			System.out.println("s for select uden parameter  ");
//			System.out.println("sp for select med parameter  ");
//			System.out.println("i for insert med strengmanipulation");
//			System.out.println("ps for insert med prepared statement ");
//			String in=inLine.readLine();
//			switch (in)
//			{case "s"  : {selectudenparm();break;}
//			 case "sp" : {selectmedparm();break;}
//			 case "i"  : {insertmedstring();break;}
//			 case "ps"  : {insertprepared();break;}
//			default : System.out.println("ukendt indtastning");
//			}
			//Opgave 14.1
//			dataForRytterMedInit();
			//Opgave 14.2
//			insert2022Results();
			//Opgave 15
//			insertPreparedPlacering();
			ArrayList<Person> liste = new ArrayList<>();
			System.out.println(liste);
			tilføjAlleRyttere(liste);
			System.out.println(liste);
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}

	//Opgave 14.1
	public static void dataForRytterMedInit() {
		try {
			// Indlæser søgestreng
			System.out.println("Indtast rytterinitaler");
			String inString = inLine.readLine();
			// Laver sql-sætning og får den udført
			String sql = "select aarstal, COALESCE(CAST(plac AS VARCHAR(9)), 'Udgået') AS placering from placering where init = '" + inString + "';";
			System.out.println("SQL-streng er "+ sql);
			ResultSet res=stmt.executeQuery(sql);
			//gennemløber svaret
			while (res.next()) {
				System.out.println(res.getString(1) + "    " + res.getString(2));
			}
			// pæn lukning
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}
	//Opgave 14.2
	public static void insert2022Results() {
		try {
			// indlæsning
			System.out.println("Vi vil nu indtaste resultaterne for 2022");
			System.out.println("Indtast initialer for rytteren (rytteren skal være oprettet på forhånd");
			String rytter=inLine.readLine();
			System.out.println("Indtast " + rytter + "s placering");
			String placering=inLine.readLine();

			// sender insert'en til db-serveren
			String sql = "insert into placering values (" + 2022 + ", '" + rytter + "', " + placering + ")";
			System.out.println("SQL-streng er "+ sql);
			stmt.execute(sql);
			// pænt svar til brugeren
			System.out.println("placeringen er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			switch (e.getErrorCode())
			// fejl-kode 547 svarer til en foreign key fejl
			{ case 547 : {if (e.getMessage().contains("initforeign"))
				System.out.println("Rytter findes ikke, så placeringen er ikke oprettet");
			else
				System.out.println("ukendt fremmednøglefejl");
				break;
			}
			// fejl-kode 2627 svarer til primary key fejl
				case 2627: {System.out.println("den pågældende placering er allerede oprettet");
					break;
				}
				default: System.out.println("fejlSQL:  "+e.getMessage());
			};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};
	//Opgave 15
	public static void insertPreparedPlacering() {
		try {
			// indl�sning
			System.out.println("Vi vil nu indtaste resultaterne for 2022");
			System.out.println("Indtast initialer for rytteren (rytteren skal være oprettet på forhånd");
			String rytter=inLine.readLine();
			System.out.println("Indtast " + rytter + "s placering");
			String placering=inLine.readLine();
			// Anvendelse af prepared statement
			String sql = "insert into placering values (2022, ?,?)";
			PreparedStatement prestmt = minConnection.prepareStatement(sql);
			prestmt.clearParameters();
			prestmt.setString(1,rytter);
			prestmt.setInt(2,Integer.parseInt(placering));
			// Udf�rer s�tningen
			prestmt.execute();
			// p�nt svar til brugeren
			System.out.println("Ansættelsen er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			switch (e.getErrorCode())
			// fejl-kode 547 svarer til en foreign key fejl
			{ case 547 : {if (e.getMessage().contains("initforeign"))
				System.out.println("Rytter findes ikke, så placeringen er ikke oprettet");
			else
				System.out.println("ukendt fremmednøglefejl");
				break;
			}
			// fejl-kode 2627 svarer til primary key fejl
				case 2627: {System.out.println("den pågældende placering er allerede oprettet");
					break;
				}
				default: System.out.println("fejlSQL:  "+e.getMessage());
			};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};
	public static class Person {
		private String initialer;
		private String navn;

		public Person(String initialer, String navn) {
			this.initialer = initialer;
			this.navn = navn;
		}

		@Override
		public String toString() {
			return "{" + initialer + " " + navn + "}";
		}
	}
	public static void tilføjAlleRyttere(ArrayList liste) {
		try {
			String sql = "select init, rytternavn from rytter";
			System.out.println("SQL-streng er "+ sql);
			ResultSet res=stmt.executeQuery(sql);
			while (res.next()) {
				String init = res.getString("init");
				String rytterNavn = res.getString("rytternavn");
				Person p1 = new Person(init, rytterNavn);
				liste.add(p1);
			}
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}


}

