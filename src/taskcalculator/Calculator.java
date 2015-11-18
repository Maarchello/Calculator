package taskcalculator;

import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {

    /**
     * Приводим строку к постфиксному виду
     */
    private static String toPostfix(String input)  {
        StringBuilder  operand = new StringBuilder("");
        Stack<Character> operatorsStack = new Stack<>();
        char ch, temp;

        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            if (isOperator(ch)) {
                while (operatorsStack.size() > 0) {
                    temp = operatorsStack.peek();
                    if (isOperator(temp) && (getPriority(ch) <= getPriority(temp))) {
                        operand.append(" ").append(temp).append(" ");
                        operatorsStack.pop();
                    } else {
                        operand.append(" ");
                        break;
                    }
                }
                operand.append(" ");
                operatorsStack.push(ch);
            } else {
                operand.append(ch);
            }
        }
        // Оставшиеся операторы в выходную строку
        while (operatorsStack.size() > 0) {
            operand.append(" ").append(operatorsStack.pop());
        }
        return  operand.toString();
    }

    /**
     * Проверяем строку на формат 'целая часть'.'дробная часть'
     */
    private static boolean isCorrect(String s){
        if(s.contains(".")){
            char[] ch = s.toCharArray();
            for(int i=0; i<ch.length; i++){
                if(ch[i] == '.') {
                    int temp = Character.getNumericValue(ch[i-1]);
                    return (temp>=0 && temp<=9);
                }
            }
        }
        return true;
    }

    /**
     * Метод проверяет, является ли текущий символ оператором
     */
    private static boolean isOperator(char ch) {
        switch (ch) {
            case '-':
            case '+':
            case '*':
            case '/':
                return true;
        }
        return false;
    }

    /**
     * Метод возвращаем приоритет оператора
     */
    private static byte getPriority(char ch) {
        switch (ch) {
            case '*':
            case '/':
                return 2;
        }
        return 1; // Для '+' и '-'
    }

    /**
     * Вычисляет значение постфиксной записи
     */
    public static double eval(String input) throws Exception {
        double penultimate = 0, last = 0;
        String temp;
        Stack<Double> stack = new Stack<>();
        if (isCorrect(input)) {
            input = toPostfix(input);
            StringTokenizer st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                try {
                    temp = st.nextToken().trim();
                    if (1 == temp.length() && isOperator(temp.charAt(0))) {
                        if (stack.size() < 2) {
                            throw new Exception("Нужно больше данных");
                        }
                        last = stack.pop();
                        penultimate = stack.pop();
                        switch (temp.charAt(0)) {
                            case '+':
                                penultimate += last;
                                break;
                            case '-':
                                penultimate -= last;
                                break;
                            case '/':
                                penultimate /= last;
                                break;
                            case '*':
                                penultimate *= last;
                                break;
                            default:
                                throw new Exception("Доступные операторы: '+', '-', '*', '/' ");
                        }
                        stack.push(penultimate);
                    } else {
                        penultimate = Double.parseDouble(temp);
                        stack.push(penultimate);
                    }
                } catch (Exception e) {
                    throw new Exception("Ошибка в выражении");
                }
            }
            if (stack.size() > 1) {
                throw new Exception("Пробелы внутри числа недопустимы");
            }
        }
        return stack.pop();
    }
}
