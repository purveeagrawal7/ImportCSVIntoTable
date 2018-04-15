package com.examples;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.opencsv.CSVReader;

public class ImportCsv
{
		public static void main(String[] args)
		{
				readCsv();
				readCsvUsingLoad();
		}

		private static void readCsv()
		{

				try (CSVReader reader = new CSVReader(new FileReader("Employee.csv"), ','); Connection connection = DBConnection.getConnection();)
				{
						String insertQuery = "Insert into Employee(id,name, contact_no, address) values (?,?,?,?)";
						PreparedStatement pstatement = connection.prepareStatement(insertQuery);
						String [] nextValue; 
            				int i = 0;											while ((nextValue= reader.readNext()) != null) {
                        i++;
				pstatement.setInt(1, nextValue[0]);
                        pstatement.setString(2, nextValue[1]);                   	pstatement.setDouble(3,Double.parseDouble(nextValue[2]));
pstatement.setString(4, nextValue[3]);
                        // Add the record to batch
                        pstatement.addBatch();
                }                                   
                int[] totalRecords = new int[5];
                try {
                        totalRecords = pstatement .executeBatch();
                } catch(BatchUpdateException e) {
                        //you should handle exception for failed records here
                        totalRecords = e.getUpdateCounts();
                }
                System.out.println ("Total records inserted in bulk from CSV file " + totalRecords.length);                
                /* Close prepared statement */
                pstatement .close();
                /* COMMIT transaction */
                conn.commit();
                /* Close connection */
                conn.close();
        }
}