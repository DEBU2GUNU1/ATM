package atm;

	

	import java.util.ArrayList;
	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	import java.util.logging.Level;
	import java.util.logging.Logger;

	public class User {

	    private String firstName;
	    private String lastName;
	    private String uuid;
	    private byte pinHash[];
	    private ArrayList<Account> accounts;

	    public User(String firstName , String lastName , String pin , Bank theBank) {

	        this.firstName = firstName;
	        this.lastName = lastName;

	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            this.pinHash = md.digest(pin.getBytes());
	        } catch (NoSuchAlgorithmException ex) {
	            System.err.println("Error, cought NoSuchAlgorithmException");
	            Logger.getLogger(User.class.getName()).log(Level.SEVERE , null , ex);
	            System.exit(1);
	        }

	        this.uuid = theBank.getNewUSerUUID();

	        this.accounts = new ArrayList<Account>();

	        System.out.printf("New user %s, %s with ID %s created. \n " , firstName , lastName , this.uuid);
	    }

	  
	    public String getUUID() {
	        return this.uuid;
	    }

	    public String getFirstName() {
	        return this.firstName;
	    }

	    public double getAcctBalance(int acctIdx) {
	        return this.accounts.get(acctIdx).getBalance();
	    }

	    public String getAcctUUID(int acctIdx) {
	        return this.accounts.get(acctIdx).getUUID();
	    }

	    public void addAccount(Account anAcct) {
	        this.accounts.add(anAcct);
	    }

	    public boolean validatePin(String aPin) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            return MessageDigest.isEqual(md.digest(aPin.getBytes()) , this.pinHash);
	        } catch (NoSuchAlgorithmException ex) {
	            System.err.println("Error, cought NoSuchAlgorithmException");
	            Logger.getLogger(User.class.getName()).log(Level.SEVERE , null , ex);
	            System.exit(1);
	        }
	        return false;
	    }

	    public void printAccountSummary() {
	        System.out.printf("\n\n%s's accounts summary\n" , this.firstName);
	        for (int a = 0 ; a < this.accounts.size() ; a++) {
	            System.out.printf("%d) %s\n" , a + 1 , this.accounts.get(a).getSummaryLine());
	        }
	        System.out.println();
	    }

	    public int numAccounts() {
	        return this.accounts.size();
	    }

	    public void printAccTransaHistory(int acctIdx) {
	        this.accounts.get(acctIdx).printTransHistory();

	    }

	    public void addAcctTransaction(int acctIdx , double amount , String memo) {
	        this.accounts.get(acctIdx).addTransaction(amount , memo);
	    }
	}



