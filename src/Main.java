//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        abstract class Creature {
//            abstract void bear();
//
//            abstract void die();
//
//            String name = null;
//            boolean isAlive = false;
//
//            void shoutName() {
//                if (name != null) System.out.println(name);
//                else System.out.println("Error!");
//            }
//        }
//        class Human extends Creature {
//            Human (String name) {
//                this.name = name;
//            }
//            @Override
//            void bear() {
//                System.out.println("The Human " + name + " was born");
//            }
//
//            void die() {
//                System.out.println("The Human " + name + " was died");;
//            }
//        }
//        class Dog extends Creature {
//            Dog (String name) {
//                this.name = name;
//            }
//            @Override
//            void bear() {
//                System.out.println("The Dog " + name + " was born");
//            }
//
//            void die() {
//                System.out.println("The Dog " + name + " was died");;
//            }
//        }
//        class Alien extends Creature {
//            Alien (String name) {
//                this.name = name;
//            }
//            @Override
//            void bear() {
//                System.out.println("The Alien    " + name + " was born");
//            }
//
//            void die() {
//                System.out.println("The Alien " + name + " was died");;
//            }
//        }
//        class AbstractClassDemonstration {
//            AbstractClassDemonstration() {
//                ArrayList<Creature> creatures = new ArrayList();
//                creatures.add(new Human("Sobolev"));
//                creatures.add(new Dog("Andrew"));
//                creatures.add(new Alien("Alexandrovich"));
//                for (Creature creature: creatures){
//                    creature.bear();
//                    creature.die();
//                }
//            }
//        }
//        AbstractClassDemonstration abstractClassDemonstration = new AbstractClassDemonstration();
//    }
//}
import java.io.IOException;
import java.util.Scanner;
public class  Main {
    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        String TypeOfCalculator = Scanner.nextLine();
        Integer AmountOfOperations = Scanner.nextInt();
        for (int i = 0; i < AmountOfOperations; i++){
            String Operator = Scanner.next();
            if (Scanner.hasNextInt()){
                Integer FirstTerm = Integer.parseInt(Scanner.next());
                Integer SecondTerm = Integer.parseInt(Scanner.next());
            }
            else if (Scanner.hasNextDouble()){
                Double FirstTerm = Double.parseDouble(Scanner.next());
                Double SecondTerm = Double.parseDouble(Scanner.next());
            }
            else{
                String FirstTerm = Scanner.next();
                String SecondTerm = Scanner.next();
            }
        }
    }

    private CalculatorType readCalculator(String TypeOfCalculator){
        switch (TypeOfCalculator){
            case ("INTEGER"):
                return CalculatorType.Integer;
            case ("DOUBLE"):
                return CalculatorType.Double;
            case ("STRING"):
                return CalculatorType.String;
            default:
                return CalculatorType.Incorrect;
        }
    }
}

enum CalculatorType{Integer, Double, String, Incorrect};
abstract class Calculator{
    abstract String add(String a, String b);
    abstract String subtract(String a, String b);
    abstract String multiply(String a, String b);
    abstract String divide(String a, String b);
}
class IntegerCalculator extends Calculator{
    @Override
    String add(String a, String b){
        try {
            Integer res = Integer.parseInt(a)+Integer.parseInt(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String subtract(String a, String b){
        try {
            Integer res = Integer.parseInt(a)-Integer.parseInt(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String multiply(String a, String b){
        try {
            Integer res = Integer.parseInt(a)*Integer.parseInt(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String divide(String a, String b){
        try {
            Integer res = Integer.parseInt(a)/Integer.parseInt(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
}
class DoubleCalculator extends Calculator{
    @Override
    String add(String a, String b){
        try {
            Double res = Double.parseDouble(a)+Double.parseDouble(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String subtract(String a, String b){
        try {
            Double res = Double.parseDouble(a)-Double.parseDouble(b);
            return res.t    oString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String multiply(String a, String b){
        try {
            Double res = Double.parseDouble(a)*Double.parseDouble(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String divide(String a, String b){
        try {
            Double res = Double.parseDouble(a)/Double.parseDouble(b);
            return res.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
}
class StringCalculator extends Calculator{
    @Override
    String add(String a, String b){
        return a+b;
    }
    @Override
    String subtract(String a, String b){
        return "Unsupported operation for strings";
    }
    @Override
    String multiply(String a, String b){
        try {
            Integer c = Integer.parseInt(b);
            StringBuilder d = new StringBuilder();
            for (int i = 0; i < c; i++){
                d.append(a);
            }
            return d.toString();
        }catch(NumberFormatException e){
            return "Wrong argument type";
        }
    }
    @Override
    String divide(String a, String b){
        return "Unsupported operation for strings";
    }
}