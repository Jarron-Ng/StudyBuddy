package com.example.project1d;

public class QuestionBank {


    private String questionBankLevel1 [] = {
            "Five identical semicircles are arranged as shown.\n Find the diameter of one circle.",
            "What is x?",
            "What is the derivative of f'(x-1)?",
            "What is the determinant of the inverse of the above matrix?"
    };


    private String optionsLevel1 [][] = {
            {"36", "46", "21", "5"},
            {"8","16","22","55"},
            {"1", "2", "0", "-1"},
            {"1","-0.14","3.5","-2"}
    };

    private String correctAnswers[] = {"36","8", "-1","-0.14"};

    private int imageLibrary [] = {
            R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4
    };

    public int getLength() {
        return questionBankLevel1.length;
    }


    public String getQuestion(int a) {
        String question = questionBankLevel1[a];
        return question;
    }

    public int getImage(int a) {
        int image = imageLibrary[a];
        return image;
    }


    public String getOption1(int a) {
        String choice0 = optionsLevel1[a][0];
        return choice0;
    }


    public String getOption2(int a) {
        String choice1 = optionsLevel1[a][1];
        return choice1;
    }

    public String getOption3(int a) {
        String choice2 = optionsLevel1[a][2];
        return choice2;
    }

    public String getOption4(int a) {
        String choice2 = optionsLevel1[a][3];
        return choice2;
    }

    public String getCorrectAnswer(int a) {
        String answer = correctAnswers[a];
        return answer;
    }

}
