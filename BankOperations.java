import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BankOperations {
Scanner in=new Scanner(System.in);
static int withAmt=0;
static int acc1bal=0;
	public void register(PreparedStatement insertstmt){
	
		
		try {
			System.out.println("Enter the customer id: ");
			insertstmt.setInt(1,in.nextInt());
			System.out.println("Enter the customer name: ");
			insertstmt.setString(2,in.next());
			System.out.println("Enter the customer address: ");
			insertstmt.setString(3,in.next());
			System.out.println("Enter the account no: ");
			insertstmt.setInt(4,in.nextInt());
			System.out.println("Enter the account pin: ");
			insertstmt.setInt(5,in.nextInt());
			System.out.println("Enter the customer balance: ");
			insertstmt.setLong(6,in.nextLong());
		int rowsinserted=insertstmt.executeUpdate();
			System.out.println("Successfully inserted");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
	
	public int login(Connection con){
		System.out.println("Enter your Account No. and Pin: ");
		String cname=null;
		int secAccno=0;
		long accno=in.nextLong();
		int accpin=in.nextInt();
		try {
			PreparedStatement stmt=con.prepareStatement("Select CUSTOMER_NAME,ACCOUNT_NO from Customer where ACCOUNT_NO=? and ACCOUNT_PIN=?");
			stmt.setLong(1,accno);
			stmt.setInt(2,accpin);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			 cname=rs.getString("CUSTOMER_NAME");
			 secAccno=rs.getInt("ACCOUNT_NO");
		}
		if(cname!=null && secAccno!=0){
			System.out.println("Welcome "+cname+" !!!!!!");
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secAccno;
		
	}
	
	public void withdrawal(int Accno,Connection con){
		int bal=0;
		try {
			PreparedStatement withdrawstmt=con.prepareStatement("Select Account_Balance from Customer where ACCOUNT_NO=?");
			withdrawstmt.setInt(1,Accno);
			ResultSet rs=withdrawstmt.executeQuery();
			while(rs.next()){
				 bal=rs.getInt("ACCOUNT_BALANCE");
				
			}
			if(bal!=0){
				System.out.println("Enter the amount to withdraw: ");
				 withAmt=in.nextInt();
				if(withAmt<bal){
					int remBal=bal-withAmt;
					PreparedStatement update=con.prepareStatement("Update Customer Set ACCOUNT_BALANCE=? where ACCOUNT_NO=?");
					update.setInt(1,remBal);
					update.setInt(2,Accno);
					update.executeUpdate();
					System.out.println("The money withdrawn is: "+withAmt);
					System.out.println("The remaining balance is: "+remBal);
				}
				else{
					throw new NotEnoughBalance();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NotEnoughBalance not){
			not.balancedetails();
		}
	}
	
	public void deposit(int Accno,Connection con){
		int actualAmt=0;
		try {
			PreparedStatement depstmt=con.prepareStatement("select ACCOUNT_BALANCE from customer where ACCOUNT_NO=?");
			depstmt.setInt(1,Accno);
			ResultSet rs=depstmt.executeQuery();
			while(rs.next()){
				actualAmt=rs.getInt("ACCOUNT_BALANCE");
			}
			
			System.out.println("Enter the amount to deposit:");
			int depAmt=in.nextInt();
			int newAmt=actualAmt+depAmt;
			PreparedStatement depstmt1=con.prepareStatement("update customer set account_balance=? where ACCOUNT_NO=?");
			depstmt1.setInt(1,newAmt);
			depstmt1.setInt(2,Accno);
			int result=depstmt1.executeUpdate();
			System.out.println("The amount "+depAmt+" is deposited successfully!!!!");
			System.out.println("The current balance is"+newAmt);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	public void deposit_transfer(int Accno,Connection con){
		int actualAmt=0;
		try {
			PreparedStatement depstmt=con.prepareStatement("select ACCOUNT_BALANCE from customer where ACCOUNT_NO=?");
			depstmt.setInt(1,Accno);
			ResultSet rs=depstmt.executeQuery();
			while(rs.next()){
				actualAmt=rs.getInt("ACCOUNT_BALANCE");
			}
			
			//System.out.println("Enter the amount to deposit:");
			int depAmt=withAmt;
			int newAmt=actualAmt+depAmt;
			PreparedStatement depstmt1=con.prepareStatement("update customer set account_balance=? where ACCOUNT_NO=?");
			depstmt1.setInt(1,newAmt);
			depstmt1.setInt(2,Accno);
			int result=depstmt1.executeUpdate();
			System.out.println("The amount "+depAmt+" is deposited successfully!!!!");
			System.out.println("The current balance is"+newAmt);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	public void fundTransfer(int Accno,Connection con){
		System.out.println("Enter the another Account Number:");
		int acc2=in.nextInt();
	 acc1bal=0;
		String recName=null;
		try {
			PreparedStatement trnstmt=con.prepareStatement("Select Customer_name,Account_balance from customer where account_no=?");
			trnstmt.setInt(1,acc2);
			ResultSet rs=trnstmt.executeQuery();
			while(rs.next()){
				recName=rs.getString("Customer_name");
				acc1bal=rs.getInt("Account_balance");
			}
			if(recName!=null){
				this.withdrawal(Accno,con);
				this.deposit_transfer(acc2, con);
			}
			else{
				System.out.println("The account not found!!!!!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printDetails(int Accno,Connection con){
		try {
			PreparedStatement get=con.prepareStatement("Select * from customer where ACCOUNT_NO=?");
			get.setInt(1,Accno);
			ResultSet rs=get.executeQuery();
			while(rs.next()){
				System.out.println("The customer ID is:");
				System.out.println(rs.getInt("customer_id"));
				System.out.println("The customer name is:");
				System.out.println(rs.getString("customer_name"));
				System.out.println("The customer address is:");
				System.out.println(rs.getString("customer_address"));
				System.out.println("The account no. is:");
				System.out.println(rs.getInt("account_no"));
//				System.out.println("The account pin is:");
//				System.out.println(rs.getInt("account_pin"));
				System.out.println("The account balance is:");
				System.out.println(rs.getInt("account_balance"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
