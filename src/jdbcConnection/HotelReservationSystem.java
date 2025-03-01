package jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservationSystem {

	private static final String url="jdbc:mysql://localhost:3306/hotel_db";
	
	private static final String username="root";
	
    private static final String password="2532003H@rsh";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException ,InterruptedException
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection con=DriverManager.getConnection(url,username,password);
		
		while(true)
		{
			System.out.println();
			System.out.println("HOTEL MANAGEMENT SYSTEM");
			Scanner sc=new Scanner(System.in);
			System.out.println("1. Reserve a room");
			System.out.println("2. View Reservation");
			System.out.println("3. Get Room Number");
			System.out.println("4. Update Reservation");
			System.out.println("5. Delete Reservation");
			System.out.println("6. Exit");
			int choice=sc.nextInt();			
			switch(choice)
			{
			   case 1: reservationRoom(con,sc); 
			           break;
			   case 2: viewReservation(con,sc);
			           break;
 			   case 3: getRoomNumber(con,sc);
	                   break;
			   case 4: updateReservation(con,sc);
	                   break;
			   case 5: deleteReservation(con, sc);
                       break;
			   case 6: exit();
	                    sc.close();
	                    return;
	               default:System.out.println("Invalid Choice");
			}
		}

	}

	public static void reservationRoom(Connection con, Scanner sc) throws SQLException 
	{
		System.out.println("================Reserve A Room==============");
		System.out.println("Enter guest Name");
		String guestName=sc.next();
		System.out.println("Enter room number");
		int roomNumber=sc.nextInt();
		System.out.println("Enter guest Contact Number");
		String contactNumber=sc.next();
		
		String sql="INSERT INTO reservation(guest_name,room_number,contact_number)" +
				"values ('" + guestName + "', " + roomNumber + " , '" + contactNumber + "')";
		Statement st=con.createStatement();		
		st.executeUpdate(sql);
		System.out.println("Room reserverd successfully!");
	}
	
	
	public static void viewReservation(Connection con, Scanner sc) throws SQLException 
	{
		
	    
		String sql="SELECT * FROM reservation";
		Statement st=con.createStatement();		
		ResultSet rs=st.executeQuery(sql);
		System.out.println(" id"+"  |  "+"Guest"+"     |    "+"room"+"  |  "+"contact"+"    |  "+"date");
		System.out.println();
	    while(rs.next())
	    {
	    	int id=rs.getInt("reservation_id");
	    	String guest=rs.getString("guest_name");
	    	int room=rs.getInt("room_number");
	    	String contact=rs.getString("contact_number");
	    	String date=rs.getTimestamp("reservation_date").toString();
	    	System.out.println(" "+ id +".  ||  "+guest +"    ||    " + room +"  ||   "+ contact +"  ||  "+ date);
	    }
	}
	
	
	public static void getRoomNumber(Connection con,Scanner sc) throws SQLException
	{
		System.out.println("Enter Reservation Id : ");
		int reservationId=sc.nextInt();	
		
		if (!reservationExists(con, reservationId)) {
			System.out.println("Reservation not found for the given ID.");
			return;
		}
		
		String sql="SELECT room_number FROM reservation WHERE reservation_id = " + reservationId ;
		
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(sql);
		if(rs.next())
		{
			int roomNumber=rs.getInt("room_number");
			System.out.println("Room Number for reservation id : "+reservationId + 
					 " is : "+roomNumber);
		}
		else
		{
			System.out.println("Reservation not Found");
		}		
	}
	
	private static void updateReservation(Connection con, Scanner sc) throws SQLException {
	
			System.out.print("Enter reservation ID to update: ");
			int reservationId = sc.nextInt();
			sc.nextLine(); // Consume the newline character

			if (!reservationExists(con, reservationId)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}

			System.out.print("Enter new guest name: ");
			String newGuestName = sc.nextLine();
			System.out.print("Enter new room number: ");
			int newRoomNumber = sc.nextInt();
			System.out.print("Enter new contact number: ");
			String newContactNumber = sc.next();

			String sql = "UPDATE reservation SET guest_name = '" + newGuestName + "', " + "room_number = "
					+ newRoomNumber + ", " + "contact_number = '" + newContactNumber + "' " + "WHERE reservation_id = "
					+ reservationId;

			Statement statement = con.createStatement();
			int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) 
				{
					System.out.println("Reservation updated successfully!");
				} else 
				{
					System.out.println("Reservation update failed.");
				}
	}

	private static boolean reservationExists(Connection connection, int reservationId) 
	{
		try {
			String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = " + reservationId;

			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(sql)) {

				return resultSet.next(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false; 
		}
	}
	public static void deleteReservation(Connection con,Scanner sc) throws SQLException
	{
        System.out.println("Enter the Reservation Id to delete");
        int id=sc.nextInt();
        
        if (!reservationExists(con, id)) {
			System.out.println("Reservation not found for the given ID.");
			return;
		}
		String sql="DELETE FROM reservation WHERE reservation_id="+id;
		Statement st=con.createStatement();
		int row=st.executeUpdate(sql);
		if(row>0)
			System.out.println("Deletion of Reservation id :"+ id+ " successfull");
		else
			System.out.println("Deletion Failed");
	}
	
	public static void exit() throws InterruptedException
	{
        System.out.print("Exiting System");
        for(int i=0;i<5;i++)
        {
        	Thread.sleep(300);
        	System.out.print("."+" ");
        }
        System.out.println("\nThank you");
	}

}
