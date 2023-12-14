
	package atm;

	import java.util.Scanner;

	public class ATM {

	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);

	        Bank theBank = new Bank("Axis Bank of India");

	        User aUser = theBank.addUser("Mama" , "Rani" , "0000");

	        Account newAccount = new Account("checking" , aUser , theBank);
	        aUser.addAccount(newAccount);
	        theBank.addAccount(newAccount);

	        User curUser;
	        while (true) {

	            // stay at the login prompt until successful login
	            curUser = ATM.MainMenuPrompt(theBank , sc);

	            //stay in main menu until user exit
	            ATM.printUserMenu(curUser , sc);
	        }
	    }

	    /**
	     * print the ATM's login menu
	     *
	     * @param theBank
	     * @param sc
	     * @return
	     */
	    public static User MainMenuPrompt(Bank theBank , Scanner sc) {
	        String userID, pin;
	        User authUser;

	        do {
	            System.out.printf("\n\n Welcome to %s\n\n" , theBank.getName());
	            System.out.print("Enter User ID: ");
	            userID = sc.nextLine();
	            System.out.print("Enter Pin: ");
	            pin = sc.nextLine();

	            //try to get the  user object corresponding to the id and pin compo
	            authUser = theBank.userLogin(userID , pin);
	            if (authUser == null) {
	                System.out.println("Incorrect user ID/pin combination. " + "Please try again");
	            }
	        } while (authUser == null);
	        return authUser;
	    }

	    public static void printUserMenu(User theUser , Scanner sc) {

	        // print a summary of the user's accounts
	        theUser.printAccountSummary();

	        //init
	        int choise;

	        //user menu
	        do {
	            System.out.printf("Welcome %s, What would you like to do?\n" , theUser.getFirstName());
	            System.out.println("  1) Show account transaction history.");
	            System.out.println("  2) Withdrawl.");
	            System.out.println("  3) Doposit.");
	            System.out.println("  4) Transfer.");
	            System.out.println("  5) Quit.");
	            System.out.println();
	            System.out.println("Enter choise: ");
	            choise = sc.nextInt();

	            if (choise < 1 || choise > 5) {
	                System.out.println("Invalid choise. Please choose 1-5");
	            }
	        } while (choise < 1 || choise > 5);

	        //process the choise 
	        switch (choise) {
	            case 1:
	                ATM.showTransHistory(theUser , sc);
	                break;
	            case 2:
	                ATM.withdrawFunds(theUser , sc);
	                break;
	            case 3:
	                ATM.depositFunds(theUser , sc);
	                break;
	            case 4:
	                ATM.transferFunds(theUser , sc);
	                break;
	            case 5:
	                //gobble up rest of previous input
	                sc.nextLine();
	                break;
	        }

	        //redisplay this menu unless the user wants to quit
	        if (choise != 5) {
	            ATM.printUserMenu(theUser , sc);
	        }
	    }

	    /**
	     * show the transaction history for
	     * an account
	     *
	     * @param theUser
	     * @param sc
	     */
	    public static void showTransHistory(User theUser , Scanner sc) {

	        int theAcct;

	        //get account whose transaction history to look at 
	        do {
	            System.out.printf("Enter the number (1-%d) of the account " + " Whose transactions you want to see:  " , theUser.numAccounts());
	            theAcct = sc.nextInt() - 1;
	            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
	                System.out.println("Invalid account. Please try again");
	            }
	        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

	        //print the transaction history
	        theUser.printAccTransaHistory(theAcct);
	    }

	    /**
	     * process transferring fund from
	     * one account to another
	     *
	     * @param theUser
	     * @param sc
	     */
	    public static void transferFunds(User theUser , Scanner sc) {

	        //inits
	        int fromAcct, toAcct;
	        double amount, acctBal;

	        //get the account to transfer from
	        do {
	            System.out.printf("Enter the number (1-%d) of the account " + "to transfer from: " , theUser.numAccounts());
	            fromAcct = sc.nextInt() - 1;
	            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
	                System.out.println("Invaid account. Please try again.");
	            }

	        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

	        acctBal = theUser.getAcctBalance(fromAcct);

	        // get the account to transfer to
	        do {
	            System.out.printf("Enter the number (1-%d) of the account " + "to transfer to: " , theUser.numAccounts());
	            toAcct = sc.nextInt() - 1;
	            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
	                System.out.println("Invaid account. Please try again.");
	            }

	        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

	        //get the amount to transfer
	        do {
	            System.out.printf("Enter the amount to transfer (max $%.02f): $" , acctBal);
	            amount = sc.nextDouble();
	            if (amount < 0) {
	                System.out.println("Amount must be greater than zero");
	            } else if (amount > acctBal) {
	                System.out.printf("Amount must be greater than " + "balance of $%.02f.\n" , acctBal);
	            }
	        } while (amount < 0 || amount > acctBal);

	        // finally, do the transfer
	        theUser.addAcctTransaction(fromAcct , -1 * amount , String.format("Transfer to account %s" , theUser.getAcctUUID(toAcct)));
	        theUser.addAcctTransaction(toAcct , amount , String.format("Transfer to account %s" , theUser.getAcctUUID(fromAcct)));

	    }

	    /**
	     * process a fund withdraw from an
	     * account
	     *
	     * @param theUser
	     * @param sc
	     */
	    public static void withdrawFunds(User theUser , Scanner sc) {

	        //inits
	        int fromAcct;
	        double amount, acctBal;
	        String memo;

	        //get the account to withdraw from
	        do {
	            System.out.printf("Enter the number (1-%d) of the account " + "to withdraw from: " , theUser.numAccounts());
	            fromAcct = sc.nextInt() - 1;
	            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
	                System.out.println("Invaid account. Please try again.");
	            }

	        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

	        acctBal = theUser.getAcctBalance(fromAcct);

	        //get the amount to transfer
	        do {
	            System.out.printf("Enter the amount to withdraw (max $%.02f): $" , acctBal);
	            amount = sc.nextDouble();
	            if (amount < 0) {
	                System.out.println("Amount must be greater than zero");
	            } else if (amount > acctBal) {
	                System.out.printf("Amount must be greater than " + "balance of $%.02f.\n" , acctBal);
	            }
	        } while (amount < 0 || amount > acctBal);

	        //gobble up rest of previous input
	        sc.nextLine();

	        // get a memo
	        System.out.print("Enter a memo ");
	        memo = sc.nextLine();

	        //do the withdraw
	        theUser.addAcctTransaction(fromAcct , -1 * amount , memo);
	    }

	    /**
	     * process a fund deposit to an
	     * account
	     *
	     * @param theUser
	     * @param sc
	     */
	    public static void depositFunds(User theUser , Scanner sc) {

	        //inits
	        int toAcct;
	        double amount, acctBal;
	        String memo;

	        //get the account to deposit in
	        do {
	            System.out.printf("Enter the number (1-%d) of the account " + "to deposit in: " , theUser.numAccounts());
	            toAcct = sc.nextInt() - 1;
	            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
	                System.out.println("Invaid account. Please try again.");
	            }
	        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

	        acctBal = theUser.getAcctBalance(toAcct);

	        //get the amount to transfer
	        do {
	            System.out.printf("Enter the amount to transfer (max $%.02f): $" , acctBal);
	            amount = sc.nextDouble();
	            if (amount < 0) {
	                System.out.println("Amount must be greater than zero");
	            }
	        } while (amount < 0);

	        //gobble up rest of previous input
	        sc.nextLine();

	        // get a memo
	        System.out.print("Enter a memo ");
	        memo = sc.nextLine();

	        //do the withdraw
	        theUser.addAcctTransaction(toAcct , amount , memo);
	    }
	}


