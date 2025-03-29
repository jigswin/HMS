package HMS;

import java.sql.*;
import java.util.Scanner;

public class Hms {
    private static final String url = "jdbc:mysql://127.0.0.1:3366/hms";
    private static final String username = "root";
    private static final String password = "";

    public static void main(String[] args)
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        // View Doctor
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        // Exit
                        System.out.println("Thanks for using Hospital Management System :) ");
                        System.out.println();
                        return;
                    default:
                        System.out.println("Enter Valid Choice !!!");
                        break;

                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Book Appointment
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner)
    {
        System.out.println("Enter Patient Id : ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id : ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId))
        {
            if(checkAvailability(doctorId, appointmentDate, connection))
            {
                String query = "INSERT INTO appointment (p_id, d_id, appointment_date) VALUES(?, ?, ?)";
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();

                    if(affectedRows > 0)
                    {
                        System.out.println("Appointment Booked.");
                    }
                    else
                    {
                        System.out.println("Appointment Failed To Book.");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Doctor Not Availabele :( ");
            }
        }
        else
        {
            System.out.println("Either Doctor Or Patient Dosen't Exist :(");
        }
    }

    //   Check Appointment Availability
    public static boolean checkAvailability(int doctorId, String appointmentDate, Connection connection)
    {
        String query = "SELECT count(*) FROM appointment WHERE d_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
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

        return  false;
    }


}
