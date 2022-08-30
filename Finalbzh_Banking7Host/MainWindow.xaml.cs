using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

using Intel.Dal;
using System.Security.Cryptography;
using System.Net;
using System.Net.Sockets;
using Finalbzh_Banking7Host;


namespace Finalbzh_Banking7Host
{
    /// <summary>
    /// Iinteraction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

         
        byte[] sendBuff;               // A message to send to the TA
        char SpaceChar = (char)0x1F;   //US used for separation between every intput data in the arrays
        int sold = 250;

        public MainWindow()
        {
            InitializeComponent();
        }


        private void Login_Click(object sender, RoutedEventArgs e)   // LOGIN CLIK 
        {

            byte[] ArrayPsswd = new byte[17];
            ArrayPsswd = UTF32Encoding.UTF8.GetBytes(UsernameEntrance.Text + SpaceChar + PassewordEntrance.Password);  // request


                if (TAService(ArrayPsswd, 3)[0] == 0)
                {

                 LoginGrid.Visibility = Visibility.Hidden;
                 BankInterface.Visibility = Visibility.Visible;

                // BALANCE ?? 
                byte[] sendBuff = new byte[1];
                sendBuff[0] = 1;

                 byte[] receiveBuff = new byte[50];
                 receiveBuff = TAService(sendBuff, 5);

                //  MessageBox.Show( UTF32Encoding.UTF8.GetString( receiveBuff) , "arrayaccount") ; // DISPLAY ACCOUNT

                string[] name = UTF32Encoding.UTF8.GetString(receiveBuff).Split(SpaceChar);  // 0 RIEN  1 FNAME 2 LNAME 3 TZ 4 BALANCE

                 // text = UsernameEntrance.Text);  // Can we send information by Window
                 WelcomUser.Content = "Welcome " + name[1];
                 soldTB.Content = "Your Sold : " + name[4]+" $";
                }

                else
                {
                    UsernameEntrance.Clear();
                    UsernameEntrance.Focus();
                    PassewordEntrance.Clear();
                    MessageBox.Show("This Username or password incorrect");
                }
        }



        private void Button_Click(object sender, RoutedEventArgs e)  // LOGIN  cwdId 3
        {
            MenuStack.Visibility = Visibility.Hidden;
            LoginGrid.Visibility = Visibility.Visible;

            // checker passd DS? JAVA APPLET 
            // et donner l'accès au compte avec bouton deconnexion retour menu stack
        }

        private void DisplayPasseword(object sender, RoutedEventArgs e)
        {
           PassewordEntrance.Visibility = System.Windows.Visibility.Collapsed;
           MyTextBox.Visibility = System.Windows.Visibility.Visible;
           MyTextBox.Text = PassewordEntrance.Password;
           MyTextBox.Focus();

        }

        private void NotDisplayPasseword(object sender, RoutedEventArgs e)
        {
            PassewordEntrance.Visibility = System.Windows.Visibility.Visible;
            MyTextBox.Visibility = System.Windows.Visibility.Collapsed;
            PassewordEntrance.Password = MyTextBox.Text;
            PassewordEntrance.Focus();
        }


        private void Button_Click_1(object sender, RoutedEventArgs e) // Menu to REGISTER
        {
            MenuStack.Visibility = Visibility.Hidden; // Open FORMS REGISTER 
            RegisterGrid.Visibility = Visibility.Visible;
        }

        private void Button_Click_2(object sender, RoutedEventArgs e)  // Log Out
        {
            BankInterface.Visibility = Visibility.Hidden;
            MenuStack.Visibility = Visibility.Visible; // Open FORMS REGISTER 
            UsernameEntrance.Clear();
            UsernameEntrance.Focus();
            PassewordEntrance.Clear();
            MyTextBox.Clear();
        }

        private void Button_Click_3(object sender, RoutedEventArgs e)
        {

        }

        private void BackMenu_Click(object sender, RoutedEventArgs e) // Back To Menu
        {
            LoginGrid.Visibility = Visibility.Hidden;
            RegisterGrid.Visibility = Visibility.Hidden;
            MenuStack.Visibility = Visibility.Visible; // Open FORMS REGISTER 
        }

        private void Button_Click_4(object sender, RoutedEventArgs e)  // EXIT APPLICATION
        {
            Close();
        }

        private void Button_Click_5(object sender, RoutedEventArgs e)   // REGISTER CLICK
        {
            if( TBFNAME.Text.Length > 10 || TBLNAME.Text.Length > 10 || TBTZ.Text.Length > 9 || TBMDP.Password.Length > 8 || TBMDP.Password.Length < 5)
            {
                MessageBox.Show("Error please check your informations size", "ERROR SIZE");
                TBMDP.Clear();
                TBMDP2.Clear(); 
                return;
            }
            

            if(TBMDP.Password != TBMDP2.Password)
            {
                MessageBox.Show("Error the two passewords are different", "ERROR SIZE");
                TBMDP.Clear();
                TBMDP2.Clear();
                return;
            }


            // creation of buffer :  
            sendBuff = UTF32Encoding.UTF8.GetBytes(TBFNAME.Text + SpaceChar +
                TBLNAME.Text +  SpaceChar + TBTZ.Text + SpaceChar + 250 ); 

            if ((TAService(sendBuff, 1)[0]) == 0)   // SEND THE DATA TO APPLET
            {

                 MessageBox.Show("Registration success", "Ok");            

                 sendBuff = UTF32Encoding.UTF8.GetBytes(TBTZ.Text + SpaceChar + TBMDP.Password);
                // SAVE TZ ET MDP :  array passwd

                if (TAService(sendBuff, 2)[0] == 0)  
                {
                      MessageBox.Show("Passeword Saved ", "Ok"); // SAVE MDP in arrayPassword 

                      LoginGrid.Visibility = Visibility.Hidden;
                      RegisterGrid.Visibility = Visibility.Hidden;
                      MenuStack.Visibility = Visibility.Visible; // Open FORMS REGISTER
                      MessageBox.Show("You have to deposit 250$ minimum for create your account", "WELCOME", MessageBoxButton.OK, MessageBoxImage.Information); // METTRE BOUTON OK
                }
            }

            else
            {
                MessageBox.Show("Registration failed", "ERROR");
            }
        }


        public static byte[] TAService(byte[] sendBuff, int cmdId)   // comunication with our JAVA Applet
        {
#if AMULET
            // When compiled for Amulet the Jhi.DisableDllValidation flag is set to true 
            // in order to load the JHI.dll without DLL verification.
            // This is done because the JHI.dll is not in the regular JHI installation folder, 
            // and therefore will not be found by the JhiSharp.dll.
            // After disabling the .dll validation, the JHI.dll will be loaded using the Windows search path
            // and not by the JhiSharp.dll (see http://msdn.microsoft.com/en-us/library/7d83bc18(v=vs.100).aspx for 
            // details on the search path that is used by Windows to locate a DLL) 
            // In this case the JHI.dll will be loaded from the $(OutDir) folder (bin\Amulet by default),
            // which is the directory where the executable module for the current process is located.
            // The JHI.dll was placed in the bin\Amulet folder during project build.
            Jhi.DisableDllValidation = true;
#endif

            Jhi jhi = Jhi.Instance;
            JhiSession session;

            // This is the UUID of this Trusted Application (TA).
            //The UUID is the same value as the applet.id field in the Intel(R) DAL Trusted Application manifest.
            string appletID = "880ac3de-131a-4edf-8be3-423baa9a0edf";
            // This is the path to the Intel Intel(R) DAL Trusted Application .dalp file that was created by the Intel(R) DAL Eclipse plug-in.
            string appletPath = "C:/Users/33768/eclipse-workspace\\Finalbzh_Banking7\\bin\\Finalbzh_Banking7.dalp";

            // Install the Trusted Application
            jhi.Install(appletID, appletPath);

            // Start a session with the Trusted Application
            byte[] initBuffer = new byte[] { }; // Data to send to the applet onInit function
            jhi.CreateSession(appletID, JHI_SESSION_FLAGS.None, initBuffer, out session);

            // Send and Receive data to/from the Trusted Application
            byte[] recvBuff = new byte[1000]; // A buffer to hold the output data from the TA

            int responseCode; // The return value that the TA provides using the IntelApplet.setResponseCode method

            jhi.SendAndRecv2(session, cmdId, sendBuff, ref recvBuff, out responseCode);

            // Close the session
            jhi.CloseSession(session);

            //Uninstall the Trusted Application
            jhi.Uninstall(appletID);

            return recvBuff;
        }

        private void Button_Click_6(object sender, RoutedEventArgs e)   // DEPOSIT 
        {
            WithDrawStack.Visibility = Visibility.Hidden;
            TransferStack.Visibility = Visibility.Hidden;
            DepositStack.Visibility = Visibility.Visible;

        }

        private void Button_Click_7(object sender, RoutedEventArgs e)  // DEPOSIT VALIDATION CLICK 
        {
            byte[] amount = new byte[3] ;

            int montant = Int32.Parse(AmountTB.Text);


            amount = TBtoByte(AmountTB.Text);
            // Amount recovered  
            // amount = Encoding.ASCII.GetBytes(AmountTB.Text); 

            amount = TAService(amount, 6);

            //   soldTB.Content = "Your Sold : " + Convert.ToBase64String(amount) + " $";// int montant = BitConverter.ToInt32(amount, 0); // soldTB.Content = "Your Sold : " + BitConverter.ToString(amount) + " $";  // 30 35 33  HEX

            string STRINGsold = Reverse(Encoding.ASCII.GetString(amount));

            sold = sold + montant;
            // FOR GRAPHIC ONLY (because the parsing byte are difficult and long) but the account are modified in the APPLET 

            try
            {
                if (sold == Int16.Parse(STRINGsold))
                {
                    soldTB.Content = "Your Sold : " + STRINGsold + " $";

                }

                else soldTB.Content = "Your Sold : " + sold + " $";
            }

            catch
            {
                soldTB.Content = "Your Sold : " + sold + " $"; 
            }

            DepositStack.Visibility = Visibility.Hidden;
        }

        private void Button_Click_8(object sender, RoutedEventArgs e)  // WITHDRAW CLIK STACK
        {
            DepositStack.Visibility = Visibility.Hidden;
            TransferStack.Visibility = Visibility.Hidden;
            WithDrawStack.Visibility = Visibility.Visible;
        }



        private void Button_Click_9(object sender, RoutedEventArgs e)   // WITHDRAWW  OK CLICK
        {
            byte[] amount = new byte[3];
            int montant = Int32.Parse(AmountWTB.Text);

            amount = TBtoByte(AmountWTB.Text);

            // Amount recovered  
            // amount = Encoding.ASCII.GetBytes(AmountTB.Text);

            amount = TAService(amount, 7);

            string STRINGsold = Reverse(Encoding.ASCII.GetString(amount));

            sold = sold - montant;
            // FOR GRAPHIC ONLY (because the parsing byte are difficult and long) but the account are modified in the APPLET 

            try
            {
                if (sold == Int16.Parse(STRINGsold))
                {
                    soldTB.Content = "Your Sold : " + STRINGsold + " $";  

                }         
                
                else  soldTB.Content = "Your Sold : " + sold + " $";

            }
            catch
            {
                soldTB.Content = "Your Sold : " + sold + " $";

            }



            WithDrawStack.Visibility = Visibility.Hidden;
        }

        private void Button_Click_10(object sender, RoutedEventArgs e)
        {
         
            DepositStack.Visibility = Visibility.Hidden;
            WithDrawStack.Visibility = Visibility.Hidden;  
            TransferStack.Visibility = Visibility.Visible;


        }

        private void Button_Click_11(object sender, RoutedEventArgs e)  // TRANSFER CLIK OK 
        {
            byte[] amount = new byte[3] ;

            int montant = Int32.Parse(AmountTTB.Text);

            amount = TBtoByte(AmountTTB.Text);

            // Amount recovered  
            // amount = Encoding.ASCII.GetBytes(AmountTB.Text);

            amount = TAService(amount, 8);

            string STRINGsold = Reverse(Encoding.ASCII.GetString(amount));

            sold = sold - montant;
            // FOR GRAPHIC ONLY (because the parsing byte are difficult and long) but the account are modified in the APPLET 

            try
            {
                if (sold == Int16.Parse(STRINGsold))
                {
                    soldTB.Content = "Your Sold : " + STRINGsold + " $";

                }

                else soldTB.Content = "Your Sold : " + sold + " $";

            }
            catch
            {
            }

            TransferStack.Visibility = Visibility.Hidden;
        }

        static string Reverse(string text)
        {
            char[] charArray = text.ToCharArray();
            string reverse = String.Empty;
            for (int i = charArray.Length - 1; i >= 0; i--)
            {
                reverse += charArray[i];
            }
            return reverse;
        }

        static byte[] TBtoByte(string text)
        {
            char[] charArray = text.ToCharArray();
            byte[] numByte = new byte[3];
            // for (int i = charArray.Length - 1; i >= 0; i--)
            for (int i = 0; i < 3; i++)
            {
                numByte[i] = Convert.ToByte(charArray[i].ToString());
            }
            return numByte;
        }
    }
}

