/**
 * 学号：20211003207   姓名：张景深  班级：软工2101
 *
 *该源代码给出了程序的整体结构，大家补充完成功能。

 *本程序的需要实现的核心功能是功能1和功能2。
 *完成功能1可获得65分，后续功能根据完成度及复杂度相应给分。
 *出现完全雷同的程序，均为0分，除非其中一人能提出自证清白的证据。
 *你可以在网络上学习相关知识，获得或改进相关代码片段来完成自己需要的功能，但程序还是要在本源代码框架下完成，如果是提交网络找来的，完全无关的数独程序源代码，程序也视为无效，作为未完成大作业处理。

 * 以下各个功能完成了的，将后面的“未完成”字样删除。

 * 已完成
 * 1. 在Sudoku类中实现数独的核心相关算法，可通过控制台方式测试功能，在测试无误的基础上再完成后续功能（如果觉得无法完成图形版本，可以提交一个SudoKu.java的源文件，当作控制台版本，不过只能获得对应的65分）。

 * 已完成
 * 2. 使用JavaFx将数独程序图形化，完成基本的数独功能
 *        提供类似九宫格界面用于数独数字的输入和显示（提示：可以在GridPane的单元格中嵌入TextField，用于显示和输入数字）
 *        提供两个按钮用来求解及清空界面。求解成功就将对应的数字填写到九宫格界面中，清空就是将界面所有数字清空。
 *        在程序的合适的位置，提供信息栏，用于显示程序求解的相关信息，例如显示“求解完成”，“输入有误”，“无解”或其它自己认为有需要的信息。


 * 扩展功能部分
 * 已完成
 * 3. 提供一个检验功能，对于用户自己输入的求解方案，验证是否正确，验证信息可以显示在上面功能中的信息栏中。
 *
 * 已完成
 * 4. 提供打开和保存功能。具体来说，就是可以通过保存按钮，打开一个文件保存对话框，将当前九宫格界面的数字状态保存到一个文本存档文件；
 *    通过打开按钮，打开一个文件对话框，用于加载保存的存档文件。（提示：可以按照二维矩阵的样式将九宫格中的数字保存到文本文件，没有填写数字的
 *    空白部分就设置为0。（提示，关于文件对话框的知识，见慕课的第十四讲：JavaFx中其它的知识点-》JavaFX的对话框）

 * 已完成
 * 5. 在程序的合适位置显示时间信息，当程序打开时就开始计时，显示用户使用数独程序的时长，注意，更新GUI中的显示信息，也许需要用到Platform.runLater方法

 * 已完成
 * 6. 提供背景音乐功能，允许用户开启和关闭背景音乐，注意，为了性能，使用在新线程中播放音乐的方式。（提示，关于播放音乐的知识，见慕课的第十四讲：JavaFx中其它的知识点-》JavaFX的对话框）。

 * 7. 难度自选择。有easy、middle和difficult三种难度模式供用户选择
 *
 * 8. 答案查阅。用户可以点击answer按钮查看答案的同时不影响自己作答的答案，且答案查阅自带check功能
 *
 * 9. 时间计时提供选择重置或者停止等功能
 *
 * 10. 记录用户的通过答题数
 *

 * */

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import netscape.javascript.JSObject;
import sun.java2d.Spans;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class SudokuFx extends Application {
    Button buttonExit = new Button("Save");
    Button buttonMusic;
    Button buttonOpen = new Button("Open");
    public Media media;
    public MediaPlayer mediaPlayer;
    public File file;
    public boolean OnorOff = true;
    private Stage stage;
    private boolean saveYesNo = true;
    private boolean haveSaved = false;
    private String saveFileName;
    SudoKu sudoKu;
    MenuPane menuPane;
    BottomPane bottomPane;
    RightPand rightPand;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        sudoKu = new SudoKu();
        menuPane = new MenuPane(sudoKu, primaryStage);
        bottomPane = new BottomPane();
        rightPand = new RightPand(sudoKu, menuPane, bottomPane);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuPane);
        borderPane.setCenter(sudoKu);
        borderPane.setRight(rightPand);
        borderPane.setBottom(bottomPane);

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Author: 小张同学你好ku~");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        buttonExit.setPrefHeight(50);
        buttonExit.setPrefWidth(70);
        buttonExit.setFont(Font.font("方正粗黑宋简体", 14));
        buttonExit.setOnAction(event -> {
            exit(primaryStage, menuPane, rightPand, sudoKu, bottomPane);
        });
        menuPane.hBox.getChildren().add(buttonExit);

        buttonMusic = new Button("Music");
        buttonMusic.setPrefWidth(60);
        buttonMusic.setPrefHeight(50);
        buttonMusic.setFont(Font.font("方正粗黑宋简体", 13));

        file = new File(System.getProperty("user.dir") + "/SudoMusic.mp4");
        if(file.exists()) {
            Runnable music = new BackgroundMusic();
            Thread thread = new Thread(music);
            thread.start();
            buttonMusic.setOnAction(event -> {
                if (OnorOff == true) {
                    mediaPlayer.stop();
                    OnorOff = false;
                    buttonMusic.setStyle(null);
                } else {
                    mediaPlayer.play();
                    OnorOff = true;
                    buttonMusic.setStyle("-fx-base: rgb(203,193,186);");
                }
            });
            buttonMusic.setStyle("-fx-base: rgb(203,193,186);");
        } else System.out.println("Sorry, Can't find the background music");

        menuPane.hBox.getChildren().add(buttonMusic);

        buttonOpen.setPrefWidth(60);
        buttonOpen.setPrefHeight(50);
        buttonOpen.setFont(Font.font("方正粗黑宋简体", 13));
        buttonOpen.setOnAction(event -> {
            try {
                openGame();
            } catch (Exception e) {

            }
        });
        menuPane.hBox.getChildren().add(buttonOpen);

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            finalSave();
        });

    }
    void finalSave() {
        Alert alertfinal = new Alert(Alert.AlertType.NONE);
        alertfinal.setTitle("See You Next Time");
        alertfinal.setHeaderText(null);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alertfinal.getButtonTypes().addAll(buttonTypeYes, buttonTypeNo);
        alertfinal.setContentText("Do you want to save the current record ? ");
        alertfinal.showAndWait();
        if(alertfinal.getResult() == buttonTypeYes) {
            if(haveSaved == false) {
                saveFileName = getFileName();
                saveFileName += "_SudoKuUsr.txt";
            }
            if(saveYesNo == false) stage.close();
            else {
                rightPand.stop();
                mediaPlayer.stop();
                File file = new File(saveFileName);
                String saveTime = rightPand.labelrealTime.getText();
                int saveSolveNum = rightPand.solveTime;
                int saveModern = menuPane.flight;
                int[][] savesudoNums = new int[10][10];
                int[][] originalNums = new int[10][10];
                int[][] answerSudo = new int[10][10];
                for(int i = 1; i <= 9; i++) {
                    for(int j = 1; j <= 9; j++) {
                        if(sudoKu.buttons[i][j].getText() == "")savesudoNums[i][j] = 0;
                        else savesudoNums[i][j] = Integer.parseInt(sudoKu.buttons[i][j].getText());
                        originalNums[i][j] = sudoKu.original[i][j];
                        answerSudo[i][j] = sudoKu.nums[i][j];
                    }
                }
                try{
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.print(saveTime + "\n");
                    printWriter.print(saveModern + "\n");
                    printWriter.print(saveSolveNum + "\n");
                    for(int i = 1; i <= 9; i++) {
                        for(int j = 1; j <= 9; j++) {
                            printWriter.print(savesudoNums[i][j] + " ");
                        }
                        printWriter.println();
                    }
                    for(int i = 1; i <= 9; i++) {
                        for(int j = 1; j <= 9; j++) {
                            printWriter.print(originalNums[i][j] + " ");
                        }
                        printWriter.println();
                    }
                    for(int i = 1; i <= 9; i++) {
                        for(int j = 1; j <= 9; j++) {
                            printWriter.print(answerSudo[i][j] + " ");
                        }
                        printWriter.println();
                    }
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("TIP");
                    alert1.setHeaderText(null);
                    alert1.setContentText("The current records you have saved in " + saveFileName);
                    alert1.showAndWait();
                    printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            stage.close();
        }
    }
    void openGame() throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Record file", "*_SudoKuUsr.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File findFile = fileChooser.showOpenDialog(stage);
        File file = new File(findFile.toString());
        if(file.exists()) {
            Scanner input = new Scanner(file, "utf-8");
            String realTime = input.next();

            int intTime = 0;
            intTime += Integer.parseInt(realTime.substring(4,5)) * 1;
            intTime += Integer.parseInt(realTime.substring(3,4)) * 10;
            intTime += Integer.parseInt(realTime.substring(1,2)) * 60;
            intTime += Integer.parseInt(realTime.substring(0,1)) * 600;

            int modern = input.nextInt();
            int solveNum = input.nextInt();
            int[][] buttomsNums = new int[10][10];
            int[][] originalNums = new int[10][10];
            int[][] answerSudo = new int[10][10];
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    buttomsNums[i][j] = input.nextInt();
                }
            }
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    originalNums[i][j] = input.nextInt();
                }
            }
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    answerSudo[i][j] = input.nextInt();
                }
            }
            rightPand.time = intTime;
            rightPand.labelrealTime.setText(rightPand.getFormattime(intTime));
            if(modern == 1)menuPane.setEasyPattern();
            else if (modern == 2) {
                menuPane.setMiddlePattern();
            }else menuPane.setDifficultPattern();
            rightPand.solveTime = solveNum;
            rightPand.labelsolved.setText("Solved:\t" + solveNum);
            bottomPane.inforlabel.setText(bottomPane.BackGame);
            sudoKu.BackSudoKu(buttomsNums, originalNums, answerSudo);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Tip");
            alert.setHeaderText("The file you saved called ");
        }
    }
    String getFileName() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Save Game Record");
        textInputDialog.setHeaderText("Please input a name for \nthe convenience of reopening next time");
        textInputDialog.setContentText("File Name");
        textInputDialog.showAndWait();
        String saveName = textInputDialog.getResult();
        if(saveName == null)saveYesNo = false;
        haveSaved = true;
        return saveName;
    }
    void exit(Stage primaryStage, MenuPane menuPane, RightPand rightPand, SudoKu sudoKu, BottomPane bottomPane) {
        if(haveSaved == false) {
            saveFileName = getFileName();
            saveFileName += "_SudoKuUsr.txt";
        }
        if(saveYesNo == false) return;
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Function");
        alert.setContentText("Would you like to save the current game record and exit or just save(￢‸￢) ?");
        alert.setHeaderText(null);
        ButtonType buttonTypeYes = new ButtonType("Save And Exit");
        ButtonType buttonTypeNot = new ButtonType("Just Save");
        alert.getButtonTypes().addAll(buttonTypeYes, buttonTypeNot);
        alert.showAndWait();
        File file = new File(saveFileName);
        if(alert.getResult() == buttonTypeYes || alert.getResult() == buttonTypeNot){
            rightPand.stop();
            mediaPlayer.stop();
            String saveTime = rightPand.labelrealTime.getText();
            int saveSolveNum = rightPand.solveTime;
            int saveModern = menuPane.flight;
            int[][] savesudoNums = new int[10][10];
            int[][] originalNums = new int[10][10];
            int[][] answerSudo = new int[10][10];
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    if(sudoKu.buttons[i][j].getText() == "")savesudoNums[i][j] = 0;
                    else savesudoNums[i][j] = Integer.parseInt(sudoKu.buttons[i][j].getText());
                    originalNums[i][j] = sudoKu.original[i][j];
                    answerSudo[i][j] = sudoKu.nums[i][j];
                }
            }
            try{
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.print(saveTime + "\n");
                printWriter.print(saveModern + "\n");
                printWriter.print(saveSolveNum + "\n");
                for(int i = 1; i <= 9; i++) {
                    for(int j = 1; j <= 9; j++) {
                        printWriter.print(savesudoNums[i][j] + " ");
                    }
                    printWriter.println();
                }
                for(int i = 1; i <= 9; i++) {
                    for(int j = 1; j <= 9; j++) {
                        printWriter.print(originalNums[i][j] + " ");
                    }
                    printWriter.println();
                }
                for(int i = 1; i <= 9; i++) {
                    for(int j = 1; j <= 9; j++) {
                        printWriter.print(answerSudo[i][j] + " ");
                    }
                    printWriter.println();
                }
                printWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } if(alert.getResult() == buttonTypeYes) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("TIP");
            alert1.setHeaderText(null);
            alert1.setContentText("The current records you have saved in " + saveFileName);
            alert1.showAndWait();
            primaryStage.close();
        } if(alert.getResult() == buttonTypeNot) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("TIP");
            alert1.setHeaderText(null);
            alert1.setContentText("The current records you have saved in " + saveFileName);
            alert1.showAndWait();
            rightPand.stop();
            if(OnorOff == true) mediaPlayer.play();
        }
    }
    class BackgroundMusic implements Runnable{
        public BackgroundMusic() {
//            file = new File(System.getProperty("user.dir") + "/SudoMusic.mp4");
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        public void run() {
            mediaPlayer.play();
        }
    }
}

/**
 * 封装数独的核心算法
 * */
class SudoKu extends GridPane {
    public Button[][] buttons = new Button[10][10];
    public int[][] nums = new int[10][10]; // 存放数独解
    public int[][] col = new int[10][10];
    public int[][] row = new int[10][10];
    public int[][] grid = new int[10][10];
    public int[][] original = new int[10][10]; // 存放数独初始形状
    public int[][] answerUse = new int[10][10];
    public int clozeNum = 0, modern = 1;
    public boolean flag = false, answer = false, checkUse = false;
    public CELL[] cells = new CELL[81];
    public void BackSudoKu(int[][] buttonsStatus, int[][] original, int[][] answerSudo) {
        this.clozeNum = 0;
        this.flag = false;
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                this.original[i][j] = original[i][j];
                Button tempButton = new Button();
                tempButton.setPrefWidth(60);
                tempButton.setPrefHeight(60);
                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                if(original[i][j] == 0){
                    if(buttonsStatus[i][j] == 0) tempButton.setText("");
                    else tempButton.setText("" + buttonsStatus[i][j]);
                    tempButton.setTextFill(new Color(0.3,0.54,1,0.9));
                    tempButton.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (tempButton.getText().equals("")) {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("1");
                            } else if (tempButton.getText().equals("9")) {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("");
                            } else {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("" + (Integer.parseInt(tempButton.getText()) + 1));
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (tempButton.getText().equals("")) {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("9");
                            } else if (tempButton.getText().equals("1")) {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("");
                            } else {
                                tempButton.setFont(Font.font("方正粗黑宋简体", 26));
                                tempButton.setText("" + (Integer.parseInt(tempButton.getText()) - 1));
                            }
                        }
                    });
                } else tempButton.setText("" + original[i][j]);
                buttons[i][j] = tempButton;
                add(buttons[i][j], i, j);

                nums[i][j] = answerSudo[i][j];
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6) continue;
            for(int j = 4; j <= 6; j++) {
                if(original[j][i] == 0) buttons[j][i].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:rgb(76, 137, 255)");
                else buttons[j][i].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6)continue;
            for(int j = 4; j <= 6; j++) {
                if(original[i][j] == 0)buttons[i][j].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:-fx-text-fill:rgb(76, 137, 255)");
                else buttons[i][j].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
//        for(int i = 1; i <= 9; i++) {
//            for(int j = 1; j <= 9; j++) {
//                if(original[i][j] == 0)buttons[i][j].setTextFill(new Color(0.23,0.45,0.78,0.9));
//            }
//        }
    }
    public void ReSudoKu(int modern) { // 数独初始化
        this.modern = modern;
        this.clozeNum = 0;
        this.flag = false;
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                nums[i][j] = 0;
                original[i][j] = 0;
                col[i][j] = 0;
                row[i][j] = 0;
                grid[i][j] = 0;
            }
        }
        getFreeCellList();
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                original[i][j] = nums[i][j];
            }
        }
        search(1); // 此处开始nums存放了数独答案
        int setNum = 0, x, y;
        if(modern == 1){ // diger
            while (setNum < 30){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        if(modern == 2){
            while (setNum < 20){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        if(modern == 3){
            while (setNum < 9){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                Button temp_button = new Button();
                temp_button.setPrefWidth(60);
                temp_button.setPrefHeight(60);
                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                if(original[i][j] == 0) {
                    temp_button.setText("");
                    temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                }
                else {
                    temp_button.setText("" + nums[i][j]);
                }
                if(original[i][j] == 0){
                    temp_button.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (temp_button.getText().equals("")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("1");
                            } else if (temp_button.getText().equals("9")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("");
                            } else {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("" + (Integer.parseInt(temp_button.getText()) + 1));
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (temp_button.getText().equals("")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("9");
                            } else if (temp_button.getText().equals("1")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("");
                            } else {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("" + (Integer.parseInt(temp_button.getText()) - 1));
                            }
                        }
                    });
                }
                buttons[i][j] = temp_button;
                add(buttons[i][j], i, j);
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6) continue;
            for(int j = 4; j <= 6; j++) {
                if(original[j][i] == 0) buttons[j][i].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:rgb(76, 137, 255)");
                else buttons[j][i].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6)continue;
            for(int j = 4; j <= 6; j++) {
                if(original[i][j] == 0)buttons[i][j].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:rgb(76, 137, 255)");
                else buttons[i][j].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
        boolean check = false;
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                if(nums[i][j] == 0) {
                    check = true;
                    break;
                }
            }
        }
        if(check == true) ReSudoKu(this.modern);
    }
    public SudoKu() { // 数独初始化
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5, 5, 5, 5));
        setHgap(2.0);
        setVgap(2.0);
        gridLinesVisibleProperty();
        getFreeCellList(); // 得到随机初始化数组种子
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                original[i][j] = nums[i][j];
            }
        }
        search(1); // 此处开始nums存放了数独答案
        int setNum = 0, x, y;
        if(modern == 1){
            while (setNum < 30){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        if(modern == 2){
            while (setNum < 20){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        if(modern == 3){
            while (setNum < 9){
                x = (int)(Math.random() * 9 + 1);
                y = (int)(Math.random() * 9 + 1);
                if(original[x][y] == 0) {
                    original[x][y] = nums[x][y];
                    setNum++;
                }
            }
        }
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                Button temp_button = new Button();
                temp_button.setPrefWidth(60);
                temp_button.setPrefHeight(60);
                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                if(original[i][j] == 0) {
                    temp_button.setText("");
                    temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                }
                else {
                    temp_button.setText("" + nums[i][j]);
                }
                if(original[i][j] == 0){
                    temp_button.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (temp_button.getText().equals("")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                                temp_button.setText("1");
                            } else if (temp_button.getText().equals("9")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                                temp_button.setText("");
                            } else {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("" + (Integer.parseInt(temp_button.getText()) + 1));
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (temp_button.getText().equals("")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("9");
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                            } else if (temp_button.getText().equals("1")) {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("");
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                            } else {
                                temp_button.setFont(Font.font("方正粗黑宋简体", 26));
                                temp_button.setText("" + (Integer.parseInt(temp_button.getText()) - 1));
                                temp_button.setTextFill(new Color(0.3,0.54,1,0.9));
                            }
                        }
                    });
                }
                buttons[i][j] = temp_button;
                add(buttons[i][j], i, j);
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6) continue;
            for(int j = 4; j <= 6; j++) {
                if(original[j][i] == 0) buttons[j][i].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:rgb(76, 137, 255)");
                else buttons[j][i].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(i >= 4 && i <= 6)continue;
            for(int j = 4; j <= 6; j++) {
                if(original[i][j] == 0)buttons[i][j].setStyle("-fx-base: rgb(203,193,186);-fx-text-fill:rgb(76, 137, 255)");
                else buttons[i][j].setStyle("-fx-base: rgb(203,193,186);");
            }
        }
        boolean check = false;
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                if(nums[i][j] == 0) {
                    check = true;
                    break;
                }
            }
        }
        if(check == true) ReSudoKu(this.modern);
//        for(int i = 1; i <= 9; i++) {
//            for(int j = 1; j <= 9; j++) {
//                if(original[i][j] == 0)buttons[i][j].setTextFill(new Color(0.23,0.45,0.78,0.9));
//            }
//        }
    }
    public void ResetSudo() {
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                if(original[i][j] == 0) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBorder(null);
                }
            }
        }
        answer = false;
    }
    public void getFreeCellList() {
        int tmp = 0;
        int x, y, num;
        while(tmp < 20) {
            x = (int)(Math.random() * 9 + 1);
            y = (int)(Math.random() * 9 + 1);
            num = (int)(Math.random() * 9 + 1);
            if(nums[x][y] == 0) {
                if(isValid(x, y, num) == true) {
                    nums[x][y] = num;
                    row[x][num] = 1;
                    col[y][num] = 1;
                    int t = (x-1)/3*3 + (y-1)/3 + 1;
                    grid[t][num] = 1;
                    tmp++;
                }
            }
        }
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                if(nums[i][j] == 0) {
                    clozeNum++;
                    cells[clozeNum] = new CELL();
                    cells[clozeNum].row = i;
                    cells[clozeNum].col = j;
                }
            }
        }
    }
    public boolean isValid(int x, int y, int num) {
        for(int i = (x-1)/3*3 + 1; i <= ((x-1)/3 + 1)*3; i++) { // 检验同个小九宫格是否有重复
            for(int j = ((y-1)/3)*3 + 1; j <= ((y-1)/3 + 1)*3; j++) {
                if(nums[i][j] == num) return false;
            }
        }
        for(int i = 1; i <= 9; i++) {
            if(nums[x][i] == num || nums[i][y] == num) return false; // 检验同行同列
        }
        return true;
    }
    public void search(int t) {
        if(flag == true) return;
        if(t > clozeNum) {
            System.out.println("This Sudo answer is:");
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    System.out.print(nums[j][i] + " ");
                }
                System.out.println();
            }
            System.out.println("--------------------\n");
            flag = true;
            return;
        }
        for(int i = 1; i <= 9; i++) {
            int x = cells[t].row;
            int y = cells[t].col;
            int m = (x - 1)/3 * 3 + (y - 1)/3 + 1;

            if(row[x][i] == 0 && col[y][i] == 0 && grid[m][i] == 0) {
                nums[x][y] = i;
                row[x][i] = 1;
                col[y][i] = 1;
                grid[m][i] = 1;
                search(t + 1);
                if(flag == true)return;
                row[x][i] = 0;
                col[y][i] = 0;
                grid[m][i] = 0;
            }
        }
    }
    public void answer() {
        if(answer == false) {
            check();
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    if(buttons[i][j].getText() == "")answerUse[i][j] = 0;
                    else answerUse[i][j] = Integer.parseInt(buttons[i][j].getText());
                }
            }
            for (int i = 1; i <= 9; i++) {
                for (int j = 1; j <= 9; j++) {
                    buttons[i][j].setText("" + nums[i][j]);
                }
            }
            answer = true;
            checkUse = false;
        }
        else{
            for (int i = 1; i <= 9; i++) {
                for (int j = 1; j <= 9; j++) {
                    buttons[i][j].setBorder(null);
                    if(answerUse[i][j] == 0) buttons[i][j].setText("");
                    else buttons[i][j].setText("" + answerUse[i][j]);
                }
            }
            answer = false;
        }
    }
    public void check(){
        if(checkUse == false) {
            for (int i = 1; i <= 9; i++) {
                for (int j = 1; j <= 9; j++) {
                    BorderStroke falseborderStroke = new BorderStroke(Paint.valueOf("#FF2433"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2));
                    BorderStroke trueborderStroke = new BorderStroke(Paint.valueOf("#24FF44"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2));
                    Border falseborder = new Border(falseborderStroke);
                    Border trueborder = new Border(trueborderStroke);
                    if (buttons[i][j].getText() == "") buttons[i][j].setBorder(falseborder);
                    else {
                        if (Integer.parseInt(buttons[i][j].getText()) == nums[i][j] && original[i][j] == 0)
                            buttons[i][j].setBorder(trueborder);
                        if (Integer.parseInt(buttons[i][j].getText()) != nums[i][j] && original[i][j] == 0)
                            buttons[i][j].setBorder(falseborder);
                    }
                }
            }
            checkUse = true;
        }
        else{
            for(int i = 1; i <= 9; i++) {
                for(int j = 1; j <= 9; j++) {
                    buttons[i][j].setBorder(null);
                }
            }
            checkUse = false;
        }
    }
    public int submit() {
        for(int i = 1; i <= 9; i++) {
            for(int j = 1; j <= 9; j++) {
                buttons[i][j].setBorder(null);
            }
        }
        int tag = 1;
        for(int i = 1; i <= 9; i++) {
            if(tag == 0) break;
            for(int j = 1; j <= 9; j++) {
                if(buttons[i][j].getText() == "" || Integer.parseInt(buttons[i][j].getText()) != nums[i][j]) {
                    tag = 0;
                    break;
                }
            }
        }
        return tag;
    }

}
class CELL{
    public int row, col;
    public CELL(){
    }
}
class MenuPane extends StackPane {
    Button buttonEasy = new Button("Easy");
    Button buttonMiddle = new Button("Middle");
    Button buttonDifficult = new Button("Difficult");
    Button buttonReset = new Button("Reset");
    Button buttonAnswer = new Button("Answer");
    Button buttonCheck = new Button("Check");
    Button buttonExit = new Button("Exit");
    public boolean answerUse = false;
    public boolean save = false;
    private Color activeColor = new Color(0.23,0.45,0.78,0.9);
    public Color unactiveColor = new Color(0,0,0,0.9);
    int flight = 1;
    public SudoKu sudoKu;
    public HBox hBox = new HBox();
    private Stage stage;
    public MenuPane(SudoKu sudoKu, Stage stage) {

        this.sudoKu = sudoKu;
        this.stage = stage;
        setAlignment(hBox, Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        setEasyPattern();

        buttonEasy.setPrefHeight(50);
        buttonEasy.setPrefWidth(80);
        buttonEasy.setFont(Font.font("方正粗黑宋简体", 14));
        buttonEasy.setOnAction(event -> {
            setEasyPattern();
        });

        buttonMiddle.setPrefHeight(50);
        buttonMiddle.setPrefWidth(80);
        buttonMiddle.setFont(Font.font("方正粗黑宋简体", 14));
        buttonMiddle.setOnAction(event -> {
            setMiddlePattern();
        });

        buttonDifficult.setPrefHeight(50);
        buttonDifficult.setPrefWidth(80);
        buttonDifficult.setFont(Font.font("方正粗黑宋简体", 14));
        buttonDifficult.setOnAction(event -> {
            setDifficultPattern();
        });

        buttonReset.setPrefHeight(50);
        buttonReset.setPrefWidth(80);
        buttonReset.setFont(Font.font("方正粗黑宋简体", 14));
        buttonReset.setOnAction(event -> {
            ResetSudo();
        });

        buttonAnswer.setPrefHeight(50);
        buttonAnswer.setPrefWidth(80);
        buttonAnswer.setFont(Font.font("方正粗黑宋简体", 14));
        buttonAnswer.setOnAction(event -> {
            getAnswer();
        });

        buttonCheck.setPrefHeight(50);
        buttonCheck.setPrefWidth(80);
        buttonCheck.setFont(Font.font("方正粗黑宋简体", 14));
        buttonCheck.setOnAction(event -> {
            check();
        });

        buttonExit.setPrefHeight(50);
        buttonExit.setPrefWidth(80);
        buttonExit.setFont(Font.font("方正粗黑宋简体", 14));
        buttonExit.setOnAction(event -> {
            exit();
        });

        setPadding(new Insets(10, 10, 10, 2));
        hBox.getChildren().add(buttonEasy);
        hBox.getChildren().add(buttonMiddle);
        hBox.getChildren().add(buttonDifficult);
        hBox.getChildren().add(buttonReset);
        hBox.getChildren().add(buttonAnswer);
        hBox.getChildren().add(buttonCheck);
//        hBox.getChildren().add(buttonExit);
        getChildren().add(hBox);
    }
    public void setEasyPattern() {
//        buttonEasy.setTextFill(activeColor);
//        buttonMiddle.setTextFill(unactiveColor);
//        buttonDifficult.setTextFill(unactiveColor);
        buttonEasy.setStyle("-fx-base: rgb(203,193,186);");
        buttonMiddle.setStyle(null);
        buttonDifficult.setStyle(null);
        if(flight != 1) {
            flight = 1;
            sudoKu.ReSudoKu(flight);
        }
    }
    public void setMiddlePattern() {
//        buttonEasy.setTextFill(unactiveColor);
//        buttonMiddle.setTextFill(activeColor);
//        buttonDifficult.setTextFill(unactiveColor);
        buttonEasy.setStyle(null);
        buttonMiddle.setStyle("-fx-base: rgb(203,193,186);");
        buttonDifficult.setStyle(null);
        if(flight != 2) {
            flight = 2;
            sudoKu.ReSudoKu(flight);
        }
    }
    public void setDifficultPattern() {
//        buttonEasy.setTextFill(unactiveColor);
//        buttonMiddle.setTextFill(unactiveColor);
//        buttonDifficult.setTextFill(activeColor);
        buttonEasy.setStyle(null);
        buttonMiddle.setStyle(null);
        buttonDifficult.setStyle("-fx-base: rgb(203,193,186);");
        if(flight != 3) {
            flight = 3;
            sudoKu.ReSudoKu(flight);
        }
    }
    public void ResetSudo() {
        buttonAnswer.setTextFill(unactiveColor);
        buttonAnswer.setStyle(null);
        answerUse = false;
        sudoKu.ResetSudo();
    }
    public void getAnswer() {
        if(answerUse == false) {
            buttonAnswer.setStyle("-fx-base: rgb(203,193,186);");
//            buttonAnswer.setTextFill(activeColor);
            sudoKu.answer();
            answerUse = true;
        }
        else {
            buttonAnswer.setStyle(null);
            buttonAnswer.setTextFill(unactiveColor);
            sudoKu.answer();
            answerUse = false;
        }
    }
    public void check(){
        sudoKu.check();
    }
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Exit Window");
        alert.setContentText("You are currently choosing to leave the game......\nWould you like to save the current game record(￢‸￢) ?");
        alert.setHeaderText(null);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNot = new ButtonType("No");
        alert.getButtonTypes().addAll(buttonTypeYes, buttonTypeNot);
        alert.showAndWait();
        if(alert.getResult() == buttonTypeYes){
            System.out.println("Yes");
        } if(alert.getResult() == buttonTypeNot) {
            stage.close();
        }
    }
}
class BottomPane extends StackPane {
    public Label inforlabel = new Label();
    public String first = "Having A Good Game!!!(..•˘_˘•..)";
    public String fail = "SORRY!!! Your Answer Is Wrong(〒︿〒)";
    public String success = "WELL DONE!!! ヾ(≧▽≦*)o";
    public String newGame = "You Rebegin A New Game\\(☆o☆)/";
    public String BackGame = "Welcome Back!!!（★^ O ^★）";
    public HBox hBox;
    public BottomPane() {
        hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER);

        inforlabel.setText(first);
        inforlabel.setFont(Font.font("方正粗黑宋简体", 25));
        hBox.getChildren().add(inforlabel);

        setAlignment(hBox, Pos.CENTER);
        setPadding(new Insets(10, 25, 20, 10));
        getChildren().add(hBox);
    }
    public void setInforlabel(int tag) {
        if(tag == 1) {
            inforlabel.setText(success);
            inforlabel.setTextFill(new Color(0.34,0.956,0.45,0.83));
        } else {
            inforlabel.setText(fail);
            inforlabel.setTextFill(new Color(0.956,0.129,0.213,0.83));
        }
    }
}
class RightPand extends StackPane{
    public Button buttonChange = new Button("Change");
    public Button buttonSubmit = new Button("Submit");
    public Label labeltimeTitle = new Label("Time");
    public Label labelrealTime = new Label("00:00");
    public Label labelsolved = new Label("Solved:\t0");
    public Button buttonStop = new Button("Stop");
    public Button buttonZero = new Button("Zero");
    private Color activeColor = new Color(0.23,0.45,0.78,0.9);
    private Color unactiveColor = new Color(0,0,0,0.9);
    public Border nextborder = new Border(new BorderStroke(Paint.valueOf("#3DFF36"), BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(5)));
    public Timeline timeline;
    public int solveTime = 0;
    public boolean stopping = false;
    public boolean doing = true;
    public int time = 0;
    public SudoKu sudoKu;
    public MenuPane menuPane;
    public BottomPane bottomPane;
    public int[][] puzzle = new int[10][10];
    public RightPand(SudoKu sudo, MenuPane menu, BottomPane bottom) {
        sudoKu = sudo;
        menuPane = menu;
        bottomPane = bottom;
        VBox vBox = new VBox();
        setAlignment(vBox, Pos.CENTER);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_CENTER);

        buttonChange.setPrefHeight(50);
        buttonChange.setPrefWidth(100);
        buttonChange.setFont(Font.font("方正粗黑宋简体", 16));
        buttonChange.setOnAction(event -> {
            change();
        });

        buttonSubmit.setPrefHeight(50);
        buttonSubmit.setPrefWidth(100);
        buttonSubmit.setFont(Font.font("方正粗黑宋简体", 16));
        buttonSubmit.setOnAction(event -> {
            submit();
        });

        VBox vBoxtime = new VBox();
        setAlignment(vBoxtime, Pos.CENTER);
        vBoxtime.setSpacing(10);
        vBoxtime.setPadding(new Insets(10));
        vBoxtime.setAlignment(Pos.TOP_CENTER);

        labeltimeTitle.setFont(Font.font("方正粗黑宋简体", 25));
        labelrealTime.setFont(Font.font("方正粗黑宋简体", 25));
        vBoxtime.getChildren().add(labeltimeTitle);
        vBoxtime.getChildren().add(labelrealTime);
        BorderStroke timeborderStroke = new BorderStroke(Paint.valueOf("#DDAFAA"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6));
        Border falseborder = new Border(timeborderStroke);
        vBoxtime.setBorder(falseborder);

        buttonStop.setPrefHeight(40);
        buttonStop.setPrefWidth(100);
        buttonStop.setFont(Font.font("方正粗黑宋简体", 16));
        buttonStop.setOnAction(event -> {
            stop();
        });

        buttonZero.setPrefHeight(40);
        buttonZero.setPrefWidth(100);
        buttonZero.setFont(Font.font("方正粗黑宋简体", 16));
        buttonZero.setOnAction(event -> {
            zero();
        });

        vBoxtime.getChildren().add(buttonStop);
        vBoxtime.getChildren().add(buttonZero);

        labelsolved.setText("Solved:\t" + solveTime);
        labelsolved.setFont(Font.font("方正粗黑宋简体", 24));
        labelsolved.setTextFill(new Color(0.86,0.652,0.58,0.83));

        setPadding(new Insets(25, 15, 10, 10));
        vBox.getChildren().add(buttonChange);
        vBox.getChildren().add(buttonSubmit);
        vBox.getChildren().add(vBoxtime);
        vBox.getChildren().add(labelsolved);
        getChildren().add(vBox);

        timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            time++;
            setLabelrealTime(time);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void change(){
        if(doing == true){
            menuPane.answerUse = false;
            menuPane.buttonAnswer.setTextFill(menuPane.unactiveColor);
            sudoKu.answer = false;
            sudoKu.ReSudoKu(sudoKu.modern);
            bottomPane.inforlabel.setText(bottomPane.newGame);
            bottomPane.inforlabel.setTextFill(unactiveColor);
        } else {
            menuPane.answerUse = false;
            menuPane.buttonAnswer.setTextFill(menuPane.unactiveColor);
            menuPane.buttonAnswer.setStyle(null);
            sudoKu.answer = false;
            sudoKu.ReSudoKu(sudoKu.modern);
            bottomPane.inforlabel.setText(bottomPane.newGame);
            bottomPane.inforlabel.setTextFill(unactiveColor);
            buttonChange.setText("change");
            buttonChange.setBorder(null);
            doing = true;
        }

    }
    public void submit() {
        int tag = sudoKu.submit();
        if(tag == 1 && doing == true) {
            doing = false;
            solveTime += 1;

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Good Job");
            alert.setHeaderText(null);
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().addAll(buttonTypeYes, buttonTypeNo);
            alert.setContentText("You meet the secret stage, do you want to challenge ???");
            alert.showAndWait();
            if(alert.getResult() == buttonTypeNo){
                labelsolved.setText("Solved:\t" + solveTime);
                buttonChange.setText("NEXT");
                buttonChange.setBorder(nextborder);
            } else {
                String url = "file:///D://EVCapture/vedios/courier.mp4";
                Media media = new Media(url);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(700);
                mediaView.setFitHeight(480);
                mediaPlayer.play();
                BorderPane borderPane = new BorderPane();
                Scene scene = new Scene(borderPane);
                Stage stage = new Stage();
                mediaPlayer.setOnEndOfMedia(() -> {
                    borderPane.setVisible(false);
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    stage.close();
                    travelSeller();
                    challengePuzzle();
                    labelsolved.setText("Solved:\t" + solveTime);
                    buttonChange.setText("NEXT");
                    buttonChange.setBorder(nextborder);
                });
                borderPane.setCenter(mediaView);
                stage.setTitle("拯救快递小哥计划");
                stage.setScene(scene);
                stage.show();
            }
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle(null);
//            alert.setHeaderText("Please Click ' NEXT '");
//            alert.setContentText("CONGRATULATION !!!");
//            alert.showAndWait();
//            labelsolved.setText("Solved:\t" + solveTime);
//            buttonChange.setText("NEXT");
//            buttonChange.setBorder(nextborder);
        }
        bottomPane.setInforlabel(tag);
    }
    public void travelSeller(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("拯救快递小哥计划");
        alert.setHeaderText("游戏背景介绍");
        alert.setContentText("    2020 年年初，中国邮政快递报社发布《2019 年全国快递员职业调查报告》。\n    统计指出，我国 46.85%的快递小哥每天工作 8—10 小时，33.69% 的人每天工作10—12小时，约 20% 的人工12小时以上。");
        alert.showAndWait();
    }
    public void challengePuzzle(){
        for(int i = 1; i <= 9; i++){
            for(int j = 1; j <= 9; j++) {
                puzzle[i][j] = sudoKu.nums[j][i];
            }
        }
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("拯救快递小哥计划");
        textInputDialog.setHeaderText("\t对于先前解出的数独棋盘，可以将其视作为城市的81个分区；\n" +
                "\t小张是一位勤劳的快递员，每天奔波于为城市居民送快递的工作中，全年无休；\n" +
                "\t正值春节即将到来，家家户户为了囤年货，Z城市的快递行业又迎来高峰期，81个分区均有快递需要送；\n" +
                "\t他被安排从城市的最西北角送快递至城市的最东南角，要求每次移动只能向右或向下一格；\n" +
                "\t每个格子的数值代表小张经过该分区可获得的利润，小张希望努把力挣更多的钱；\n" +
                "\t请你为小张构思一条挣钱最多的路线，并将所挣的金额填在下方对应的方框中。\n\n" +
                "\t\t此处出发 ->\t" + puzzle[1][1] + "\t" + puzzle[1][2] + "\t" + puzzle[1][3] + "\t" + puzzle[1][4] + "\t" + puzzle[1][5] + "\t" + puzzle[1][6] + "\t" + puzzle[1][7] + "\t" + puzzle[1][8] + "\t" + puzzle[1][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[2][1] + "\t" + puzzle[2][2] + "\t" + puzzle[2][3] + "\t" + puzzle[2][4] + "\t" + puzzle[2][5] + "\t" + puzzle[2][6] + "\t" + puzzle[2][7] + "\t" + puzzle[2][8] + "\t" + puzzle[2][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[3][1] + "\t" + puzzle[3][2] + "\t" + puzzle[3][3] + "\t" + puzzle[3][4] + "\t" + puzzle[3][5] + "\t" + puzzle[3][6] + "\t" + puzzle[3][7] + "\t" + puzzle[3][8] + "\t" + puzzle[3][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[4][1] + "\t" + puzzle[4][2] + "\t" + puzzle[4][3] + "\t" + puzzle[4][4] + "\t" + puzzle[4][5] + "\t" + puzzle[4][6] + "\t" + puzzle[4][7] + "\t" + puzzle[4][8] + "\t" + puzzle[4][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[5][1] + "\t" + puzzle[5][2] + "\t" + puzzle[5][3] + "\t" + puzzle[5][4] + "\t" + puzzle[5][5] + "\t" + puzzle[5][6] + "\t" + puzzle[5][7] + "\t" + puzzle[5][8] + "\t" + puzzle[5][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[6][1] + "\t" + puzzle[6][2] + "\t" + puzzle[6][3] + "\t" + puzzle[6][4] + "\t" + puzzle[6][5] + "\t" + puzzle[6][6] + "\t" + puzzle[6][7] + "\t" + puzzle[6][8] + "\t" + puzzle[6][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[7][1] + "\t" + puzzle[7][2] + "\t" + puzzle[7][3] + "\t" + puzzle[7][4] + "\t" + puzzle[7][5] + "\t" + puzzle[7][6] + "\t" + puzzle[7][7] + "\t" + puzzle[7][8] + "\t" + puzzle[7][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[8][1] + "\t" + puzzle[8][2] + "\t" + puzzle[8][3] + "\t" + puzzle[8][4] + "\t" + puzzle[8][5] + "\t" + puzzle[8][6] + "\t" + puzzle[8][7] + "\t" + puzzle[8][8] + "\t" + puzzle[8][9] + "\n" +
                "\t\t\t\t       \t" + puzzle[9][1] + "\t" + puzzle[9][2] + "\t" + puzzle[9][3] + "\t" + puzzle[9][4] + "\t" + puzzle[9][5] + "\t" + puzzle[9][6] + "\t" + puzzle[9][7] + "\t" + puzzle[9][8] + "\t" + puzzle[9][9] + "\t<- 到达此处"
        );
        textInputDialog.setContentText("所能挣的最大金额：");
        textInputDialog.showAndWait();
        String maxNum = textInputDialog.getResult();
        solvePuzzle();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        if(Integer.parseInt(maxNum) == puzzle[9][9]) {
            alert.setHeaderText("恭喜少侠回答正确！！！");
            alert.setContentText("少侠你可真是太聪明了，小张因为你又挣了一大笔钱，你可真是天才。");
        } else {
            alert.setHeaderText("很遗憾，少侠回答错误了喔，正确答案是： " + puzzle[9][9]);
            alert.setContentText("太可惜了少侠，没能帮小张走到利润最高的路。");
        }
        alert.showAndWait();
    }
    public void solvePuzzle(){
        for(int i = 2; i <= 9; i++) {
            puzzle[i][1] += puzzle[i-1][1];
            puzzle[1][i] += puzzle[1][i-1];
        }
        for(int i = 2; i <= 9; i++) {
            for(int j = 2; j <= 9; j++) {
                puzzle[i][j] += Math.max(puzzle[i-1][j], puzzle[i][j-1]);
            }
        }
    }
    public void setLabelrealTime(int time){
        String formattime = getFormattime(time);
        labelrealTime.setText(formattime);
    }
    public String getFormattime(int count) {
        String second = null;
        String minute = null;
        if (count / 60 < 10) {
            minute = "0" + (count / 60);
        } else {
            minute = "" + (count / 60);
        }
        if (count % 60 < 10) {
            second = ":0" + count % 60;
        } else {
            second = ":" + count % 60;
        }
        return minute + second;
    }
    public void stop() {
        if(stopping == false) {
            buttonStop.setStyle("-fx-base: rgb(203,193,186);");
//            buttonStop.setTextFill(activeColor);
            buttonStop.setText("Continue");
            buttonZero.setTextFill(unactiveColor);
            timeline.stop();
            stopping = true;
        }
        else {
            buttonStop.setStyle(null);
            buttonStop.setTextFill(unactiveColor);
            buttonStop.setText("Stop");
            timeline.play();
            stopping = false;
        }
    }
    public void zero() {
        time = 0;
        labelrealTime.setText(getFormattime(time));
    }
}