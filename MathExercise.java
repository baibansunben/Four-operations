package demo6;
import java.io.*;
import java.util.*;

public class MathExercise {
    public static void main(String[] args) {
        // 解析命令行参数
        System.out.println("Usage: java MathExercise [-n numQuestions] [-r range]");
        Scanner input=new Scanner(System.in);
        System.out.println("请输入生成题目的数量：");
        int numQuestions = input.nextInt();
        Scanner input2=new Scanner(System.in);
        System.out.println("请输入题目中数值的范围：");
        int range = input.nextInt();
//        int numQuestions = 10;
//        int range = 10;
        boolean generateExercises = true;
        boolean gradeExercises = true;
        String exerciseFile ="exercises.txt";;
        String answerFile = "answers.txt";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    numQuestions = Integer.parseInt(args[++i]);
                    break;
                case "-r":
                    range = Integer.parseInt(args[++i]);
                    break;
                case "-e":
                    exerciseFile = args[++i];
                    generateExercises = false;
                    break;
                case "-a":
                    answerFile = args[++i];
                    gradeExercises = true;
                    break;
            }
        }

        // 生成题目和答案
        List<String> exercises = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        if (generateExercises) {
            Random random = new Random();

            for (int i = 0; i < numQuestions; i++) {
                String exercise = generateExercise(random, range);
                String answer = evaluateExpression(exercise);

                exercises.add(exercise);
                answers.add(answer);
            }
        }

        // 打印题目和答案
        for (int i = 0; i < numQuestions; i++) {
            System.out.println("Exercise " + (i + 1) + ": " + exercises.get(i));
            System.out.println("Answer " + (i + 1) + ": " + answers.get(i));
        }

        // 写入题目和答案到文件
        if (generateExercises) {
            try {
                FileWriter exerciseWriter = new FileWriter("Exercises.txt");
                FileWriter answerWriter = new FileWriter("Answers.txt");

                for (String exercise : exercises) {
                    exerciseWriter.write(exercise + "\n");
                }

                for (String answer : answers) {
                    answerWriter.write(answer + "\n");
                }

                exerciseWriter.close();
                answerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (true) {

            if (exerciseFile == null || answerFile == null) {
                System.out.println("Exercise file or answer file not specified.");
                return;
            }

            try {

                Scanner exerciseScanner = new Scanner(new File(exerciseFile));
                Scanner answerScanner = new Scanner(new File(answerFile));

                List<String> correctAnswers = new ArrayList<>();
                List<String> wrongAnswers = new ArrayList<>();

                int questionNumber = 1;
                while (exerciseScanner.hasNextLine() && answerScanner.hasNextLine()) {
                    String exercise = exerciseScanner.nextLine();
                    String answer = answerScanner.nextLine();

                    String userAnswer = evaluateExpression(exercise);
                    if (userAnswer.equals(answer)) {
                        correctAnswers.add(String.valueOf(questionNumber));
                    } else {
                        wrongAnswers.add(String.valueOf(questionNumber));
                    }

                    questionNumber++;
                }

                FileWriter gradeWriter = new FileWriter("Grade.txt");

                gradeWriter.write("Correct: " + correctAnswers.size() + " (" + String.join(", ", correctAnswers) + ")\n");
                gradeWriter.write("Wrong: " + wrongAnswers.size() + " (" + String.join(", ", wrongAnswers) + ")\n");
                gradeWriter.close();
                System.out.println("Grade file created successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
    // 生成四则运算题目
    public static String generateExercise(Random random, int range) {
        int a = random.nextInt(range) + 1;
        int b = random.nextInt(range) + 1;
        int operator = random.nextInt(4);

        String exercise;
        switch (operator) {
            case 0:
                exercise = a + " + " + b;
                break;
            case 1:
                exercise = a + " - " + b;
                break;
            case 2:
                exercise = a + " * " + b;
                break;
            case 3:
                exercise = a + " / " + b;
                break;
            default:
                exercise = "";
        }

        return exercise;
    }

 
// 计算四则运算表达式的结果
public static String evaluateExpression(String expression) {
    Stack<Integer> operandStack = new Stack<>();
    Stack<Character> operatorStack = new Stack<>();

    for (int i = 0; i < expression.length(); i++) {
        char ch = expression.charAt(i);

        if (ch == ' ')
            continue;

        if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
            while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(ch)) {
                processAnOperator(operandStack, operatorStack);
            }

            operatorStack.push(ch);
        } else if (ch >= '0' && ch <= '9') {
            StringBuilder operand = new StringBuilder();
            while (i < expression.length() && expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                operand.append(expression.charAt(i++));
            }
            i--;

            operandStack.push(Integer.parseInt(operand.toString()));
        } else {
            // 操作数不合法
            throw new IllegalArgumentException("Invalid operand: " + ch);
        }
    }

    while (!operatorStack.isEmpty()) {
        processAnOperator(operandStack, operatorStack);
    }

    if (operandStack.size() != 1) {
        // 表达式不合法
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }

    return operandStack.pop().toString();
}

    // 处理运算符
    public static void processAnOperator(Stack<Integer> operandStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();
        int operand1 = operandStack.pop();
        int operand2 = operandStack.pop();

        switch (operator) {
            case '+':
                operandStack.push(operand2 + operand1);
                break;
            case '-':
                operandStack.push(operand2 - operand1);
                break;
            case '*':
                operandStack.push(operand2 * operand1);
                break;
            case '/':
                operandStack.push(operand2 / operand1);
                break;
        }
    }

    // 运算符优先级
    public static int precedence(char operator) {
        if (operator == '+' || operator == '-')
            return 1;
        else if (operator == '*' || operator == '/')
            return 2;
        else
            return 0;
    }}