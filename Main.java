package Finalbzh_Banking7;

import com.intel.crypto.*;
import com.intel.langutil.ArrayUtils;
import com.intel.util.*; 

//
// Implementation of DAL Trusted Application: Finalbzh_Banking7 
//
// **************************************************************************************************
// NOTE:  This default Trusted Application implementation is intended for DAL API Level 7 and above
// **************************************************************************************************

public class Main extends IntelApplet {
	
	//boolean logged = false;
	/*RsaAlg rsa_Keys = RsaAlg.create();
	byte[] MyZero = new byte[1];
	{MyZero[1]= 0;} */
	
	
// INTIALISATION OF TABLES 
	

	//private static final byte[] arrayAccount = new byte[47];         // This array contains FirstName, Name, TZ, Balance and PreviousTransaction
	//private static final byte[] arrayPassword = new byte[17];        // This array contains TZ and the password
	//static final byte[] arrayCrypto = new byte[200];          // This array contains TZ and the cryptographic password

/*{
	for(int a = 0; a < arrayAccount.length; a++) {                // Initialization of the first array
		for(int b = 0; b <= 5; b++) {
			arrayAccount[a][b] = 0;
		}
	}

	for(int a = 0; a < arrayPassword.length; a++) {                // Initialization of the second and third array
		for(int b = 0; b <= 2; b++) {
			arrayPassword[a][b] = 0;
			arrayCrypto[a][b] = 0;
		} 
	}
}*/

	/**
	 * This method will be called by the VM when a new session is opened to the Trusted Application 
	 * and this Trusted Application instance is being created to handle the new session.
	 * This method cannot provide response data and therefore calling
	 * setResponse or setResponseCode methods from it will throw a NullPointerException.
	 * 
	 * @param	request	the input data sent to the Trusted Application during session creation
	 * 
	 * @return	APPLET_SUCCESS if the operation was processed successfully, 
	 * 		any other error status code otherwise (note that all error codes will be
	 * 		treated similarly by the VM by sending "cancel" error code to the SW application).
	 */
	
	
	
	
	public int onInit(byte[] request) {
		DebugPrint.printString("Hello, DAL!");
		return APPLET_SUCCESS;
	}
	
	/**
	 * This method will be called by the VM to handle a command sent to this
	 * Trusted Application instance.
	 * 
	 * @param	commandId	the command ID (Trusted Application specific) 
	 * @param	request		the input data for this command 
	 * @return	the return value should not be used by the applet
	 */
	
	
	public int invokeCommand(int commandId, byte[] request) {
		
		DebugPrint.printString("Received command Id: " + commandId + ".");
		if(request != null)
		{
			DebugPrint.printString("Received buffer:");
			DebugPrint.printBuffer(request);
		}
		
		
		
		
		
		 byte[] response = new byte[50];
			
			switch (commandId)  {
			case 1:
				response= Register(request);
			    break;
			case 2:
				response = SavePasseword(request);
				break;
			case 3:
				response = Login(request);
				break;
		///  case 4:
		//		response = ResetPassword(request);  // SAVE PSSWD 
		//		break;
			case 5:
				response = DisplayAccount(request);
				break;
			case 6:
			   response = Deposit(request);  // rajouter balance
				break;
			case 7:
				response = Withdraw(request);  // enlever balance
				break;
			case 8:
				response = transfer(request);  // enelver balance
				break;
				 
			    
				  
			default:
				break;
			}
		/*
		 * To return the response data to the command, call the setResponse
		 * method before returning from this method. 
		 * Note that calling this method more than once will 
		 * reset the response data previously set.
		 */
		setResponse(response, 0, response.length);

		/*
		 * In order to provide a return value for the command, which will be
		 * delivered to the SW application communicating with the Trusted Application,
		 * setResponseCode method should be called. 
		 * Note that calling this method more than once will reset the code previously set. 
		 * If not set, the default response code that will be returned to SW application is 0.
		 */
		setResponseCode(commandId);

		/*
		 * The return value of the invokeCommand method is not guaranteed to be
		 * delivered to the SW application, and therefore should not be used for
		 * this purpose. Trusted Application is expected to return APPLET_SUCCESS code 
		 * from this method and use the setResposeCode method instead.
		 */
		return APPLET_SUCCESS;
	}
	
	

	private byte[] SavePasseword(byte[] request) {   // COPIE TZ + MDP
	
		byte[] arrayPassword = new byte[FlashStorage.getFlashDataSize(1)];
		
		FlashStorage.writeFlashData(1, request, 0, request.length );   // fichier 0   applet de 0 à 50
		FlashStorage.readFlashData(1, arrayPassword, 0);  // copie du fichier au buffer  arrayPSSW
		
	//  FOR TEST   return arrayPassword;

		return new byte[] {0};
	}
	

	private byte[] Deposit(byte[] request) { // montant à deposer     // prenom Nom 1234 100     
		
		byte[] arrayAccount=new byte[FlashStorage.getFlashDataSize(0)];

		if(arrayAccount.length != 0) {
		
			FlashStorage.readFlashData(0, arrayAccount, 0);  // Si le flash storage n'est pas vide, alors lecture du flash storage

		}
			
		
		if(arrayAccount.length == 0)  // Si le flash storage est vide, alors écritue dans flash storage du request
		{
			FlashStorage.writeFlashData(0, arrayAccount, 0, arrayAccount.length);
			//FlashStorage.readFlashData(0, response, 2);
			
		}
		
		// int i = arrayAccount.length;
		
	//	String s = String.valueOf(i);  
	//  byte[] test = new byte[1];
	//	test = s.getBytes() ;
	
	
		
	  byte[] balance = new byte[3];  //   100 et 999 $  max 
	     	int count = 0;
	     	int i = 0 ;
	     	
	     	
	   	for (int j = 0; j < arrayAccount.length; j++)     // prenom Nom 1234 250     15   
		{
				   if (arrayAccount[j] == 31 ) // EPSACE
					   {
					     count++;
					     j++;
					   }			
				   if (count ==3)
				   {
						balance[i] = arrayAccount[j];   // 250 
						i++;
						
						
				   }
					
		}     
	   	
	  /* 		for (int i = 0; i < 6; i++) {
			amount[i] = request[i];
		}
			
			
		for(int i = 0, j = 22; i < 6 && j < 28; i++, j++) {
			balance[i] = arrayAccount[j]; /////////    BALNCE A LA FIN   PRENDRE LES 6 DERNIER BYTE
		}
		
		// A A AA AAA 100 
		 return arrayAccount ;	// FOR TEST
		 }
			 
	*/		
		
			
		int x = byteArrayToInt(request);
		int y = byteArrayToInt(balance);
		int res;
			
		res = x + y;
		
		balance = intToByteArray(res);  
		//   TO   SAVE ARRAY ACCOUNT : 
		/* count = 0;
     	 i = 0 ;
		 for (int j = 0; i < 4; j++)     // prenom Nom 1234 250     15   
		{
				   if (arrayAccount[j] == 31 ) // EPSACE
					   {
					     count++;
					     j++;
					   }			
				   if (count ==3)
				   {
						 arrayAccount[j]= balance[i] ;   // 250   reecrire dans arra account
						i++;
						
				   }
					
		} */
		return balance;
	}
	
	
	private byte[] intToByteArray(int a) {    // convert integer to byte array
	    byte[] ret = new byte[3];             // CHANGER 3 EN 6
	    ret[0] = (byte) (a & 0xFF);   
	    ret[1] = (byte) ((a >> 8) & 0xFF);   
	    ret[2] = (byte) ((a >> 16) & 0xFF);   
	/*    ret[3] = (byte) ((a >> 24) & 0xFF);  // AJOUTER LES 3 DERNIERES LIGNES UNE FOIS QUE CA MARCHERA
	    ret[4] = (byte) ((a >> 32) & 0xFF);
	    ret[5] = (byte) ((a >> 40) & 0xFF);*/
	    return ret;
	}

	private int byteArrayToInt(byte[] b) {    // convert byte array to integer 
	    int value = 0;                        // ARRANGER 3 EN 6 UNE FOIS QUE CA MARCHERA
	    for (int i = 0; i < 3; i++) {
	        int shift = (3 - 1 - i) * 8;
	        value += (b[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
	
	
	
	
	private byte[] Withdraw(byte[] request) {
		
		byte[] arrayAccount=new byte[FlashStorage.getFlashDataSize(0)];

		if(arrayAccount.length != 0) {
		
			FlashStorage.readFlashData(0, arrayAccount, 0);  // Si le flash storage n'est pas vide, alors lecture du flash storage

		}
			
		
		if(arrayAccount.length == 0)  // Si le flash storage est vide, alors écritue dans flash storage du request
		{
			FlashStorage.writeFlashData(0, arrayAccount, 0, arrayAccount.length);
			//FlashStorage.readFlashData(0, response, 2);
			
		}
		
	
		
	  byte[] balance = new byte[3];  //   100 et 999 $  max 
	     	int count = 0;
	     	int i = 0 ;
	     	
	     	
	   	for (int j = 0; j < arrayAccount.length; j++)     // prenom Nom 1234 250     15   
		{
				   if (arrayAccount[j] == 31 ) // EPSACE
					   {
					     count++;
					     j++;
					   }			
				   if (count ==3)
				   {
						balance[i] = arrayAccount[j];   // 250 
						i++;
						
						
				   }
					
		}     
		
			
		int x = byteArrayToInt(request);
		int y = byteArrayToInt(balance);
		int res;
			
		res = y-x ;
		
		balance = intToByteArray(res);  
		//   TO   SAVE ARRAY ACCOUNT : 
		/* count = 0;
     	 i = 0 ;
		 for (int j = 0; i < 4; j++)     // prenom Nom 1234 250     15   
		{
				   if (arrayAccount[j] == 31 ) // EPSACE
					   {
					     count++;
					     j++;
					   }			
				   if (count ==3)
				   {
						 arrayAccount[j]= balance[i] ;   // 250   reecrire dans arra account
						i++;
						
				   }
					
		} */
		return balance;
	}
	/*
	private byte[] Withdraw(byte[] request) {
		byte[] response = new byte[0];
		
		
		if(logged) {
			byte[] amount = new byte[6];
			byte[] balance = new byte[6];
			for (int i = 0; i < 6; i++) {  // copy request in amount buffer
				amount[i] = request[i];
			}
			       												 // Verifier j values in arrayAccount 
			for(int i = 0, j = 22; i < 6 && j < 28; i++, j++) {  // copy balance of arrayAccount in balance buffer
				balance[i] = arrayAccount[j];
			}
			
			int x = byteArrayToInt(amount);   // Convert amount buffer in integer
			int y = byteArrayToInt(balance);  // Convert balance buffer in integer
			int res;
			
			res = y - x;  // Calculate the new balance
			balance = intToByteArray(res);   // Convert the new balance in byte buffer
			
			FlashStorage.writeFlashData(0, balance, 22, balance.length);
			
			response[0] = 0;
		}
		
		else
			response[0] = 1;
		
		return response;
	} */
	
	private byte[] transfer(byte[] request) { // montant à deposer     // prenom Nom 1234 100     
		
		byte[] arrayAccount=new byte[FlashStorage.getFlashDataSize(0)];
		if(arrayAccount.length != 0) {
			FlashStorage.readFlashData(0, arrayAccount, 0);  // Si le flash storage n'est pas vide, alors lecture du flash storage
		}
		if(arrayAccount.length == 0)  // Si le flash storage est vide, alors écritue dans flash storage du request
		{
			FlashStorage.writeFlashData(0, arrayAccount, 0, arrayAccount.length);
		}
	  byte[] balance = new byte[3];  //   100 et 999 $  max 
	     	int count = 0;
	     	int i = 0 ;
	     	
	   	for (int j = 0; j < arrayAccount.length; j++)     // prenom Nom 1234 250     15   
			{		 if (arrayAccount[j] == 31 ) // EPSACE
					   {count++;
					     j++;}			
				   if (count ==3)
				   {balance[i] = arrayAccount[j];   // 250 
						i++; }
		}     
		
		int x = byteArrayToInt(request);
		int y = byteArrayToInt(balance);
		int res;
		res = y-x;
		balance = intToByteArray(res);  
		return balance;
	}

	/*

	private byte[] ResetPassword(byte[] request) {
		
		byte[] response = new byte[1];
		
		if(logged) {
			FlashStorage.writeFlashData(0, request, 39, request.length);
			FlashStorage.readFlashData(0, arrayAccount, 39);
			response[0] = 0;
		}
		
		else
			response[0] = 1;
		
		return response;
	}*/

	private byte[] Register (byte[] request) {           
		byte[] response = new byte[1];
		byte[] arrayAccount=new byte[FlashStorage.getFlashDataSize(0)];
		
		 //   request =     0-19   Noam Semhoun 9999123   31: 0944334444     40-50 : psswd
	
		/*       0-9 => FirstName    10 BYTE max
		 *       10 => " "    
		 *       11-20 => LastName   10 BYTE max
		 *       21 => " "    
		 *       22-27 => Balance    6 BYTE max
		 *       28 => " "
		 *       29-37 => TZ         9 BYTE max
		 *       38 => " "    
		 *       39-46 => pwd        8 BYTE max   (min 5)
		 *       //*/
			// arrayAccount = new byte[FlashStorage.getFlashDataSize(0)];  

			FlashStorage.writeFlashData(0, request, 0, request.length );   // fichier 0   applet de 0 à 50
			FlashStorage.readFlashData(0, arrayAccount, 0);  // copie du fichier au buffer  arrayAccount
			
			response[0] = 0;  // Ca a marché
			
			if (arrayAccount.length == 0)
				{
				  response[0] = 1;
				}
			
			// arrayCrypto = createKeys(arrayAccount[39]); */
		
			return response;
		
		/*if(arrayAccount.length != 0)   { //  for several users 
			FlashStorage.readFlashData(0, arrayAccount, 0);  //    
		     response[0] = 3 ;  // TEST
				return response;
		}*/
	// FOR TEST	System.out.println(arrayAccount);

		//System.out.println("Registration completed");
		
		
		/*for(; i < arrayAccount.length; i++) {     // IF YOU WANT SEVERAL USERS IN THE TABLES
		if(arrayAccount[i][0] == 0) {  // Founded a free place for registration
			arrayAccount[i][0] = request[1];  // FirstName
			arrayAccount[i][1] = request[1];  // Name
			arrayAccount[i][2] = request[1];  // TZ
			arrayAccount[i][3] = request[1];  // Balance
			arrayAccount[i][4] = request[1];  // Previous transaction
			
			arrayPassword[i][0] = request[1];  // TZ
			arrayPassword[i][1] = request[1];  // Password
				
			arrayCrypto[i][0] = request[1];    // TZ
			//  arrayCrypto[i][1] = CreateKeys(request4[1]);  // Cryptographic Password
			
			System.out.println("Registration completed");
			response[0] = 0;
			return response;
		}*/

	}
	
	
	private byte[] Login(byte[] request) {  // envoie TZ et pwd et compare avec bytes 30->47 de arrayAccount
		
		byte[] response = new byte[1];

		byte[] arrayPassword = new byte[FlashStorage.getFlashDataSize(1)];
		//FlashStorage.readFlashData(0, arrayPassword, 0); // Request contient TZ et pwd qui seront comparés dans le if
		
		// Voir après l'ajout de comparaison avec crypto pwd à faire dans register
		
		if(arrayPassword.length != 0)  // Lecture du flash storage
			FlashStorage.readFlashData(1, arrayPassword, 0);
		
		if(arrayPassword.length == request.length && ArrayUtils.compareByteArray(arrayPassword, 0, request, 0, arrayPassword.length))  
		{
			//logged = true;  // Le client est à présent connecté à son compte
			response[0] = 0;  // Login réussi
			
			// reponse 2 - 47 ARRAY ACCOUNT
		}
		
		
		else 
			response[0] = 1;  // Echec login
		
		return response;
		
		//return arrayPassword;
		
		}
	
		/*for(int i = 0; i < request.length; i++) 
		{
			if(request[i] != arrayPassword[i])  // Wrong TZ or pwd or both
				return new byte[] {1};
			
		}
		
		// Good TZ and pwd
		 logged = true;
		return new byte[] {0};
		
	}
		
		/* /if(  ArrayUtils.compareByteArray(arrayPassword, 0, request, 0, request.length) 	)   
		{
			// Good TZ and pwd
			logged = true;
			response[0] = 0;
	 	}
		
	    else                // Wrong TZ or pwd or both
			response[0] = 1; */
		
		

		//return response / request;  FOR TEST
		
	
		
		//int i = 0;
		//for(; i < arrayPassword.length; i++) {  // Search the TZ of a client to login
				/*if(arrayPassword[i][0] == 0)
					continue;
				
				else if(arrayPassword[i][0] != 0 && arrayPassword[i][0] != request0) { // The TZ is different
					continue;
				}*/
				
			/*	if(arrayPassword[i][0] == request0[1]) {  // Good TZ
					if(request1[1] == arrayPassword[i][1] && CreateKeys(request1) == GetKeys(arrayCrypto[i][1])) { // Good password
						System.out.println("Login successful! Welcome to your account!"); // Ouvrir dï¿½tails compte
						response[0] = 0;
						break;
					}
					
					else
						continue;
				}
				
				else   
					continue;
					
					
				
					
			}
			
			
			
		/*byte[] array=new byte[FlashStorage.getFlashDataSize(0)];
		
		if(arr.length != 0)
			FlashStorage.readFlashData(0, array, 0);
		
		if(arr.length == request.length && ArrayUtils.compareByteArray(array, 0, request, 0, arr.length))
		{
			is_logged = true;
			response[0] = 0;
		}
		else 
			response[0] = 1;
		*/
		
		
	

	private byte[] DisplayAccount(byte[] request) {

		byte[] response = new byte[49]; // return success " " arrayAccount or failed
		
		byte[] arrayAccount=new byte[FlashStorage.getFlashDataSize(0)];

		if(arrayAccount.length != 0) {
			response[0] = 0;
			response[1] = 0x1F ;
			FlashStorage.readFlashData(0, response, 2);  // Si le flash storage n'est pas vide, alors lecture du flash storage

		}
			
		
		if(arrayAccount.length == 0)  // Si le flash storage est vide, alors écritue dans flash storage du request
		{
			FlashStorage.writeFlashData(0, arrayAccount, 0, arrayAccount.length);
			response[0] = 0;  // Réussite d'écriture dans le flash storage
			response[1] = 0x1F; 
			FlashStorage.readFlashData(0, response, 2);
			
		}
			/*FlashStorage.writeFlashData(0, request, 0, request.length);
			
			FlashStorage.readFlashData(0, arrayAccount, 0);  //  FILE 0 = ARRAY ACCOUNT 
			FlashStorage.readFlashData(0, response, 1);*/
			
			/*for(int i = 0; i < response.length; i++ )
			{
				response[i] = arrayAccount[i];
		
			}*/
		
		
		
		return response;
	}  



	
	
	/**
	 * This method will be called by the VM when the session being handled by
	 * this Trusted Application instance is being closed 
	 * and this Trusted Application instance is about to be removed.
	 * This method cannot provide response data and therefore
	 * calling setResponse or setResponseCode methods from it will throw a NullPointerException.
	 * 
	 * @return APPLET_SUCCESS code (the status code is not used by the VM).
	 */
	public int onClose() {
		DebugPrint.printString("Goodbye, DAL!");
		return APPLET_SUCCESS;
	}
}
