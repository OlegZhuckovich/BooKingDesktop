package com.epam.zhuckovich.server;

import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Gender;
import com.epam.zhuckovich.entity.Genre;
import com.epam.zhuckovich.entity.Person;
import com.epam.zhuckovich.server.dao.LibraryDAO;
import com.epam.zhuckovich.server.dao.LibraryDAOConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ServerThread extends Thread{

    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);

    private ObjectInputStream serverReader;
    private ObjectOutputStream serverWriter;
    private Socket client;
    private LibraryDAO libraryDAO;

    public ServerThread(Socket client) throws IOException, SQLException {
        this.client = client;
        serverWriter = new ObjectOutputStream(this.client.getOutputStream());
        serverReader = new ObjectInputStream(this.client.getInputStream());
        libraryDAO = new LibraryDAO();
    }

    @Override
    public void run() {
        try {
            while(true){
                int choose = serverReader.readInt();
                  switch(choose){
                      case 1:enterInProgram();break;
                      case 2:registrationClient();break;
                      case 3:findBooks();break;
                      case 4:orderBook();break;
                      case 5:changeClientData();break;
                      case 6:buildChart();break;
                      case 7:clientLibrary();break;
                      case 8:viewClients();break;
                      case 9:addBook();break;
                      case 10:controlViewLibrary();break;
                      case 11:updateLibrary();break;
                      case 12:adminBuildChart();break;
                      case 13:addLibrarian();break;
                      case 14:returnBook();break;
                      case 15:deleteAccount();break;
                      case 16:restoreAccess();break;
                  }
                  if(choose==-1){
                      break;
                  }
            }
            LOGGER.log(Level.INFO,"Client was disconnected from the server");
            Server.clientCounter-=1;
            serverWriter.close();
            serverReader.close();
        } catch (IOException e) {
            LOGGER.log(Level.ERROR,"Server IOException was happened",e);
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            LOGGER.log(Level.ERROR,"Server ClassNotFoundException was happened",e);
            e.printStackTrace();
        } catch (SQLException e){
            LOGGER.log(Level.ERROR,"Server SQLException was happened",e);
            e.printStackTrace();
        }
    }

    void enterInProgram() throws IOException, ClassNotFoundException, SQLException {
        String email = (String) serverReader.readObject();
        String password = (String) serverReader.readObject();

        if(libraryDAO.clientValidation(email,password)==0){
            serverWriter.writeInt(0);
            serverWriter.flush();
        } else if(libraryDAO.clientValidation(email,password)==1){
            serverWriter.writeInt(1);
            serverWriter.flush();
        } else {
            serverWriter.writeInt(2);
            serverWriter.flush();
        }
    }

    void registrationClient() throws IOException, ClassNotFoundException, SQLException {

        String email = (String) serverReader.readObject();
        Person newPerson = (Person) serverReader.readObject();

        if(libraryDAO.checkClientForRegistration(email)){
            libraryDAO.insertClientIntoDatabase(newPerson);
            serverWriter.writeBoolean(true);
            serverWriter.flush();
        } else {
            serverWriter.writeBoolean(false);
            serverWriter.flush();
        }
    }

    void findBooks() throws IOException, ClassNotFoundException, SQLException {
        String searchParameter = (String) serverReader.readObject();
        String searchValue = (String) serverReader.readObject();

        ResultSet findBooksResultSet = libraryDAO.findBooksInDatabase(searchParameter,searchValue);
        ArrayList<Book> findBooksList = new ArrayList<>();
        if(!findBooksResultSet.next()){
            serverWriter.writeObject(findBooksList);
        } else{
            findBooksResultSet.previous();
            while(findBooksResultSet.next()){
                if(findBooksResultSet.getInt("Quantity")!=0) {
                    Book newBook = new Book(findBooksResultSet.getString("Title"), findBooksResultSet.getString("Author"), Genre.valueOf(findBooksResultSet.getString("Genre")),
                            findBooksResultSet.getString("PublishingHouse"), findBooksResultSet.getInt("Year"), findBooksResultSet.getInt("Pages"), findBooksResultSet.getInt("Quantity"));
                    newBook.setBookID(findBooksResultSet.getInt("BookID"));
                    findBooksList.add(newBook);
                }
            }
            serverWriter.writeObject(findBooksList);
        }
    }

    void orderBook() throws IOException, ClassNotFoundException {
        Book orderedBook = (Book) serverReader.readObject();
        String personEmail = (String) serverReader.readObject();
        if(libraryDAO.orderBookInLibrary(orderedBook.getBookID(), personEmail)){
            serverWriter.writeBoolean(true);
        } else {
            serverWriter.writeBoolean(false);
        }
        serverWriter.flush();
    }

    void changeClientData() {
        try {
            String email = (String) serverReader.readObject();
            String password = (String) serverReader.readObject();
            Person changeablePerson = libraryDAO.getClientData(email, password);
            serverWriter.writeObject(changeablePerson);
            serverWriter.flush();
            changeablePerson = (Person) serverReader.readObject();
            if (libraryDAO.updateClientData(changeablePerson)) {
                serverWriter.writeBoolean(true);
            } else {
                serverWriter.writeBoolean(false);
            }
            serverWriter.flush();
        } catch(SQLException | ClassNotFoundException | IOException e){
            LOGGER.log(Level.ERROR,"Operation with database didn't end successful");
        }
    }

    void buildChart() throws SQLException, IOException {
        ResultSet graphicData = libraryDAO.getGraphicDataFromDatabase();
        ArrayList<String> titleBookList = new ArrayList<>();
        ArrayList<Number> quantityBookList = new ArrayList<>();
        if (!graphicData.next()) {
            serverWriter.writeObject(titleBookList);
            serverWriter.flush();
            serverWriter.writeObject(quantityBookList);
            serverWriter.flush();
        } else {
            graphicData.previous();
            while (graphicData.next()) {
                titleBookList.add(graphicData.getString("Title"));
                quantityBookList.add(graphicData.getInt("Quantity"));
            }
            serverWriter.writeObject(titleBookList);
            serverWriter.flush();
            serverWriter.writeObject(quantityBookList);
            serverWriter.flush();
        }
    }

    void clientLibrary() throws IOException, ClassNotFoundException, SQLException {
        String clientEmail = (String) serverReader.readObject();
        ArrayList<Book> bookArrayList = libraryDAO.getMyLibraryArrayList(clientEmail);
        serverWriter.writeObject(bookArrayList);
        serverWriter.flush();
    }

    void viewClients() throws SQLException, IOException {
        ResultSet foundedUsers = libraryDAO.findUsersInDatabase();
        ArrayList<Person> foundedUsersList = new ArrayList<>();
        if(!foundedUsers.next()) {
            serverWriter.writeObject(foundedUsersList);
            serverWriter.flush();
        } else {
            foundedUsers.previous();
            while(foundedUsers.next()){
                int counter = 0;
                int personID = foundedUsers.getInt("PersonID");
                PreparedStatement quantityBooks = LibraryDAOConnection.getConnection().prepareStatement("SELECT BookID FROM BooKingDatabase.OrderedBooks WHERE PersonID=?");
                quantityBooks.setInt(1,personID);
                ResultSet quantityBookSet = quantityBooks.executeQuery();
                while(quantityBookSet.next()){
                    counter++;
                }
                foundedUsersList.add(new Person(foundedUsers.getInt("PersonID"),foundedUsers.getString("Name"),foundedUsers.getString("Surname"),
                        foundedUsers.getString("Email"),foundedUsers.getDate("RegistrationDate"),
                        Gender.valueOf(foundedUsers.getString("Gender")),counter));
            }
            serverWriter.writeObject(foundedUsersList);
            serverWriter.flush();
        }
    }

    void addBook() throws IOException, ClassNotFoundException, SQLException {
        Book newBook = (Book) serverReader.readObject();

        if(libraryDAO.checkBookForAdding(newBook.getTitle(),newBook.getAuthor())){
            libraryDAO.insertBookIntoDatabase(newBook);
            serverWriter.writeBoolean(true);
            serverWriter.flush();
        } else {
            serverWriter.writeBoolean(false);
            serverWriter.flush();
        }
    }

    void controlViewLibrary() throws SQLException, IOException {
        ArrayList<Book> bookList = new ArrayList<>();
        ResultSet foundedBooks = libraryDAO.selectBooksFromDatabase();
            if(!foundedBooks.next()) {
                serverWriter.writeObject(bookList);
                serverWriter.flush();
            } else {
                foundedBooks.previous();
                while(foundedBooks.next()){
                    Book newBook = new Book(foundedBooks.getString("Title"), foundedBooks.getString("Author"), Genre.valueOf(foundedBooks.getString("Genre")),
                            foundedBooks.getString("PublishingHouse"),foundedBooks.getInt("Year"),foundedBooks.getInt("Pages"), foundedBooks.getInt("Quantity"));
                    newBook.setBookID(foundedBooks.getInt("BookID"));
                    bookList.add(newBook);
                }
                serverWriter.writeObject(bookList);
                serverWriter.flush();
            }
    }

    void updateLibrary() throws IOException, ClassNotFoundException {
        int errorCounter = 0;
        try {
            ArrayList<Book> bookList = (ArrayList<Book>) serverReader.readObject();
            for (Book currentBook : bookList) {
                if (currentBook.getYear() < 0 || currentBook.getPages() < 0 || currentBook.getQuantity() < 0) {
                    errorCounter++;
                } else {
                    libraryDAO.updateBookData(currentBook);
                }
            }
        } catch(SQLException e){
            errorCounter=-1;
            e.printStackTrace();
        }
        serverWriter.writeInt(errorCounter);
        serverWriter.flush();
    }

    void adminBuildChart() throws SQLException, IOException {
        HashMap<String,Integer> genderHashMap = new HashMap<>();
        HashMap<String,Integer> genreHashMap = new HashMap<>();
        PreparedStatement womanStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT COUNT(Gender) AS Woman FROM Person WHERE Gender='WOMAN'");
        ResultSet womanSet = womanStatement.executeQuery();
        womanSet.next();
        int woman = womanSet.getInt("Woman");
        PreparedStatement manStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT COUNT(Gender) AS Man FROM Person WHERE Gender='MAN'");
        ResultSet manSet = manStatement.executeQuery();
        manSet.next();
        int man = manSet.getInt("Man");
        genderHashMap.put("Woman",woman);
        genderHashMap.put("Man",man);
        PreparedStatement genreStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT DISTINCT Genre FROM Book");
        ResultSet genreResultSet = genreStatement.executeQuery();
        while(genreResultSet.next()){
            PreparedStatement newGenreStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT COUNT(Genre) AS Genre FROM Book WHERE Genre=?");
            newGenreStatement.setString(1,genreResultSet.getString("Genre"));
            ResultSet genreQuantitySet =  newGenreStatement.executeQuery();
            genreQuantitySet.next();
            int quantity = genreQuantitySet.getInt("Genre");
            genreHashMap.put(genreResultSet.getString("Genre"),quantity);
        }
        serverWriter.writeObject(genderHashMap);
        serverWriter.flush();
        serverWriter.writeObject(genreHashMap);
        serverWriter.flush();
    }

    void addLibrarian() throws IOException, ClassNotFoundException, SQLException {
        Person newLibrarian = (Person) serverReader.readObject();
        if(libraryDAO.checkClientForRegistration(newLibrarian.getEmail())){
            libraryDAO.insertClientIntoDatabase(newLibrarian);
            serverWriter.writeBoolean(true);
            serverWriter.flush();
        } else {
            serverWriter.writeBoolean(false);
            serverWriter.flush();
        }
    }

    void returnBook() throws IOException, ClassNotFoundException, SQLException {
        String email = (String) serverReader.readObject();
        Book returnedBook = (Book) serverReader.readObject();
        boolean successOperation = libraryDAO.returnBook(email,returnedBook);
        serverWriter.writeBoolean(successOperation);
        serverWriter.flush();
    }

    void deleteAccount() throws IOException, ClassNotFoundException {
        String email = (String) serverReader.readObject();
        boolean successOperation = libraryDAO.deleteAccount(email);
        serverWriter.writeBoolean(successOperation);
        serverWriter.flush();
    }

    void restoreAccess() throws IOException, ClassNotFoundException, SQLException {
        String email = (String) serverReader.readObject();
        int newPassword = serverReader.readInt();
        String password = String.valueOf(newPassword);
        boolean successOperation = libraryDAO.restoreAccount(email,password);
        serverWriter.writeBoolean(successOperation);
        serverWriter.flush();
    }
}
