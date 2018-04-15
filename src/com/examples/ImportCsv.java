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
						String insertQuery = "Insert into Employee(id,name, contact_no, address) values (null,?,?,?)";
						PreparedStatement pstmt = connection.prepareStatement(insertQuery);
						String[] rowData = null;
						int i = 0;
						while((rowData = reader.readNext()) != null){
						for (String data : rowData)
						{
								pstmt.setString((i % 3) + 1, data);

								if (++i % 3 == 0)
										pstmt.addBatch();// add batch

								if (i % 30 == 0)// insert when the batch size is 10
										pstmt.executeBatch();
						}}
						System.out.println("Data Successfully Uploaded");
				}
				catch (Exception e)
				{
						e.printStackTrace();
				}

		}

		private static void readCsvUsingLoad()
		{
				try (Connection connection = DBConnection.getConnection())
				{

						String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:\\Employee.csv" + "' INTO TABLE Employee FIELDS TERMINATED BY ','"
										+ " LINES TERMINATED BY '\n' (id, Name, Contact, Address) ";
						System.out.println(loadQuery);
						Statement stmt = connection.createStatement();
						stmt.execute(loadQuery);
				}
				catch (Exception e)
				{
						e.printStackTrace();
				}
		}

}
