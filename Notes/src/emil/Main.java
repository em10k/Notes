package emil;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class Main extends Application {

    private Label lb_menu,lb_curTabName,lb_search,lb_settings,lb_notes,lb_reminders,lb_archive,lb_basket,lb_createFolder;
    private Button btn_createNote, btn_closeListItem;
    private ArrayList<VBox> noteBlock, listContainer, reminderBlock;      //список-массив из VBox (сами заметки)
    private ArrayList<HBox> noteList;                      //список из HBox, на которых располагаются пункты в списке заметок
    private ArrayList<TextArea> taList;
    private ArrayList<ArrayList> listOfList;
    private HashMap<Long, ArrayList<VBox>> hashMapList;
    private FlowPane fp_centerPaneNote, fp_centerPaneRem;
    private ScrollPane sp_scrollNote;
    private TextArea ta_listItem;
    private VBox vb_listArea_NB;
    private int currentPanelType;
    private Thread thread;
    private List monthsList;

    public static void main(String[] args) {launch(args);}

    public void start(Stage myStage) throws InterruptedException {
        noteBlock = new ArrayList<>();
        reminderBlock = new ArrayList<>();
        noteList = new ArrayList<>();
        taList = new ArrayList<>();
        hashMapList = new HashMap<>();



        Runnable task = () ->{
            int i=1;
            /*while (i>0) {
                System.out.println(i+" Hello");
                if (i==5) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                                            } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i=0;
                }
                i++;
            }
           */

        };
        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        Date curDate = new Date();

        GregorianCalendar calendar = new GregorianCalendar();
        Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd M YYYY");

        //Date date = calendar.getTime();
        System.out.println(df.format(calendar.getTime()));

        currentPanelType = curPanelType.note.value;

        //главная сетка
        BorderPane vb_main = new BorderPane();             //главный вертикальный бокс растянут на всю сцену

        //сетка для верхнего меню
        HBox hb_top_menu = new HBox(); //панель для верхнего меню
        hb_top_menu.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        //сетка для левой части меню
        HBox hb_left_menu = new HBox();        //бокс для горизонтального меню
        hb_left_menu.setSpacing(5);
        //сетка для правой части меню
        HBox hb_right_menu = new HBox();        //бокс для горизонтального меню
        hb_right_menu.setSpacing(5);
        //создаем спейсер (распорку), чтобы вставить между элементами в верхнее меню
        Pane spacer_topMenu = new Pane();
        HBox.setHgrow(spacer_topMenu,Priority.ALWAYS);  //говорим о том, что в приоретете растягивать именно пружину
        spacer_topMenu.setMinSize(10,1);
        //соединение двух частей верхнего меню
        hb_top_menu.getChildren().addAll(hb_left_menu,spacer_topMenu,hb_right_menu);

        //боковая панель
        VBox vb_sidePanel = new VBox();
        vb_sidePanel.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        vb_sidePanel.setSpacing(5);
            //сетка для верхней части
        VBox vb_sidePanel_top = new VBox();                                     //верхняя часть вертикального бокса выдвигающегося меню
        //vb_sidePanel_top.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
            //сетка для нижней части
        VBox vb_sidePanel_bottom = new VBox();                                     //нижняя часть вертикального бокса выдвигающегося меню
        //vb_sidePanel_bottom.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

        //сетка для правой части от боковой панели
        fp_centerPaneNote = new FlowPane(10,10);                                        //горизонтальный бокс справа от боковой панели с расстоянием между элементами
        fp_centerPaneNote.setPadding(new Insets(10));                                       //расстояние от полей
        fp_centerPaneNote.setOrientation(Orientation.HORIZONTAL);                                            //горизонтальное расположение элементов внутри сетки
        fp_centerPaneNote.setBackground(new Background(new BackgroundFill(Color.rgb(90,90,90),null,null)));

        fp_centerPaneRem = new FlowPane(10,10);                                        //горизонтальный бокс справа от боковой панели с расстоянием между элементами
        fp_centerPaneRem.setPadding(new Insets(10));                                       //расстояние от полей
        fp_centerPaneRem.setOrientation(Orientation.HORIZONTAL);                                            //горизонтальное расположение элементов внутри сетки
        fp_centerPaneRem.setBackground(new Background(new BackgroundFill(Color.rgb(90,90,90),null,null)));
        fp_centerPaneRem.setVisible(false);

        //скрол для панели заметок
        sp_scrollNote = new ScrollPane(fp_centerPaneNote);       //создали скролл и добавили основную панель заметок
        sp_scrollNote.setFitToWidth(true);                              //разрешаем панели заметок растягиваться на всю ширину окошка
        sp_scrollNote.setFitToHeight(true);                             //разрешаем панели заметок растягиваться на всю высоту окошка

        //компоновка основной сетки
        vb_main.setTop(hb_top_menu);
        vb_main.setLeft(vb_sidePanel);
        vb_main.setCenter(sp_scrollNote);                               //натянули скролл панель на главную сетку

        //сцена с основной сеткой
        Scene myScene = new Scene(vb_main, 1000,700);     //размер сцены в уменьшенном состоянии
        myScene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
        //myStage.setFullScreen(true);  //войти в режим "на весь экран"
        //myStage.setMaximized(true);
        myStage.setTitle("Заметки");
        myStage.setScene(myScene);
        myStage.show();

        //Создаем лейблы и кнопки
        lb_menu = new Label("M");
        lb_menu.getStyleClass().add("label_whiteTopMenu");
        lb_menu.setMinSize(30,30);

        lb_curTabName = new Label("Заметки");
        lb_curTabName.getStyleClass().add("label_yelTopMenu");
        lb_curTabName.setMinSize(70,30);

        btn_createNote = new Button("+");
        btn_createNote.getStyleClass().add("button_round");
        btn_createNote.setMinSize(20,20);


        lb_search = new Label("Поиск");
        lb_search.getStyleClass().add("label_whiteTopMenu");
        lb_search.setMinSize(70,30);

        lb_settings = new Label("Настройки");
        lb_settings.getStyleClass().add("label_whiteTopMenu");
        lb_settings.setMinSize(70,30);

        hb_left_menu.getChildren().addAll(lb_menu, lb_curTabName, btn_createNote);
        hb_left_menu.setAlignment(Pos.CENTER);
        hb_right_menu.getChildren().addAll(lb_search, lb_settings);
        hb_right_menu.setAlignment(Pos.CENTER);

        //создаем спейсер (распорку), чтобы вставить между элементами в боковое меню
        Pane spacer_sidePanel = new Pane();
        VBox.setVgrow(spacer_sidePanel,Priority.ALWAYS);  //говорим о том, что в приоретете растягивать именно пружину
        spacer_sidePanel.setMinSize(1,10);

        //создаем лейблы для бокоого меню
        lb_notes = new Label("Заметки");
        lb_notes.getStyleClass().add("label_sidePanel");
        lb_notes.setMinSize(120,30);

        lb_reminders = new Label("Напоминания");
        lb_reminders.getStyleClass().add("label_sidePanel");
        lb_reminders.setMinSize(120,30);

        lb_archive = new Label("Архив");
        lb_archive.getStyleClass().add("label_sidePanel");
        lb_archive.setMinSize(120,30);

        lb_basket = new Label("Корзина");
        lb_basket.getStyleClass().add("label_sidePanel");
        lb_basket.setMinSize(120,30);

        lb_createFolder = new Label("Создать папку");
        lb_createFolder.getStyleClass().add("label_sidePanel");
        lb_createFolder.setMinSize(120,30);

        //размещаем лейблы на боковой панели
        vb_sidePanel_top.getChildren().addAll(lb_notes,lb_reminders,lb_archive,lb_basket);
        vb_sidePanel_bottom.getChildren().addAll(lb_createFolder);
        vb_sidePanel_top.setAlignment(Pos.TOP_CENTER);
        vb_sidePanel_bottom.setAlignment(Pos.BOTTOM_CENTER);
        vb_sidePanel.getChildren().addAll(vb_sidePanel_top,spacer_sidePanel,vb_sidePanel_bottom);
        vb_sidePanel.setMaxWidth(0);
        vb_sidePanel.setMinWidth(0);

        //действия, которые выполняются по нажатию на элементы
        lb_menu.setOnMouseClicked(lb_menuClickEvent -> {
            //показываем боковое меню
            if (vb_sidePanel.getWidth()==0){
                vb_sidePanel.setMinWidth(120);
            }
            else{
            //скрываем боковое меню
                vb_sidePanel.setMaxWidth(0);
                vb_sidePanel.setMinWidth(0);
            }
        });

        lb_notes.setOnMouseClicked(lb_notesClickEvent -> {
            lb_curTabName.setText(lb_notes.getText());
            btn_createNote.setVisible(true);
            fp_centerPaneRem.setVisible(false);
            sp_scrollNote.setContent(fp_centerPaneNote);
            fp_centerPaneNote.setVisible(true);
            btn_createNote.setOnAction(eh_createNote);  //назначили действие выполняемое по клику на кнопку
            currentPanelType=curPanelType.note.value;
        });

        lb_reminders.setOnMouseClicked(lb_remindersClickEvent -> {
            lb_curTabName.setText(lb_reminders.getText());
            btn_createNote.setVisible(true);
            fp_centerPaneNote.setVisible(false);
            sp_scrollNote.setContent(fp_centerPaneRem);
            fp_centerPaneRem.setVisible(true);
            btn_createNote.setOnAction(eh_createReminder);  //назначили действие выполняемое по клику на кнопку
            currentPanelType=curPanelType.reminder.value;
        });

        lb_archive.setOnMouseClicked(lb_archiveClickEvent -> {
            lb_curTabName.setText(lb_archive.getText());
            btn_createNote.setVisible(false);
        });

        lb_basket.setOnMouseClicked(lb_basketClickEvent -> {
            lb_curTabName.setText(lb_basket.getText());
            btn_createNote.setVisible(false);
        });

        lb_createFolder.setOnMouseClicked(lb_createFolderClickEvent -> {
            btn_createNote.setVisible(false);
        });

        btn_createNote.setOnAction(eh_createNote);  //назначили действие выполняемое по клику на кнопку
    }
    //действие выполняемое по клику на кнопку Создание заметки
    private EventHandler<ActionEvent> eh_createNote = btn_createNoteEvent -> {
        VBox vb_noteBlock = new VBox();                         //основная сетка заметки
        HBox hb_name_close_NB = new HBox();                     //сетка для заголовка и кнопки удаления
        VBox vb_noteArea_NB = new VBox();                       //сетка для размещения поля ввода зметки
        vb_noteArea_NB.setPrefSize(245,250);
        vb_listArea_NB = new VBox();                       //сетка для размещения списка заметок
        vb_listArea_NB.setPrefSize(245,250);
        vb_listArea_NB.setSpacing(2);
        HBox hb_listItems = new HBox();                         //сетка для укладки элементов списка заметки в один ряд
        hb_listItems.setPadding(new Insets(5));
        hb_listItems.setSpacing(2);
        hb_listItems.setAlignment(Pos.CENTER);

        //скрол для панели заметок
        ScrollPane sp_scrollNoteListArea = new ScrollPane(vb_noteArea_NB);       //создали скролл и добавили панель со списком заметком
        sp_scrollNoteListArea.setFitToWidth(true);                              //разрешаем панели заметок растягиваться на всю ширину окошка
        sp_scrollNoteListArea.setFitToHeight(true);                             //разрешаем панели заметок растягиваться на всю высоту окошка

        TextField tf_name_NB = new TextField("Заголовок...");
        tf_name_NB.setMinSize(174,20);
        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустой строки
        //в тот же момент времени, чтобы при нажатии на поле удалялся стандартный заголовок
        tf_name_NB.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            if (!newValue){                                         //если ушел фокус, то
                if (tf_name_NB.getText().equals(""))                //проверяем на наличе тек текста
                    tf_name_NB.setText("Заголовок...");             //устанавливаем стандартное
            }
            else{                                                   //если фокус пришел на поле
                if (tf_name_NB.getText().equals("Заголовок..."))    //проверяем тек текст равность стандартному
                    tf_name_NB.setText("");                         //удаляем его
            }
        });

        //Кнопка создания пункта списка заметок
//        Button btn_addListItem = new Button("+");
//        btn_addListItem.setPrefSize(25,20);
//        btn_addListItem.setTextAlignment(TextAlignment.CENTER);

        Button btn_noteList_NB = new Button("L");                   //кнопка переключения с Заметки на Список и обратно
        btn_noteList_NB.setPrefSize(25,20);
        btn_noteList_NB.setTextAlignment(TextAlignment.CENTER);

        Button btn_arch_NB = new Button("A");                       //кнопка архивирования заметки
        btn_arch_NB.setPrefSize(25,20);
        btn_arch_NB.setTextAlignment(TextAlignment.CENTER);

        Button btn_close_NB = new Button("X");                      //кнопка удаления заметки в корзину
        btn_close_NB.setPrefSize(25, 20);
        btn_close_NB.setTextAlignment(TextAlignment.CENTER);

        TextArea ta_text_NB = new TextArea("Текст заметки...");
        ta_text_NB.setPrefSize(245,250);
        ta_text_NB.setWrapText(true);
        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустого поля
        //в тот же момент времени, чтобы при нажатии на поле удалялась стандартная запись
        ta_text_NB.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            if (!newValue){                                             //если ушел фокус, то
                if (ta_text_NB.getText().equals(""))                    //проверяем на наличе тек текста
                    ta_text_NB.setText("Текст заметки...");             //устанавливаем стандартное
            }
            else{                                                       //если фокус пришел на поле
                if (ta_text_NB.getText().equals("Текст заметки..."))    //проверяем тек текст равность стандартному
                    ta_text_NB.setText("");                             //удаляем его
            }
        });

        //элементы для списка заметок
        CheckBox chb_listItem = new CheckBox();                     //чекбокс для того, чтобы отметить завершенные пункты заметки
        chb_listItem.getStyleClass().add("ch_listItem");

        ta_listItem = new TextArea("Новый пункт...");               //поля ввода пункта заметки
        ta_listItem.setWrapText(true);                              //разрешаем переносить
        ta_listItem.getStyleClass().add("ta_listItem");
        ta_listItem.setPrefRowCount(1);                             //заметки не шире, чем 1 строка

        btn_closeListItem = new Button("X");                    //кнопка удаления пункта заметки
        btn_closeListItem.setPrefSize(23,23);

        hb_listItems.getChildren().addAll(chb_listItem,ta_listItem,btn_closeListItem);
        vb_listArea_NB.getChildren().add(hb_listItems);
        taList.add(ta_listItem);
        noteList.add(hb_listItems);

        hb_name_close_NB.getChildren().addAll(tf_name_NB, btn_noteList_NB, btn_arch_NB, btn_close_NB);
        vb_noteArea_NB.getChildren().add(ta_text_NB);
        vb_noteBlock.getChildren().addAll(hb_name_close_NB, sp_scrollNoteListArea);
        fp_centerPaneNote.getChildren().add(vb_noteBlock);

        noteBlock.add(vb_noteBlock);        //каждую новую заметку добавляем в ArrayList

        //действия кнопки удаления пункта из списка заметок
        btn_closeListItem.setOnMouseClicked(event -> {
            for (int i=0; i<noteList.size(); i++){                      //пробегаем по массиву из пунктов списка заметок
                if (btn_closeListItem.getParent()==noteList.get(i)){    //если родитель кнопки (т.е. HBox) есть в этом списке
                    if (i<noteList.size()-1) {                          //и он не равен последнему
                        vb_listArea_NB.getChildren().remove(btn_closeListItem.getParent()); //удалить HBox (заметку), на котором расположена кнопка
                        taList.remove(i);
                        noteList.remove(i);
                        if (taList.size()==0){
                            System.out.println("monthsList");
                        }
                    }
                }
            }
        });

        //действие при нажатии на кнопку Закрыть (удалить в корзину) заметку
        btn_close_NB.setOnMouseClicked(btn_close_NB_clickEvent -> {
            //--------записать в файл заметку, восстановить еге в корзине---------
            fp_centerPaneNote.getChildren().remove(btn_close_NB.getParent().getParent()); //удалить VBox (заметку), на котором расположена кнопка
            noteBlock.remove(btn_close_NB.getParent().getParent());    //удалить VBox (заметку), на котором расположена кнопка из ArrayList
        });

        //действие при нажатия кнопки смены заметки на список
        btn_noteList_NB.setOnMouseClicked(btn_noteList_NB_clickEvent -> {
            if (btn_noteList_NB.getText().equals("L")){     //сейчас режим заметок
                btn_noteList_NB.setText("N");
                sp_scrollNoteListArea.setContent(vb_listArea_NB);
                //hb_name_close_NB.getChildren().add(1, btn_addListItem);
                //tf_name_NB.setMinWidth(145);
            }
            else{                                           //сейчас режим списка
                btn_noteList_NB.setText("L");
                sp_scrollNoteListArea.setContent(vb_noteArea_NB);
                //hb_name_close_NB.getChildren().remove(1);
                //tf_name_NB.setMinWidth(174);
            }
        });

        //действие при нажатии галочку
        chb_listItem.setOnMouseClicked(event -> {
            if (chb_listItem.isSelected()) {
                for (int i = 0; i < noteList.size(); i++) {                      //пробегаем по массиву из пунктов списка заметок
                    if (chb_listItem.getParent().equals(taList.get(i).getParent())) {
                        taList.get(i).setStyle("-fx-text-fill: red; -fx-font-style: italic");
                    }
                }
            }
            else{
                for (int i = 0; i < noteList.size(); i++) {                      //пробегаем по массиву из пунктов списка заметок
                    if (chb_listItem.getParent().equals(taList.get(i).getParent())) {
                        taList.get(i).setStyle("-fx-text-fill: black; -fx-font-style: normal");
                    }
                }
            }
        });

        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустого поля
        //в тот же момент времени, чтобы при нажатии на поле удалялась стандартная запись
        ta_listItem.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            ta_listItem.setOnMouseClicked(event -> {                                         //создали действие по нажатию на поле, чтобы определить индекс поля
                if (newValue){                                                               //если фокус пришел на поле
                    if (((TextArea) event.getSource()).getText().equals("Новый пункт...")) { //проверяем тек текст на равность стандартному
                        ((TextArea) event.getSource()).setText("");                          //если равно, то обнуляем
                    }
                    System.out.println("--> " + event.getSource());
                    boolean a = false;                                                       //флаг, чтобы понимать имеется ли поле в списке после его прохождения
                    for (int i = 0; i < taList.size() - 1; i++) {                            //идем по списку полей
                        System.out.println(taList.get(i) + " " + noteList.get(i));
                        if (event.getSource().equals(taList.get(i))) {                       //проверяем нажатое поле на существование его уже в списке полей
                            a = true;                                                        //нажато на существующее в списке поле
                            break;
                        }
                    }
                    if (!a) {                                                                //если до конца списка, так ничего и не было найдено
                        newListItem();                                                       //то надо создать новый пункт списка
                    }
                    for (int i = 0; i < taList.size(); i++) {
                        System.out.println("!!! " + taList.get(i) + " " + noteList.get(i));
                    }
                    System.out.println("-----");
                }
            });
            if (!newValue) {                                                                 //после того, как фокус ушел из поля
                for (int i = 0; i < taList.size(); i++) {
                    System.out.println("$$$ " + taList.get(i) + " " + noteList.get(i));
                }
                System.out.println(taList.get(taList.size()-2).getText());
                System.out.println(taList.size());
                if (taList.get(taList.size()-2).getText().equals("")) {                      //проверяем 2 поле с конца списка (т.к. последнее "Новый пункт") на пустоту
                    taList.remove(taList.size()-2);                                    //удаляем поле из списка полей, тк оно пустое
                    noteList.remove(noteList.size()-2);                                //удаляем HBox поля из списка HBoxов
                    vb_listArea_NB.getChildren().remove(taList.size()-1);              //удаляем поле из отображения на экране
                }
                taList.get(taList.size()-1).getParent().setDisable(false);                    //разблокируем поле с новым пунктом
            }

        });
    };

    //создание нового пунка списка заметок
    private void newListItem() {
        //элементы для списка заметок
        HBox hb_listItems = new HBox();                             //сетка для укладки элементов списка заметки в один ряд
        hb_listItems.setPadding(new Insets(5));
        hb_listItems.setSpacing(2);
        hb_listItems.setAlignment(Pos.CENTER);

        CheckBox chb_listItem = new CheckBox();                     //чекбокс для того, чтобы отметить завершенные пункты заметки
        chb_listItem.getStyleClass().add("ch_listItem");

        ta_listItem = new TextArea("Новый пункт...");               //поля ввода пункта заметки
        ta_listItem.setWrapText(true);                              //разрешаем переносить
        ta_listItem.getStyleClass().add("ta_listItem");
        ta_listItem.setPrefRowCount(1);                             //заметки не шире, чем 1 строка

        Button btn_closeListItem = new Button("X");            //кнопка удаления пункта заметки
        btn_closeListItem.setPrefSize(23, 23);

        //создаем спейсер (распорку), чтобы вставить между элементами в боковое меню
        /*Pane spacer_sidePan = new Pane();
        VBox.setVgrow(spacer_sidePan,Priority.ALWAYS);  //говорим о том, что в приоретете растягивать именно пружину
        spacer_sidePan.setMinSize(1,10);
        */

        hb_listItems.getChildren().addAll(chb_listItem, ta_listItem, btn_closeListItem);
        ta_listItem.getParent().setDisable(true);                   //блокируем поле с новым пунктом, чтобы на него нельзя было нажать преждевременно

        vb_listArea_NB.getChildren().addAll(hb_listItems);


        //добалвяем элементы в списки
        taList.add(ta_listItem);                                    //поле в список полей
        noteList.add(hb_listItems);                                 //HBox в список HBoxов

        //действие при нажатии галочку
        chb_listItem.setOnMouseClicked(event -> {
            if (chb_listItem.isSelected()) {
                for (int i = 0; i < noteList.size(); i++) {                      //пробегаем по массиву из пунктов списка заметок
                    if (chb_listItem.getParent().equals(taList.get(i).getParent())) {
                        taList.get(i).setStyle("-fx-text-fill: red; -fx-font-style: italic");
                    }
                }
            }
            else{
                for (int i = 0; i < noteList.size(); i++) {                      //пробегаем по массиву из пунктов списка заметок
                    if (chb_listItem.getParent().equals(taList.get(i).getParent())) {
                        taList.get(i).setStyle("-fx-text-fill: black; -fx-font-style: normal");
                    }
                }
            }

        });

        //действие кнопки удаления пункта из списка заметок
        btn_closeListItem.setOnMouseClicked(event -> {
            for (int i = 0; i < taList.size(); i++) {
                if (btn_closeListItem.getParent() == taList.get(i).getParent()) {
                    if (i < taList.size() - 1) {
                        vb_listArea_NB.getChildren().remove(btn_closeListItem.getParent()); //удалить HBox (заметку), на котором расположена кнопка
                        taList.remove(i);
                        noteList.remove(i);
                        if (taList.size() == 0) {
                            System.out.println("monthsList");
                        }
                    }
                }
            }
        });

        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустого поля
        //в тот же момент времени, чтобы при нажатии на поле удалялась стандартная запись
        ta_listItem.focusedProperty().addListener((observable, oldValue, newValue) -> {         //установили слушатель фокуса
            ta_listItem.setOnMouseClicked(event -> {                                            //создали действие по нажатию на поле, чтобы определить индекс поля
                if (newValue) {                                                                 //если фокус пришел на поле
                    if (((TextArea) event.getSource()).getText().equals("Новый пункт...")) {    //проверяем тек текст равность стандартному
                        ((TextArea) event.getSource()).setText("");                             //если равно, то обнуляем
                    }
                    System.out.println("--> " + event.getSource());
                    boolean a = false;                                                          //флаг, чтобы понимать имеется ли поле в списке после его прохождения
                    int x;
                    for (int i = 0; i < taList.size() - 1; i++) {                               //идем по списку полей
                        System.out.println(taList.get(i) + " " + noteList.get(i));
                        if (event.getSource().equals(taList.get(i))) {                          //проверяем нажатое поле на существование его уже в списке полей
                            a = true;                                                           //нажато на существующее в списке поле
                            break;
                        }
                        System.out.println(taList.get(i).getParent());
                        System.out.println(taList.get(i).getParent().getParent());
                        for (int j=0;j<noteBlock.size();j++){
                            if (taList.get(i).getParent().getParent().getParent().getParent().getParent().getParent().equals(noteBlock.get(j)))
                                System.out.println("asasasda  "+j);
                        }
                    }
                    if (!a) {                                                                   //если до конца списка, так ничего и не было найдено
                        newListItem();                                                          //то надо создать новый пункт списка
                    }
                    for (int i = 0; i < taList.size(); i++) {
                        System.out.println("!!! " + taList.get(i) + " " + noteList.get(i));
                    }
                    System.out.println("-----");
                }
            });
            if (!newValue){                                                                     //после того, как фокус ушел из поля
                for (int i=0;i<taList.size();i++){
                    System.out.println("$$$ "+taList.get(i)+" "+noteList.get(i));
                }
                System.out.println(taList.get(taList.size()-2).getText());
                System.out.println(taList.size());
                if (taList.get(taList.size()-2).getText().equals("")) {                         //проверяем 2 поле с конца списка (т.к. последнее "Новый пункт") на пустоту
                   taList.remove(taList.size()-2);                                        //удаляем поле из списка полей, тк оно пустое
                   noteList.remove(noteList.size()-2);                                    //удаляем HBox поля из списка HBoxов
                   vb_listArea_NB.getChildren().remove(taList.size()-1);                  //удаляем поле из отображения на экране
                }
                taList.get(taList.size()-1).getParent().setDisable(false);                      //разблокируем поле с новым пунктом
           }
        });
    }

    private EventHandler<ActionEvent> eh_createReminder = btn_createRemEvent -> {
        VBox vb_remindBlock = new VBox();                         //основная сетка заметки
        HBox hb_name_close_RB = new HBox();                       //сетка для заголовка и кнопки удаления
        HBox hb_date = new HBox();                                //сетка для даты
        HBox hb_btnRem = new HBox();                              //сетка для кнопок напоминания

        TextField tf_name_RB = new TextField("Заголовок...");
        tf_name_RB.setPrefSize(175,20);
        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустой строки
        //в тот же момент времени, чтобы при нажатии на поле удалялся стандартный заголовок
        tf_name_RB.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            if (!newValue){                                         //если ушел фокус, то
                if (tf_name_RB.getText().equals(""))                //проверяем на наличе тек текста
                    tf_name_RB.setText("Заголовок...");             //устанавливаем стандартное
            }
            else{                                                   //если фокус пришел на поле
                if (tf_name_RB.getText().equals("Заголовок..."))    //проверяем тек текст равность стандартному
                    tf_name_RB.setText("");                         //удаляем его
            }
        });

        //Кнопки
        Button btn_arch_RB = new Button("A");                  //кнопка архивирования заметки
        btn_arch_RB.setPrefSize(20,20);
        btn_arch_RB.setTextAlignment(TextAlignment.CENTER);

        Button btn_close_RB = new Button("X");                  //кнопка удаления заметки в корзину
        btn_close_RB.setPrefSize(20, 20);
        btn_close_RB.setTextAlignment(TextAlignment.CENTER);

        //Установка даты
        //Комбобокс с месяцами
        ComboBox cb_months = new ComboBox();
        monthsList = new ArrayList();
        monthsList.add(0,"Январь");
        monthsList.add(1,"Февраль");
        monthsList.add(2,"Март");
        monthsList.add(3,"Апрель");
        monthsList.add(4,"Май");
        monthsList.add(5,"Июнь");
        monthsList.add(6,"Июль");
        monthsList.add(7,"Август");
        monthsList.add(8,"Сентябрь");
        monthsList.add(9,"Октябрь");
        monthsList.add(10,"Ноябрь");
        monthsList.add(11,"Декабрь");

        cb_months.getItems().addAll(monthsList);

        cb_months.setValue("Месяц");
        cb_months.setMinWidth(95);
        cb_months.setMaxWidth(95);
        cb_months.getStyleClass().add("reminder_dis");

        //Комбобокс с годами
        ComboBox cb_year = new ComboBox();
        cb_year.getItems().addAll("2021","2022","2023");
        cb_year.setValue("Год");
        cb_year.setMinWidth(80);
        cb_year.setMaxWidth(90);
        cb_year.getStyleClass().add("reminder_dis");

        //Поле для ввода дня
        TextField tf_day = new TextField();
        tf_day.setText("День");
        tf_day.setMinSize(48,27);
        tf_day.setMaxSize(48,27);
        tf_day.getStyleClass().add("reminder_dis");

        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустой строки
        //в тот же момент времени, чтобы при нажатии на поле удалялся стандартный заголовок
        tf_day.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            if (!newValue){                                         //если ушел фокус, то
                if (tf_day.getText().equals(""))               //проверяем на наличе тек текста
                    tf_day.setText("День");             //устанавливаем стандартное
                //Смотрим выбранный месяц в комбобоксе, чтобы установить валидатор
                if (cb_months.getValue().equals("Январь")||cb_months.getValue().equals("Март")||cb_months.getValue().equals("Май")||
                        cb_months.getValue().equals("Июль")||cb_months.getValue().equals("Август")||cb_months.getValue().equals("Октябрь")||cb_months.getValue().equals("Декабрь")) {
                    //Устанавливаем правила ввода даты в поле (валидатор)
                    if (!tf_day.getText().matches("0?[1-9]|[12][0-9]|3[01]"))
                        tf_day.setText("День");
                }
                else if (cb_months.getValue().equals("Февраль")){
                    if (!cb_year.getValue().equals("Год")) {
                        //Определение високосного года
                        int curYear;
                        curYear = Integer.parseInt((String) cb_year.getValue());
                        if (curYear % 4 != 0 || curYear % 100 == 0 || curYear % 400 != 0) {
                            if (!tf_day.getText().matches("0?[1-9]|[12][0-8]"))     //обыный
                                tf_day.setText("День");
                        }
                    }
                    else{
                        if (!tf_day.getText().matches("0?[1-9]|[12][0-9]"))     //високосный
                            tf_day.setText("День");
                    }
                }
                else{
                    if (!tf_day.getText().matches("0?[1-9]|[12][0-9]|3[0]"))
                        tf_day.setText("День");
                }

            }
            else{                                                   //если фокус пришел на поле
                if (tf_day.getText().equals("День"))    //проверяем тек текст равность стандартному
                    tf_day.setText("");                         //удаляем его
            }
        });

        //При изменении месяца поправляем дату на корректную
        cb_months.setOnAction(event -> {
            for (int i = 0; i< monthsList.size(); i++){
                if (cb_months.getValue().equals(monthsList.get(i))){
                    System.out.println(i);
                }
            }
            if (cb_months.getValue().equals("Апрель") || cb_months.getValue().equals("Июнь") ||
                    cb_months.getValue().equals("Сентябрь") || cb_months.getValue().equals("Ноябрь")) {
                if (tf_day.getText().equals("31"))
                    tf_day.setText("30");
            }
            if (cb_months.getValue().equals("Февраль")){
                if ((tf_day.getText().equals("29"))||(tf_day.getText().equals("30"))||(tf_day.getText().equals("31")))
                    tf_day.setText("28");
            }
        });


        //При изменении месяца поправляем дату на корректную
        cb_year.setOnAction(event -> {
            //Определение високосного года
            int curYear;
            curYear = Integer.parseInt((String) cb_year.getValue());
            if (curYear%4!=0 || curYear%100==0 || curYear%400!=0){
                if (cb_months.getValue().equals("Февраль")){            //обычный
                    if ((tf_day.getText().equals("29"))||(tf_day.getText().equals("30"))||(tf_day.getText().equals("31")))
                        tf_day.setText("28");
                }
            }
        });

        TextArea ta_text_rem = new TextArea("Текст напоминания...");
        ta_text_rem.setPrefSize(220,70);
        ta_text_rem.setWrapText(true);
        ta_text_rem.getStyleClass().add("reminder_dis");

        Button btn_saveRem = new Button("Сохранить");                  //кнопка архивирования заметки
        btn_saveRem.setMinSize(112,25);
        btn_saveRem.setTextAlignment(TextAlignment.CENTER);
        btn_saveRem.getStyleClass().add("reminder_dis");

        Button btn_changeRem = new Button("Изменить");                  //кнопка архивирования заметки
        btn_changeRem.setPrefSize(112,25);
        btn_changeRem.setTextAlignment(TextAlignment.CENTER);
        btn_changeRem.getStyleClass().add("reminder_dis");
        btn_changeRem.setDisable(true);

        //делаем так, чтобы в момент смены фокуса с текстового поля не оставалось пустого поля
        //в тот же момент времени, чтобы при нажатии на поле удалялась стандартная запись
        ta_text_rem.focusedProperty().addListener((observable, oldValue, newValue) -> {      //установили слушатель фокуса
            if (!newValue){                                         //если ушел фокус, то
                if (ta_text_rem.getText().equals(""))                //проверяем на наличе тек текста
                    ta_text_rem.setText("Текст напоминания...");             //устанавливаем стандартное
            }
            else{                                                   //если фокус пришел на поле
                if (ta_text_rem.getText().equals("Текст напоминания..."))    //проверяем тек текст равность стандартному
                    ta_text_rem.setText("");                         //удаляем его
            }
        });

        hb_name_close_RB.getChildren().addAll(tf_name_RB, btn_arch_RB, btn_close_RB);
        hb_date.getChildren().addAll(cb_months,tf_day,cb_year);
        hb_btnRem.getChildren().addAll(btn_saveRem,btn_changeRem);
        vb_remindBlock.getChildren().addAll(hb_name_close_RB,hb_date,ta_text_rem,hb_btnRem);
        fp_centerPaneRem.getChildren().add(vb_remindBlock);

        reminderBlock.add(vb_remindBlock);        //каждую новую заметку добавляем в ArrayList

        //действие при нажатии на кнопку Закрыть (удалить в корзину) заметку
        btn_close_RB.setOnMouseClicked(btn_close_RB_clickEvent -> {
            //--------записать в файл заметку, восстановить еге в корзине---------
            fp_centerPaneRem.getChildren().remove(btn_close_RB.getParent().getParent()); //удалить VBox (заметку), на котором расположена кнопка
            reminderBlock.remove(btn_close_RB.getParent().getParent());    //удалить VBox (заметку), на котором расположена кнопка из ArrayList
        });

        btn_saveRem.setOnAction(btn_saveRem_clickEvent -> {
            if ((!cb_months.getValue().equals("Месяц"))&&(!tf_day.getText().equals("День"))&&(!cb_year.getValue().equals("Год"))) {
                cb_months.setDisable(true);
                tf_day.setDisable(true);
                cb_year.setDisable(true);
                ta_text_rem.setDisable(true);
                btn_saveRem.setDisable(true);
                btn_changeRem.setDisable(false);
            }
        });

        btn_changeRem.setOnAction(btn_changeRem_clickEvent -> {
            cb_months.setDisable(false);
            tf_day.setDisable(false);
            cb_year.setDisable(false);
            ta_text_rem.setDisable(false);
            btn_changeRem.setDisable(true);
            btn_saveRem.setDisable(false);
        });
    };
}
