package com.epam.archive.client.controllers;

import java.io.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;

import com.epam.archive.client.MainClient;
import com.epam.archive.client.controllers.threads.ArchiveRepainterThread;
import com.epam.archive.client.controllers.threads.UserRepainterThread;
import com.epam.archive.mainGui.main;
import com.epam.archive.models.Person;
import com.epam.archive.models.Request;
import com.epam.archive.models.User;
import com.epam.archive.service.GeneratePDF;
import com.epam.archive.service.GenerateTXT;
import com.epam.archive.service.SerializeMaker;
import com.itextpdf.text.DocumentException;
import javafx.animation.AnimationTimer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ClientMainController {

    private static BorderPane mainPane;
    private static MainClient mainClient;
    private static ArchiveRepainterThread archiveThread;
    private static UserRepainterThread userThread;
    private static Person selectedPerson;
    private static User selectedUser;
    private static LinkedList<Person> archiveCatalog = null;
    private static LinkedList<User> userCatalog = null;
    private static TableView archiveTable = null;
    private static TableView userTable = null;
    private static TextField surnameField;
    private static TextField fathernameField;
    private static TextField nameField;
    private static TextField phoneField;
    private static TextField emailField;
    private static TextField nameJobField;
    private static TextField experienceJobField;
    private static Button showAllBtn;
    private static Button editBtn;
    private static Button deleteBtn;
    private static Button addBtn;
    private static Button searchBtn;
    private static Button changeRightsBtn;
    private static Button changeParserBtn;
    private static Button exitBtn;
    private static int rightsUser;
    private static ContextMenu contextParserMenu;
    private static ContextMenu contextRightsMenu;
    private static Boolean searchFlag = false;
    private static ComboBox parserBox;
    private static Button saveParserBtn;
    private static Label timeLbl;
    private static AnimationTimer at;
    private static Button exportBtn;
    private static final Logger log = Logger.getLogger(ClientMainController.class);

    public static BorderPane getPane(int rightsLevel, MainClient client) throws IOException {

        mainClient = client;
        rightsUser = rightsLevel;
        surnameField = new TextField();
        fathernameField = new TextField();
        nameField = new TextField();
        phoneField = new TextField();
        emailField = new TextField();
        nameJobField = new TextField();
        experienceJobField = new TextField();

        surnameField.lengthProperty().addListener(searchListener());
        nameField.lengthProperty().addListener(searchListener());
        fathernameField.lengthProperty().addListener(searchListener());
        phoneField.lengthProperty().addListener(searchListener());
        emailField.lengthProperty().addListener(searchListener());
        nameJobField.lengthProperty().addListener(searchListener());
        experienceJobField.lengthProperty().addListener(searchListener());

        surnameField.setPromptText("-surname-");
        fathernameField.setPromptText("-father name-");
        nameField.setPromptText("-name-");
        phoneField.setPromptText("-mobile phone-");
        emailField.setPromptText("-email-");
        nameJobField.setPromptText("-name of job-");
        experienceJobField.setPromptText("-job experience-");

        searchBtn = new Button("Search");
        searchBtn.setDisable(true);
        searchBtn.setOnAction(searchAction());
        exitBtn = new Button("Exit");
        exitBtn.setOnAction(exitAction());
        addBtn = new Button("Add");
        addBtn.setOnAction(addAction());
        deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(deleteAction());
        deleteBtn.setDisable(true);
        editBtn = new Button("Edit");
        editBtn.setDisable(true);
        editBtn.setOnAction(editAction());
        showAllBtn = new Button("Show all");
        showAllBtn.setOnAction(showAllAction());

        exportBtn = new Button("Export...");
        exportBtn.setPrefWidth(90);
        exportBtn.setOnAction(exportAction());

        searchBtn.setPrefWidth(100);
        exitBtn.setPrefWidth(90);
        addBtn.setPrefWidth(90);
        deleteBtn.setPrefWidth(90);
        editBtn.setPrefWidth(90);
        showAllBtn.setPrefWidth(90);
        searchBtn.setMinWidth(90);
        exitBtn.setMinWidth(80);
        addBtn.setMinWidth(80);
        deleteBtn.setMinWidth(80);
        editBtn.setMinWidth(80);
        showAllBtn.setMinWidth(80);

        HBox searchFirstLayout = new HBox(5);
        searchFirstLayout.getChildren().addAll(surnameField, nameField, fathernameField, phoneField);
        searchFirstLayout.setAlignment(Pos.CENTER);

        HBox searchSecondLayout = new HBox(5);
        searchSecondLayout.getChildren().addAll(emailField, nameJobField, experienceJobField, searchBtn);
        searchSecondLayout.setAlignment(Pos.CENTER);

        timeLbl = new Label("Time");

        HBox timeL = new HBox(5);
        timeL.getChildren().addAll(timeLbl);
        timeL.setAlignment(Pos.BOTTOM_CENTER);

        VBox finishSearchLayout = new VBox(5);
        if (rightsLevel != 0)
            finishSearchLayout.getChildren().addAll(timeL, searchFirstLayout, searchSecondLayout);
        else
            finishSearchLayout.getChildren().addAll(searchFirstLayout, searchSecondLayout);
        finishSearchLayout.setPadding(new Insets(0, 20, 20, 20));
        finishSearchLayout.setAlignment(Pos.CENTER);

        VBox serviceBtnLayout = new VBox(5);
        serviceBtnLayout.getChildren().addAll(exitBtn, showAllBtn, exportBtn, editBtn, deleteBtn, addBtn);
        serviceBtnLayout.setPadding(new Insets(20, 20, 20, 20));
        serviceBtnLayout.setAlignment(Pos.TOP_RIGHT);

        VBox tableLayout = new VBox(5);
        tableLayout.getChildren().addAll(createArchiveTable());
        tableLayout.setPadding(new Insets(20, 0, 20, 20));
        tableLayout.setAlignment(Pos.TOP_LEFT);

        mainPane = new BorderPane();

        if (rightsUser == 0) { // admin

            changeRightsBtn = new Button("Change rights");
            changeParserBtn = new Button("Change parser");
            saveParserBtn = new Button("Save");
            changeRightsBtn.setDisable(true);
            changeParserBtn.setDisable(true);
            changeRightsBtn.setPrefWidth(120);
            changeParserBtn.setPrefWidth(120);
            saveParserBtn.setPrefWidth(120);
            saveParserBtn.setOnAction(saveParserAction());

            parserBox = new ComboBox();
            parserBox.getItems().addAll("DOM", "JDOM", "SAX", "StAX");
            parserBox.setValue("DOM");
            parserBox.setPrefWidth(120);

            contextParserMenu = new ContextMenu();

            MenuItem domItem = new MenuItem("DOM");
            domItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setParser(User.ParserName.DOM);
                    ChangeUserParser(selectedUser);
                }
            });

            MenuItem jDomItem = new MenuItem("JDOM");
            jDomItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setParser(User.ParserName.JDOM);
                    ChangeUserParser(selectedUser);
                }
            });

            MenuItem saxItem = new MenuItem("SAX");
            saxItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setParser(User.ParserName.SAX);
                    ChangeUserParser(selectedUser);
                }
            });

            MenuItem staxItem = new MenuItem("STAX");
            staxItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setParser(User.ParserName.STAX);
                    ChangeUserParser(selectedUser);
                }
            });

            contextRightsMenu = new ContextMenu();

            MenuItem adminItem = new MenuItem("ADMINISTRATOR");
            adminItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setRights(User.Rights.ADMINISTRATOR);
                    ChangeUserRights(selectedUser);
                }
            });

            MenuItem seniorItem = new MenuItem("SENIOR USER");
            seniorItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setRights(User.Rights.SENIORUSER);
                    ChangeUserRights(selectedUser);
                }
            });

            MenuItem userItem = new MenuItem("USER");
            userItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectedUser.setRights(User.Rights.USER);
                    ChangeUserRights(selectedUser);
                }
            });

            contextParserMenu.getItems().addAll(domItem, jDomItem, saxItem, staxItem);
            changeParserBtn.setContextMenu(contextParserMenu);
            changeParserBtn.setOnAction(changeParserAction());

            contextRightsMenu.getItems().addAll(adminItem, seniorItem, userItem);
            changeRightsBtn.setContextMenu(contextRightsMenu);
            changeRightsBtn.setOnAction(changeRightsAction());

            timeLbl = new Label("Time");

            HBox timeLayout = new HBox(5);
            timeLayout.getChildren().addAll(timeLbl);
            timeLayout.setAlignment(Pos.TOP_LEFT);

            HBox saveParserL = new HBox(5);
            saveParserL.getChildren().addAll(parserBox, saveParserBtn);

            HBox adminBtnLayout = new HBox(5);
            adminBtnLayout.getChildren().addAll(changeParserBtn, changeRightsBtn);

            VBox btnL = new VBox(5);
            btnL.getChildren().addAll(adminBtnLayout, saveParserL);

            HBox tablePlusBtnLayout = new HBox(5);
            tablePlusBtnLayout.getChildren().addAll(tableLayout, serviceBtnLayout);
            tablePlusBtnLayout.setPadding(new Insets(0, 0, 20, 20));
            tablePlusBtnLayout.setAlignment(Pos.TOP_LEFT);

            VBox userTableLayout = new VBox(5);
            userTableLayout.getChildren().addAll(timeLayout, createUserTable());
            userTableLayout.setPadding(new Insets(20, 20, 0, 0));
            userTableLayout.setAlignment(Pos.TOP_RIGHT);

            VBox adminFinishLayout = new VBox(5);
            adminFinishLayout.getChildren().addAll(userTableLayout, btnL);
            adminFinishLayout.setPadding(new Insets(0, 20, 20, 10));

            mainPane.setCenter(tablePlusBtnLayout);
            mainPane.setBottom(finishSearchLayout);
            mainPane.setRight(adminFinishLayout);

            userCatalog = new LinkedList<User>();
            userThread = new UserRepainterThread();
            userThread.start();

        } else {

            if (rightsUser == 1) { // Guest
                setGuestHide();
            } else if (rightsUser == 2) { // User,else Senior user
                setUserHide();
            }

            mainPane.setCenter(tableLayout);
            mainPane.setBottom(finishSearchLayout);
            mainPane.setRight(serviceBtnLayout);

        }
        archiveCatalog = new LinkedList<Person>();
        archiveThread = new ArchiveRepainterThread();
        archiveThread.start();

        at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLbl.setText("" + Calendar.getInstance().getTime() + "");
            }
        };
        at.start();
        return mainPane;

    }

    public static TableView createUserTable() {

        if (userTable == null) {
            userTable = new TableView();
            userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (newValue != null) {
                        selectedUser = (User) userTable.getSelectionModel().getSelectedItem();
                        if (rightsUser != 1) {
                            changeParserBtn.setDisable(false);
                            changeRightsBtn.setDisable(false);
                        }
                    }
                }
            });

            TableColumn login = new TableColumn("Login");
            login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
            login.setPrefWidth(110);

            TableColumn pass = new TableColumn("Password");
            pass.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
            pass.setPrefWidth(110);

            TableColumn rights = new TableColumn("Rights");
            rights.setCellValueFactory(new PropertyValueFactory<User, String>("rights"));
            rights.setPrefWidth(100);

            TableColumn parser = new TableColumn("Parser");
            parser.setCellValueFactory(new PropertyValueFactory<User, String>("parser"));
            parser.setPrefWidth(70);

            userTable.getColumns().addAll(login, pass, rights, parser);
            userTable.setPrefSize(400, 300);
        }
        Request request = new Request("GETUSERTABLE", null, null, null);
        String response = new String();
        try {
            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
            response = mainClient.getInputStream().readUTF();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        changeUserTable((LinkedList<User>) SerializeMaker.deserializeFromXML(response));
        return userTable;
    }

    public static TableView createArchiveTable() {

        if (archiveTable == null) {
            archiveTable = new TableView();

            archiveTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (newValue != null) {
                        selectedPerson = (Person) archiveTable.getSelectionModel().getSelectedItem();
                        if (rightsUser != 1) {
                            deleteBtn.setDisable(false);
                            editBtn.setDisable(false);
                        }
                    }
                }
            });

            TableColumn surname = new TableColumn("Surname");
            surname.setCellValueFactory(new PropertyValueFactory<Person, String>("surname"));
            surname.setPrefWidth(70);

            TableColumn name = new TableColumn("Name");
            name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
            name.setPrefWidth(70);

            TableColumn fathername = new TableColumn("Fathername");
            fathername.setCellValueFactory(new PropertyValueFactory<Person, String>("fathername"));
            fathername.setPrefWidth(110);

            TableColumn phone = new TableColumn("Phone number");
            phone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
            phone.setPrefWidth(110);

            TableColumn email = new TableColumn("eMail");
            email.setCellValueFactory(new PropertyValueFactory<Person, String>("eMail"));
            email.setPrefWidth(130);

            TableColumn nameJob = new TableColumn("Job name");
            nameJob.setCellValueFactory(new PropertyValueFactory<Person, String>("nameJob"));
            nameJob.setPrefWidth(70);

            TableColumn experienceJob = new TableColumn("Job experience");
            experienceJob.setCellValueFactory(new PropertyValueFactory<Person, String>("experienceJob"));
            experienceJob.setPrefWidth(90);

            archiveTable.getColumns().addAll(surname, name, fathername, phone, email, nameJob, experienceJob);
            archiveTable.setPrefSize(800, 1500);
        }
        if (archiveCatalog != null) {
            ObservableList<Person> item = FXCollections.observableArrayList();
            for (int i = 0; i < archiveCatalog.size(); i++) {
                Person person = archiveCatalog.get(i);
                item.addAll(person);
            }
            archiveTable.setItems(item);
        }

        return archiveTable;
    }

    public static EventHandler<ActionEvent> exportAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == exportBtn) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Export...");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF", "*.pdf");
                    FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("TXT", "*.txt");
                    fileChooser.getExtensionFilters().addAll(extFilter,extFilter1);
                    File file = fileChooser.showSaveDialog(main.get_stage());
                    if (file != null) {
                        String fileName = file.getName();
                        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                            fileName = fileName.substring(fileName.lastIndexOf("."));
                        if (file != null && fileName.equals(".pdf")) {

                            try {
                                GeneratePDF.createPDF(file.toString());
                                log.info("User export table into .pdf format.");
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (DocumentException e1) {
                                e1.printStackTrace();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }


                        } else if (file != null && fileName.equals(".txt")) {
                            try {
                                GenerateTXT.GenerateTXT(file.toString());
                                log.info("User export table into .txt format.");

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }

                }

            }
        };
    }

    public static EventHandler<ActionEvent> saveParserAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == saveParserBtn) {
                    File file = new File("D:\\Intellij_workspace\\Archive_realize\\server\\ArchiveServer\\Data\\parser.txt");

                    try {
                        if (!file.exists())
                            file.createNewFile();
                        else {
                            file.delete();
                            file.createNewFile();
                        }
                        FileWriter fstream = new FileWriter("D:\\Intellij_workspace\\Archive_realize\\server\\ArchiveServer\\Data\\parser.txt");// конструктор с одним параметром - для перезаписи
                        BufferedWriter out = new BufferedWriter(fstream); //  создаём буферезированный поток
                        out.write(parserBox.getValue().toString());
                        out.close();
                        fstream.close();
                        log.info("Admin change parser for loading data on "+parserBox.getValue().toString()+".");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        };
    }

    public static EventHandler<ActionEvent> changeRightsAction() {

        return new EventHandler<ActionEvent>() {


            public void handle(ActionEvent e) {
                if (e.getSource() == changeRightsBtn) {

                    contextRightsMenu.show(changeRightsBtn, Side.BOTTOM, 0, 0);
                }

            }
        };
    }

    public static EventHandler<ActionEvent> changeParserAction() {

        return new EventHandler<ActionEvent>() {


            public void handle(ActionEvent e) {
                if (e.getSource() == changeParserBtn) {

                    contextParserMenu.show(changeParserBtn, Side.BOTTOM, 0, 0);
                }

            }
        };
    }

    public static EventHandler<ActionEvent> searchAction() {

        return new EventHandler<ActionEvent>() {


            public void handle(ActionEvent e) {
                if (e.getSource() == searchBtn) {
                    deleteBtn.setDisable(true);
                    editBtn.setDisable(true);
                    synchronized (mainClient) {
                        Person searchPerson = new Person(surnameField.getText(), nameField.getText(),
                                fathernameField.getText(), phoneField.getText(), emailField.getText(),
                                nameJobField.getText(), experienceJobField.getText());

                        Request request = new Request("SEARCH", searchPerson, null, null);
                        String response = new String();
                        try {
                            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                            response = mainClient.getInputStream().readUTF();
                            log.info("User made search request.");
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        synchronized (archiveCatalog) {

                            synchronized (archiveTable) {
                                changeArchiveTable((LinkedList<Person>) SerializeMaker.deserializeFromXML(response));
                                searchFlag = true;
                            }

                        }

                    }
                }

            }
        };
    }

    public static EventHandler<ActionEvent> exitAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == exitBtn) {
                    synchronized (mainClient) {
                        Request request = new Request("EXIT", null, null, null);
                        try {
                            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    archiveCatalog = null;
                    archiveTable = null;
                    userTable = null;
                    userCatalog = null;
                    searchFlag = false;
                    archiveThread.interrupt();
                    at.stop();
                    if (rightsUser == 0)
                        userThread.interrupt();
                    main.get_stage().setScene(ClientEntranceController.getEntranceScene());
                    log.info("User exit from system.");

                }

            }
        };
    }

    public static EventHandler<ActionEvent> showAllAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == showAllBtn) {
                    Request request = new Request("SHOWALL", null, null, null);
                    String response = new String();
                    synchronized (mainClient) {
                        try {
                            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
                            response = mainClient.getInputStream().readUTF();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        synchronized (archiveCatalog) {
                            synchronized (archiveTable) {
                                changeArchiveTable((LinkedList<Person>) SerializeMaker.deserializeFromXML(response));
                                searchFlag = false;
                            }

                        }
                    }
                }

            }
        };
    }

    public static EventHandler<ActionEvent> deleteAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == deleteBtn) {
                    Request request = new Request("DELETE", selectedPerson, null, null);
                    String response = new String();
                    synchronized (mainClient) {
                        try {
                            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));

                            response = mainClient.getInputStream().readUTF();
                            log.info("User delete '"+selectedPerson.getName()+" "+selectedPerson.getSurname()+"' from archive.");
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        synchronized (archiveCatalog) {
                            synchronized (archiveTable) {
                                changeArchiveTable((LinkedList<Person>) SerializeMaker.deserializeFromXML(response));
                                searchFlag = false;
                            }

                        }
                        deleteBtn.setDisable(true);
                        editBtn.setDisable(true);
                    }
                }

            }
        };
    }

    public static EventHandler<ActionEvent> addAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == addBtn)
                    ClientDialogController.getDialogWindow(true);

            }
        };
    }

    public static EventHandler<ActionEvent> editAction() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (e.getSource() == editBtn)
                    ClientDialogController.getDialogWindow(false);
                deleteBtn.setDisable(true);
                editBtn.setDisable(true);
            }
        };
    }

    public static ChangeListener<Number> searchListener() {

        return new ChangeListener<Number>() {


            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                // TODO Auto-generated method stub

                if (!surnameField.getText().equals("") || !nameField.getText().equals("")
                        || !fathernameField.getText().equals("") || !phoneField.getText().equals("")
                        || !emailField.getText().equals("") || !nameJobField.getText().equals("")
                        || !experienceJobField.getText().equals("")) {
                    searchBtn.setDisable(false);
                } else {
                    searchBtn.setDisable(true);
                }

            }

        };
    }

    public static void changeArchiveTable(LinkedList<Person> archive) {
        archiveCatalog = archive;
        ObservableList<Person> item = FXCollections.observableArrayList();
        for (int i = 0; i < archiveCatalog.size(); i++) {
            Person person = archiveCatalog.get(i);
            item.addAll(person);
        }
        archiveTable.setItems(item);

    }

    public static void changeUserTable(LinkedList<User> userList) {
        userCatalog = userList;
        ObservableList<User> item = FXCollections.observableArrayList();
        for (int i = 0; i < userCatalog.size(); i++) {
            User user = userCatalog.get(i);
            item.addAll(user);
        }
        userTable.setItems(item);

    }

    public static void ChangeUserParser(User user) {

        Request request = new Request("CHANGEPARSER", null, null, user);
        try {
            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
            String response = new String();
            response = mainClient.getInputStream().readUTF();
            changeUserTable((LinkedList<User>) SerializeMaker.deserializeFromXML(response));
            log.info("Admin changed parser for "+user.getLogin()+".");
            changeParserBtn.setDisable(true);
            changeRightsBtn.setDisable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void ChangeUserRights(User user) {
        Request request = new Request("CHANGERIGHTS", null, null, user);
        try {
            mainClient.getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
            String response = new String();
            response = mainClient.getInputStream().readUTF();
            changeUserTable((LinkedList<User>) SerializeMaker.deserializeFromXML(response));
            log.info("Admin changed rights for "+user.getLogin()+".");
            changeParserBtn.setDisable(true);
            changeRightsBtn.setDisable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setGuestHide() {
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);
        addBtn.setVisible(false);
    }

    public static void setUserHide() {
        deleteBtn.setVisible(false);
        addBtn.setVisible(false);
    }

    public static int getRights() {
        return rightsUser;
    }

    public static Person getSelectedPerson() {
        return selectedPerson;
    }

    public static LinkedList<Person> getArchiveList() {
        return archiveCatalog;
    }

    public static LinkedList<User> getUserList() {
        return userCatalog;
    }

    public static MainClient getClient() {
        return mainClient;
    }

    public static TableView getArchiveTable() {
        return archiveTable;
    }

    public static Boolean getSearchFlag() {
        return searchFlag;
    }

    public static void setArchiveList(LinkedList<Person> list) {
        archiveCatalog = list;
    }

    public static void setSearchFlag(Boolean flag) {
        searchFlag = flag;
    }

    public static void setUserList(LinkedList<User> list) {
        userCatalog = list;
    }

    public static TableView getUserTable() {
        return userTable;
    }


}