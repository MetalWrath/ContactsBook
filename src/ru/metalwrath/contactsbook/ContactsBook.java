package ru.metalwrath.contactsbook;

import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;



public class ContactsBook {


    protected void mainMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int answer = 0;
        System.out.println("Добро пожаловать в записную книжку!");
        System.out.println("Выберите нужный вам пункт меню: ");
        System.out.println("1. Создание контакта. ");
        System.out.println("2. Поиск контакта. ");
        System.out.println("3. Изменение контакта. ");
        System.out.println("4. Удаление контакта. ");
        System.out.println("5. Просмотр контактной книги. ");
        System.out.println("6. Выход. ");


        try {
            answer = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.out.println("Вы ввели неверное значение! Возможно у вас кривые пальцы и вы не туда нажали. Только числа");
            mainMenu();
        }
        switch (answer) {
            case 1 -> createContact();
            case 2 -> searchContact();
            case 3 -> changeContact();
            case 4 -> deleteContact();
            case 5 -> showContactBook();
            case 6 -> System.exit(0);
            default -> System.out.println("Такого пункта нет в меню!");
        }

    }

    protected void createContact() {

        String nameCont = "", numberCont = "", addressCont = "", companyCont = "";

        System.out.println("Введите имя: ");
        while (true) {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                nameCont = br.readLine();
            } catch (IOException e) {
                System.out.println("Вы ввели что-то не то. И все пошло под откос! Звоните маме!");
            }
            if (nameCont.isEmpty()) {
                System.out.println("Поле не может быть пустым.");
            } else break;
        }
        System.out.println("Введите номер телефона: ");
        while (true) {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                numberCont = br.readLine();
            } catch (IOException e) {
                System.out.println("Вы ввели что-то не то. И все пошло под откос! Звоните маме!");
            }
            if (numberCont.isEmpty()) {
                System.out.println("Поле не может быть пустым.");
            } else if ((numberCont.length()>15) || (numberCont.length() < 11)){
                System.out.println("Неверный формат телефона.");
            }else if (!checkNumber(numberCont)){
                System.out.println("Неверный формат номера. Он должен состоять из 11 цифр или 11 цифр и \"-\"");
            }else{
                numberCont = fixNumber(numberCont);
                break;
            }

        }


        System.out.println("(Можно оставить пустым) Введите адрес: ");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            addressCont = br.readLine();
        } catch (IOException e) {
            System.out.println("Вы ввели что-то не то. И все пошло под откос! Звоните маме!");
        }

        System.out.println("(Можно оставить пустым) Введите название компании: ");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            companyCont = br.readLine();
        } catch (IOException e) {
            System.out.println("Вы ввели что-то не то. И все пошло под откос! Звоните маме!");
        }




        createConnect("INSERT INTO ContactsBook(Name, Number, Address, Company) VALUES('" + nameCont + "', '" + numberCont +
                "', '" + addressCont + "', '" + companyCont + "');");


        System.out.println("Добавление контакта " + nameCont + " прошло успешно. Возврат в главное меню... \n\n");
        mainMenu();


    }
    protected boolean checkNumber(String tempNumber){
        tempNumber = tempNumber.replace("-", "");
        try{
            Long.parseLong(tempNumber);

            return true;
        }catch (Exception e){
            return false;
        }
    }
    protected String fixNumber(String tempNumber){
        tempNumber = tempNumber.replace("-", "");
        return tempNumber.charAt(0) + "-" + tempNumber.charAt(1) + tempNumber.charAt(2) +
                tempNumber.charAt(3) + "-" + tempNumber.charAt(4) + tempNumber.charAt(5) +
                tempNumber.charAt(6) + "-" + tempNumber.charAt(7) + tempNumber.charAt(8) +
                "-" + tempNumber.charAt(9) + tempNumber.charAt(10);

    }
    protected void searchContact() {
        int answer = 0;

        System.out.println("По какому полю будем искать?");
        System.out.println("1. По имени.");
        System.out.println("2. По городу.");
        System.out.println("3. По компании.");
        System.out.println("4. Венуться в главное меню.");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            answer = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.out.println("Вы ввели неверное значение! Возможно у вас кривые пальцы и вы не туда нажали. Только числа");
            searchContact();
        }
        switch (answer) {


            case 1 -> searchContactByName();
            case 2 -> searchContactByAddress();
            case 3 -> searchContactByCompany();
            case 4 -> mainMenu();
            default -> System.out.println("Такого пункта нет в меню!");
        }
    }
    protected void searchContactByName(){
        System.out.println("Введите имя для поиска: ");
        String search = enterStrValue();

        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE LOWER(name) = LOWER('" + search + "');");
        showRS(rs);
    }
    protected void searchContactByAddress(){
        System.out.println("Введите имя для поиска: ");
        String search = enterStrValue();

        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE LOWER(Address) = LOWER('" + search + "');");
        showRS(rs);
    }
    protected void searchContactByCompany(){
        System.out.println("Введите имя для поиска: ");
        String search = enterStrValue();

        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE LOWER(Company) = LOWER('" + search + "');");
        showRS(rs);
    }

    protected void changeContact() {
        int answer = 0;

        System.out.println("Какое поле будем изменять?");
        System.out.println("1. Имя.");
        System.out.println("2. Номер телефона.");
        System.out.println("3. Город.");
        System.out.println("4. Компанию.");
        System.out.println("5. Венуться в главное меню.");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            answer = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.out.println("Вы ввели неверное значение! Возможно у вас кривые пальцы и вы не туда нажали. Только числа");
            searchContact();
        }
        switch (answer) {


            case 1 -> changeContactByName();
            case 2 -> changeContactByNumber();
            case 3 -> changeContactByAddress();
            case 4 -> changeContactByCompany();
            case 5 -> mainMenu();
            default -> System.out.println("Такого пункта нет в меню!");
        }
    }
    protected void changeContactByName(){
        System.out.println("Введите точный номер контакта, чьи данные вы хотите изменить.");
        String search = enterStrValue();
        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRSBase(rs);

        System.out.println("Введите новое имя для этого контакта: ");
        String newName = enterStrValue();
        createConnect("UPDATE ContactsBook SET Name = '" + newName + "' WHERE Number = '" + search + "';");
        System.out.println("Изменения прошли успешно: ");
        rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRS(rs);

    }
    protected void changeContactByNumber(){
        System.out.println("Введите точный номер контакта, чьи данные вы хотите изменить.");
        String search = enterStrValue();
        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRSBase(rs);

        System.out.println("Введите новый номер для этого контакта: ");
        String newName = enterStrValue();
        createConnect("UPDATE ContactsBook SET Number = '" + newName + "' WHERE Number = '" + search + "';");
        System.out.println("Изменения прошли успешно: ");
        rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRS(rs);

    }
    protected void changeContactByAddress(){
        System.out.println("Введите точный номер контакта, чьи данные вы хотите изменить.");
        String search = enterStrValue();
        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRSBase(rs);

        System.out.println("Введите новый город для этого контакта: ");
        String newName = enterStrValue();
        createConnect("UPDATE ContactsBook SET Address = '" + newName + "' WHERE Number = '" + search + "';");
        System.out.println("Изменения прошли успешно: ");
        rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRS(rs);

    }
    protected void changeContactByCompany(){
        System.out.println("Введите точный номер контакта, чьи данные вы хотите изменить.");
        String search = enterStrValue();
        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRSBase(rs);

        System.out.println("Введите новую компанию для этого контакта: ");
        String newName = enterStrValue();
        createConnect("UPDATE ContactsBook SET Company = '" + newName + "' WHERE Number = '" + search + "';");
        System.out.println("Изменения прошли успешно: ");
        rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRS(rs);

    }

    protected void deleteContact() {
        System.out.println("Введите точный номер контакта, который вы хотите удалить: ");
        String search = enterStrValue();
        ResultSet rs = createConnect("SELECT * FROM ContactsBook WHERE Number = '" + search + "';");
        showRSBase(rs);
        System.out.println("Этот контакт будет удален. Согласны? Y/N");
        String answer;
        do {
            answer = enterStrValue();
        } while ((answer.equalsIgnoreCase("y")) && (answer.equalsIgnoreCase("n")));
        if (answer.equalsIgnoreCase("n")){
            System.out.println("Удаление отменено! Возвращаемся в главное меню... ");
        }else{
            createConnect("DELETE FROM ContactsBook WHERE Number = '" + search + "';");
            System.out.println("Удаление прошло успешно! Возвращаемся в главное меню...");
        }
        mainMenu();


    }

    protected void showContactBook(){

        ResultSet rs = createConnect("SELECT * FROM ContactsBook ORDER BY Name;");
        showRS(rs);
    }

    protected void showRSBase(ResultSet rs){
        System.out.println("Вот, что мы имеем: ");
        System.out.println();
        try {
            while (rs.next()) {


                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));

                System.out.println();


            }
        }catch (SQLException e){
            System.out.println("Что-то пошло не так на этапе вывода данных из книги.");
        }catch (NullPointerException ex){
            System.out.println("По данным параметрам ничего не нашлось! Возвращаемся в главное меню...");
            mainMenu();
        }
    }
    protected void showRS(ResultSet rs) {
        showRSBase(rs);
        System.out.println("Это все, что есть. Возвращаемся в главное меню... \n");
        mainMenu();
    }
    protected String enterStrValue() {
        String search = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try{
            search = br.readLine();
        }catch (IOException e){
            System.out.println("Ошибка ввода имени для поиска!");
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия потока");
            e.printStackTrace();
        }
        return search;
    }
    protected ResultSet createConnect(String query) {
        Connection con;
        Statement stm;
        ResultSet rs = null;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "12345";

        try {
            con = DriverManager.getConnection(url, user, password);
            stm = con.createStatement();
            try {
                rs = stm.executeQuery(query);
                con.close();
            } catch (PSQLException e) {
                //IGNORE
            }

        } catch (SQLException e) {
            System.out.println("Ошибка соединения с базой. Проверьте подключение.");
            e.printStackTrace();
        }

        return rs;

    }
}
