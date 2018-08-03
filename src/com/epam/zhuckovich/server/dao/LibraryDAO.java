package com.epam.zhuckovich.server.dao;

import com.epam.zhuckovich.entity.Book;
import com.epam.zhuckovich.entity.Gender;
import com.epam.zhuckovich.entity.Genre;
import com.epam.zhuckovich.entity.Person;

import java.sql.*;
import java.util.ArrayList;

public class LibraryDAO {

    private static LibraryDAOConnection databaseConnection;

    private static final String checkClient = "SELECT Email FROM BooKingDatabase.Person";
    private static final String entranceClient = "SELECT * FROM BooKingDatabase.Person";
    private static final String insertClient = "INSERT INTO BooKingDatabase.Person  VALUES (DEFAULT,?,?,?,?,?,?,?)";
    private static final String selectClientData = "SELECT * FROM BooKingDatabase.Person WHERE Email=? AND Password=?";
    private static final String updateClientData = "UPDATE BooKingDatabase.Person SET Name=?, Surname=?, Email=?, Password=? WHERE Email=? AND Password=?";
    private static final String findUsers = "SELECT * FROM BooKingDatabase.Person WHERE isLibrarian=0";
    private static final String checkBook = "SELECT Title, Author FROM BooKingDatabase.Book";
    private static final String insertBook = "INSERT INTO BooKingDatabase.Book VALUES (DEFAULT,?,?,?,?,?,?,?)";
    private static final String selectBooks = "SELECT * FROM BooKingDatabase.Book";
    private static final String updateBookQuery = "UPDATE BooKingDatabase.Book SET Title=?, Author=?, PublishingHouse=?, Year=?, Pages=?, Quantity=? WHERE BookID=?";
    private static final String orderBook = "INSERT INTO BooKingDatabase.OrderedBooks VALUES (?,?,CURDATE(),CURDATE() + INTERVAL 14 DAY)";

    public LibraryDAO() throws SQLException {
        if(databaseConnection==null){
            databaseConnection = LibraryDAOConnection.getInstance();
        }
    }

    //functions for client registration

    public boolean checkClientForRegistration(String email) throws SQLException {
        PreparedStatement selectClient = LibraryDAOConnection.getConnection().prepareStatement(checkClient);
        ResultSet clients = selectClient.executeQuery();
        if(!clients.next()){
            return false;
        }
        clients.beforeFirst();
        while(clients.next()){
            if(clients.getString("Email").equals(email)){
                return false;
            }
        }
        return true;
    }


    public void insertClientIntoDatabase(Person newClient) throws SQLException {
        Date sqlDate = new Date(newClient.getRegistrationDate().getTime());
        PreparedStatement insertClientQuery = LibraryDAOConnection.getConnection().prepareStatement(insertClient);
        insertClientQuery.setString(1,newClient.getName());
        insertClientQuery.setString(2,newClient.getSurname());
        insertClientQuery.setString(3,newClient.getEmail());
        insertClientQuery.setString(4,newClient.getPassword());
        insertClientQuery.setDate(5, sqlDate);
        insertClientQuery.setString(6, String.valueOf(newClient.getGender()));
        insertClientQuery.setBoolean(7,newClient.isLibrarian());
        insertClientQuery.executeUpdate();
    }

    //functions for entering to the menu

    public int clientValidation(String email, String password) throws SQLException {
        PreparedStatement selectClient = LibraryDAOConnection.getConnection().prepareStatement(entranceClient);
        ResultSet clients = selectClient.executeQuery();
        if(!clients.next()){
            return 0;
        }
        clients.beforeFirst();
        while(clients.next()){
            if(clients.getString("Email").equals(email) && clients.getString("Password").equals(password) && clients.getBoolean("isLibrarian")){
                return 2;
            } else if (clients.getString("Email").equals(email) && clients.getString("Password").equals(password)){
                return 1;
            }
        }
        return 0;
    }


    public boolean registerClient(){
        return true;
    }

    public Person getClientData(String email, String password) throws SQLException {
        PreparedStatement selectClient = LibraryDAOConnection.getConnection().prepareStatement(selectClientData);
        selectClient.setString (1,email);
        selectClient.setString(2,password);
        ResultSet client = selectClient.executeQuery();
        client.next();
        return new Person(client.getString("Name"),client.getString("Surname"),client.getString("Email"),client.getString("Password"),
                client.getDate("RegistrationDate"), Gender.valueOf(client.getString("Gender")),client.getBoolean("isLibrarian"));
    }

    public boolean updateClientData(Person changeablePerson) {
        try {
            PreparedStatement updateClient = LibraryDAOConnection.getConnection().prepareStatement(updateClientData);
            updateClient.setString(1, changeablePerson.getName());
            updateClient.setString(2, changeablePerson.getSurname());
            updateClient.setString(3, changeablePerson.getEmail());
            updateClient.setString(4, changeablePerson.getPassword());
            updateClient.setString(5, changeablePerson.getOldEmail());
            updateClient.setString(6, changeablePerson.getOldPassword());
            updateClient.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    ////////////////////////////////

    public boolean checkBookForAdding(String title, String author) throws SQLException {
        PreparedStatement selectTitleAuthorBook = LibraryDAOConnection.getConnection().prepareStatement(checkBook);
        ResultSet booksTitleAuthor = selectTitleAuthorBook.executeQuery();
        if(!booksTitleAuthor.next()){
            return true;
        }
        booksTitleAuthor.previous();
        while(booksTitleAuthor.next()){
            if(booksTitleAuthor.getString("Title").equals(title) && booksTitleAuthor.getString("Author").equals(author)){
                return false;
            }
        }
        return true;
    }

    public void insertBookIntoDatabase(Book newBook) throws SQLException {
        PreparedStatement insertClientQuery = LibraryDAOConnection.getConnection().prepareStatement(insertBook);
        insertClientQuery.setString(1,newBook.getTitle());
        insertClientQuery.setString(2,newBook.getAuthor());
        insertClientQuery.setString(3,String.valueOf(newBook.getGenre()));
        insertClientQuery.setString(4,newBook.getPublishingHouse());
        insertClientQuery.setInt(5, newBook.getYear());
        insertClientQuery.setInt(6, newBook.getPages());
        insertClientQuery.setInt(7, newBook.getQuantity());
        insertClientQuery.executeUpdate();
    }

    public void updateBookData(Book changeableBook) throws SQLException {
        PreparedStatement updateBook = LibraryDAOConnection.getConnection().prepareStatement(updateBookQuery);
        updateBook.setString(1, changeableBook.getTitle());
        updateBook.setString(2, changeableBook.getAuthor());
        updateBook.setString(3, changeableBook.getPublishingHouse());
        updateBook.setInt(4, changeableBook.getYear());
        updateBook.setInt(5, changeableBook.getPages());
        updateBook.setInt(6, changeableBook.getQuantity());
        updateBook.setInt(7,changeableBook.getBookID());
        updateBook.executeUpdate();
    }


    //functions for booking order
    public ResultSet findBooksInDatabase(String searchParameter, String searchValue) throws SQLException {
        PreparedStatement findBooks = LibraryDAOConnection.getConnection().prepareStatement("SELECT * FROM BooKingDatabase.Book WHERE " + searchParameter + "=?");
        findBooks.setString(1,searchValue);
        return findBooks.executeQuery();
    }

    public ResultSet selectBooksFromDatabase() throws SQLException {
        PreparedStatement selectBooksQuery = LibraryDAOConnection.getConnection().prepareStatement(selectBooks);
        return selectBooksQuery.executeQuery();
    }

    public boolean orderBookInLibrary(int bookID, String personEmail) {
        PreparedStatement getPersonIDQuery = null;
        PreparedStatement orderBookQuery = null;
        PreparedStatement updateQuantityQuery = null;
        try {
            getPersonIDQuery = LibraryDAOConnection.getConnection().prepareStatement("SELECT PersonID FROM BooKingDatabase.Person WHERE Email=?");
            getPersonIDQuery.setString(1,personEmail);
            ResultSet personID = getPersonIDQuery.executeQuery();
            personID.next();
            int id = personID.getInt("PersonID");
            orderBookQuery = LibraryDAOConnection.getConnection().prepareStatement(orderBook);
            orderBookQuery.setInt(1,id);
            orderBookQuery.setInt(2,bookID);
            PreparedStatement updateQuantityStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT Quantity FROM BooKingDatabase.Book WHERE BookID=?");
            updateQuantityStatement.setInt(1,bookID);
            ResultSet quantityResultSet = updateQuantityStatement.executeQuery();
            quantityResultSet.next();
            int quantity = quantityResultSet.getInt("Quantity");
            quantity--;
            updateQuantityQuery = LibraryDAOConnection.getConnection().prepareStatement("UPDATE BooKingDatabase.Book SET Quantity=? WHERE BookID=?");
            updateQuantityQuery.setInt(1,quantity);
            updateQuantityQuery.setInt(2,bookID);
            updateQuantityQuery.executeUpdate();
            orderBookQuery.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    //function for viewing users
    public ResultSet findUsersInDatabase() throws SQLException {
        PreparedStatement findUsersStatement = LibraryDAOConnection.getConnection().prepareStatement(findUsers);
        return findUsersStatement.executeQuery();
    }

    public ResultSet getGraphicDataFromDatabase() throws SQLException {
        PreparedStatement getGraphicDataQuery = LibraryDAOConnection.getConnection().prepareStatement("SELECT Title, Quantity FROM BooKingDatabase.Book");
        return getGraphicDataQuery.executeQuery();
    }

    public ArrayList<Book> getMyLibraryArrayList(String personEmail) throws SQLException {
        PreparedStatement getClientIDStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT PersonID FROM BooKingDatabase.Person WHERE Email=?");
        getClientIDStatement.setString(1,personEmail);
        ResultSet clientIDResultSet = getClientIDStatement.executeQuery();
        clientIDResultSet.next();
        int id = clientIDResultSet.getInt("PersonID");
        PreparedStatement getDatesStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT BookID, OrderDate, ReturnDate FROM BooKingDatabase.OrderedBooks WHERE PersonID=?");
        getDatesStatement.setInt(1,id);
        ResultSet datesResultSet = getDatesStatement.executeQuery();
        ArrayList<Book> bookList = new ArrayList<>();
        while(datesResultSet.next()){
            int bookNumber = datesResultSet.getInt("BookID");
            PreparedStatement bookStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT * FROM BooKingDatabase.Book WHERE BookID=?");
            bookStatement.setInt(1,bookNumber);
            ResultSet bookSet = bookStatement.executeQuery();
            bookSet.next();
            Book newBook = new Book(bookSet.getString("Title"),bookSet.getString("Author"), Genre.valueOf(bookSet.getString("Genre")),bookSet.getString("PublishingHouse"),bookSet.getInt("Year"),bookSet.getInt("Pages"),
            bookSet.getInt("Quantity"));
            newBook.setBookID(bookSet.getInt("BookID"));
            newBook.setOrderDate(datesResultSet.getDate("OrderDate"));
            newBook.setReturnDate(datesResultSet.getDate("ReturnDate"));
            bookList.add(newBook);
        }
        return bookList;
    }

    public boolean returnBook(String email, Book returnedBook) {
        try {
            PreparedStatement getClientIDStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT PersonID FROM BooKingDatabase.Person WHERE Email=?");
            getClientIDStatement.setString(1, email);
            ResultSet clientIDResultSet = getClientIDStatement.executeQuery();
            clientIDResultSet.next();
            int clientID = clientIDResultSet.getInt("PersonID");
            PreparedStatement deleteOrderedBook = LibraryDAOConnection.getConnection().prepareStatement("DELETE FROM BooKingDatabase.OrderedBooks WHERE PersonID=? AND BookID=?");
            deleteOrderedBook.setInt(1, clientID);
            deleteOrderedBook.setInt(2, returnedBook.getBookID());
            deleteOrderedBook.executeUpdate();
            PreparedStatement plusCountQuery = LibraryDAOConnection.getConnection().prepareStatement("UPDATE BooKingDatabase.Book SET Quantity=Quantity+1 WHERE BookID=?");
            plusCountQuery.setInt(1, returnedBook.getBookID());
            plusCountQuery.executeUpdate();
            return true;
        } catch(SQLException e){
            return false;
        }
    }

    public boolean deleteAccount(String email) {
        try {
            PreparedStatement getClientIDStatement = LibraryDAOConnection.getConnection().prepareStatement("SELECT PersonID FROM BooKingDatabase.Person WHERE Email=?");
            getClientIDStatement.setString(1,email);
            ResultSet getClientIDSet = getClientIDStatement.executeQuery();
            getClientIDSet.next();
            int personID = getClientIDSet.getInt("PersonID");
            PreparedStatement deleteBooksStatement = LibraryDAOConnection.getConnection().prepareStatement("DELETE FROM BooKingDatabase.OrderedBooks WHERE PersonID=?");
            deleteBooksStatement.setInt(1, personID);
            deleteBooksStatement.executeUpdate();
            PreparedStatement deleteAccountStatement = LibraryDAOConnection.getConnection().prepareStatement("DELETE FROM BooKingDatabase.Person WHERE Email=?");
            deleteAccountStatement.setString(1, email);
            deleteAccountStatement.executeUpdate();
            return true;
        } catch(SQLException e){
            return false;
        }
    }


    public boolean restoreAccount(String email, String password) throws SQLException {
        PreparedStatement checkEmail = LibraryDAOConnection.getConnection().prepareStatement("SELECT Password FROM BooKingDatabase.Person WHERE Email=?");
        checkEmail.setString(1,email);
        ResultSet checkEmailResultSet = checkEmail.executeQuery();
        if(!checkEmailResultSet.next()){
            return false;
        } else{
            PreparedStatement updateEmail = LibraryDAOConnection.getConnection().prepareStatement("UPDATE BooKingDatabase.Person SET Password=? WHERE Email=?");
            updateEmail.setString(1,password);
            updateEmail.setString(2,email);
            updateEmail.executeUpdate();
            return true;
        }

    }

}
