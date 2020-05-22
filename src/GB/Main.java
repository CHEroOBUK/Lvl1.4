package GB;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static int fieldSize;                   //размер поля для игры
    static int winCondition;                //условие для победы
    static String[][] field;                //размер этого поля = fieldSize + 1 (добавление сетки коррдинат)
    static String[][] invertedField;        //инвертированное поле на 45 град. для обзора диагоналей
    static int maxVertX;                    //максимальное количество Крестиков в вертикали
    static int yCoordinateWithMaxX;         //У координата с максимальным количеством Крестиков
    static int maxHorX;                     //максимальное количество Крестиков в горизонтали
    static int xCoordinateWithMaxX;         //Х координата с максимальным количеством Крестиков
    static final String DOT_X = "X";        //маркер Крестика
    static final String DOT_0 = "0";        //маркер Нолика
    static final String DOT_EMPTY = "•";    //маркер Пустой ячейки

    public static void main(String[] args) {
        int runAgain;                       //указатель на повтор игры
        System.out.println("Вам предлагается сыграть c компьютером в игру 'Крестики-нолики'");
        do {
            runGame();                      //запуск игры
            System.out.print("Сыграть еще раз?\t 1 - ДА, 2 - НЕТ     Ваш ответ: ");
            runAgain = getCheckedInput(1, 2);
        } while (runAgain == 1);
    }

    static void runGame(){
        createGame();                       //создание поля для игры с желаемыми условиями
        do {
            turnUser();                     //ход пользователя
            if (isWinner()) {               //проверка на победителя
                showField();                //показ поля
                System.out.println("Вы выйграли!");
                return;                     //выход из текущей игры
            }
            if (hasEmptySpace()){           //следующий ход доступен есть есть свободные ячейки
                turnAI();
                if (isWinner()) {           //проверка на победителя
                    showField();            //показ поля
                    System.out.println("Победил компьютер!");
                    return;                 //выход из текущей игры
                }
            }
            showField();                    //показ поля
        } while (hasEmptySpace());          //игра, пока есть свободные ячейки
        System.out.println("Ничья!");
    }

    static void createGame(){
        System.out.print("Выберите размер ширины поля, от 3 до 10: ");
        fieldSize = getCheckedInput(3, 10);
        System.out.print("Выберите условие для победы, от 3 до " + fieldSize + " в ряд: ");
        winCondition = getCheckedInput(3, fieldSize);
        field = new String[fieldSize + 1][fieldSize + 1];
        System.out.println("Ваше поле для игры создано:");
        for (int i = 0; i < field.length; i++){             //цикл для вывода игрового поля с координатами
            System.out.print(i + "\t");
            for (int j = 1; j < field.length; j++){
                if (i == 0){
                    System.out.print(j + "\t");
                } else {
                    field[i][j] = DOT_EMPTY;                //заполнение поля пустыми маркерами
                    System.out.print(field[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    static void turnUser(){
        int userX;                                          //координата Х
        int userY;                                          //координата У
        do {
            System.out.println("Введите координаты, сначала X, затем Y: ");
            userX = getCheckedInput(1, fieldSize);       //запрос координаты Х
            userY = getCheckedInput(1, fieldSize);       //запрос координаты У
        } while (isTurnError(userX, userY));                //проверка, чтобы попасть в пустую ячейку
        field[userX][userY] = DOT_X;                        //заполнение игрового поля ходом пользователя
    }

    static void turnAI(){
        int xAI;                                            //координата Х
        int yAI;                                            //координата Y
        int countFreeI = 0;                                 //количество пустых ячеек в интересующей вертикали
        int countFreeJ = 0;                                 //количество пустых ячеек в интересующей горизонтали
        for (int j = 1; j < field.length; j++){             //цикл по горизонтали с максимальным кол-вом Крестиков
            if (!isTurnError(xCoordinateWithMaxX, j)){      //если есть пустая ячейка
                countFreeJ ++;                              //счетчик
            }
        }
        for (int i = 1; i < field.length; i++){             //цикл по вертикали с максимальным кол-вом Крестиков
            if (!isTurnError(i, yCoordinateWithMaxX)){      //если есть пустая ячейка
                countFreeI ++;                              //счетчик
            }
        }
        if (maxHorX >= maxVertX && countFreeJ > 0) {        //если в горизонтали Крестиков больше и есть где блокировать
            do {                                            //заполнение случайной ячейки в этой горизонтали
                xAI = xCoordinateWithMaxX;
                yAI = random.nextInt(fieldSize) + 1;
            } while (isTurnError(xAI, yAI));
            System.out.println("Компьютер сходил:\t" + xAI + ":" + yAI);
            field[xAI][yAI] = DOT_0;
        }
        else if (maxVertX >= maxHorX && countFreeI > 0){    //если в вертикали Крестиков больше и есть где блокировать
            do {                                            //заполнение случайной ячейки в этой вертикали
                xAI = random.nextInt(fieldSize) + 1;
                yAI = yCoordinateWithMaxX;
            } while (isTurnError(xAI, yAI));
            System.out.println("Компьютер сходил:\t" + xAI + ":" + yAI);
            field[xAI][yAI] = DOT_0;
        } else {
            do {                                            //произвольный выбор места по Нолик
                xAI = random.nextInt(fieldSize) + 1;
                yAI = random.nextInt(fieldSize) + 1;
            } while (isTurnError(xAI, yAI));
            System.out.println("Компьютер сходил:\t" + xAI + ":" + yAI);
            field[xAI][yAI] = DOT_0;
        }
    }

    static boolean isTurnError(int x, int y){
        return field[x][y].equals(DOT_X) || field[x][y].equals(DOT_0);  //проверка на Пустой маркер
    }

    static void showField(){
        for (int i = 0; i < field.length; i++){         //цикл показа текущего поля игры
            System.out.print(i + "\t");
            for (int j = 1; j < field.length; j++){
                if (i == 0){
                    System.out.print(j + "\t");
                } else {
                    System.out.print(field[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    static void buildInvertedField(){
        int invertedFieldSize = fieldSize * 2;  //ширина перевернутого поля со вспомогательной строчкой и столбцом
        invertedField = new String[invertedFieldSize][invertedFieldSize];
        for (int i = 0; i < invertedFieldSize; i++) {
            Arrays.fill(invertedField[i], DOT_EMPTY);                       //заполнение поля пустыми маркерами
        }
        for (int i = 1; i < field.length; i++){
            for (int j = 1; j < field.length; j++){
                invertedField[i + j - 1][j - i + fieldSize] = field[i][j];  //заполнение игровым полем
            }
        }
    }

    static boolean isWinner (){
        buildInvertedField();                   //создание перевернутого на 45 град игрового поля
        int countX;                             //счетчик Крестиков
        int count0;                             //счетчик Ноликов
        maxVertX = 0;                           //сброс вертикали с наибольшим кол-вом Крестиков предыдущего хода
        maxHorX = 0;                            //сброс горизонтали с наибольшим кол-вом Крестиков предыдущего хода
        for (int i = 1; i < field.length; i++){ //цикл прохода по горизонталям
            countX = 0;
            count0 = 0;
            for (int j = 1; j < field.length; j++){
                if (field[i][j].equals(DOT_X)) {            //счетчик Крестиков
                    countX += 1;
                    count0 = 0;                             //обнуление счетчика Ноликов
                }
                else if (field[i][j].equals(DOT_0)) {       //счетчик Ноликов
                    count0 += 1;
                    countX = 0;                             //обнуление счетчика Крестиков
                } else {                                    //обнуление обоих счетчиков
                    countX = 0;
                    count0 = 0;
                }
                if (countX == winCondition || count0 == winCondition){  //если есть победитель
                    return true;                                        //выход
                }
                if (countX > maxHorX) {         //поиск наибольшего кол-ва Крестиков стоящих по порядку в горизонтали
                    maxHorX = countX;           //фиксирование числа Крестиков горизонтали
                    xCoordinateWithMaxX = i;    //фиксирование Х координаты с макс кол-вом Крестиков
                }
            }
        }
        for (int j = 1; j < field.length; j++){ //цикл прохода по вертикалям
            countX = 0;
            count0 = 0;
            for (int i = 1; i < field.length; i++){
                if (field[i][j].equals(DOT_X)) {
                    countX += 1;
                    count0 = 0;
                }
                else if (field[i][j].equals(DOT_0)) {
                    count0 += 1;
                    countX = 0;
                } else {
                    countX = 0;
                    count0 = 0;
                }
                if (countX == winCondition || count0 == winCondition){
                    return true;
                }
                if (countX > maxVertX) {        //поиск наибольшего кол-ва Крестиков стоящих по порядку в вертикали
                    maxVertX = countX;          //фиксирование числа Крестиков вертикали
                    yCoordinateWithMaxX = j;    //фиксирование У координаты с макс кол-вом Крестиков
                }
            }
        }
        for (int i = 1; i < invertedField.length; i++){ //цикл прохода по диагоналям [/]
            countX = 0;
            count0 = 0;
            for (int j = 1; j < invertedField.length; j++){
                if (invertedField[i][j].equals(DOT_X)) {
                    countX += 1;
                    count0 = 0;
                }
                else if (invertedField[i][j].equals(DOT_0)) {
                    count0 += 1;
                    countX = 0;
                }
                else if (invertedField[i][j].equals(DOT_EMPTY) && invertedField[i][j-1].equals(DOT_EMPTY)){
                    countX = 0;         //обнуление происходит если две подряд стоящие ячейки с Пустым маркером
                    count0 = 0;
                }
                if (countX == winCondition || count0 == winCondition){
                    return true;
                }
            }
        }
        for (int j = 1; j < invertedField.length; j++){ //цикл прохода по диагоналям [\]
            countX = 0;
            count0 = 0;
            for (int i = 1; i < invertedField.length; i++){
                if (invertedField[i][j].equals(DOT_X)) {
                    countX += 1;
                    count0 = 0;
                }
                else if (invertedField[i][j].equals(DOT_0)) {
                    count0 += 1;
                    countX = 0;
                }
                else if (invertedField[i][j].equals(DOT_EMPTY) && invertedField[i-1][j].equals(DOT_EMPTY)){
                    countX = 0;
                    count0 = 0;
                }
                if (countX == winCondition || count0 == winCondition){
                    return true;
                }
            }
        }
        return false;
    }

    static boolean hasEmptySpace(){
        for (int i = 1; i < field.length; i++){
            for (int j = 1; j < field.length; j++){
                if (field[i][j].equals(DOT_EMPTY)) {    //проверка ячейки на пустую
                    return true;
                }
            }
        }
        return false;
    }

    static int getCheckedInput(int a, int b){
        int check;
        do {
            if (scanner.hasNextInt()) {             //предотвращение ввода не целого числа
                check = scanner.nextInt();
                if (check >= a && check <= b)       //проверка на допустимый диапазон ввода
                    return check;
            }
            System.out.print("Нужно ввести число от " + a + " до " + b + ":  ");
            scanner.nextLine();
        } while (true);
    }
}
