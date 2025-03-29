package HMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection connection;
    //  constructer
    public Doctor(Connection connection)
    {
        this.connection = connection;
    }


    // view doctor list
    public void viewDoctors()
    {
        String query = "SELECT * FROM doctor";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors : ");
            System.out.println("+------------+--------------------+-----------------+");
            System.out.println("| Doctor Id  | Name               | Department      |");
            System.out.println("+------------+--------------------+-----------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dept = resultSet.getString("dept");
                System.out.printf("| %-11s | %-16s | %-15s | \n", id, name, dept);
                System.out.println("+------------+--------------------+-----------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // check doctor  exist or not
    public boolean getDoctorById(int id)
    {
        String query = "SELECT * FROM doctor WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
