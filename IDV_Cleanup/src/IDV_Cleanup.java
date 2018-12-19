import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.logging.Level;


/**
 * Created by sparamasivam on 1/25/2017.
 */

public class IDV_Cleanup {
  //  private static final Logger LOGGER= Logger.getLogger(IDV_Cleanup.class.getClass());
    public static ArrayList<String> TableNames=new ArrayList<String>();

    public static ArrayList<String> readfromfile(String FileName){
        String line;
        System.out.println(FileName);
        try
        {
            InputStream is=new FileInputStream(FileName);
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            while((line=br.readLine())!=null){
                TableNames.add(line);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return TableNames;
    }

    public static String buildQuery(String db,String schema,String table,String username,String password){
        String query=null;
        query="ssacleanup -d "+ db +" -s " +schema +" -t "+ table+ " -u "+ username+"/"+password;
        return query;
    }

    public static void  Usage(){
        String Message;
        Message=" Ok! Here is the usage !!\n We will need five arguments in all: \n 1) Database Name\n2)Schema Name\n3)IDV username\n 4)IDV password\n 5)Full file name including path of a file that has all tables to be deleted listed one after another\n Thats all Folks!!!";
        System.out.println(Message);

    }

    public static void main(String[] args) {
        if (args.length !=5) {
            System.out.println("Not Enough Arguments Passed ! Please check and rerun again");
            Usage();
        } else {
        String db = String.valueOf(args[0]);
        String schema = String.valueOf(args[1]);
//        String table = String.valueOf(args[2]);
        String username = String.valueOf(args[2]);
        String password = String.valueOf(args[3]);
        String FileName = String.valueOf(args[4]);
        String cleanup = null;
        //String cleanup="ssacleanup -d "+ db +" -s " +schema +" -t "+ table+ " -u "+ username+"/"+password;



            readfromfile(FileName);
            int i = TableNames.size();
            System.out.println(i);
            for (int j = 1; j <= i; j++) {
                cleanup = buildQuery(db, schema, TableNames.get(j - 1), username, password);
                System.out.println(cleanup);


                //System.out.println(cleanup);
                Boolean silent = Boolean.FALSE;
                int exitval = 0;


                try {
                    Process process = Runtime.getRuntime().exec(cleanup);
                    if (silent) {
                        // LOGGER.log(Level.INFO, "Entering Silent mode");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                        reader.close();
                    } else {
                        System.out.println(">>> In Silent Mode <<<");
                    }
                    exitval = process.waitFor();
                    if (exitval != 1) {
                        System.out.println(">Abnormal Exit<\t" + exitval);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
