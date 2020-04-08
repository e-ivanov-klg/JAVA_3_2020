package server;

import Command.AuthCommand;
import Command.UserData;

import java.sql.*;

public class DBConnection {
    private Connection conn;
    private Statement statmt;
    private ResultSet resSet;

    DBConnection () throws ClassNotFoundException, SQLException {
        this.conn = null;
        connect();
        this.statmt = conn.createStatement();
        //initDataBase();
    }

    private void connect () throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection("jdbc:sqlite:ChatBase.s3db");
        System.out.println("База Подключена!");
    }

    private void initDataBase () throws SQLException {
        this.statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' TEXT UNIQUE, 'user_name' TEXT, 'password' TEXT);");
        try {
            execSQL("INSERT INTO 'users' ('login', 'user_name', 'password') VALUES ('ivanov', 'Ivanov', '123');");
            execSQL("INSERT INTO 'users' ('login','user_name', 'password') VALUES ('petrov', 'Petrov', '123');");
            execSQL("INSERT INTO 'users' ('login','user_name', 'password') VALUES ('sidorov', 'Sidorov', '123');");
        } catch (SQLException exc) {
            System.out.println(exc.getErrorCode());
        }

    }

    public ResultSet open (String SQLtext) throws SQLException {
        System.out.println(SQLtext);
        return statmt.executeQuery(SQLtext);
    }

    public boolean execSQL (String SQLtext) throws SQLException {
        System.out.println(SQLtext);
        return statmt.execute(SQLtext);
    }

    public UserData getUser (String login, String password) throws SQLException{
        resSet = open("SELECT user_name FROM users WHERE login = '" + login + "' AND password = '" + password + "'");
        String userName = null;
        while (resSet.next()) {
            userName = resSet.getString("user_name");
        }
        return new UserData(login, userName);
    }


    public void closeDB () throws SQLException {
        resSet.close();
        statmt.close();
        conn.close();
    }

    public boolean changeUserData(UserData currentUser, UserData newUser) throws SQLException {
        String sql = null;
        sql = "UPDATE users SET user_name = '" + newUser.getFullName() + "' WHERE login = '" + currentUser.getLogin();
        sql = sql + "' AND password = '" + currentUser.getPassword() + "'";
        execSQL(sql);
        if (getUser(newUser.getLogin(), newUser.getPassword()).getFullName().equals(newUser.getFullName())) {
            return true;
        } else return false;
    }
}
