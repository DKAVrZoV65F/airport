package com.airportapp;

import com.airportapp.dao.*;
import com.airportapp.dao.AircraftDao;
import com.airportapp.dao.AirportDao;
import com.airportapp.dao.FlightDao;
import com.airportapp.dao.PassengerDao;
import com.airportapp.dao.TicketDao;
import com.airportapp.dao.GenericDao;
import com.airportapp.dao.GenericDaoImpl;
import com.airportapp.dao.impl.*;
import com.airportapp.model.*;
import com.airportapp.util.JpaUtil;
import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                buildAirportTab(),
                buildAircraftTab(),
                buildFlightTab(),
                buildPassengerTab(),
                buildTicketTab()
        );
        stage.setTitle("Airport Management");
        stage.setScene(new Scene(tabs, 1000, 600));
        stage.show();
    }

    private Tab buildAirportTab() {
        Tab tab = new Tab("Airport");
        AirportDao dao = new AirportDaoImpl();
        TableView<Airport> tv = new TableView<>();
        TableColumn<Airport,Long> c1 = new TableColumn<>("ID");
        TableColumn<Airport,String> c2 = new TableColumn<>("Name");
        TableColumn<Airport,String> c3 = new TableColumn<>("City");
        TableColumn<Airport,String> c4 = new TableColumn<>("Country");
        TableColumn<Airport,String> c5 = new TableColumn<>("IATA");
        c1.setCellValueFactory(d-> new SimpleObjectProperty<>(d.getValue().getId()));
        c2.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getName()));
        c3.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getCity()));
        c4.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getCountry()));
        c5.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getIataCode()));
        tv.getColumns().addAll(c1,c2,c3,c4,c5);
        refresh(tv,dao);

        TextField fName = new TextField(), fCity = new TextField(),
                fCountry = new TextField(), fIata = new TextField();
        fName.setPromptText("Name");
        fCity.setPromptText("City");
        fCountry.setPromptText("Country");
        fIata.setPromptText("IATA");
        Button add = new Button("Add");
        add.setOnAction(e -> {
            Airport a = new Airport();
            a.setName(fName.getText());
            a.setCity(fCity.getText());
            a.setCountry(fCountry.getText());
            a.setIataCode(fIata.getText());
            dao.save(a);
            clear(fName,fCity,fCountry,fIata);
            refresh(tv,dao);
        });
        Button del = new Button("Delete");
        del.setOnAction(e -> {
            Airport s = tv.getSelectionModel().getSelectedItem();
            if (s!=null) dao.delete(s);
            refresh(tv,dao);
        });
        HBox hb = new HBox(5,fName,fCity,fCountry,fIata,add,del);
        hb.setPadding(new Insets(5));
        tab.setContent(new VBox(5,tv,hb));
        return tab;
    }

    private Tab buildAircraftTab() {
        Tab tab = new Tab("Aircraft");
        AircraftDao dao = new AircraftDaoImpl();
        TableView<Aircraft> tv = new TableView<>();
        TableColumn<Aircraft,Long> c1 = new TableColumn<>("ID");
        TableColumn<Aircraft,String> c2 = new TableColumn<>("Model");
        TableColumn<Aircraft,String> c3 = new TableColumn<>("Manufacturer");
        TableColumn<Aircraft,Integer> c4 = new TableColumn<>("Capacity");
        c1.setCellValueFactory(d-> new SimpleObjectProperty<>(d.getValue().getId()));
        c2.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getModel()));
        c3.setCellValueFactory(d-> new SimpleStringProperty(d.getValue().getManufacturer()));
        c4.setCellValueFactory(d-> new SimpleObjectProperty<>(d.getValue().getCapacity()));
        tv.getColumns().addAll(c1,c2,c3,c4);
        refresh(tv,dao);

        TextField fM = new TextField(), fMan = new TextField(), fCap = new TextField();
        fM.setPromptText("Model");
        fMan.setPromptText("Manufacturer");
        fCap.setPromptText("Capacity");
        Button add = new Button("Add");
        add.setOnAction(e->{
            Aircraft a = new Aircraft();
            a.setModel(fM.getText());
            a.setManufacturer(fMan.getText());
            a.setCapacity(Integer.parseInt(fCap.getText()));
            dao.save(a);
            clear(fM,fMan,fCap);
            refresh(tv,dao);
        });
        Button del = new Button("Delete");
        del.setOnAction(e->{
            Aircraft s=tv.getSelectionModel().getSelectedItem();
            if(s!=null) dao.delete(s);
            refresh(tv,dao);
        });
        tab.setContent(new VBox(5,tv,new HBox(5,fM,fMan,fCap,add,del)));
        return tab;
    }

    private Tab buildFlightTab() {
        Tab tab = new Tab("Flight");
        FlightDao dao = new FlightDaoImpl();
        AirportDao ap = new AirportDaoImpl();
        AircraftDao ac = new AircraftDaoImpl();
        TableView<Flight> tv=new TableView<>();
        TableColumn<Flight,Long> c1=new TableColumn<>("ID");
        TableColumn<Flight,String> c2=new TableColumn<>("Number");
        TableColumn<Flight,LocalDateTime> c3=new TableColumn<>("Dep");
        TableColumn<Flight,LocalDateTime> c4=new TableColumn<>("Arr");
        TableColumn<Flight,String> c5=new TableColumn<>("Aircraft");
        TableColumn<Flight,String> c6=new TableColumn<>("Orig");
        TableColumn<Flight,String> c7=new TableColumn<>("Dest");
        c1.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getId()));
        c2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getFlightNumber()));
        c3.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getDepartureTime()));
        c4.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getArrivalTime()));
        c5.setCellValueFactory(d->new SimpleStringProperty(
                d.getValue().getAircraft().getModel()));
        c6.setCellValueFactory(d->new SimpleStringProperty(
                d.getValue().getOrigin().getIataCode()));
        c7.setCellValueFactory(d->new SimpleStringProperty(
                d.getValue().getDestination().getIataCode()));
        tv.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7);
        refresh(tv,dao);

        TextField fNum = new TextField(), tDep = new TextField("10:00"), tArr = new TextField("12:00");
        DatePicker dDep = new DatePicker(), dArr = new DatePicker();
        ComboBox<Aircraft> cbA = new ComboBox<>(FXCollections.observableList(ac.findAll()));
        ComboBox<Airport>  cbO = new ComboBox<>(FXCollections.observableList(ap.findAll()));
        ComboBox<Airport>  cbD = new ComboBox<>(FXCollections.observableList(ap.findAll()));
        fNum.setPromptText("Number"); dDep.setPromptText("DepDate"); tDep.setPromptText("HH:mm");
        dArr.setPromptText("ArrDate"); tArr.setPromptText("HH:mm");
        Button add=new Button("Add");
        add.setOnAction(e->{
            Flight f=new Flight();
            f.setFlightNumber(fNum.getText());
            LocalDateTime dep=dDep.getValue().atTime(java.time.LocalTime.parse(tDep.getText()));
            LocalDateTime arr=dArr.getValue().atTime(java.time.LocalTime.parse(tArr.getText()));
            f.setDepartureTime(dep); f.setArrivalTime(arr);
            f.setAircraft(cbA.getValue()); f.setOrigin(cbO.getValue());
            f.setDestination(cbD.getValue());
            dao.save(f);
            refresh(tv,dao);
        });
        Button del=new Button("Delete");
        del.setOnAction(e->{ Flight s=tv.getSelectionModel().getSelectedItem();
            if(s!=null) dao.delete(s); refresh(tv,dao);
        });
        HBox hb=new HBox(5,fNum,dDep,tDep,dArr,tArr,cbA,cbO,cbD,add,del);
        hb.setPadding(new Insets(5));
        tab.setContent(new VBox(5,tv,hb));
        return tab;
    }

    private Tab buildPassengerTab() {
        Tab tab=new Tab("Passenger");
        PassengerDao dao=new PassengerDaoImpl();
        TableView<Passenger> tv=new TableView<>();
        TableColumn<Passenger,Long> c1=new TableColumn<>("ID");
        TableColumn<Passenger,String> c2=new TableColumn<>("First");
        TableColumn<Passenger,String> c3=new TableColumn<>("Last");
        TableColumn<Passenger,String> c4=new TableColumn<>("Passport");
        TableColumn<Passenger,LocalDate> c5=new TableColumn<>("DOB");
        c1.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getId()));
        c2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getFirstName()));
        c3.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getLastName()));
        c4.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getPassportNumber()));
        c5.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getDateOfBirth()));
        tv.getColumns().addAll(c1,c2,c3,c4,c5);
        refresh(tv,dao);
        TextField f1=new TextField(), f2=new TextField(), f3=new TextField();
        DatePicker dp=new DatePicker();
        f1.setPromptText("First"); f2.setPromptText("Last"); f3.setPromptText("Passport"); dp.setPromptText("DOB");
        Button add=new Button("Add"), del=new Button("Delete");
        add.setOnAction(e->{
            Passenger p=new Passenger();
            p.setFirstName(f1.getText()); p.setLastName(f2.getText());
            p.setPassportNumber(f3.getText()); p.setDateOfBirth(dp.getValue());
            dao.save(p); refresh(tv,dao);
        });
        del.setOnAction(e->{ Passenger s=tv.getSelectionModel().getSelectedItem();
            if(s!=null) dao.delete(s); refresh(tv,dao);
        });
        tab.setContent(new VBox(5,tv,new HBox(5,f1,f2,f3,dp,add,del)));
        return tab;
    }

    private Tab buildTicketTab(){
        Tab tab=new Tab("Ticket");
        TicketDao dao=new TicketDaoImpl();
        FlightDao fDao=new FlightDaoImpl();
        PassengerDao pDao=new PassengerDaoImpl();
        TableView<Ticket> tv=new TableView<>();
        TableColumn<Ticket,Long> c1=new TableColumn<>("ID");
        TableColumn<Ticket,String> c2=new TableColumn<>("Seat");
        TableColumn<Ticket,BigDecimal> c3=new TableColumn<>("Price");
        TableColumn<Ticket,String> c4=new TableColumn<>("Flight");
        TableColumn<Ticket,String> c5=new TableColumn<>("Passenger");
        c1.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getId()));
        c2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getSeatNumber()));
        c3.setCellValueFactory(d->new SimpleObjectProperty<>(d.getValue().getPrice()));
        c4.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getFlight().getFlightNumber()));
        c5.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getPassenger().getFirstName()));
        tv.getColumns().addAll(c1,c2,c3,c4,c5);
        refresh(tv,dao);
        TextField fs=new TextField(), fp=new TextField();
        ComboBox<Flight>  cbF=new ComboBox<>(FXCollections.observableList(fDao.findAll()));
        ComboBox<Passenger> cbP=new ComboBox<>(FXCollections.observableList(pDao.findAll()));
        fs.setPromptText("Seat"); fp.setPromptText("Price");
        Button add=new Button("Add"), del=new Button("Delete");
        add.setOnAction(e->{
            Ticket t=new Ticket();
            t.setSeatNumber(fs.getText());
            t.setPrice(new BigDecimal(fp.getText()));
            t.setFlight(cbF.getValue());
            t.setPassenger(cbP.getValue());
            dao.save(t); refresh(tv,dao);
        });
        del.setOnAction(e->{ Ticket s=tv.getSelectionModel().getSelectedItem();
            if(s!=null) dao.delete(s); refresh(tv,dao);
        });
        tab.setContent(new VBox(5,tv,new HBox(5,fs,fp,cbF,cbP,add,del)));
        return tab;
    }

    private <E> void refresh(TableView<E> tv, GenericDao<E,?> dao) {
        tv.setItems(FXCollections.observableList(dao.findAll()));
    }
    private void clear(TextField... f) {
        for(var t:f) t.clear();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        JpaUtil.close();
    }

    public static void main(String[] args) {
        launch();
    }
}